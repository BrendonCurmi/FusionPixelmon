pluginManagement {
    repositories {
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
        }
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}
rootProject.name = "fusionpixelmon"
include("fusionpixelmon-core")
include("fusionpixelmon-sponge")
include("fusionpixelmon-spigot")
include("fusionpixelmon-data")
include("fusionpixelmon-forge")
include("buildSrc")
