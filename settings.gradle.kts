pluginManagement {
    repositories {
        google()  // Google repository
        mavenCentral()  // Maven Central repository
        gradlePluginPortal()  // Gradle Plugin Portal
    }
}

dependencyResolutionManagement {
    repositories {
        google()  // Google repository
        mavenCentral()  // Maven Central repository
        maven { url = uri("https://jitpack.io") }  // JitPack repository if needed
    }
}

rootProject.name = "HopShop"
include(":app")
