import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"

    id("org.springframework.boot") version "2.4.7" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("plugin.spring") version "1.4.32" apply false
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    jcenter()
}


kotlin {
    jvm("spring") {
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        tasks.withType<Copy> {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
    js("react", IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                outputFileName = "main.js"
                outputPath = File(buildDir, "processedResources/spring/main/static")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.benasher44:uuid:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val reactMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("io.rsocket.kotlin:rsocket-core:0.12.0")
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:0.12.0")
                implementation("io.ktor:ktor-client-js:1.5.4")

                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.212-kotlin-1.5.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.212-kotlin-1.5.10")

                implementation(npm("todomvc-app-css", "2.0.0"))
                implementation(npm("todomvc-common", "1.0.0"))
            }
        }
        val springMain by getting {
            dependsOn(commonMain)
            dependencies {

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
                implementation("org.springframework.boot:spring-boot-starter-rsocket")
                implementation("org.springframework.boot:spring-boot-starter-webflux")

                implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
            }
        }
        val springTest by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("io.projectreactor:reactor-test")
            }
        }
    }
}

tasks.getByName<Copy>("springProcessResources") {
    dependsOn(tasks.getByName("reactBrowserDevelopmentWebpack"))
}
