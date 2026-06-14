plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.wire.core.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
}
