package com.turkcell.ticketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.turkcell.core.ui.theme.TicketAppTheme
import com.turkcell.ticketapp.login.LoginScreen
import com.turkcell.ticketapp.register.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketAppTheme {
                // Ekran geçişlerini yöneten durum değişkeni
                var currentScreen by remember { mutableStateOf("login") }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        "login" -> {
                            LoginScreen(
                                onNavigateToHome = {
                                    println("Giriş yapıldı, ana sayfaya yönlendiriliyor...")
                                },

                                onNavigateToRegister = {
                                    currentScreen = "register"
                                }
                            )
                        }

                        "register" -> {
                            RegisterScreen(
                                onRegisterSuccess = {

                                    currentScreen = "login"
                                },
                                onNavigateToLogin = {

                                    currentScreen = "login"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}