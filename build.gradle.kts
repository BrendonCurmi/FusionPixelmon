applyConfiguration()

plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "me.fusiondev"
version = "1.10"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}
