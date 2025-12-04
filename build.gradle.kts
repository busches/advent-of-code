plugins {
    kotlin("jvm") version "2.3.0-RC2"
    kotlin("plugin.serialization") version "2.3.0-RC2"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    testImplementation(kotlin("test"))
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}

tasks.test {
//    useJUnitPlatform()
}
