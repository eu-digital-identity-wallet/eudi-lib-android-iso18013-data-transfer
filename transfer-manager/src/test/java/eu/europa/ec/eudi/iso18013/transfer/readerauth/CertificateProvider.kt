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
package eu.europa.ec.eudi.iso18013.transfer.readerauth

import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

fun loadCert(): X509Certificate {
    return loadCertificate(cert)
}

fun loadInvalidCert(): X509Certificate {
    return loadCertificate(invalidCert)
}

fun loadTrustCert(): X509Certificate {
    return loadCertificate(trustCert)
}

private fun loadCertificate(cert: String): X509Certificate {
    val certificateFactory = CertificateFactory.getInstance("X.509")
    val certificateBytes = cert.toByteArray()
    val bais = ByteArrayInputStream(certificateBytes)
    val certificate = certificateFactory.generateCertificate(bais) as X509Certificate
    bais.close()

    return certificate
}

private val cert = """-----BEGIN CERTIFICATE-----
MIICNzCCAd2gAwIBAgIII545Wo0NixkwCgYIKoZIzj0EAwIwOzELMAkGA1UEBhMC
U0UxETAPBgNVBAoTCFNjeXRhbGVzMRkwFwYDVQQDExBTY3l0YWxlcyBSb290IENB
MB4XDTIyMDUxNjA4MzQwMFoXDTIzMTIxMTA4MzQwMFowMjEwMC4GA1UEAxMnc2N5
dGFsZXMgbWRvYyByZWFkZXIgYXV0aGVudGljYXRpb24gMDAxMFkwEwYHKoZIzj0C
AQYIKoZIzj0DAQcDQgAEJmM5OLeg4V2frBce5J1yTFbQJ0rOCM6zsKeiNz736k8/
5osL3wphIHFeMJL8E1zKObhKPTMidBXJ68IHMFEwkqOB0zCB0DAMBgNVHRMBAf8E
AjAAMB0GA1UdDgQWBBTg7CkvZqbdFfEv+6IPh/nLWvZkrTAfBgNVHSMEGDAWgBRK
b7Q/bk68GFXJwg9Vk1mHfBU1NDAOBgNVHQ8BAf8EBAMCB4AwEgYDVR0lBAswCQYH
KIGMXQUBBjAiBgNVHRIEGzAZhhdodHRwOi8vd3d3LnNjeXRhbGVzLmNvbTA4BgNV
HR8EMTAvMC2gK6AphidodHRwczovL3N0YXRpYy5tZG9jLmlkL2NybC9zY3l0YWxl
cy5jcmwwCgYIKoZIzj0EAwIDSAAwRQIgLCdgkKOjv8qad6V0UHPiLcmMp0DXWMrP
B2lpHr9KsAwCIQDEjAcdYiidxtf2pZUCwdt6utVUU10EgcQVObSu1Q5img==
-----END CERTIFICATE-----
""".trimIndent()

private val invalidCert = """-----BEGIN CERTIFICATE-----   
MIIDazCCAlOgAwIBAgIUDd7Mm6qLoItjEhp+EpN7lTAmK1AwDQYJKoZIhvcNAQEL
BQAwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoM
GEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZDAeFw0yMzA3MjQxMDE0NTJaFw0zMzA3
MjExMDE0NTJaMEUxCzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEw
HwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQwggEiMA0GCSqGSIb3DQEB
AQUAA4IBDwAwggEKAoIBAQDEdo2/C6xzYSS6Nh/yDJo6IdznZi4OHQoZZd4MSvEF
WaYfuWWglywBMwKbI9u16abilsyn1YA+UxU/Egb2SZd565hz0FkZMCuVcRfva7lI
V1J5eAeNmtceu4MQRkXxBeI0cDYtS33GUD3X29DvWsVXBg9k0qiajXP0kokqzJCm
zGyzs8T4/LyduV9Bvk3sj6COwWPgbnemTXd/MO16AMly/3lt2PQO74wgX+rNtE0R
Y+WcuNo+U9wn75c0LKUJ5aQhSp183yqw6OESX3bujFNg0Txyd5toauW509VZOjJL
arw560uGPFvEBVYfxGm+sn+znjJ7ctFntH+fIAdY7JBHAgMBAAGjUzBRMB0GA1Ud
DgQWBBS65jlNMl1hz0dA0WAn7Q+qNPO7kzAfBgNVHSMEGDAWgBS65jlNMl1hz0dA
0WAn7Q+qNPO7kzAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAy
zP7WmGdE72dNuM4JL8WhMFhtDnO2FeYAfSQtnlZ+2ALT7nKXuHKZSGzFTUTZ56DM
FYtv32yon1QhcQmbtn6NmZXOHGBT7BEc7a9KE8D6gubVAhth8LYb5EMXPq2POeZq
EKNW8MtixcEtsa06sAkvUIkq3Q2ZJHjZ0em1Z9A7dAE1lO8TTtKrXQC3clVH4KiF
w4wJUsEVYKqcBQwQXiCeehcTD+YZLwN1pRzOLv9WMgr9jAt0zp8zHLbUepAu5ZcQ
1qMpVebrWdNwcOF7nOSKEkh54KzOJgAPlX6JV4bRAVziUYVxtLekc/iuJxUy63Ro
ZCn4vkqI1/UU8PKEXcsZ
-----END CERTIFICATE-----
""".trimIndent()

private val trustCert = """-----BEGIN CERTIFICATE-----
MIICDzCCAbWgAwIBAgIJfXsIRsWZBcBTMAoGCCqGSM49BAMCMDsxCzAJBgNVBAYT
AlNFMREwDwYDVQQKEwhTY3l0YWxlczEZMBcGA1UEAxMQU2N5dGFsZXMgUm9vdCBD
QTAeFw0yMjAxMDExMjAwMDBaFw0zMjAxMDExMjAwMDBaMDsxCzAJBgNVBAYTAlNF
MREwDwYDVQQKEwhTY3l0YWxlczEZMBcGA1UEAxMQU2N5dGFsZXMgUm9vdCBDQTBZ
MBMGByqGSM49AgEGCCqGSM49AwEHA0IABIzq9cNAf2RdNgiBUIs81s1OjR5BQfey
2SqeUrilq8OF4+43EWE4xj7FpvME2fIU9xDj33qMDeDaEvXJXGr7iJ6jgaEwgZ4w
EgYDVR0TAQH/BAgwBgEB/wIBADAdBgNVHQ4EFgQUSm+0P25OvBhVycIPVZNZh3wV
NTQwCwYDVR0PBAQDAgEGMCIGA1UdEgQbMBmGF2h0dHA6Ly93d3cuc2N5dGFsZXMu
Y29tMDgGA1UdHwQxMC8wLaAroCmGJ2h0dHBzOi8vc3RhdGljLm1kb2MuaWQvY3Js
L3NjeXRhbGVzLmNybDAKBggqhkjOPQQDAgNIADBFAiA34hF87sx96Z38W48kR88q
gEoCIgOYjCLLP8iUuardIAIhAK0Z63sJ2cISZJsAf0fznPL83+/xMhoOtMmlmBr2
BiWO
-----END CERTIFICATE-----
""".trimIndent()
