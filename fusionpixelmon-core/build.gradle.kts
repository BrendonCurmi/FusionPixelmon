import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

applyConfiguration()

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "me.fusiondev"
version = "1.9"

repositories {
    mavenCentral()
    flatDir {
        dirs("$rootDir/libs")
    }
}

dependencies {
    compileOnly("ninja.leaping.configurate:configurate-hocon:3.3")
    implementation("org.json:json:20190722")
    compileOnly("org.slf4j:slf4j-api:2.0.0-alpha1")
    compileOnly(files("$rootDir/libs/forgeSrc-1.12.2-14.23.5.2768.jar"))
    compileOnly(files("$rootDir/libs/Pixelmon-1.12.2-8.1.2-universal.jar"))

    testImplementation("junit", "junit", "4.12")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("FusionPixelmon")
    archiveClassifier.set("core")
}
