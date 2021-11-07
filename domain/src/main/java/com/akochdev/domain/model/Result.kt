package com.akochdev.domain.model

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val value: T?) : Result<T>()

    data class Failure(
        val code: Int? = null,
        val msg: String? = null,
        val cause: Exception? = null
    ) : Result<Nothing>() {
        companion object {
            fun fromFailure(failure: Failure): Failure =
                Failure(failure.code, failure.msg, failure.cause)
        }
    }
}
