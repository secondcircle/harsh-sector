# Starsector Modding: Java vs JSON

This document provides a comprehensive breakdown of what can be accomplished with data-only modding (JSON/CSV) versus what requires Java programming in Starsector.

## Table of Contents

1. [Overview](#overview)
2. [JSON/CSV-Only Modding](#jsoncsvonly-modding)
3. [When Java is Required](#when-java-is-required)
4. [Java Modding API Overview](#java-modding-api-overview)
5. [Setting Up Java Modding](#setting-up-java-modding)
6. [Common Patterns and Examples](#common-patterns-and-examples)
7. [Sources](#sources)

---

## Overview

Starsector's modding system uses a file-merge approach where mods add, modify, or replace files without changing core game files. The game engine collates all files into a unified data structure at load time.

**Key principle**: CSV files merge by ID (mod entries replace vanilla entries with matching IDs), while JSON files merge all elements together.

### Quick Decision Guide

| Task | JSON/CSV Only? | Java Required? |
|------|----------------|----------------|
| New ships, weapons, fighters | Yes | No |
| Balance tweaks (stats) | Yes | No |
| New factions | Yes | No |
| Ship variants | Yes | No |
| Ship skins | Yes | No |
| Custom hullmods with logic | No | Yes |
| Custom skills | No | Yes |
| Weapons with special effects | No | Yes |
| Campaign scripts/events | No | Yes |
| Custom AI behavior | No | Yes |
| Procedural star systems | No | Yes |

---

## JSON/CSV-Only Modding

You do **NOT** need Java or an IDE for basic content creation. The following can be accomplished entirely with data files.

### Core Data Files

#### Ship Data (`data/hulls/ship_data.csv`)

Defines all ship statistics. Key columns:

| Column | Type | Description |
|--------|------|-------------|
| `id` | Text | Internal ID used to reference this ship |
| `name` | Text | Display name |
| `designation` | Text | Ship classification (Frigate, Destroyer, etc.) |
| `system id` | Text | References ship system from ship_systems.csv |
| `hitpoints` | Number | Hull strength |
| `armor rating` | Number | Armor value |
| `max flux` | Number | Base flux capacity |
| `flux dissipation` | Number | Flux recovery rate |
| `ordnance points` | Number | OP budget for weapons/hullmods |
| `fighter bays` | Number | Hangar capacity (max 6 recommended) |
| `max speed` | Number | Maximum velocity |
| `shield type` | Text | NONE, OMNI, FRONT, or PHASE |
| `shield arc` | Number | Shield coverage in degrees |
| `shield efficiency` | Number | Damage-to-flux multiplier |
| `cargo` | Number | Cargo capacity |
| `fuel` | Number | Fuel tank size |
| `max burn` | Number | Travel speed |
| `supplies/mo` | Number | Monthly maintenance |
| `hints` | Text | Tags like CARRIER, STATION, CIVILIAN |
| `tags` | Text | Blueprint package associations |

#### Weapon Data (`data/weapons/weapon_data.csv`)

Defines weapon statistics. Key columns:

| Column | Type | Description |
|--------|------|-------------|
| `id` | Text | Internal ID (must be unique) |
| `name` | Text | Display name |
| `tier` | Integer | 0-3 for market availability (5+ = unavailable) |
| `range` | Float | Maximum range |
| `damage/shot` | Float | Damage per projectile |
| `damage/second` | Float | DPS for beam weapons |
| `emp` | Float | EMP damage per shot |
| `turn rate` | Float | Turret rotation speed |
| `OPs` | Integer | Ordnance point cost |
| `ammo` | Integer | Total shots (null = unlimited) |
| `type` | Text | KINETIC, ENERGY, HIGH_EXPLOSIVE, FRAGMENTATION |
| `energy/shot` | Float | Flux generated per shot |
| `proj speed` | Float | Projectile velocity |
| `hints` | Text | AI hints (PD, STRIKE, SYSTEM, etc.) |
| `tags` | Text | Autofit/faction tags |

**Hint values**:
- `PD` - Weapon can target missiles
- `STRIKE` - Only targets destroyer+ size ships
- `SYSTEM` - Built-in/decorative (hidden from codex)

#### Fighter Wings (`data/hulls/wing_data.csv`)

Defines fighter squadrons including:
- Wing size and replacement time
- Fighter variant reference
- Formation and behavior tags

#### Descriptions (`data/strings/descriptions.csv`)

Contains all text descriptions for ships, weapons, factions, etc. Uses `id`, `type`, and `text1`/`text2`/`text3` columns.

### Faction Configuration

#### Faction Files (`data/world/factions/*.faction`)

JSON files defining faction behavior, appearance, and fleet composition.

**Required fields**:
```json
{
  "id": "my_faction",
  "displayName": "My Faction",
  "displayNameWithArticle": "the My Faction",
  "logo": "graphics/factions/my_faction.png",
  "crest": "graphics/factions/crest_my_faction.png",
  "color": [200, 100, 50, 255],

  "names": {
    "modern": 1,
    "world": 1
  },

  "portraits": {
    "standard_male": ["graphics/portraits/portrait1.png"],
    "standard_female": ["graphics/portraits/portrait2.png"]
  }
}
```

**Fleet Doctrine** (controls AI fleet composition):
```json
"factionDoctrine": {
  "warships": 3,      // 0-5: fewer to more warships
  "carriers": 2,      // 0-5: fewer to more carriers
  "phaseShips": 1,    // 0-5: fewer to more phase ships
  "officerQuality": 3, // 1-5: officer count/level
  "shipQuality": 4,   // 1-5: fewer D-mods
  "numShips": 3,      // 1-5: fleet size
  "aggression": 3     // 1=Cautious, 3=Aggressive, 5=Reckless
}
```

**Ship Roles** (what ships the faction uses):
```json
"shipRoles": {
  "fastAttack": {
    "wolf_Attack": 10,
    "tempest_Attack": 5
  },
  "combatLarge": {
    "conquest_Standard": 8
  }
}
```

**Market Configuration**:
```json
"hullMods": ["heavyarmor", "hardenedshieldemitter"],
"illegalCommodities": ["drugs", "organs"]
```

**Blueprint Knowledge**:
```json
"knownShips": ["my_frigate", "my_destroyer"],
"priorityShips": ["my_cruiser"],
"knownFighters": ["my_fighter_wing"],
"knownWeapons": ["my_laser", "my_cannon"]
```

#### Faction Registration (`data/world/factions/factions.csv`)

Register your faction file:
```csv
faction
data/world/factions/my_faction.faction
```

### Ship Variants (`data/variants/*.variant`)

JSON files defining ship loadouts:
```json
{
  "variantId": "wolf_Attack",
  "hullId": "wolf",
  "displayName": "Attack",
  "goalVariant": true,
  "fluxCapacitors": 5,
  "fluxVents": 10,
  "hullMods": ["hardenedshieldemitter"],
  "weaponGroups": [
    {
      "autofire": false,
      "mode": "LINKED",
      "weapons": {
        "WS 001": "heavyblaster"
      }
    }
  ]
}
```

### Ship Skins (`data/hulls/skins/*.skin`)

Modify existing ships without creating new hulls:
```json
{
  "baseHullId": "wolf",
  "skinHullId": "wolf_d",
  "hullName": "Wolf (D)",
  "descriptionPrefix": "A degraded version...",
  "spriteName": "graphics/ships/wolf_d.png",
  "removeHints": ["CIVILIAN"],
  "addHints": [],
  "removeBuiltInMods": ["surveying_equipment"],
  "builtInMods": ["ill_advised"]
}
```

### Industries and Economy

#### Industries (`data/campaign/industries.csv`)

| Column | Description |
|--------|-------------|
| `id` | Internal identifier |
| `name` | Display name |
| `cost mult` | Build cost = value x 5000 |
| `build time` | Days to construct |
| `upkeep` | Monthly cost = value x 500 x (market size - 2) |
| `upgrade`/`downgrade` | Related structure IDs |
| `plugin` | Script path (if custom behavior needed) |
| `order` | Display order in colony screen |

#### Market Conditions

Defined in `data/campaign/market_conditions.csv` - environmental/social conditions affecting markets.

### Star Systems (CSV Method)

Simple star systems can be created via CSV configuration in `data/campaign/starmap.json` and related files, without Java. This supports:
- Star placement and types
- Planet orbits and conditions
- Market setup
- Station placement

**Note**: Complex procedural systems require Java.

### Balance Tweaks

Any stat in the CSV files can be modified:
- Weapon damage, range, flux costs
- Ship speed, armor, shields
- Hullmod effects and costs
- Fighter stats

### Hot Reloading

With `devmode` enabled, pressing **F8** in the simulator re-reads `.json` and `.csv` files without restarting, enabling rapid iteration.

---

## When Java is Required

Java is necessary when you need **custom logic** beyond simple data values.

### Custom Hullmods

Hullmods that do more than apply flat stat bonuses require Java scripts.

**Example scenarios requiring Java**:
- Conditional effects based on ship state
- Effects that interact with combat events
- Hullmods that modify other hullmods
- Dynamic stat calculations

**Registration**: Entry in `hull_mods.csv` with `script` column pointing to your class.

### Custom Skills

Skills with unique effects beyond standard stat modifications need Java implementation.

**Key interfaces**:
- `CharacterStatsSkillEffect` - Affects character stats
- `FleetStatsSkillEffect` - Affects fleet-wide stats
- `ShipSkillEffect` - Affects individual ships

### Custom Weapons with Special Behavior

Weapons needing behavior beyond standard projectile/beam mechanics:

| Effect Type | Interface | Specified In |
|-------------|-----------|--------------|
| Every-frame effects | `EveryFrameWeaponEffectPlugin` | `.wpn` file ("everyFrameEffect") |
| On-hit effects | `OnHitEffectPlugin` | `.proj` file ("onHitEffect") |
| Beam effects | `BeamEffectPlugin` | `.wpn` file ("beamEffect") |
| Custom autofire | `AutofireAIPlugin` | Via ModPlugin |

**Examples in vanilla**:
- `sensordish.wpn` - EveryFrameWeaponEffect
- `tachyonlance.wpn` - BeamEffect
- `ioncannon_shot.proj` - OnHitEffect

### Custom Ship Systems

While basic ship systems can use built-in types (WEAPON, ENGINE_MOD, SHIELD_MOD, etc.), custom behavior requires scripts.

**Built-in system types**:
- STAT_MOD - Simple stat modifications
- TELEPORTER - Blink/teleport effects
- PHASE_CLOAK - Phase behavior
- DISPLACER - Displacement effects
- EMP - EMP discharge
- DRONE_LAUNCHER - Drone deployment

**Custom systems** require:
1. Entry in `ship_systems.csv`
2. `.system` file in `data/shipsystems/`
3. Script implementing `ShipSystemScript` or extending `BaseShipSystemScript`
4. Optional AI script implementing `ShipSystemAIScript`

### Campaign Scripts and Events

Any campaign-level logic requires Java:
- Custom events and missions
- Bar events
- Fleet interactions
- Story progression
- Economy modifications

### Procedural Star Systems

While simple systems can use CSV, complex procedural generation requires Java using `SectorAPI` and related classes.

### AI Modifications

Custom AI behavior for ships, missiles, or drones:

| AI Type | Interface | Registration |
|---------|-----------|--------------|
| Ship AI | `ShipAIPlugin` | Via `BaseModPlugin.pickShipAI()` |
| Missile AI | `MissileAIPlugin` | Via `BaseModPlugin.pickMissileAI()` |
| Drone AI | `DroneAIPlugin` | Via `BaseModPlugin.pickDroneAI()` |
| Weapon autofire | `AutofireAIPlugin` | Via `BaseModPlugin.pickWeaponAutofireAI()` |

---

## Java Modding API Overview

### Core Classes

#### Global (`com.fs.starfarer.api.Global`)

The primary entry point for accessing game systems:

```java
import com.fs.starfarer.api.Global;

// Access the sector (campaign layer)
SectorAPI sector = Global.getSector();

// Access settings/configuration
SettingsAPI settings = Global.getSettings();

// Access combat engine (in combat only)
CombatEngineAPI engine = Global.getCombatEngine();
```

#### SectorAPI (`com.fs.starfarer.api.campaign.SectorAPI`)

Campaign-level access:
```java
SectorAPI sector = Global.getSector();

// Get player fleet
CampaignFleetAPI playerFleet = sector.getPlayerFleet();

// Get faction
FactionAPI faction = sector.getFaction("hegemony");

// Get star systems
for (StarSystemAPI system : sector.getStarSystems()) {
    // ...
}
```

#### SettingsAPI (`com.fs.starfarer.api.SettingsAPI`)

Access configuration and load custom data:
```java
SettingsAPI settings = Global.getSettings();

// Load ship spec
ShipHullSpecAPI spec = settings.getHullSpec("wolf");

// Load weapon spec
WeaponSpecAPI weapon = settings.getWeaponSpec("heavyblaster");

// Load custom JSON
JSONObject json = settings.loadJSON("data/config/mymod.json");
```

### Key Interfaces

#### BaseModPlugin

The master plugin for your mod, registered in `mod_info.json`:

```java
public class MyModPlugin extends BaseModPlugin {

    @Override
    public void onApplicationLoad() throws Exception {
        // Called after all loading finishes
        // Good for validation and one-time setup
    }

    @Override
    public void onNewGame() {
        // Called when new game is created
        // Add your star systems, factions, etc.
    }

    @Override
    public void onGameLoad(boolean newGame) {
        // Called when save is loaded (and after onNewGame for new games)
        // Add campaign scripts, listeners, etc.
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        // Called after economy initialization
        // Markets are fully set up at this point
    }
}
```

**Lifecycle order**: `onNewGame` -> `onNewGameAfterProcGen` -> `onNewGameAfterEconomyLoad` -> `onEnabled` -> `onNewGameAfterTimePass` -> `onGameLoad`

#### HullModEffect

For custom hullmods (typically extend `BaseHullMod`):

```java
public class MyHullmod extends BaseHullMod {

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize,
            MutableShipStatsAPI stats, String id) {
        // Apply stat modifications - affects campaign
        stats.getArmorBonus().modifyPercent(id, 25f);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        // Apply visual/behavioral effects - does NOT affect campaign stats
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        // Called every frame in combat
        // 'amount' is time in seconds since last frame
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        // Return false if this hullmod can't be installed
        return ship.getVariant().hasHullMod(HullMods.CIVGRADE) == false;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return "Cannot be installed on civilian ships";
    }
}
```

**Critical note**: Hullmod scripts are shared across all ships. Do NOT use instance variables for ship-specific data. Use `CombatEngineAPI.getCustomData()` keyed by ship ID instead.

#### EveryFrameCombatPlugin

For combat-wide effects:

```java
public class MyCombatPlugin implements EveryFrameCombatPlugin {

    @Override
    public void init(CombatEngineAPI engine) {
        // Called once when combat starts
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine == null || engine.isPaused()) return;

        // Called every frame
    }
}
```

Register in `data/config/settings.json`:
```json
{
  "plugins": {
    "myCombatPlugin": "com.mymod.MyCombatPlugin"
  }
}
```

#### ShipSystemScript

For custom ship system behavior:

```java
public class MySystemScript extends BaseShipSystemScript {

    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        // Called when system is active
        // effectLevel ranges 0-1 during activation/deactivation
        stats.getMaxSpeed().modifyPercent(id, 100f * effectLevel);
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        // Called when system deactivates
        stats.getMaxSpeed().unmodify(id);
    }
}
```

---

## Setting Up Java Modding

### Option 1: IntelliJ Template (Recommended)

The simplest approach using pre-configured IntelliJ project files.

**Repository**: https://github.com/wispborne/Starsector-IntelliJ-Template

**Setup Steps**:

1. Download/clone the template
2. Open in IntelliJ IDEA (Community Edition is free)
3. Set up JDK 17:
   - File -> Project Structure -> Project tab
   - Under "Project SDK", click dropdown -> Add SDK -> Download JDK
   - Choose version 17 (Azul Zulu preferred, but any JDK 17 works)

4. Copy Starsector API files to `libs/`:
   - Copy `starsector-core/starfarer.api.jar` to `libs/`
   - Optional: Copy `starsector-core/starfarer.api.zip` for source documentation

5. Configure run settings:
   - Run -> Edit Configurations
   - Set working directory to your `starsector-core` folder

6. Rename the example package to your mod name

### Option 2: Gradle Template

More flexible but more complex setup.

**Repository**: https://github.com/wispborne/starsector-mod-template

**Key Configuration** (`build.gradle.kts`):

```kotlin
// Section A: Basic settings
val modName = "MyMod"
val starsectorDirectory = "C:/Program Files (x86)/Fractal Softworks/Starsector"

// Section D: Dependencies (for other mod APIs)
dependencies {
    compileOnly(fileTree("libs") { include("*.jar") })
    // Add other mod dependencies here
}
```

### Option 3: Janino (Not Recommended)

Janino allows running uncompiled `.java` files directly, but has significant limitations:
- Limited Java feature support
- Harder-to-debug errors
- Some code that compiles normally won't work
- No IDE benefits (autocomplete, error checking)

Only use for very simple scripts or quick testing.

### Project Structure

```
MyMod/
├── mod_info.json           # Required: mod metadata
├── data/
│   ├── config/
│   │   └── settings.json   # Plugin registration
│   ├── hullmods/
│   │   └── hull_mods.csv   # Hullmod definitions
│   ├── hulls/
│   │   └── ship_data.csv   # Ship definitions
│   ├── weapons/
│   │   └── weapon_data.csv # Weapon definitions
│   ├── shipsystems/
│   │   └── ship_systems.csv
│   └── scripts/            # Uncompiled scripts (Janino)
│       └── plugins/        # Auto-registered plugins
├── jars/
│   └── MyMod.jar           # Compiled code
├── graphics/               # Sprites, textures
└── sounds/                 # Audio files
```

### mod_info.json with Java

```json
{
  "id": "my_mod",
  "name": "My Mod",
  "author": "Your Name",
  "version": "1.0.0",
  "description": "My awesome mod",
  "gameVersion": "0.97a",
  "jars": ["jars/MyMod.jar"],
  "modPlugin": "com.mymod.MyModPlugin"
}
```

---

## Common Patterns and Examples

### Complete Hullmod Example

**hull_mods.csv entry**:
```csv
name,id,tier,rarity,tech/manufacturer,tags,uiTags,base_value,unlocked,hidden,hiddenEverywhere,cost_frigate,cost_destroyer,cost_cruiser,cost_capital,script
Reinforced Armor,mymod_reinforced_armor,2,0.5,Common,req_spaceport,,10000,FALSE,FALSE,FALSE,3,6,10,20,data.hullmods.ReinforcedArmor
```

**ReinforcedArmor.java**:
```java
package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ReinforcedArmor extends BaseHullMod {

    private static final float ARMOR_BONUS = 15f;
    private static final float SPEED_PENALTY = 10f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize,
            MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
        stats.getMaxSpeed().modifyPercent(id, -SPEED_PENALTY);
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) ARMOR_BONUS + "%";
        if (index == 1) return "" + (int) SPEED_PENALTY + "%";
        return null;
    }
}
```

### Weapon On-Hit Effect Example

**my_projectile.proj** (partial):
```json
{
  "id": "my_projectile",
  "onHitEffect": "data.scripts.weapons.MyOnHitEffect"
}
```

**MyOnHitEffect.java**:
```java
package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;

public class MyOnHitEffect implements OnHitEffectPlugin {

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult,
                      CombatEngineAPI engine) {

        if (target instanceof ShipAPI) {
            ShipAPI ship = (ShipAPI) target;

            // Apply EMP arc effect
            engine.spawnEmpArc(
                projectile.getSource(),  // source ship
                point,                    // from point
                target,                   // to entity
                target,                   // target entity
                DamageType.ENERGY,        // damage type
                100f,                     // damage
                200f,                     // emp damage
                10000f,                   // max range
                "tachyon_lance_emp_impact", // sound
                15f,                      // thickness
                new java.awt.Color(100, 100, 255, 255),  // fringe color
                new java.awt.Color(255, 255, 255, 255)   // core color
            );
        }
    }
}
```

### Campaign Script Example

**MyModPlugin.java**:
```java
package com.mymod;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class MyModPlugin extends BaseModPlugin {

    @Override
    public void onGameLoad(boolean newGame) {
        // Add a campaign script that runs every frame
        Global.getSector().addTransientScript(new MyCampaignScript());
    }
}
```

**MyCampaignScript.java**:
```java
package com.mymod;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

public class MyCampaignScript implements EveryFrameScript {

    private float elapsed = 0f;

    @Override
    public boolean isDone() {
        return false; // Run indefinitely
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        // Convert to campaign days
        float days = Global.getSector().getClock().convertToDays(amount);
        elapsed += days;

        // Do something every 30 days
        if (elapsed >= 30f) {
            elapsed = 0f;
            // Your periodic logic here
            CampaignFleetAPI player = Global.getSector().getPlayerFleet();
            // ...
        }
    }
}
```

---

## Sources

### Official Resources
- [Starsector Wiki - Intro to Modding](https://starsector.wiki.gg/wiki/Intro_to_Modding)
- [Starsector Wiki - Getting Started with Mod Programming](https://starsector.wiki.gg/wiki/Getting_started_with_mod_programming)
- [Starsector Wiki - Modding](https://starsector.wiki.gg/wiki/Modding)
- [Starsector Wiki - Modding Plugins](https://starsector.wiki.gg/wiki/Modding_Plugins)

### Data File Documentation
- [Ship Data CSV](https://starsector.wiki.gg/wiki/Ship_Data_CSV)
- [Weapon Data CSV](https://starsector.wiki.gg/wiki/Weapon_data.csv)
- [Hull Mods CSV](https://starsector.wiki.gg/wiki/Hull_mods.csv)
- [Industries CSV](https://starsector.wiki.gg/wiki/Industries_CSV)
- [Ship Systems CSV](https://starsector.wiki.gg/wiki/Ship_Systems_CSV)
- [Faction File Overview](https://starsector.wiki.gg/wiki/File_overview:_faction)
- [Mod Info.json Overview](https://starsector.wiki.gg/wiki/Mod_Info.json_Overview)

### Java Modding Guides
- [Modding Hullmods](https://starsector.wiki.gg/wiki/Modding_hullmods)
- [Modding Ship Systems Overview](https://starsector.wiki.gg/wiki/Modding_ship_systems_overview)
- [IntelliJ IDEA Setup](https://starsector.wiki.gg/wiki/IntelliJ_IDEA_Setup)

### API Documentation
- [Starsector API (jaghaimo mirror)](https://jaghaimo.github.io/starsector-api/)
- [BaseModPlugin API](https://fractalsoftworks.com/starfarer.api/com/fs/starfarer/api/BaseModPlugin.html)
- [EveryFrameScript API](https://fractalsoftworks.com/starfarer.api/com/fs/starfarer/api/EveryFrameScript.html)
- [SkillSpecAPI](https://fractalsoftworks.com/starfarer.api/com/fs/starfarer/api/characters/SkillSpecAPI.html)

### Templates and Tools
- [Starsector IntelliJ Template](https://github.com/wispborne/Starsector-IntelliJ-Template)
- [Starsector Gradle Mod Template](https://github.com/wispborne/starsector-mod-template)
- [Starsector Modding Tutorials Repository](https://github.com/WadeStar/Starsector-Modding-Tutorials)

### Community Resources
- [Fractal Softworks Forum - Modding](https://fractalsoftworks.com/forum/index.php?board=10.0)
- [List of Mods with Source Code](https://starsector.wiki.gg/wiki/List_of_mods_and_source_code)
