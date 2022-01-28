plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "me.centauri07.form"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    implementation("com.google.guava:guava:31.0.1-jre")
}