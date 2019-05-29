package com.shengbojia.notes.data

/**
 * Generic class for holding data and loading status when performing database operations.
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) :Result<T>()
    data class Error(val ex: Exception) : Result<Nothing>()
    // Singleton that doesn't hold any data
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success: data=$data"
            is Error -> "Error: exception=$ex"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

