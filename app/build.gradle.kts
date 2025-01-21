plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
}

android {
    namespace = "pl.hincka.hopshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "pl.hincka.hopshop"
        minSdk = 35
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    // KSP configuration for Compose Destinations
    ksp {
        arg("compose-destinations.codeGenPackageName", "pl.hincka.hopshop.nav") // Zmien na odpowiedni pakiet
    }
}

dependencies {
    // Compose Destinations
    implementation(libs.core)
    implementation(libs.firebase.database.ktx)
    ksp(libs.ksp)
    implementation(libs.animations.core)

    implementation(libs.zxing.core)
    implementation(libs.androidx.compose.ui.ui)

    // Koin Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    // Accompanist for insets
    implementation(libs.accompanist.insets)

    // Icons and UI components
    implementation(libs.androidx.material.icons.extended)

    // Dialogs
    implementation(libs.datetime)

    // Image loading with Coil
    implementation(libs.coil.compose)

    // AndroidX core and lifecycle
    implementation(libs.androidx.core.ktx.v1150)
    implementation(libs.androidx.lifecycle.runtime.ktx.v287)
    implementation(libs.androidx.activity.compose.v1100)

    // Compose BOM (Bill of Materials)
    implementation(platform(libs.androidx.compose.bom.v20240300))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // CameraX dependencies
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    implementation(libs.accompanist.permissions)

    implementation(libs.zxing.core)

    implementation(libs.guava)


    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20250100))
    androidTestImplementation(libs.ui.test.junit4)

    // Debugging tools
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
