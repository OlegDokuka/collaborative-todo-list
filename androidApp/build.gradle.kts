plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

group = "com.example.todomvc"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":shared"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("android.arch.lifecycle:extensions:1.1.1")

    testImplementation("junit:junit:4.13.2")
}
android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.example.todomvc.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
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
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/proguard/androidx-annotations.pro")
        exclude("META-INF/androidx.localbroadcastmanager_localbroadcastmanager.version")
        exclude("META-INF/androidx.legacy_legacy-support-core-utils.version")
        exclude("META-INF/androidx.print_print.version")
        exclude("META-INF/androidx.documentfile_documentfile.version")
        exclude("META-INF/androidx.versionedparcelable_versionedparcelable.version")
        exclude("META-INF/androidx.loader_loader.version")
        exclude("META-INF/androidx.core_core.version")
    }
}