plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()
    jvm()
    js(IR) {
        browser {}
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.benasher44:uuid:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val commonClientMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
                implementation("io.rsocket.kotlin:rsocket-core:0.14.0")
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:0.14.0")
            }
        }

        val jvmMain by getting {
            dependsOn(commonClientMain)
            dependencies {
                implementation("io.ktor:ktor-client-cio:1.6.3")
            }
        }

        val jsMain by getting {
            dependsOn(commonClientMain)
            dependencies {
                implementation("io.ktor:ktor-client-js:1.6.3")
            }
        }

        val androidMain by getting {
            dependsOn(commonClientMain)
            dependencies {
                implementation("io.ktor:ktor-client-android:1.6.3")
                implementation("io.ktor:ktor-client-okhttp:1.6.3")
            }
        }
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }
}