/*
 * Copyright (c) 2023 European Commission
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
package eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.profile

import android.util.Log
import eu.europa.ec.eudi.iso18013.transfer.internal.TAG
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.DEROctetString
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import java.security.MessageDigest
import java.security.cert.X509Certificate
import java.util.Arrays

internal class SubjectKey : ProfileValidation {

    override fun validate(
        readerAuthCertificate: X509Certificate,
        trustCA: X509Certificate,
    ): Boolean {
        val byteArray =
            readerAuthCertificate.getExtensionValue(Extension.subjectKeyIdentifier.id) ?: run {
                return false
            }

        val subjectKeyIdentifier: SubjectKeyIdentifier =
            SubjectKeyIdentifier.getInstance(DEROctetString.getInstance(byteArray).octets)

        val publicKeyInfoByteArray: ByteArray = SubjectPublicKeyInfo.getInstance(
            ASN1Sequence.getInstance(readerAuthCertificate.publicKey.encoded),
        ).publicKeyData.octets

        val hash = MessageDigest.getInstance("SHA-1").digest(publicKeyInfoByteArray)

        return Arrays.equals(subjectKeyIdentifier.keyIdentifier, hash).also {
            Log.d(this.TAG, "SubjectKeyIdentifier: $it")
        }
    }
}
