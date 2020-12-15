plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint")
}


android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.0")

    defaultConfig {
        applicationId = "com.example.syncedtexteditor"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", getProperty("local.properties", "BASE_URL"))
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(Dependencies.kotlin)

    // Android
    implementation(Android.appcompat)
    implementation(Android.activityKtx)
    implementation(Android.coreKtx)

    // Hilt + Dagger
    implementation(Hilt.hiltAndroid)
    implementation(Hilt.hiltViewModel)
    kapt(Hilt.daggerCompiler)
    kapt(Hilt.hiltCompiler)

    //RxJava
    implementation(RxJava.rxAndroid)
    implementation(RxJava.rxjava2)

    // Timber
    implementation(Timber.timber)

    //Retrofit
    implementation(OkHttp3.loggingInterceptor)
    implementation(Retrofit2.adapterRxjava2)
    implementation(Retrofit2.converterGson)
    implementation(Retrofit2.retrofit)

    //Chucker
    debugImplementation(Chucker.debug)
    releaseImplementation(Chucker.release)
}

fun getProperty(filename: String, propName: String): String {
    val propFile = rootProject.file(filename)
    return if (propFile.exists()) {
        val props = org.jetbrains.kotlin.konan.properties.Properties()
        props.load(propFile.inputStream())
        props.getProperty(propName)

        if (props.getProperty(propName) != null) {
            props.getProperty(propName)
        } else {
            print("No such property $propName in file $filename")
            ""
        }
    } else {
        print("$filename does not exist!")
        ""
    }
}