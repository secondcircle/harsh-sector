# Starsector Modding: Official Resources Reference

This document provides a comprehensive guide to official Starsector modding documentation, resources, and getting started information.

---

## Table of Contents

1. [Official Documentation Sources](#official-documentation-sources)
2. [Wiki Resources](#wiki-resources)
3. [API Documentation](#api-documentation)
4. [Essential Tools](#essential-tools)
5. [Utility Libraries](#utility-libraries)
6. [Basic Mod Structure](#basic-mod-structure)
7. [Getting Started Steps](#getting-started-steps)
8. [Key Data Files Reference](#key-data-files-reference)
9. [Development Environment Setup](#development-environment-setup)
10. [Community Resources](#community-resources)

---

## Official Documentation Sources

### Fractal Softworks (Developer)

| Resource | URL | Description |
|----------|-----|-------------|
| **Main Website** | https://fractalsoftworks.com/ | Official Starsector website |
| **Modding Blog Posts** | https://fractalsoftworks.com/category/modding/ | Developer blog posts about modding capabilities |
| **Mods Forum** | https://fractalsoftworks.com/forum/index.php?board=8.0 | Official forum for mod releases and discussions |
| **Modding Subforum** | https://fractalsoftworks.com/forum/index.php?board=3.0 | Technical modding discussions and help |
| **FAQ** | https://fractalsoftworks.com/faq/ | Frequently asked questions including mod support |

### Key Forum Threads

| Thread | URL | Description |
|--------|-----|-------------|
| **How to Mod Starsector Guide** | https://fractalsoftworks.com/forum/index.php?topic=12092.0 | Comprehensive modding tutorial |
| **Modding Guide for New Coders** | https://fractalsoftworks.com/forum/index.php?topic=5709.0 | Beginner-friendly coding guide |
| **Starsector API Javadoc Thread** | https://fractalsoftworks.com/forum/index.php?topic=7164.0 | API documentation discussion |
| **API Reference Thread** | https://fractalsoftworks.com/forum/index.php?topic=4297.0 | API reference materials |
| **Modding Reference List** | https://fractalsoftworks.com/forum/index.php?topic=18356.0 | Up-to-date list of tutorials and references |

---

## Wiki Resources

### Primary Wikis

| Wiki | URL | Best For |
|------|-----|----------|
| **Starsector Wiki (wiki.gg)** | https://starsector.wiki.gg/ | Most up-to-date technical documentation |
| **Starsector Wiki (Fandom)** | https://starsector.fandom.com/wiki/Modding | Step-by-step tutorials |
| **Starsector Mods Wiki** | https://starsector-mods.fandom.com/wiki/Starsector_Mods_Wiki | Mod documentation and catalogs |

### Key Wiki Pages

| Page | URL | Description |
|------|-----|-------------|
| **Modding Overview** | https://starsector.wiki.gg/wiki/Modding | Main modding hub with category links |
| **Intro to Modding** | https://starsector.wiki.gg/wiki/Intro_to_Modding | Complete beginner tutorial |
| **Intro to Modding (Fandom)** | https://starsector.fandom.com/wiki/Intro_to_Modding | Alternative beginner tutorial |
| **mod_info.json Overview** | https://starsector.wiki.gg/wiki/Mod_Info.json_Overview | Required mod configuration file |
| **Getting Started with Mod Programming** | https://starsector.wiki.gg/wiki/Getting_started_with_mod_programming | Java/Kotlin coding guide |
| **IntelliJ IDEA Setup** | https://starsector.wiki.gg/wiki/IntelliJ_IDEA_Setup | IDE configuration guide |
| **Ship Editor** | https://starsector.wiki.gg/wiki/Ship_Editor | Ship editor documentation |

### Data File Documentation

| Page | URL | Description |
|------|-----|-------------|
| **Weapon data.csv** | https://starsector.wiki.gg/wiki/Weapon_data.csv | Weapon configuration reference |
| **Ship Systems CSV** | https://starsector.wiki.gg/wiki/Ship_Systems_CSV | Ship systems configuration |
| **Descriptions CSV** | https://starsector.wiki.gg/wiki/Descriptions_CSV | Text and description files |
| **Misc CSV File Overview** | https://starsector.wiki.gg/wiki/Misc_.csv_File_Overview | Other CSV file types |
| **.faction File Overview** | https://starsector.fandom.com/wiki/.faction_File_Overview | Faction configuration |

---

## API Documentation

### Official API

| Resource | URL | Description |
|----------|-----|-------------|
| **Starsector API Javadoc** | https://fractalsoftworks.com/starfarer.api/ | Official API documentation (Javadoc format) |

### API Key Information

- **Java Version**: Starsector uses **Java 17**
- **Framework**: Built on LWJGL (Lightweight Java Game Library)
- **API Access**: Available through `starfarer.api` package in `starsector-core/`

### Main API Packages (Common)

- `com.fs.starfarer.api` - Core API interfaces
- `com.fs.starfarer.api.campaign` - Campaign/sector management
- `com.fs.starfarer.api.combat` - Combat mechanics
- `com.fs.starfarer.api.fleet` - Fleet management
- `com.fs.starfarer.api.impl` - Implementation classes
- `com.fs.starfarer.api.plugins` - Plugin interfaces

### ModPlugin Interface

The `ModPlugin` is the master plugin for a mod, used to:
- Load resources when Starsector starts
- Add campaign scripts when a game is loaded
- Generate campaign content
- Register other plugins and scripts

---

## Essential Tools

### Ship and Weapon Editors

| Tool | URL | Description |
|------|-----|-------------|
| **STARSECTOR Ship & Weapon Editor** | https://fractalsoftworks.com/forum/index.php?topic=11491.0 | Primary community editor (v3.0.0-alpha for 0.95a+) |
| **Ship Editor by Ontheheavens** | https://github.com/Ontheheavens/Ship-Editor | Modern JSON/CSV editor with layer system |
| **Trylobot's Ship Editor** | https://github.com/Trylobot/sf-ship-ed | Alternative ship data editor |

### Text and Code Editors

| Tool | Platform | Notes |
|------|----------|-------|
| **VS Code** | Cross-platform | Recommended; full-featured with extensions |
| **Notepad++** | Windows | Lightweight, fast |
| **IntelliJ IDEA Community** | Cross-platform | Recommended for Java development |
| **NetBeans** | Cross-platform | Alternative IDE |

### Spreadsheet Editors (for CSV files)

| Tool | Notes |
|------|-------|
| **Ron's CSV Editor** | Free; recommended for CSV editing |
| **Microsoft Excel** | Works if language set to US English |
| **LibreOffice Calc** | Free alternative |

### Art Programs

| Tool | Notes |
|------|-------|
| **Krita** | Free; recommended for sprites |
| **GIMP** | Free; supports transparency |
| **Adobe Photoshop** | Industry standard |

---

## Utility Libraries

These are essential mods that provide shared functionality for other mods.

### LazyLib

| | |
|---|---|
| **Description** | Core utility library with helper methods for common modding tasks |
| **GitHub** | https://github.com/LazyWizard/lazylib |
| **Documentation** | https://lazywizard.github.io/lazylib/ |
| **Features** | AI utilities, collision detection, sector messages, ~4,000 lines of documentation |
| **Note** | Required dependency for many mods |

### MagicLib

| | |
|---|---|
| **Description** | Community-driven library of scripts and plugins |
| **Forum Thread** | https://fractalsoftworks.com/forum/index.php?topic=25868.0 |
| **GitHub** | https://github.com/MagicLibStarsector/MagicLib |
| **Features** | Scripting functions, campaign utilities, visual effects |

### GraphicsLib

| | |
|---|---|
| **Description** | Enhanced graphics and visual effects library |
| **Features** | Lighting effects, shaders, visual enhancements |
| **Note** | Required for mods with advanced visual effects |

---

## Basic Mod Structure

### Required File

Every mod **must** have a `mod_info.json` file in its root folder.

### Standard Folder Layout

```
Starsector/mods/YourModName/
├── mod_info.json          # REQUIRED - Mod configuration
├── data/
│   ├── hulls/             # Ship hull definitions (.ship files)
│   │   └── ship_data.csv  # Ship statistics
│   ├── variants/          # Ship loadout configurations (.variant files)
│   ├── weapons/           # Weapon definitions
│   │   ├── weapon_data.csv
│   │   └── ship_systems.csv
│   ├── strings/
│   │   └── descriptions.csv  # Text descriptions
│   ├── world/
│   │   └── factions/      # Faction configurations
│   │       ├── factions.csv
│   │       └── *.faction files
│   ├── config/            # Configuration overrides
│   ├── campaign/          # Campaign data
│   │   └── special_items.csv
│   └── hullmods/
│       └── hull_mods.csv
├── graphics/
│   ├── ships/             # Ship sprites (PNG)
│   ├── weapons/           # Weapon sprites
│   └── missiles/          # Projectile sprites
├── sounds/                # Audio files and music
├── src/                   # Java source code (optional)
│   └── com/yourname/modname/
└── jars/                  # Compiled JAR files (optional)
    └── YourMod.jar
```

### mod_info.json Reference

#### Minimal Example

```json
{
    "id": "yourmodid",
    "name": "Your Mod Name",
    "author": "Your Name",
    "version": "1.0.0",
    "description": "A brief description of your mod",
    "gameVersion": "0.98a"
}
```

#### Complete Example

```json
{
    "id": "example_mod",
    "name": "Example Mod",
    "author": "Mod Author Name",
    "version": {
        "major": 1,
        "minor": 0,
        "patch": 0
    },
    "description": "Full description of the mod.\nSupports line breaks with \\n",
    "gameVersion": "0.98a",
    "utility": false,
    "totalConversion": false,
    "jars": ["jars/ExampleMod.jar"],
    "modPlugin": "com.example.ExampleModPlugin",
    "dependencies": [
        {
            "id": "lw_lazylib",
            "name": "LazyLib",
            "version": "2.8"
        }
    ],
    "replace": [],
    "requiredMemoryMB": 0
}
```

#### Field Reference

| Field | Required | Type | Description |
|-------|----------|------|-------------|
| `id` | Yes | String | Unique identifier (lowercase, no spaces) |
| `name` | Yes | String | Display name in mod loader |
| `version` | Yes | String or Object | Version number or {major, minor, patch} |
| `description` | Yes | String | Mod description (supports `\n`) |
| `gameVersion` | Yes | String | Target Starsector version |
| `author` | No | String | Creator name |
| `utility` | No | Boolean | If true, loads with total conversions |
| `totalConversion` | No | Boolean | If true, prevents other non-utility mods |
| `jars` | No | Array | JAR files to load |
| `modPlugin` | No | String | Main plugin class path |
| `dependencies` | No | Array | Required mods |
| `replace` | No | Array | Core files to not merge |
| `requiredMemoryMB` | No | Number | Recommended memory |

---

## Getting Started Steps

### Phase 1: Basic Setup (No Coding Required)

1. **Install Required Tools**
   - Text editor (VS Code recommended)
   - CSV editor (Ron's CSV Editor)
   - Art program (Krita or GIMP)
   - Ship and Weapon Editor from forum

2. **Create Mod Folder**
   ```
   Starsector/mods/YourModName/
   ```

3. **Create mod_info.json**
   - Use the minimal example above
   - Set `gameVersion` to match your Starsector installation

4. **Verify Mod Loads**
   - Launch Starsector
   - Check mod list in launcher
   - Your mod should appear (even if empty)

### Phase 2: Add Content

5. **Create Folder Structure**
   - Add `data/`, `graphics/` folders as needed
   - Mirror vanilla folder structure

6. **Add Ships/Weapons**
   - Create sprites in graphics program
   - Use Ship Editor to create .ship and .variant files
   - Add entries to ship_data.csv or weapon_data.csv
   - Add descriptions to strings/descriptions.csv

7. **Test In-Game**
   - Enable devmode in settings.json
   - Use F8 in simulator to reload JSON/CSV without restart
   - Use console commands: `addship`, `findship`

### Phase 3: Add Code (Optional)

8. **Set Up IDE**
   - Download IntelliJ IDEA Community Edition
   - Download JDK 17 (Azul Zulu recommended)
   - Use the IntelliJ Template: https://github.com/wispborne/Starsector-IntelliJ-Template

9. **Configure Project**
   - Add starfarer.api as library
   - Add LazyLib, MagicLib if using
   - Set up run configuration for debugging

10. **Write Code**
    - Create ModPlugin class
    - Implement desired interfaces
    - Compile to JAR
    - Update mod_info.json with `jars` and `modPlugin`

---

## Key Data Files Reference

### Ships

| File | Location | Purpose |
|------|----------|---------|
| `ship_data.csv` | `data/hulls/` | Ship statistics and properties |
| `*.ship` | `data/hulls/` | Ship geometry, slots, bounds |
| `*.variant` | `data/variants/` | Ship loadout configurations |
| `wing_data.csv` | `data/hulls/` | Fighter wing data |

### Weapons

| File | Location | Purpose |
|------|----------|---------|
| `weapon_data.csv` | `data/weapons/` | Weapon statistics |
| `*.wpn` | `data/weapons/` | Weapon visual configuration |
| `*.proj` | `data/weapons/` | Projectile definitions |
| `ship_systems.csv` | `data/weapons/` | Ship systems data |

### Factions

| File | Location | Purpose |
|------|----------|---------|
| `factions.csv` | `data/world/factions/` | Faction list |
| `*.faction` | `data/world/factions/` | Individual faction configuration |
| `default_ship_roles.json` | `data/world/factions/` | Fleet composition rules |

### Other Important Files

| File | Location | Purpose |
|------|----------|---------|
| `descriptions.csv` | `data/strings/` | All text descriptions |
| `hull_mods.csv` | `data/hullmods/` | Hullmod definitions |
| `special_items.csv` | `data/campaign/` | Special item definitions |
| `settings.json` | `data/config/` | Game settings overrides |

---

## Development Environment Setup

### IntelliJ IDEA Setup (Recommended)

**Source**: https://starsector.wiki.gg/wiki/IntelliJ_IDEA_Setup

1. **Download and Install**
   - IntelliJ IDEA Community Edition (~650 MB)
   - JDK 17 (Azul Zulu preferred)

2. **Get Mod Template**
   - Download from: https://github.com/wispborne/Starsector-IntelliJ-Template
   - Extract to `Starsector/mods/YourModName/`

3. **Open Project**
   - Open IntelliJ
   - Select "Open" and choose mod folder

4. **Configure SDK**
   - File > Project Structure
   - Set Project SDK to JDK 17
   - Set Language Level to "17 - Sealed types, always-strict floating-point semantics"

5. **Add Dependencies**
   - Libraries tab > Add `starfarer.api` from starsector-core
   - Add LazyLib.jar if using
   - Add other mod JARs as needed

6. **Configure Build**
   - Artifacts tab > Configure JAR output
   - Enable "Include in Project Build"

7. **Set Up Run Configuration**
   - Run > Edit Configurations
   - Set working directory to starsector-core path
   - Configure JVM arguments for memory

### Alternative: Gradle Template

For more advanced builds: https://github.com/wispborne/starsector-mod-template

Features:
- Works with any IDE
- Kotlin support
- Automated build process

### Janino (Not Recommended)

Janino allows running uncompiled .java files directly but:
- Has limitations and inconsistencies
- Provides worse error messages
- Does not support debugging
- Only recommended for quick prototyping

---

## Community Resources

### Mod Indexes

| Resource | URL | Description |
|----------|-----|-------------|
| **Starsector Mods** | https://starsectormods.com/ | Community mod catalog |
| **Forum Mod Index** | https://fractalsoftworks.com/forum/index.php?board=8.0 | Official forum releases |

### GitHub Resources

| Repository | URL | Description |
|------------|-----|-------------|
| **IntelliJ Template** | https://github.com/wispborne/Starsector-IntelliJ-Template | Pre-configured mod template |
| **Gradle Template** | https://github.com/wispborne/starsector-mod-template | Gradle-based template |
| **Modding Tutorials** | https://github.com/WadeStar/Starsector-Modding-Tutorials | Learning exercises |
| **LazyLib** | https://github.com/LazyWizard/lazylib | Utility library source |
| **MagicLib** | https://github.com/MagicLibStarsector/MagicLib | Community library source |

### Learning Resources

- **University of Helsinki Java MOOC** - Recommended for learning Java
- **Kotlin Koans** - For Kotlin syntax learning
- **Existing Mod Source Code** - Study open-source mods

### Tips from the Community

1. **Use Devmode**: Enable in settings.json for testing
2. **F8 Reload**: In simulator, press F8 to reload JSON/CSV without restart
3. **Search Forum Posts by Alex**: Developer posts contain authoritative information
4. **Older Info Often Valid**: Game updates add rather than remove, so old tutorials usually work
5. **Read Other Mods**: Source code is the best learning resource

---

## Quick Reference: Console Commands

Enable devmode, then use these commands for testing:

| Command | Description |
|---------|-------------|
| `addship [hull_id]` | Add ship to fleet |
| `findship [hull_id]` | Locate ship in markets |
| `finditem [hull_id]` | Find fighter wings |
| `forceMarketUpdate` | Refresh market inventories |
| `god` | Toggle invincibility |
| `infiniteammo` | Toggle infinite ammo |

---

## Version Information

- **Document Created**: January 2026
- **Starsector Version**: 0.98a (as of March 2025)
- **Java Version**: 17

---

## Additional Notes

### File Format Requirements

- **Sprites**: PNG format, transparent background, 8 bits per channel
- **CSV Files**: Comma-delimited, UTF-8 encoding
- **JSON Files**: Standard JSON format, comments not supported in mod_info.json
- **Ship Files**: JSON format with specific structure (use Ship Editor)

### Common Mistakes to Avoid

1. Missing or mislocated mod_info.json
2. Incorrect gameVersion string
3. Non-unique mod ID
4. Incorrect file paths in CSV files
5. Wrong image dimensions or format
6. Missing descriptions.csv entries

---

*This document is a compilation of information from official Starsector sources and community wikis. Always check the official forum and wiki for the most current information.*
