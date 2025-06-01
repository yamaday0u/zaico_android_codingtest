plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version libs.versions.kotlin.get() // Kotlinのバージョン（1.9.24）を動的に使用 HttpClientのPOSTでここが原因でエラーしていた。

    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "jp.co.zaico.codingtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "jp.co.zaico.codingtest"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.android.v164)


    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler.android)

    // test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4") // 使用しているCoroutinesのバージョンに合わせる
    testImplementation("io.mockk:mockk:1.13.8") // MockK（モックライブラリ）
    // Ktor Client Mock Engine (ViewModelのHTTP Clientをモックするため)
    testImplementation("io.ktor:ktor-client-mock:2.0.0")
    testImplementation(kotlin("test"))
}