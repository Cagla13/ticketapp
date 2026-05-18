 Ticket App — Multi-Module Clean Architecture & Auth Lifecycle Management

Bu proje, **Turkcell Geleceği Yazanlar Geliştirici Akademisi (GYGY)** kapsamında geliştirilen, modern Android mimari standartlarına (`Clean Architecture` ve `Multi-Module`) tam uyumlu bir bilet ve etkinlik yönetimi uygulamasıdır. 

Projede JWT tabanlı kimlik doğrulama döngüsü, otomatik token yenileme mekanizmaları ve dinamik veri listeleme özellikleri sıfırdan ve tamamen katmanlı mimari prensiplerine sadık kalınarak implement edilmiştir.

---

##  Proje Mimarisi (Multi-Module Clean Architecture)

Uygulama, kodun sürdürülebilirliği, test edilebilirliği ve ekipler arası bağımsız geliştirilebilmesi amacıyla **3 ana modüle** ayrılmıştır:

1. **`:app` (Presentation Layer):** Jetpack Compose arayüz bileşenlerini, ekran tasarımlarını (`Login`, `Register`, `Home`), State yönetimini (`StateFlow`, `UI State`) ve Koin ViewModel enjeksiyonlarını barındırır.
2. **`:core` (Domain Layer):** Uygulamanın iş mantığını (Business Logic) içerir. Tamamen saf Kotlin (`Pure Kotlin`) kütüphanesidir; hiçbir framework bağımlılığı (Retrofit, Android SDK vb.) taşımaz. `User`, `AuthSession` gibi veri modellerini ve `AuthRepository` arayüzünü (interface) içerir.
3. **`:data` (Data Layer):** Verinin neredeyse tamamının yönetildiği katmandır. API servisleri (`Retrofit Interface`), veri transfer nesneleri (`DTO`), veritabanı/SharedPreferences işlemleri ve `AuthRepositoryImpl` bu katmanda yer alır.

---

##  Gerçekleştirilen Teknik Özellikler & Ödev Maddeleri

Hocanın talep ettiği istekler doğrultusunda, uygulamanın kimlik doğrulama (Authentication) ve veri çekme altyapısına şu gelişmiş OkHttp ve Jetpack Compose özellikleri entegre edilmiştir:

### 1. Token Store (Cihazda Güvenli Veri Saklama)
* API'den başarılı bir giriş (`/auth/login`) yapıldığında dönen `accessToken` ve `refreshToken` değerlerini cihaz hafızasında kalıcı olarak tutmak amacıyla `TokenStore.kt` yazılmıştır.
* Android `SharedPreferences` altyapısı kullanılarak verilerin oturum kapansa dahi cihazda güvenle saklanması sağlanmıştır.

### 2. Auth Interceptor (Her İstekte Otomatik Token Gönderimi)
* `AuthInterceptor.kt` sınıfı aracılığıyla API'ye giden tüm ağ istekleri merkezi bir noktadan yakalanır.
* Eğer cihazda kayıtlı bir `accessToken` varsa, isteğin başlığına (Header) otomatik olarak `Authorization: Bearer <token>` ifadesi eklenir. Bu sayede UI veya ViewModel katmanında her istek için manuel token ekleme zahmeti ortadan kaldırılmıştır.

### 3. Token Authenticator (401 Unauthorized & Arka Planda Token Rotasyonu)
* Token sürelerinin dolması durumunda API'nin döndüğü `401 Unauthorized` hatasını yakalamak üzere `TokenAuthenticator.kt` (OkHttp Authenticator) yazılmıştır.
* Kullanıcı arayüzünde hiçbir kesinti veya yüklenme ekranı hissetmeden, arka planda (thread-safe ve `runBlocking` senkronizasyonunda) `/auth/refresh` endpoint'ine gidilerek yeni token çifti istenir.
* Yeni token'lar `TokenStore`'a güncellenir ve başarısız olan ilk istek yeni token ile otomatik olarak tekrarlanır. Yenileme tamamen başarısız olursa güvenli çıkış (`clear`) tetiklenir.

### 4. Ana Sayfa (Etkinlikler ve Biletlerim) Listeleme Tasarımı
* Giriş başarıyla tamamlandığında `MainActivity` rotası üzerinden `HomeScreen.kt` paneline geçiş sağlanır.
* **Sekme Yönetimi:** Jetpack Compose `TabRow` ve `Tab` bileşenleri kullanılarak "Etkinlikler" ve "Biletlerim" adında iki sekmeli yapı kurulmuştur.
* **Dinamik Listeleme:** `LazyColumn` ve `Card` mimarisiyle, uygulamanın yeşil kurumsal renk paletine (Desaturated Emerald Green) uygun şık arayüz kartları tasarlanmıştır.

### 5. Swagger API Entegrasyonu
Uygulama, canlıda bulunan canlı backend servislerine (`https://tickets-api.halitkalayci.com/`) bağlanmıştır. Entegre edilen canlı endpoint'ler:
* `POST /auth/login` - Kullanıcı girişi ve token çiftinin alınması.
* `POST /auth/refresh` - Süresi dolan session'ın rotasyonu.
* `GET /events` - Güncel etkinliklerin ana sayfada listelenmesi.
* `GET /me/tickets` - Giriş yapmış kullanıcıya ait satın alınmış biletlerin dökümü.

---

##  Kullanılan Teknolojiler

* **Language:** Kotlin & Coroutines (Flow, StateFlow, runBlocking)
* **UI Framework:** Jetpack Compose (Material 3, Scaffold, TabRow, LazyColumn)
* **Dependency Injection:** Koin (Koin Android, Koin Compose)
* **Networking:** Retrofit2 & OkHttpClient (Interceptors, Authenticators)
* **Serialization:** Kotlinx Serialization Json Parser

---

##  Dosya Yapısı İncelemesi

Eklenen kritik mimari dosyaların konumları şu şekildedir:
```text
├── data/text
│   └── src/main/java/com/turkcell/data/
│       ├── di/DataModul.kt             # OkHttpClient, Retrofit ve Api servislerinin Koin tanımları
│       ├── dto/                        # CredentialsDto, RefreshRequestDto, TokenPairDto
│       ├── network/
│       │   ├── AuthInterceptor.kt     # Header'a Bearer Token ekleyen mekanizma
│       │   └── TokenAuthenticator.kt  # 401 hatasında /refresh tetikleyen mekanizma
│       ├── remote/
│       │   ├── AuthApi.kt             # Login, Register, Refresh endpoint tanımları
│       │   └── EventApi.kt            # Events ve Me/Tickets endpoint tanımları
│       ├── repository/
│       │   └── AuthRepositoryImpl.kt  # login() sonrası TokenStore.saveTokens() tetiklenen yer
│       └── util/
│           └── TokenStore.kt          # SharedPreferences bilet/token kayıt sınıfı
│
└── app/
    └── src/main/java/com/turkcell/ticketapp/
        ├── MainActivity.kt            # "login" -> "home" ekran geçiş kontrolü
        ├── di/AppModule.kt            # HomeViewModel Koin tanımı
        └── home/
            ├── HomeViewModel.kt       # EventApi'den verileri UI State akışı ile yöneten sınıf
            └── HomeScreen.kt          # Etkinlikler ve Biletlerim Compose UI tasarımı
<img width="352" height="622" alt="image" src="https://github.com/user-attachments/assets/c5cd0a80-7479-4dca-9235-768f1d5a08ca" />
