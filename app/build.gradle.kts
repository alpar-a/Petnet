plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.petnet"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.petnet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))

    // Add the dependency for the Cloud Storage library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.storage)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.espresso.core)
    implementation(libs.play.services.tagmanager.v4.impl)
    implementation(libs.androidx.appcompat)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.material)
    implementation(libs.ui.tooling)
    implementation(libs.ui)
    implementation(libs.material3) // Latest stable Material3
    implementation(libs.androidx.ui.v160)
    implementation(libs.androidx.ui.tooling.v160)

    implementation(libs.androidx.material3.v120)
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.material3)
    implementation(libs.androidx.material.icons.extended.v160)
    implementation(libs.androidx.core.splashscreen)
        implementation(libs.coil.compose)

    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.coil.compose.v222)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.compose) // Fix for 'viewModel'
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)

    // ViewModel support
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Ensure these match your Compose BOM version
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.runtime.v143)
    implementation ("com.github.bumptech.glide:glide:4.14.2@aar")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

    // Jetpack Compose
    implementation(libs.androidx.ui.v160)
    implementation (libs.androidx.material3.v120)
    implementation (libs.ui.tooling.preview)

// Coil for image loading
    implementation (libs.coil.compose)
    implementation (libs.androidx.navigation.compose)
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation ("androidx.navigation:navigation-runtime-ktx:2.8.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation (libs.androidx.navigation.compose.v260)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.lifecycle.viewmodel.compose.v260)

    implementation("com.google.android.gms:play-services-maps:3.5.0")
    implementation("com.google.maps.android:maps-compose:2.13.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.libraries.places:places:3.1.0")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("org.json:json:20210307")

}