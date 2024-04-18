plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.tripnila"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tripnila"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-text-android:1.5.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")

    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.0")
    implementation("androidx.navigation:navigation-compose:2.7.2")
    implementation ("androidx.paging:paging-runtime:3.1.1")
    implementation ("androidx.paging:paging-compose:1.0.0-alpha16")
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    implementation("com.paypal.checkout:android-sdk:1.2.0")

    implementation("androidx.compose.material:material-icons-extended:1.5.3")

    // maps compose
    implementation ("com.google.maps.android:maps-compose:2.11.5")

    // google map services
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.1.0")

    // google maps utils
    implementation ("com.google.maps.android:android-maps-utils:3.4.0")

    // CHART
    implementation("com.patrykandpatrick.vico:compose:1.13.1")
    implementation("com.patrykandpatrick.vico:compose-m2:1.13.1")
    implementation("com.patrykandpatrick.vico:compose-m3:1.13.1")
    implementation("com.patrykandpatrick.vico:core:1.13.1")
    implementation("com.patrykandpatrick.vico:views:1.13.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Pags
    implementation ("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation ("com.wdullaer:materialdatetimepicker:4.2.3")
    implementation ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:1.3.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.maps.android:android-maps-utils:2.4.0")
    implementation ("com.google.maps:google-maps-services:2.1.0")
    implementation ("org.slf4j:slf4j-nop:2.0.0")

    implementation ("org.apache.poi:poi:5.2.5")
    implementation ("org.apache.poi:poi-ooxml:5.2.5")
}