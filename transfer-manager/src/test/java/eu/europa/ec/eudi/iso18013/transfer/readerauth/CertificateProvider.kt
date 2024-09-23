/*
 * Copyright (c) 2023-2024 European Commission
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
package eu.europa.ec.eudi.iso18013.transfer.readerauth

import org.bouncycastle.asn1.ASN1EncodableVector
import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.bouncycastle.asn1.DERSequence
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.CRLDistPoint
import org.bouncycastle.asn1.x509.DistributionPoint
import org.bouncycastle.asn1.x509.DistributionPointName
import org.bouncycastle.asn1.x509.ExtendedKeyUsage
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.asn1.x509.KeyUsage
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import java.time.Instant
import java.util.Date

private const val signatureAlgorithm: String = "SHA256withECDSA"
private val extUtils = JcaX509ExtensionUtils()
private val trustedKeyPair: KeyPair by lazy {
    KeyPairGenerator.getInstance("EC").apply {
        initialize(ECGenParameterSpec("secp256r1"))
    }.generateKeyPair()
}

val trustedCertificate: X509Certificate by lazy {
    val serialNumber = BigInteger(64, SecureRandom())
    val issuer = X500Name("CN=Root CA")
    val subject = X500Name("CN=Root CA")
    val notBefore = Date.from(Instant.now().minusSeconds(86400))
    val notAfter = Date(notBefore.time + 30 * 86400000L)
    val builder = JcaX509v3CertificateBuilder(
        issuer,
        serialNumber,
        notBefore,
        notAfter,
        subject,
        trustedKeyPair.public
    ).apply {
        addExtension(
            Extension.subjectKeyIdentifier,
            false,
            extUtils.createSubjectKeyIdentifier(trustedKeyPair.public)
        )
    }
    val signer = JcaContentSignerBuilder(signatureAlgorithm).build(trustedKeyPair.private)
    JcaX509CertificateConverter().getCertificate(builder.build(signer))
}

val validCertificate: X509Certificate by lazy {
    val keyPair = KeyPairGenerator.getInstance("EC").apply {
        initialize(ECGenParameterSpec("secp256r1"))
    }.generateKeyPair()
    val serialNumber = BigInteger(64, SecureRandom())
    val issuer = X500Name(trustedCertificate.issuerX500Principal.name)
    val subject = X500Name("CN=ValidCertificate")
    val notBefore = Date()
    val notAfter = Date(notBefore.time + 30 * 86400000L)
    val builder = JcaX509v3CertificateBuilder(
        issuer,
        serialNumber,
        notBefore,
        notAfter,
        subject,
        keyPair.public
    ).apply {
        addExtension(
            Extension.subjectKeyIdentifier,
            false,
            extUtils.createSubjectKeyIdentifier(keyPair.public)
        )
        addExtension(
            Extension.authorityKeyIdentifier,
            false,
            extUtils.createAuthorityKeyIdentifier(trustedCertificate)
        )
        addExtension(Extension.keyUsage, false, KeyUsage(KeyUsage.digitalSignature))
        addExtension(Extension.extendedKeyUsage, false, ExtendedKeyUsage.getInstance(
            ASN1EncodableVector().apply {
                add(ASN1ObjectIdentifier("1.0.18013.5.1.6"))
            }.let { DERSequence(it) }
        ))
        addExtension(Extension.cRLDistributionPoints,
            false,
            GeneralNames(GeneralName(GeneralName.uniformResourceIdentifier, "https://example.com"))
                .let {
                    DistributionPoint(DistributionPointName(it), null, null)
                }.let {
                    CRLDistPoint(arrayOf(it))
                })
    }
    val signer = JcaContentSignerBuilder(signatureAlgorithm).build(trustedKeyPair.private)
    JcaX509CertificateConverter().getCertificate(builder.build(signer))
}

private val untrustedKeyPair = KeyPairGenerator.getInstance("RSA").apply {
    initialize(2048)
}.generateKeyPair()

private val untrustedRoot: X509Certificate
    get() {
        val serialNumber = BigInteger(64, SecureRandom())
        val issuer = X500Name("CN=Untrusted Root CA")
        val subject = X500Name("CN=Untrusted Root CA")
        val notBefore = Date.from(Instant.now().minusSeconds(86400))
        val notAfter = Date(notBefore.time + 30 * 86400000L)
        val builder = JcaX509v3CertificateBuilder(
            issuer,
            serialNumber,
            notBefore,
            notAfter,
            subject,
            untrustedKeyPair.public
        ).apply {
            addExtension(
                Extension.subjectKeyIdentifier,
                false,
                extUtils.createSubjectKeyIdentifier(untrustedKeyPair.public)
            )
        }
        val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(untrustedKeyPair.private)
        return JcaX509CertificateConverter().getCertificate(builder.build(signer))
    }

val invalidCertificate: X509Certificate by lazy {
    val keyPair = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }.generateKeyPair()
    val serialNumber = BigInteger(64, SecureRandom())
    val issuer = X500Name(untrustedRoot.issuerX500Principal.name)
    val subject = X500Name("CN=ValidCertificate")
    val notBefore = Date.from(Instant.now().minusSeconds(10))
    // add 10 years to notBefore
    val notAfter = Date.from(notBefore.toInstant().plusSeconds(10 * 365 * 86400L))
    val builder = JcaX509v3CertificateBuilder(
        issuer,
        serialNumber,
        notBefore,
        notAfter,
        subject,
        keyPair.public
    )
    val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(untrustedKeyPair.private)
    JcaX509CertificateConverter().getCertificate(builder.build(signer))


}


fun loadCert(): X509Certificate = validCertificate

fun loadInvalidCert(): X509Certificate = invalidCertificate

fun loadTrustCert(): X509Certificate = trustedCertificate
