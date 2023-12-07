plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.5"
    }
}
