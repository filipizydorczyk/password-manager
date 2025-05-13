package pl.filipizydorczyk.passwordmanager

data class PasswordDataModel(
    val iv: String,
    val encryptedData: String
)

fun PasswordDataModel.toJson(): String {
    return "{\"iv\": \"${this.iv}\", \"encryptedData\": \"${this.encryptedData}\"}"
}
