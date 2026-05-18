package com.turkcell.data.network
import com.turkcell.data.dto.RefreshRequestDto
import com.turkcell.data.dto.TokenPairDto
import com.turkcell.data.util.TokenStore
import com.turkcell.data.remote.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val authApiProvider: () -> AuthApi // Koin circular dependency (kısır döngü) yapmasın diye lazy çağırıyoruz
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenStore.getRefreshToken() ?: return null

        synchronized(this) {
            val currentAccessToken = tokenStore.getAccessToken()
            val requestToken = response.request.header("Authorization")?.replace("Bearer ", "")

            // Eğer başka bir thread token'ı çoktan yenilediyse, direkt yeni token'ı kullan
            if (currentAccessToken != requestToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            // Senkron bir şekilde askıya alıp (blocking) token yenileme isteği atıyoruz
            return runBlocking {
                try {
                    val api = authApiProvider()

                    // Hatalı satırı bu şekilde güncelliyoruz:
                    val newPair: TokenPairDto = api.refresh(RefreshRequestDto(refreshToken = refreshToken))

                    // Yeni gelen token çiftini cihaza kaydet
                    tokenStore.saveTokens(newPair.accessToken, newPair.refreshToken)

                    // Başarısız olan ilk isteği yeni gelen access token ile süsleyip tekrar gönder
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newPair.accessToken}")
                        .build()
                } catch (e: Exception) {
                    // Yenileme tamamen başarısız olduysa token'ları sil
                    tokenStore.clear()
                    null
                }
            }
        }
    }
}