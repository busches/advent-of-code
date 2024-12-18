plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")

    testImplementation(kotlin("test"))
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

tasks.test {
//    useJUnitPlatform()
}
