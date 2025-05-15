package pl.filipizydorczyk.passwordmanager

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun encryptString(plainText: String, key: ByteArray, iv: String): String? {
    try {
        val ivBytes = iv.substring(0, 16).toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes) // should be creating other iv in node?
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
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

fun decryptString(encryptedText: String, key: ByteArray, iv: String): String? {
    try {
        val ivBytes = iv.substring(0, 16).toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
        return String(decryptedBytes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
