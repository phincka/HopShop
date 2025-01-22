plugins {
    id("com.android.application")  // Wtyczka dla aplikacji Android
    id("org.jetbrains.kotlin.android")  // Wtyczka dla Kotlina na Androida
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"  // Wtyczka dla Compose
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"  // KSP (Kotlin Symbol Processing)
    id("com.google.gms.google-services")  // Google Services (np. Firebase)
    id("com.google.firebase.appdistribution")  // Firebase App Distribution
}

android {
    namespace = "pl.hincka.hopshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "pl.hincka.hopshop"
        minSdk = 29
        targetSdk = 35

        versionCode = 2
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true  // Włączenie Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"  // Wersja rozszerzenia kompilatora Compose
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")  // Źródła w Kotlinie
    }

    // KSP konfiguracja dla Compose Destinations
    ksp {
        arg("compose-destinations.codeGenPackageName", "pl.hincka.hopshop.nav")
    }
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0-beta09")
    ksp("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0-beta09")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")

    // KSP dla Compose Destinations
    ksp("androidx.room:room-compiler:2.6.1")

    // Barcode Scanning
    implementation("com.google.zxing:core:3.5.3")

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.ui:ui-test-junit4:1.7.6")

    // Koin DI
    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose-navigation:3.5.3")
    implementation("io.insert-koin:koin-annotations:1.2.2")
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")

    // Accompanist
    implementation("com.google.accompanist:accompanist-insets:0.30.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Material Icons and UI Components
    implementation("androidx.compose.material:material-icons-extended:1.7.6")

    // DateTime Dialogs
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    // Image loading with Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    // AndroidX core and lifecycle
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.ui:ui-graphics:1.7.6")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation("androidx.compose.material3:material3:1.3.1")

    // CameraX dependencies
    implementation("androidx.camera:camera-core:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")
    implementation("androidx.camera:camera-extensions:1.4.1")
    implementation("androidx.camera:camera-camera2:1.4.1")

    // Guava utilities
    implementation("com.google.guava:guava:31.1-android")

    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")

    // Debugging tools
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.6")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.6")

    implementation("io.github.raamcosta.compose-destinations:core:1.11.8")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.11.8")
}
