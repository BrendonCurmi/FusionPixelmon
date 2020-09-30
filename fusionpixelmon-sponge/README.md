# FusionPixelmon

[![Release](https://img.shields.io/github/v/release/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/releases)
[![Minecraft](https://img.shields.io/badge/MC-1.12.2-brightgreen.svg)](https://github.com/BrendonCurmi/FusionPixelmon)
[![License](https://img.shields.io/github/license/BrendonCurmi/FusionPixelmon)](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/LICENSE)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/01bca84076714665a643eedcba9d1182)](https://www.codacy.com/manual/BrendonCurmi/FusionPixelmon?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BrendonCurmi/FusionPixelmon&amp;utm_campaign=Badge_Grade)
[![Discord](https://discordapp.com/api/guilds/699764448155533404/widget.png)](https://discord.gg/VFNTycm)

Adds a little extra to your Pixelmon experience.

## Features
Features can be disabled in the config

### PokeDesigner
An interactive GUI for modifying your Pokemon.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pokedesigner-1.gif" width="300" height="150">

### ArcPlates
An interactive GUI for storing Plates for your Arceus, and quickly switching the active held plate.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/arcplates-1.gif" width="300" height="150">

### Picking up Shrines
Makes Shrines, Altars, and Chalices "breakable."
Right clicking one of these structures while holding the appropriate diamond tool in survival mode will drop this item in your inventory, if there is space available.
Clicking with an empty hand will lock it to prevent other players from picking it up. Clicking again will unlock it.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pickup-1.gif" width="300" height="150">
<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/pickup-2.gif" width="300" height="150">

### Craftable Master Balls
Adds the Master Ball crafting recipe back in the game.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/masterball-1.png" width="300" height="150">

### Anti-Fall Damage
After winning a battle while flying on a pokemon, the player will fall to the ground as they will no longer be in flight.
There is an issue on some servers that when the player falls, they'll take fall damage.
This plugin prevents taking fall damage by giving a 5 second grace period after defeating a pokemon.

<img src="https://raw.githubusercontent.com/BrendonCurmi/FusionPixelmon/assets/assets/readme/fall-1.gif" width="300" height="150">

## Commands and Permissions
| Command                               | Permission                                | Description                   |
|---------------------------------------|-------------------------------------------|-------------------------------|
| `/pd`                                 | fusionpixelmon.command.pokedesigner       | Opens the PokeDesigner UI     |
| `/pokedesigner`                       |                                           |                               |
| `/arc`                                | fusionpixelmon.command.arc                | Opens the ArcPlates UI        |
| `/pokemodifier <modifier> [player]`   | fusionpixelmon.command.admin.pokemodifier | Gives Pokemon modifier tokens |

Note: Empty fields will follow the values of the row above them.

## Install
Add the plugin to the 'mods' folder of your Sponge server.

## Download
You can download the latest version from [Ore](https://ore.spongepowered.org/FusionDev/FusionPixelmon).

## Dependencies
- [Forge](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
  - forge-1.12.2-14.23.5.2847-universal.jar
- [Sponge Forge](https://www.spongepowered.org/downloads/spongeforge/stable/1.12.2)
  - spongeforge-1.12.2-2825-7.1.6.jar
- [Pixelmon Reforged](https://reforged.gg/)
  - Pixelmon-1.12.2-7.2.2-universal.jar
  - Pixelmon-1.12.2-7.1.1-universal.jar
  - Pixelmon-1.12.2-7.0.8-universal.jar

Multiple versions may be listed under the same mod to show which versions have been tested with this plugin.
Only one version of each entry needs to be installed at a given time.

## Development Dependencies
These are the dependencies used for developing FusionPixelmon
- [Pixelmon](https://reforged.gg/)
  - Pixelmon-1.12.2-7.0.8-universal.jar
- [Sponge API](https://www.spongepowered.org/downloads/spongeapi/stable/7)
  - spongeapi-7.2.0-20191130.150509-32-shaded.jar
- [Sponge Forge](https://www.spongepowered.org/downloads/spongeforge/stable/1.12.2)
  - spongeforge-1.12.2-2825-7.1.6-dev-shaded.jar
- [Forge Src](https://github.com/BrendonCurmi/FusionPixelmon/blob/master/libs/forgeSrc-1.12.2-14.23.5.2768.jar)
  - forgeSrc-1.12.2-14.23.5.2768.jar

## Config
The config files can be found on the server in `config/fusionpixelmon`.

To modify features of this plugin, open `fusionpixelmon.conf`, and start the server after saving any changes made.

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
