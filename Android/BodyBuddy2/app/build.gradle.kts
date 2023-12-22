plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.choiri.bodybuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.choiri.bodybuddy"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        debug {
            buildConfigField("String", "API_URL", "\"https://authbackend-ac6j6yrowq-et.a.run.app/\"")
        }
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
    buildFeatures {
        mlModelBinding = true
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation ("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation ("org.tensorflow:tensorflow-lite:2.5.0")
    implementation ("org.tensorflow:tensorflow-lite-gpu:2.5.0")
    implementation ("org.tensorflow:tensorflow-lite-support:0.3.0")
    implementation ("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation ("androidx.activity:activity-ktx:1.2.3")
    implementation ("com.google.android.material:material:1.3.0")
    implementation ("androidx.fragment:fragment-ktx:1.3.5")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.paging:paging-common-android:3.3.0-alpha02")
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")

    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    //api
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.5")

    //core
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}