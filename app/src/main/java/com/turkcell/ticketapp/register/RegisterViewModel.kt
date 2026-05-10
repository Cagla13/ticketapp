package com.turkcell.ticketapp.register

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onRegisterClick(onSuccess: () -> Unit) {
        // Validasyonlar
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Tüm alanları doldurunuz."
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Şifreler eşleşmiyor."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = authRepository.register(email, password)
            isLoading = false

            result.onSuccess {
                onSuccess()
            }.onFailure {
                errorMessage = it.message ?: "Kayıt başarısız."
            }
        }
    }
}