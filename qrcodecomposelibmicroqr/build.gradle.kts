plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlinter)
}

android {
    namespace = "com.pedroid.qrcodecomposelib.extensions.microqr"
    compileSdk = 37

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
    }
}

dependencies {
    implementation(project(":qrcodecomposelib"))
    // native (C++) decoder, the only one of the engines used here that can read Micro QR Codes
    implementation(libs.zxing.cpp.android)

    implementation(libs.androidx.camera2)
    implementation(libs.androidx.core.ktx)
    // to read the orientation of image files
    implementation(libs.androidx.exifinterface)

    testImplementation(libs.junit)
}
