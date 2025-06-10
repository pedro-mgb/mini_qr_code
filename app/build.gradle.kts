import com.google.protobuf.gradle.GenerateProtoTask

plugins {
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.pedroid.qrcodecompose.androidapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pedroid.qrcodecompose.androidapp"
        minSdk = 21
        targetSdk = 36
        versionCode = 10
        versionName = "0.7.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("qr_app_release") {
            keyAlias = System.getenv("PEDROID_QR_CODE_KEY_ALIAS") ?: ""
            keyPassword = System.getenv("PEDROID_QR_CODE_KEY_PW") ?: ""
            storeFile = file(System.getenv("PEDROID_QR_CODE_KEYSTORE_FILE_NAME") ?: "invalidPath")
            storePassword = System.getenv("PEDROID_QR_CODE_KEY_STORE_PW") ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("qr_app_release")
        }
    }

    // include all languages in aab, to ensure changing language works in the future
    //   shouldn't add much extra size to the apk users download in play store
    bundle.language.enableSplit = false

    room {
        schemaDirectory("$projectDir/dbSchemas")
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("plain")
}

dependencies {

    implementation(project(":qrcodecomposelib"))
    implementation(project(":qrcodecomposelibmlkit"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.camera2)
    implementation(libs.androidx.contraintlayout.compose)
    // region compose bom
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.animation)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material3.adaptive)
    implementation(libs.material3.windowSizeClass)
    // endregion compose bom
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    implementation(libs.arrow.optics)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.protobuf.kotlin.lite)
    // region test utils
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
    // endregion test utils
    //region baseline profile
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
    //endregion baseline profile

    coreLibraryDesugaring(libs.desugaring.jdk)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.arrow.optics.ksp)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.turbine)


    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.ui.test.junit4)

    kspAndroidTest(libs.androidx.room.compiler)

    debugImplementation(libs.androidx.test.monitor)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}

// TEMPORARY fix for proto-datastore errors with ksp and hilt
// credits: https://github.com/google/ksp/issues/1590#issuecomment-1826387452
androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val protoTask =
                project.tasks.getByName("generate" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Proto") as GenerateProtoTask

            project.tasks.getByName("ksp" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Kotlin") {
                dependsOn(protoTask)
                (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                    protoTask.outputBaseDir
                )
            }
        }
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    // Generates the java Protobuf-lite code for the Protobuf's in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
