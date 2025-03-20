package com.ammar.core.data.source.local.preference

object XOREncryptionHelper {
    private const val SECRET_KEY = "MySecretKey"

    fun encrypt(input: String): String {
        return input.mapIndexed { index, char ->
            char.code.xor(SECRET_KEY[index % SECRET_KEY.length].code).toChar()
        }.joinToString("")
    }

    fun decrypt(input: String): String {
        return encrypt(input)
    }
}
