# FusionPixelmon
[![license](https://img.shields.io/github/license/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/LICENSE)
[![version](https://img.shields.io/github/v/release/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/releases)

This is a plugin/mod hybrid that adds extra features to your Pixelmon Sponge server.

# Features

### PokeDesigner
An interactive GUI for modifying your Pokemon.

Inspired by the pokemon designer on PokeCentral.org

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pokedesigner-1.gif" width="300" height="150">

### ArcPlates
An interactive GUI for storing Plates for your Arceus, and quickly switching the active held plate.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/arcplates-1.gif" width="300" height="150">

### Picking up Shrines
Shrines, Timespace Altars, and Arc Chalices are unbreakable blocks in the Pixelmon mod. This plugin allows the "breaking" and picking up of these structures.
Right clicking one of these structures while holding an iron or diamond pickaxe in survival mode will drop this item in your inventory, if there are available slots.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pickup-1.gif" width="300" height="150">
<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pickup-2.gif" width="300" height="150">

### Craftable Master Balls
Master Balls are made craftable again with the old recipe.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/masterball-1.png" width="300" height="150">

### Anti-Fall Damage
After winning a battle while flying on a pokemon, the player will fall to the ground as they will no longer be in flight.
There is an issue on some servers that when the player falls, they'll take fall damage.
This plugin prevents taking fall damage by giving a 5 second grace period after defeating a pokemon.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/fall-1.gif" width="300" height="150">

### Sponge Inventory API
An API for handling and creating custom GUI inventories.

### Pixelmon API
An API wrapper for some features from the Pixelmon mod.

### Pixelmon Currency API
A Bank API for the Pixelmon currency. 

# Installing
As this is executed as a plugin, it only needs to be installed server-side in the 'mods' folder of a Sponge server.

# Mod/Plugin Dependencies
These are the mods/plugins that need to be installed on the server for FusionPixelmon to work
- [Sponge Forge](https://www.spongepowered.org/downloads/spongeforge/stable/1.12.2)
  - spongeforge-1.12.2-2825-7.1.6.jar
- [Pixelmon](https://reforged.gg/)
  - Pixelmon-1.12.2-7.0.8-universal.jar

# Development Dependencies
These are the dependencies used for developing FusionPixelmon
- [Pixelmon](https://reforged.gg/)
  - Pixelmon-1.12.2-7.0.8-universal.jar
- [Sponge API](https://www.spongepowered.org/downloads/spongeapi/stable/7)
  - spongeapi-7.2.0-20191130.150509-32-shaded.jar
- [Sponge Forge](https://www.spongepowered.org/downloads/spongeforge/stable/1.12.2)
  - spongeforge-1.12.2-2825-7.1.6-dev-shaded.jar
- [Forge Src](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/libs/forgeSrc-1.12.2-14.23.5.2768.jar)
  - forgeSrc-1.12.2-14.23.5.2768.jar
