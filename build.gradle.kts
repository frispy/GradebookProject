plugins {
    kotlin("jvm") version "2.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.4")

    testImplementation("io.mockk:mockk:1.14.6")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}