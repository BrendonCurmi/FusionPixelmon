# FusionPixelmon

[![Release](https://img.shields.io/github/v/release/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/releases)
[![Minecraft](https://img.shields.io/badge/MC-1.12.2-brightgreen.svg)](https://github.com/BrendonCurmi/FusionPixelmon)
[![License](https://img.shields.io/github/license/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/LICENSE)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/01bca84076714665a643eedcba9d1182)](https://www.codacy.com/manual/BrendonCurmi/FusionPixelmon?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BrendonCurmi/FusionPixelmon&amp;utm_campaign=Badge_Grade)
[![Discord](https://discordapp.com/api/guilds/699764448155533404/widget.png)](https://discord.gg/VFNTycm)

Adds a little extra to your Pixelmon experience.

## Summary
1. [Install](#install)
2. [Download](#download)
3. [Dependencies](#dependencies)
4. [Features](#features)
5. [Commands and Permissions](#commands-and-permissions)
6. [Config](#config)
7. [Known Issues](#known-issues)
8. [Links](#links)

## Install
As this is executed as a plugin, it only needs to be installed server-side in the 'mods' folder of a Sponge server.

## Download
You can download the latest version from:
- [FusionPixelmon Sponge]()
- [FusionPixelmon Spigot]()
- [FusionPixelmon Forge]()

## Dependencies
General dependencies that are required for FusionPixelmon to run:
- [Forge](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
  - forge-1.12.2-14.23.5.2847-universal.jar
- [Pixelmon Reforged](https://reforged.gg/)
  - Pixelmon-1.12.2-7.2.2-universal.jar
  - Pixelmon-1.12.2-7.1.1-universal.jar
  - Pixelmon-1.12.2-7.0.8-universal.jar

Multiple versions may be listed under the same mod to show which versions have been tested with this plugin.
Only one version of each entry needs to be installed at a given time.

Look at each plugin/mod for their specific dependencies.

## Features

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

## Commands and Permissions
| Command       | Permission                          | Description               |
|---------------|-------------------------------------|---------------------------|
| /pd           | fusionpixelmon.command.pokedesigner | Opens the PokeDesigner UI |
| /pokedesigner                                                                   |
| /arc          | fusionpixelmon.command.arc          | Opens the ArcPlates UI    |

Note: Empty fields will follow the values of the row above them.

## Config
To modify features of this plugin, use `[config]/fusionpixelmon.conf`.

Above each setting in the config file, there will be comments explaining the feature and how to disable it.

For example:
```text
# Block the player from taking fall damage after winning a battle against a pokemon.
anti-fall-damage=true
# Pixelmon blocks that the player is able to collect.
# To disable entirely, delete everything inside the brackets.
shrine-pickup=[
    "pixelmon:timespace_altar",
    "pixelmon:plateholder",
    "pixelmon:articuno_shrine",
    "pixelmon:zapdos_shrine",
    "pixelmon:moltres_shrine"
]
...
```

To return the `fusionpixelmon.conf` config file back to the original, you can either delete it and it'll be created upon next server startup, or copy-paste the values from the [default config](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/fusionpixelmon-core/src/main/resources/assets/fusionpixelmon/default.conf).

## Known Issues
- Using Sponge 7.2.0 causes levels to be bought 1 at a time, and Pokemon to revert back to original form after battling or returning to pokeball.
- Using inventory mods (like InventoryTweaks) on an open GUI menu can break the menu or cause item duplications.

## Links
- [Discord](https://discord.gg/VFNTycm)
- [Issue tracker](https://github.com/BrendonCurmi/FusionPixelmon/issues)
