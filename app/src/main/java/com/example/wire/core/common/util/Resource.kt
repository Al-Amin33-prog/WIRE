package com.example.wire.core.common.util

/**
 * A professional wrapper for UI states.
 * This is what your ViewModel should expose to the UI.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: AppError) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

/**
 * A centralized way to handle all types of errors in the app.
 * This makes your app "Top 10%" because you can handle specific errors differently.
 */
sealed class AppError {
    sealed class Network : AppError() {
        object NoInternet : Network()
        object Timeout : Network()
        object ServerError : Network()
        object Unauthorized : Network()
        data class Unknown(val message: String?) : Network()
    }

    sealed class Database : AppError() {
        object DiskFull : Database()
        object NotFound : Database()
        data class Unknown(val message: String?) : Database()
    }

    data class Validation(val message: String) : AppError()
}

/**
 * Extension to convert AppError to a user-friendly string (using your strings.xml logic)
 */
fun AppError.toUserMessage(): String {
    return when (this) {
        is AppError.Network.NoInternet -> "Please check your internet connection"
        is AppError.Network.Unauthorized -> "Session expired. Please log in again"
        is AppError.Network.ServerError -> "Server is currently down. Try again later"
        is AppError.Validation -> this.message
        else -> "An unexpected error occurred"
    }
}
