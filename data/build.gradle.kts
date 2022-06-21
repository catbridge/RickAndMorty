import com.android.build.gradle.internal.tasks.getProjectNativeLibs

plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        val baseUrl: String  by project
        buildConfigField("String", "RAM_URL", baseUrl)

        val rickImageUrl: String by project
        buildConfigField("String", "RICK_IMG", rickImageUrl)

        val rickName: String by project
        buildConfigField("String", "RICK_NAME", rickName)

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)

    //Koin
    implementation(libs.io.koin)

    //Room
    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)

    //Retrofit
    implementation(libs.bundles.retrofit2)

    testImplementation(libs.junit)
    androidTestImplementation (libs.androidx.test.ext)
    androidTestImplementation (libs.androidx.test.espresso)
}