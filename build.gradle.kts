import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.10"
    application
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register("runOnGitHub") {
    dependsOn("test", "dokkaHtml")
    group = "custom"      // 3
    description = "$ ./gradlew runOnGitHub # runs on GitHub Action"
}

application {
    mainClass.set("MainKt")
}