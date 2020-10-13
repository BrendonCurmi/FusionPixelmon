# FusionPixelmon

[![Release](https://img.shields.io/github/v/release/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/releases)
[![Minecraft](https://img.shields.io/badge/MC-1.12.2-brightgreen.svg)](https://github.com/BrendonCurmi/FusionPixelmon)
[![License](https://img.shields.io/github/license/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/LICENSE)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/01bca84076714665a643eedcba9d1182)](https://www.codacy.com/manual/BrendonCurmi/FusionPixelmon?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BrendonCurmi/FusionPixelmon&amp;utm_campaign=Badge_Grade)
[![Discord](https://discordapp.com/api/guilds/699764448155533404/widget.png)](https://discord.gg/VFNTycm)

Add a little extra to your Pixelmon experience

## Summary
1. [Download](#download)
2. [Install](#install)
3. [Dependencies](#dependencies)
4. [Features](#features)
5. [Commands and Permissions](#commands-and-permissions)
6. [Config](#config)
7. [Known Issues](#known-issues)
8. [Links](#links)

## Download
You can download the latest versions from:
- [FusionPixelmon Sponge](https://ore.spongepowered.org/FusionDev/FusionPixelmon)
- [FusionPixelmon Spigot]()
- [FusionPixelmon Forge](https://www.curseforge.com/minecraft/mc-mods/fusionpixelmon)

## Dependencies
General dependencies that are required for FusionPixelmon to run:
- [Forge](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
  - forge-1.12.2-14.23.5.2847-universal.jar
- [Pixelmon Reforged](https://reforged.gg/)
  - Pixelmon-1.12.2-8.1.2-universal.jar (1.10-all)
  - Pixelmon-1.12.2-7.2.2-universal.jar (1.9-sponge)
  - Pixelmon-1.12.2-7.1.1-universal.jar (1.9-sponge)
  - Pixelmon-1.12.2-7.0.8-universal.jar (1.9-sponge)

Multiple versions may be listed under the same library to show which versions have been tested with this plugin.
Only one version of each entry needs to be installed at a given time.

For each plugin's specific dependencies check:
- [Sponge](fusionpixelmon-sponge/README.md)
- [Spigot](fusionpixelmon-spigot/README.md)
- [Forge](fusionpixelmon-forge/README.md)

## Features

### PokeDesigner
An interactive PokeDesigner with 12 shops for modifying your Pokemon.

Inspired by the pokemon designer on PokeCentral.org

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pokedesigner-1.gif" width="300" height="150">

### ArcPlates
An interactive management UI for storing Arceus Plates and quickly switching between them.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/arcplates-1.gif" width="300" height="150">

### Shrine Pickup
Allows the "breaking" and picking up of Pixelmon's unbreakable Shrines, Timespace Altars, and Chalices.
Clicking one of these structures in survival mode while holding the appropriate diamond tool will drop this item in your inventory, if there are available slots.
In the multiplayer versions, shrines can also be locked to prevent other players from picking them up.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pickup-1.gif" width="300" height="150">
<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pickup-2.gif" width="300" height="150">

### Modifier Tokens
Token items which can be used to modify specific aspects of your Pokemon.
Admins could add modifier tokens to chest loot or prizes for players, if original item data is preserved.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/modifier-1.gif" width="300" height="150">

### Craftable Master Balls
Adds the Master Ball crafting recipe back into the game.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/masterball-1.png" width="300" height="150">

### Anti-Fall Damage
After winning a battle while flying on a pokemon, the player will fall to the ground as they will no longer be in flight.
There is an issue on some servers that when the player falls, they'll take fall damage.
This plugin prevents taking fall damage by giving a 5 second grace period after defeating a pokemon.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/fall-1.gif" width="300" height="150">

## Commands and Permissions
| Command                               | Permission                                | Description                   |
|---------------------------------------|-------------------------------------------|-------------------------------|
| `/pd` or `/pokedesigner`              | fusionpixelmon.command.pokedesigner       | Opens the PokeDesigner UI     |
| `/arc`                                | fusionpixelmon.command.arc                | Opens the ArcPlates UI        |
| `/pokemodifier <modifier> [player]`   | fusionpixelmon.command.admin.pokemodifier | Gives Pokemon modifier tokens |

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
- **all**: Using inventory mods (like InventoryTweaks) on an open GUI menu can break the menu or cause item duplications.

## Links
- [Discord](https://discord.gg/VFNTycm)
- [Issue tracker](https://github.com/BrendonCurmi/FusionPixelmon/issues)
