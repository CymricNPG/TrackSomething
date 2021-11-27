import org.openjfx.gradle.JavaFXOptions

plugins {
    java
    idea
    application
    id("org.springframework.boot") version "2.6.0"
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.sonarqube") version "3.3"
}

group = "net.npg"
version = "1.0-SNAPSHOT"

apply(plugin = "io.spring.dependency-management")

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
val slf4jVersion by extra("1.7.29")

configure<JavaApplication> {
    mainClass.set("net.npg.tracktime.TrackTime")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

sonarqube {
    properties {
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.login", "eed9ea7ce4c8c46c8c1fdba8ccb8923e7acef0a4")
    }
}

repositories {
    mavenCentral()
}

configure<JavaFXOptions> {
    modules("javafx.controls", "javafx.fxml", "javafx.graphics")
    version = "17"
}

dependencies {
    implementation("org.slf4j", "slf4j-api", slf4jVersion)
    implementation("ch.qos.logback", "logback-classic", "1.2.3")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.12.4")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.8.1")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.1")
    implementation("org.springframework.boot", "spring-boot-starter", "2.6.0") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation("org.springframework.boot", "spring-boot-starter-log4j2", "2.6.0")
    implementation("com.google.guava", "guava", "31.0.1-jre")
    implementation("org.apache.commons", "commons-lang3", "3.12.0")
    implementation("org.apache.logging.log4j", "log4j-api", "2.14.1")
    implementation("org.apache.logging.log4j", "log4j-core", "2.14.1")
}
