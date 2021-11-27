import org.openjfx.gradle.JavaFXOptions

plugins {
    java
    idea
    application
    id("org.springframework.boot") version "2.6.0"
    id("org.openjfx.javafxplugin") version "0.0.10"
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

//tasks.getByName<BootJar>("bootJar") {
//    mainClass.set("net.npg.tracktime.TrackTime")
//}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.google.guava", "guava", "31.0.1-jre")
    implementation("org.apache.commons", "commons-lang3", "3.12.0")
    implementation("org.slf4j", "slf4j-api", "1.7.32")
    implementation("ch.qos.logback", "logback-classic", "1.2.7")
    implementation("commons-io", "commons-io", "2.11.0")
}
