/*
 * Copyright (c) 2024 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.jk1.license.filter.ExcludeTransitiveDependenciesFilter
import com.github.jk1.license.filter.LicenseBundleNormalizer
import com.github.jk1.license.filter.ReduceDuplicateLicensesFilter
import com.github.jk1.license.render.InventoryMarkdownReportRenderer
import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import org.jetbrains.dokka.gradle.formats.DokkaFormatPlugin
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Locale

plugins {
    alias(libs.plugins.android.library)
    id("kotlin-parcelize")
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependency.license.report)
    alias(libs.plugins.dependencycheck)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kover)
}

val NAMESPACE: String by project
val GROUP: String by project
val POM_SCM_URL: String by project
val POM_DESCRIPTION: String by project

android {
    namespace = NAMESPACE
    group = GROUP
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testApplicationId = "$NAMESPACE.test"
        testHandleProfiling = true
        testFunctionalTest = true

        consumerProguardFiles("consumer-rules.pro")

    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}")
            excludes += listOf("/META-INF/versions/9/OSGI-INF/MANIFEST.MF")
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.eudi.document.manager)
    implementation(libs.multipaz.android) {
        exclude(group = "org.bouncycastle")
        exclude(group = "io.ktor")
    }
    implementation(libs.multipaz.android.legacy) {
        exclude(group = "org.bouncycastle")
        exclude(group = "io.ktor")
    }

    implementation(libs.kotlinx.io.core)
    implementation(libs.kotlinx.io.bytestring)

    // Zxing for qr code
    implementation(libs.zxing.core)

    // Bouncy Castle
    implementation(libs.bouncy.castle.prov)
    implementation(libs.bouncy.castle.pkix)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.json)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric.robolectric)
    testImplementation(libs.cbor)
    testImplementation(libs.cose)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Dependency check

dependencyCheck {
    formats = listOf("XML", "HTML")
    nvd.apiKey = System.getenv("NVD_API_KEY") ?: properties["nvdApiKey"]?.toString() ?: ""
    nvd.delay = 10000
    nvd.maxRetryCount = 2
}

// Dokka generation

@OptIn(InternalDokkaGradlePluginApi::class)
abstract class DokkaMarkdownPlugin : DokkaFormatPlugin(formatName = "markdown") {
    override fun DokkaFormatPlugin.DokkaFormatPluginContext.configure() {
        project.dependencies {
            dokkaPlugin(dokka("gfm-plugin"))
            formatDependencies.dokkaPublicationPluginClasspathApiOnly
                .dependencies.addLater(dokka("gfm-template-processing-plugin"))
        }
    }
}

apply<DokkaMarkdownPlugin>()

dokka {
    moduleName.set("transfer-manager")
    dokkaPublications.named("markdown") {
        outputDirectory.set(rootDir.resolve("docs"))
    }
}

tasks.named("dokkaGeneratePublicationMarkdown").configure {
    doFirst { delete(rootDir.resolve("docs")) }
}

tasks.named("dokkaGenerateMarkdown").configure {
    group = "documentation"
}

tasks.register<Jar>("dokkaHtmlJar") {
    group = "documentation"
    description = "Assembles HTML documentation with Dokka."
    dependsOn("dokkaGeneratePublicationHtml")
    from(tasks.named("dokkaGeneratePublicationHtml"))
    archiveClassifier.set("html-docs")
}

// Third-party licenses report

licenseReport {
    unionParentPomLicenses = false
    filters = arrayOf(
        LicenseBundleNormalizer(),
        ReduceDuplicateLicensesFilter(),
        ExcludeTransitiveDependenciesFilter()
    )
    configurations = arrayOf("releaseRuntimeClasspath")
    excludeBoms = true
    excludeOwnGroup = true
    renderers = arrayOf(InventoryMarkdownReportRenderer("licenses.md", POM_DESCRIPTION))
}

tasks.generateLicenseReport.configure {
    doLast {
        copy {
            from(layout.buildDirectory.file("reports/dependency-license/licenses.md"))
            into(rootDir)
        }
    }
}

// Build documentation and license report
tasks.register<Task>("buildDocumentation") {
    group = "documentation"
    description = "Builds the documentation and license report."
    dependsOn("dokkaGenerateMarkdown", "generateLicenseReport")
}
tasks.assemble.configure {
    finalizedBy("buildDocumentation")
}

// Publish

mavenPublishing {
    configure(
        AndroidMultiVariantLibrary(
            sourcesJar = true,
            publishJavadocJar = true,
            setOf("release")
        )
    )
    pom {
        ciManagement {
            system = "github"
            url = "${POM_SCM_URL}/actions"
        }
    }
}
// handle java.lang.UnsupportedOperationException: PermittedSubclasses requires ASM9
// when publishing module
afterEvaluate {
    tasks.named("javaDocReleaseGeneration").configure {
        enabled = false
    }
}

val coverageExclusions = listOf(
    "**/databinding/*Binding.*",
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    // butterKnife
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/Lambda\$*.class",
    "**/Lambda.class",
    "**/*Lambda.class",
    "**/*Lambda*.class",
    "**/*_MembersInjector.class",
    "**/Dagger*Component*.*",
    "**/*Module_*Factory.class",
    "**/di/module/*",
    "**/*_Factory*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    // kotlin
    "**/*MapperImpl*.*",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*\$Lambda\$*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*"
)

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
