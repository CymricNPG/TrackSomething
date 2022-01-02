plugins {
    java
    idea
    application
    id("org.springframework.boot") version "2.6.2"
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.sonarqube") version "3.3"
    id("org.beryx.runtime") version "1.12.7"
    id("com.github.ben-manes.versions") version "0.40.0"
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

configure<org.openjfx.gradle.JavaFXOptions> {
    modules("javafx.controls", "javafx.fxml", "javafx.graphics")
    version = "17"
}

dependencies {
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.13.1")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.8.2")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.2")
    implementation("org.springframework.boot", "spring-boot-starter", "2.6.2") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation("org.springframework.boot", "spring-boot-starter-log4j2", "2.6.2")
    implementation("com.google.guava", "guava", "31.0.1-jre")
    implementation("org.apache.commons", "commons-lang3", "3.12.0")
    implementation("org.apache.logging.log4j", "log4j-api", "2.17.1")
    implementation("org.apache.logging.log4j", "log4j-core", "2.17.1")
}

val currentOs = org.gradle.internal.os.OperatingSystem.current()
val imgType = if (currentOs.isWindows) "ico" else if (currentOs.isMacOsX) "icns" else "png"

runtime {
    options.addAll("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        noConsole = false
    }
    jpackage {

        imageOptions.addAll(listOf("--icon", "src/main/resources/hellofx.$imgType"))
        installerOptions.addAll(listOf("--resource-dir", "src/main/resources"))
        installerOptions.addAll(listOf("--vendor", "Acme Corporation"))
        if (currentOs.isWindows) {
            installerOptions.addAll(
                listOf(
                    "--win-per-user-install", "--win-dir-chooser", "--win-menu", "--win-shortcut"
                )
            )
        }
    }
}