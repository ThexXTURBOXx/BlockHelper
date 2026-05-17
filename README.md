# Block Helper

A direct WAILA port for old versions of Minecraft, with almost the same API and many, many additional fixes!

## Download

[**Download on CurseForge**](https://www.curseforge.com/minecraft/mc-mods/block-helper)

[**Download on Modrinth**](https://modrinth.com/mod/block-helper) (More versions are available here)

### Fixers

Some Minecraft versions have bugs. That's normal. In order to fix some of them, you can install the following fixers:

- [Class Loader Fixer](https://modrinth.com/mod/class-loader-fixer)
- [Font Fixer](https://modrinth.com/mod/font-fixer)

## Old source code

This repository hosts the source code for BlockHelper versions 2.x and onwards.  
The source code for older versions (0.x and 1.x), has moved [here](https://github.com/ThexXTURBOXx/BlockHelper-old).

## Installation

### Forge (all versions) / ModLoaderMP (≤ 1.2.3)

#### Client

Just drop the mod into the `mods` folder. Done! :)

#### Server

If you are running a server on MC 1.3.x or newer, just copy the Block Helper jar file into the `mods` folder.

On older versions, you need to download the dedicated server build of the mod.
It can be found under "Additional files" both on Modrinth and CurseForge.  
In MC b1.5_02 and older, you need to copy all the files from this jar into the server jar.  
In all newer versions, it is sufficient to just copy the jar file into the `mods` folder.

#### MCPC+/Cauldron/Bukkit

If you are running a server on MC 1.3.x or newer, just copy the Block Helper jar file into the `mods` folder.

On older versions, you need to download the dedicated Bukkit/MCPC+ build of the mod.
It can be found under "Additional files" both on Modrinth and CurseForge.  
In MC b1.7.3 and older, you need to copy all the files from this jar into the Bukkit jar.  
In all newer versions, it is sufficient to just copy the jar file into the `mods` folder.

### Fabric

Drop the mod into the `mods` folder and install [Apron](https://modrinth.com/mod/apron) or [BetaLoader](https://github.com/paulevsGitch/BetaLoader) (for b1.7.3)
or [Fabricated Forge](https://modrinth.com/mod/fabricated-forge) (for 1.3.2+).

## Features

### Mod Compatibility

Next to Vanilla blocks, integration modules for the following mods are in place (list updated only irregularly!):
- Advanced Machines
- Advanced Solar Panels
- Applied Energistics 1
- Barrels
- BuildCraft 2 and 3
- ChickenChunks
- Ender Storage
- Equivalent Exchange 1, 2 and 3
- Factorization
- Falling Meteors
- Flora & Soma / Natura
- Forestry
- Forge Multipart
- GregTech
- IC²
- Immibis's Microblocks
- IndustrialCraft
- InfiCraft
- NEI
- Pam's Mods
- Project Zulu
- Railcraft
- Red Power
- Thaumcraft
- Thermal Expansion
- Total Panels
- Twilight Forest

Some of these mods still host downloads to this day.
If you cannot find downloads for some of these,
chances are that they are still downloadable via [MCModArchive](https://mcmodarchive.femtopedia.de/).

### API

Block Helper features WAILA's API (with minimal changes) from version 2.x onwards.
Since there are many resources online on how to use the API, I will keep this as simple as possible here and just list a few "good" examples:
- [Block Helper's own plugins](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.5.2/src/main/java/mcp/mobius/waila/addons)
- [BlockHelperAddons](https://github.com/VintageModsReforged/BlockHelperAddons)

Just try to follow these examples and you will succeed. To safely register your plugin, use the code snippet from the
JavaDoc of the `mod_BlockHelper#registerPlugin` function.

### Other features

Yes, Block Helper has a few additional features and integrations you probably have not seen before :)  
On the Modrinth and CurseForge page, there are a few images of most of these features in action!  
_Please note that not every feature is available for every Minecraft version due to modding limitations._

#### NEI/AMI/HMI integration

- Two new keybinds (default: <kbd>NUM3</kbd> and <kbd>NUM4</kbd>), which show all the recipes for or using the
  block currently looked at
- Show the mod an item is from in the item's tooltip \[this also works without NEI/AMI/HMI in Apron\]
- Another keybind specific for NEI (default: <kbd>I</kbd>), which shows all the applicable enchantments for the
  currently highlighted item in the inventory

#### World Overlays

Just like NEI in MC 1.4.7 and onwards, Block Helper features a light level overlay and chunk border overlay. The default
keybinds for these are <kbd>F7</kbd> and <kbd>F9</kbd>, respectively.

## Source Code

Block Helper is being developed for:
- [a1.2.6](https://github.com/ThexXTURBOXx/BlockHelper/tree/a1.2.6)
- [b1.1_01/b1.1_02](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.1_02)
- [b1.2_02](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.2_02)
- [b1.3_01](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.3_01)
- [b1.4_01](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.4_01)
- [b1.5/b1.5_01](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.5_01)
- [b1.6.5/b1.6.6](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.6.6)
- [b1.7-b1.7.3](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.7.3)
- [b1.8/b1.8.1](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.8.1)
- [b1.9p5](https://github.com/ThexXTURBOXx/BlockHelper/tree/b1.9p5)
- [1.0/1.0.1](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.0)
- [1.1](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.1)
- [1.2.3](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.2.3)
- [1.2.4/1.2.5](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.2.5)
- [1.3.2](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.3.2)
- [1.4](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.4)
- [1.4.1/1.4.2](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.4.2)
- [1.4.3](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.4.3)
- [1.4.4/1.4.5](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.4.5)
- [1.4.6/1.4.7](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.4.7)
- [1.5.x](https://github.com/ThexXTURBOXx/BlockHelper/tree/1.5.2)
