package com.akochdev.data.extensions

import java.math.BigInteger
import java.security.MessageDigest

private const val MD5_ALGORITHM = "MD5"
private const val POSITIVE = 1
private const val STRING_RADIX = 16
private const val KEY_LENGTH = 32
private const val ZERO_AS_CHAR = '0'

fun String.toMarvelHash(): String {
    val md5Digest = MessageDigest.getInstance(MD5_ALGORITHM)
    return BigInteger(POSITIVE, md5Digest.digest(this.toByteArray()))
        .toString(STRING_RADIX)
        .padStart(KEY_LENGTH, ZERO_AS_CHAR)
}
