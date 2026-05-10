package com.turkcell.data.util

sealed interface ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>
    data class Error(val error: Throwable) : ApiResult<Nothing>
    // Buradaki Loading veya diğer durumları silebilir ya da ekleyebilirsin
}

// Fold fonksiyonunu sealed interface'in dışına taşıyoruz
inline fun <T, R> ApiResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (Throwable) -> R
): R = when (this) {
    is ApiResult.Success -> onSuccess(data)
    is ApiResult.Error -> onError(error)
}