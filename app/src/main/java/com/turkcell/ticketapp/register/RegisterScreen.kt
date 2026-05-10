package com.turkcell.ticketapp.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Senin LoginScreen'deki gradyanın aynısı
    val gradient = Brush.verticalGradient(listOf(Color(0xFF1B5E20), Color(0xFF000000)))

    Box(modifier = Modifier.fillMaxSize().background(gradient)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ConfirmationNumber,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color(0xFF81C784)
            )

            Text("Hesap Oluştur", color = Color.White, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(48.dp))

            // E-posta Alanı
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("E-posta", color = Color(0xFFA5D6A7)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Şifre Alanı
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Şifre", color = Color(0xFFA5D6A7)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Şifre Tekrar Alanı
            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.confirmPassword = it },
                label = { Text("Şşifre Tekrar", color = Color(0xFFA5D6A7)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            if (viewModel.errorMessage != null) {
                Text(text = viewModel.errorMessage!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Kayıt Butonu
            Button(
                onClick = { viewModel.onRegisterClick(onRegisterSuccess) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Kayıt Ol", fontSize = 18.sp)
                }
            }

            // Geri Dönüş Linki
            TextButton(onClick = onNavigateToLogin) {
                Text("Zaten hesabın var mı? Giriş Yap", color = Color(0xFFA5D6A7))
            }
        }
    }
}