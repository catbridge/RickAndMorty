plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("androidx.navigation.safeargs")
    id ("kotlin-kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.rickandmorty"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val googleMapKey:String by project

        manifestPlaceholders["googleMapKey"]  = googleMapKey

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)

    //Navigation
    implementation(libs.bundles.androidx.navigation)

    //Retrofit
    implementation(libs.bundles.retrofit2)

    //Coil
    implementation(libs.io.coil)

    //Lifecycle
    implementation(libs.androidx.lifecycle.runtime)

    //Coroutines
    implementation(libs.kotlinx.coroutines)

    //Room
    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)

    //SwipeRefresh
    implementation(libs.androidx.swiperefreshlayout)

    //Fragment
    implementation(libs.androidx.fragment)

    //Koin
    implementation(libs.io.koin)

    // Google Maps
    implementation (libs.bundles.google.maps)

    //Circle ImageView
    implementation(libs.circleImageView)

    testImplementation(libs.junit)
    androidTestImplementation (libs.androidx.test.ext)
    androidTestImplementation (libs.androidx.test.espresso)
}