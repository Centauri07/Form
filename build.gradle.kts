plugins {
    kotlin("jvm") version "1.5.31"
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "me.centauri07.form"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.centauri07.form"
            artifactId = "Form"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}
