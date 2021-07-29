import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

applyConfiguration()

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "me.fusiondev"
version = "1.11"

repositories {
    mavenCentral()
    jcenter()
    maven {
        name = "shadow-repo"
        url = uri("https://plugins.gradle.org/m2/")
    }
    maven {
        name = "spigot-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    flatDir {
        dirs("/libs")
    }
    flatDir {
        dirs("$rootDir/libs")
    }
    mavenLocal()
}

dependencies {
    implementation(project(":fusionpixelmon-core"))
    compileOnly(files("$rootDir/libs/Pixelmon-1.12.2-8.2.0-universal.jar"))
    compileOnly(files("$rootDir/libs/forgeSrc-1.12.2-14.23.5.2768.jar"))

    compileOnly("org.bukkit:bukkit:1.12.2-R0.1-SNAPSHOT") {
        exclude("junit", "junit")
    }
    //compileOnly(files("/libs/spigot-1.12.2.jar"))
    compileOnly(files("/libs/craftbukkit-1.12.2.jar"))

    testCompile("junit", "junit", "4.12")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("FusionPixelmon")
    archiveClassifier.set("spigot")
}
