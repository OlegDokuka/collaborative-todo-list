import org.jetbrains.kotlin.gradle.utils.toSetOrEmpty

plugins {
    kotlin("jvm")
    id("application")
}

application {
    mainClass.set("todomvcfx.tornadofx.app.TornadoFXAppKt")
}

dependencies {
    implementation(project(":shared"))

    implementation("com.benasher44:uuid:0.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.5.1")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("de.jensd:fontawesomefx:8.9")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Class-Path" to configurations.runtimeClasspath.toSetOrEmpty().joinToString(" ") { it.name },
            "Main-Class" to "todomvcfx.tornadofx.app.TornadoFXAppKt"
        ))
    }
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map { zipTree(it) }
    }) {
        exclude("META-INF/versions/9/module-info.class")
        exclude("*module-info.class")
        exclude("META-INF/MANIFEST.MF")
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}