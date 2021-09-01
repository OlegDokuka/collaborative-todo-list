plugins {
    kotlin("js")
}

group = "com.example.todomvc"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.214-kotlin-1.5.20")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.214-kotlin-1.5.20")

    implementation(npm("todomvc-app-css", "2.0.0"))
    implementation(npm("todomvc-common", "1.0.0"))
}
kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                outputFileName = "main.js"
                outputPath = File(buildDir, "../../backendApp/build/resources/main/static")
            }
        }
    }
}
