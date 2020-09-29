# Compiling

To compile FusionPixelmon, you need to have the [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) for Java 8 or newer, and to include module-specific dependencies.

The build uses Gradle and is a multi-module project:
- `fusionpixelmon-core` is the FusionPixelmon API
- `fusionpixelmon-sponge` is the Sponge plugin
- `fusionpixelmon-spigot` is the Spigot plugin
- `fusionpixelmon-forge` is the Forge mod
- `fusionpixelmon-data` is the 1.9+ ArcPlate DataMigrator Sponge plugin

## How to compile
The process has been automated through the `compile.bat` batch script.

1. On Windows, run `compile.bat`
2. Wait for it to finish executing
3. Find the jars in `/builds`

## Where to find jars
You will find:
- The core FusionPixelmon API in **builds/core/libs**
- FusionPixelmon for Sponge in **builds/sponge/libs**
- FusionPixelmon for Spigot in **builds/spigot/libs**
- FusionPixelmon for Forge in **builds/forge/reobfShadowJar**
- FusionPixelmon DataMigrator for Sponge in **builds/data/libs**
