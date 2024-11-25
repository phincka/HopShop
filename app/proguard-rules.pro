# ProGuard config for Android

# Zachowaj linie numerów dla debugowania
-keepattributes SourceFile,LineNumberTable

# Ukryj oryginalną nazwę pliku źródłowego
-renamesourcefileattribute SourceFile

# Zachowaj wszystkie klasy pochodzące z Android SDK
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Zachowaj metody i klasy Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Zachowaj klasy i metody wykorzystywane przez Kotlin
-keepclassmembers class kotlin.Metadata { *; }
-keepclassmembers class kotlin.reflect.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Zachowaj klasy i metody wykorzystywane przez Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Zachowaj klasy i metody dla Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
-keepclassmembers class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# Coil - zachowanie klas potrzebnych do ładowania obrazów
-keep class coil.** { *; }
-dontwarn coil.**

# Dodatkowe reguły dla Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Dodatkowe reguły dla WebView z JavaScript (jeśli używasz)
# -keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
# }

# Zachowanie klas używanych przez JUnit i Espresso do testowania
-keep class org.junit.** { *; }
-dontwarn org.junit.**
-keep class androidx.test.** { *; }
-dontwarn androidx.test.**

# Reguły ogólne dla zapobiegania nadmiernej optymalizacji
-dontoptimize
-dontobfuscate

# Zachowaj klasy wykorzystywane przez system loggerów (jeśli używasz)
-keep class timber.log.Timber { *; }
-dontwarn timber.log.**
