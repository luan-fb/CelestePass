plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    //id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.luanferreira.celestepass"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.luanferreira.celestepass"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")


    // Firebase BoM
    val firebaseBomVersion = "33.1.1"
   // implementation(platform("com.google.firebase:firebase-bom:$firebaseBomVersion"))

    // Firebase
    //implementation("com.google.firebase:firebase-firestore")
    //implementation("com.google.firebase:firebase-messaging")
    //implementation("com.google.firebase:firebase-auth")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // ViewModel & LiveData
    val lifecycleVersion = "2.8.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.fragment:fragment-ktx:1.8.0")


    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")


    // Retrofit para chamadas de API
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Ou a versão mais recente
    // Conversor Gson para Retrofit (para interpretar o JSON da API)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil para carregar imagens (escudos)
    implementation("io.coil-kt:coil:2.6.0") // Verifique a versão mais recente

    // OkHttp Logging Interceptor (para depurar chamadas de API, muito útil!)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Verifique a versão a versão

    

}