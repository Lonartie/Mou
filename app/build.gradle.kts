import java.io.FileInputStream
import java.util.Properties

fun loadConfigFile(): Properties {
    val props = Properties()
    props.load(FileInputStream(rootProject.file("config/secrets.properties")))
    return props
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.team.app"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.team.app"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.team.app.HiltTestRunner"
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

    buildTypes.forEach {
        it.buildConfigField("String", "STOCK_API_KEY", "\"${loadConfigFile()["STOCK_API_KEY"] as String}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
        }
    }
}

dependencies {
    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.test:monitor:1.6.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Pull to refresh
    implementation("eu.bambooapps:compose-material3-pullrefresh:1.0.1")

    // Navigation compose
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.4.0")

    // Network
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Hilt worker support
    implementation("androidx.hilt:hilt-work:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    // Database
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Workmanager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coil
    implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Plot
    implementation("com.github.madrapps:plot:0.1.1")

    // JUnit Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    // Dagger Tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.48.1")
}