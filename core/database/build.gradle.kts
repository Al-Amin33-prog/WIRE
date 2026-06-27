plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.example.wire.core.database"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    
    // We need Hilt here to provide the database
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
