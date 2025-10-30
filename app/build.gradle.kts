import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.anggapamb.assessmenttesteratani"
    compileSdk = 36

    val properties = Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }

    defaultConfig {
        applicationId = "com.anggapamb.assessmenttesteratani"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            manifestPlaceholders["crashlyticsEnabled"] = false
            manifestPlaceholders["performanceEnabled"] = false
            manifestPlaceholders["useClearText"] = true

            buildConfigField("String", "baseUrl", "\"${properties.getProperty("baseUrl")}\"")
            buildConfigField("String", "token", "\"${properties.getProperty("token")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "baseUrl", "\"${properties.getProperty("baseUrl")}\"")
            buildConfigField("String", "token", "\"${properties.getProperty("token")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidcore.lite)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    /* dependency injection */
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}