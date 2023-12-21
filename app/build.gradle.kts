plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false

}

android {
    namespace = "com.example.sinyal"
    compileSdk = 34


    buildFeatures {
        dataBinding = true
        viewBinding = true

    }
    defaultConfig {
        applicationId = "com.example.sinyal"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation ("androidx.test.espresso:espresso-contrib:3.4.0")
    implementation("com.google.android.ads:mediation-test-suite:3.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //special testing
    testImplementation ("androidx.arch.core:core-testing:2.1.0") // InstantTaskExecutorRule
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")

    //TestCoroutineDispatcher
    testImplementation ("org.mockito:mockito-core:3.12.4")
    testImplementation ("org.mockito:mockito-inline:3.12.4")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    testImplementation("io.strikt:strikt-core:0.31.0")
    androidTestImplementation ("com.squareup.okhttp3:mockwebserver3:5.0.0-alpha.2")

    //special instrumentation testing
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0") // InstantTaskExecutorRule
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")

    //TestCoroutineDispatcher//RecyclerViewActions
    debugImplementation ("androidx.fragment:fragment-testing:1.4.1")
    implementation ("androidx.test.espresso:espresso-idling-resource:3.4.0")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.4.0")//IntentsTestRule


    implementation ("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation ("androidx.room:room-paging:2.6.0")

    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")

    implementation ("com.github.bumptech.glide:glide:4.15.0")
    implementation ("id.zelory:compressor:3.0.1")

    // api
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.5")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.google.android.gms:play-services-ads-lite:21.0.0")

    // core
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
}