package pl.filipizydorczyk.passwordmanager

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


fun encryptString(plainText: String, key: ByteArray, ivHexString: String): String? {
    try {
        val ivBytes = ivHexString.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes) // should be creating other iv in node?
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return encryptedBytes.joinToString("") { "%02x".format(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun encryptString(plainText: String, key: ByteArray): PasswordDataModel? {
    val iv = ByteArray(16).also { SecureRandom().nextBytes(it) }
    val ivString = iv.joinToString("") { "%02x".format(it) }
    val pass = encryptString(plainText, key, ivString) ?: return null

    return PasswordDataModel(ivString, pass)
}

fun decryptString(encryptedHexText: String, key: ByteArray, ivHexString: String): String? {
    try {
        val ivBytes = ivHexString.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        val plainTextBytes = encryptedHexText.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decryptedBytes = cipher.doFinal(plainTextBytes)
        return String(decryptedBytes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
