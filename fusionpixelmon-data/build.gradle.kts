import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

applyConfiguration()

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "me.fusiondev"
version = "1.9"

repositories {
    jcenter()
    maven {
        name = "sponge-repo"
        url = uri("https://repo.spongepowered.org/maven")
    }
}

dependencies {
    implementation(project(":fusionpixelmon-core"))
    compile("org.spongepowered:spongeapi:7.2.0")

    testCompile("junit", "junit", "4.12")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("FusionPixelmonDataMigrator")
    archiveClassifier.set("")

    dependencies {
        include(project(":fusionpixelmon-core"))
    }
    exclude("assets/")
    minimize()
}
