import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

applyConfiguration()

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "me.fusiondev"
version = "1.10"

repositories {
    mavenCentral()
    maven {
        name = "sponge-repo"
        url = uri("https://repo.spongepowered.org/maven")
    }
    maven {
        name = "bstats-repo"
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
    flatDir {
        dirs("$rootDir/libs")
    }
}

dependencies {
    implementation(project(":fusionpixelmon-core"))
    compileOnly("ninja.leaping.configurate:configurate-hocon:3.3")
    compileOnly(files("$rootDir/libs/Pixelmon-1.12.2-8.1.2-universal.jar"))
    compileOnly(files("$rootDir/libs/forgeSrc-1.12.2-14.23.5.2768.jar"))

    compileOnly("org.spongepowered:spongeapi:7.2.0")
    compileOnly("org.spongepowered:spongeforge:1.12.2-2825-7.1.6")
    implementation("org.bstats:bstats-sponge:1.7")

    testCompile("junit", "junit", "4.12")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("FusionPixelmon")
    archiveClassifier.set("sponge")
    dependencies {
        include(project(":fusionpixelmon-core"))
        include(dependency("org.json:json:20190722"))
        include(dependency("org.bstats:bstats-sponge:1.7"))
    }
}
