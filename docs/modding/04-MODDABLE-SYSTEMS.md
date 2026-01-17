# Starsector Moddable Systems Reference

This document catalogs the game systems in Starsector that can be modified through modding, focusing on QOL enhancements, rebalancing, and new mechanics (excluding visual/sprite-only modifications).

---

## Table of Contents

1. [Combat Mechanics](#1-combat-mechanics)
2. [Campaign and Economy Systems](#2-campaign-and-economy-systems)
3. [Fleet Management and Logistics](#3-fleet-management-and-logistics)
4. [Skills and Character Progression](#4-skills-and-character-progression)
5. [Hullmods and Ship Systems](#5-hullmods-and-ship-systems)
6. [AI Behavior](#6-ai-behavior)
7. [Events and Storyline/Missions](#7-events-and-storylinemissions)
8. [Settings and Configuration](#8-settings-and-configuration)
9. [Popular QOL Mods Reference](#9-popular-qol-mods-reference)
10. [Essential Modding Libraries](#10-essential-modding-libraries)

---

## 1. Combat Mechanics

### Overview
Combat in Starsector revolves around flux management, damage types, shields, armor, and weapon systems. Nearly all aspects can be modified through CSV files and Java scripting.

### 1.1 Weapons

**What it controls:**
- Damage values and types (Kinetic, Energy, High Explosive, Fragmentation)
- Fire rates, burst patterns, and reload mechanics
- Range, accuracy, and projectile behavior
- Flux generation per shot
- EMP damage and special effects

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `weapon_data.csv` | `data/weapons/` | CSV |
| `.wpn` files | `data/weapons/` | JSON |
| `.proj` files | `data/weapons/` | JSON |
| `descriptions.csv` | `data/strings/` | CSV |

**Key weapon_data.csv columns:**
- `damage/shot`, `damage/second` - Damage values
- `emp` - EMP damage per shot
- `type` - KINETIC, ENERGY, HIGH_EXPLOSIVE, FRAGMENTATION
- `range` - Maximum effective distance
- `energy/shot`, `energy/second` - Flux cost
- `chargeup`, `chargedown` - Firing delays
- `burst size`, `burst delay` - Multi-shot patterns
- `min spread`, `max spread` - Accuracy
- `hints` - AI behavior (PD, STRIKE, SYSTEM)
- `tags` - Autofit and availability flags

**Difficulty:** Beginner to Intermediate (CSV-only changes are beginner-friendly; custom projectile behavior requires Java)

**Example mods:**
- [Starsector Rebalance Pack](https://fractalsoftworks.com/forum/index.php?topic=12502.0) - Adjusts weapon damage and stats

### 1.2 Damage Types and Combat Balance

**What it controls:**
- Damage multipliers vs shields, armor, and hull
- Flux system parameters
- Shield efficiency calculations
- Armor damage reduction formulas

**How damage types work:**
| Type | vs Shields | vs Armor | vs Hull |
|------|------------|----------|---------|
| Kinetic | 200% | 50% | 100% |
| High Explosive | 50% | 200% | 100% |
| Energy | 100% | 100% | 100% |
| Fragmentation | 25% | 25% | 100% |

**Files to modify:**
- `settings.json` - Global combat parameters
- Individual weapon files for per-weapon changes

**Difficulty:** Intermediate (requires understanding of combat math)

### 1.3 Ships and Hulls

**What it controls:**
- Hull stats (HP, armor, flux capacity/dissipation)
- Ordnance points allocation
- Weapon slot configurations
- Shield arc and efficiency
- Speed, maneuverability, and acceleration

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `ship_data.csv` | `data/hulls/` | CSV |
| `.ship` files | `data/hulls/` | JSON |
| `.variant` files | `data/variants/` | JSON |
| `.skin` files | `data/hulls/skins/` | JSON |

**Difficulty:** Beginner to Intermediate

**Example mods:**
- Most faction mods modify ship stats alongside adding new ships

---

## 2. Campaign and Economy Systems

### 2.1 Markets and Trade

**What it controls:**
- Commodity prices and availability
- Tariff rates (30% default on Open Market)
- Black market mechanics
- Military market restrictions
- Supply and demand simulation

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `commodities.csv` | `data/campaign/` | CSV |
| `economy.json` | `data/config/` | JSON |
| Market condition files | `data/campaign/` | CSV/JSON |

**Difficulty:** Intermediate

**Example mods:**
- [Starpocalypse](https://github.com/jaghaimo/starpocalypse) - Makes weapons and combat ships scarce; restricts higher-tier items to Military/Black Markets only
- [Galactic Markets](https://fractalsoftworks.com/forum/index.php?topic=19383.0) - Economy services mod

### 2.2 Colonies and Industries

**What it controls:**
- Industry types and their effects
- Production values and costs
- Build slots and requirements
- Colony growth mechanics
- Market conditions and modifiers

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `industries.csv` | `data/campaign/` | CSV |
| Industry Java scripts | `data/scripts/` | Java |
| `market_conditions.csv` | `data/campaign/` | CSV |

**Difficulty:** Intermediate to Advanced (simple stat changes are intermediate; new industries require Java)

**Example mods:**
- [Industrial.Evolution](https://github.com/SirHartley/Industrial.Evolution) - Adds new industries, courier systems, engineering hubs
- [StarSectorIndustry](https://github.com/alycecil/StarSectorIndustry) - Colony builder with AI that constructs buildings

### 2.3 Factions

**What it controls:**
- Faction relationships and diplomacy
- Ship/weapon access and blueprints
- Fleet doctrine and composition
- Market presence and territory
- Faction-specific mechanics

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| Faction files | `data/world/factions/` | JSON |
| `default_ship_roles.json` | `data/world/factions/` | JSON |
| `factions.csv` | `data/world/` | CSV |

**Difficulty:** Intermediate

**Example mods:**
- [Nexerelin](https://github.com/Histidine91/Nexerelin) - Full 4X gameplay with diplomacy, faction wars, invasions, and player faction creation

---

## 3. Fleet Management and Logistics

### 3.1 Combat Readiness (CR)

**What it controls:**
- Base maximum CR (70% default)
- CR recovery rates
- Peak performance time
- CR degradation in combat
- Malfunction thresholds

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `settings.json` | `data/config/` | JSON |
| Ship data files | `data/hulls/` | CSV |
| Hullmod scripts | `data/hullmods/` | Java |

**Key mechanics:**
- CR decreases 0.25% per second after peak performance time expires
- At 40% CR, malfunctions begin occurring
- Crew shortages proportionally reduce max CR

**Difficulty:** Beginner (settings.json) to Intermediate (hullmods)

### 3.2 Supplies and Maintenance

**What it controls:**
- Monthly supply consumption per ship
- Repair costs and rates
- Deployment costs in combat
- Recovery costs post-battle
- Mothballing mechanics (90% maintenance reduction)

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `ship_data.csv` | `data/hulls/` | CSV |
| `settings.json` | `data/config/` | JSON |

**Difficulty:** Beginner

**Example mods:**
- [Efficiency Overhaul](https://starsector.wiki.gg/wiki/Efficiency_Overhaul) (vanilla hullmod) - 50% faster CR recovery

### 3.3 Logistics Rating

**What it controls:**
- Fleet logistics capacity
- Supply usage calculations
- Maximum CR penalties when over logistics limit
- Accident chances at 0% logistics rating

**Formula:** LR = logistics_stat / daily_supply_use (capped at 100%)

**Files to modify:**
- `settings.json` for global parameters
- Skills that affect logistics (see Skills section)

**Difficulty:** Intermediate

---

## 4. Skills and Character Progression

### 4.1 Player Skills

**What it controls:**
- 40 skills across 4 aptitude trees (Combat, Leadership, Technology, Industry)
- Skill effects and bonuses
- Tier requirements and progression
- Elite skill upgrades
- Story Point costs for respec

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `skills.csv` | `data/characters/skills/` | CSV |
| Skill scripts | `data/scripts/skills/` | Java |
| `settings.json` | `data/config/` | JSON |

**Skill tree structure:**
- Tier 1: Basic bonuses, immediately available
- Tiers 2-4: Require points in previous tiers
- Capstones: Require 4-6 points in lower tiers

**Difficulty:** Advanced (skills are heavily Java-dependent)

**Example mods:**
- [Fully Adjustable Skills](https://github.com/AudaxLudos/fully-adjustable-skills) - Adjust vanilla skill thresholds
- [Second-in-Command](https://secondincommand.wiki.gg/) - Major skill system overhaul with 12 aptitudes and 125 skills

### 4.2 Officers

**What it controls:**
- Maximum officer count (8 default, 10 with skill)
- Officer level cap (5 default, 6 with skill)
- Elite skill limits (1 default, up to 4 with skills)
- Skill selection pool
- Personality types

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `settings.json` | `data/config/` | JSON |
| Officer-related scripts | `data/scripts/` | Java |

**Difficulty:** Intermediate to Advanced

**Example mods:**
- [Officer Extension](https://github.com/qcwxezda/Starsector-Officer-Extension) - Manage unlimited officers, suspend/demote officers, customize skill choices on level-up

---

## 5. Hullmods and Ship Systems

### 5.1 Hullmods

**What it controls:**
- Ship stat modifications (armor, speed, flux, etc.)
- Special abilities and behaviors
- OP costs and restrictions
- S-mod (built-in) bonus effects
- D-mod (damage) penalties

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `hull_mods.csv` | `data/hullmods/` | CSV |
| Hullmod scripts | `data/hullmods/` | Java |

**Key methods in hullmod scripts:**
| Method | Purpose |
|--------|---------|
| `applyEffectsBeforeShipCreation()` | Stat changes (affects campaign) |
| `applyEffectsAfterShipCreation()` | Non-stat effects |
| `advanceInCombat()` | Per-frame combat updates |
| `getUnapplicableReason()` | Installation restrictions |

**CRITICAL:** Hullmod scripts are singleton - one instance shared across ALL ships. Cannot use instance variables.

**Difficulty:** Intermediate (CSV stats) to Advanced (Java scripting)

**Source:** [Starsector Wiki - Modding Hullmods](https://starsector.wiki.gg/wiki/Modding_hullmods)

### 5.2 Ship Systems

**What it controls:**
- Active abilities (phase cloak, burn drive, etc.)
- System effects and durations
- Cooldowns and charges
- AI usage behavior
- Flux costs

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `ship_systems.csv` | `data/weapons/` | CSV |
| `.system` files | `data/shipsystems/` | JSON |
| System scripts | `data/scripts/shipsystems/` | Java |

**Built-in system types:**
WEAPON, ENGINE_MOD, SHIELD_MOD, STAT_MOD, FAST_RELOAD, AMMO_RELOAD, TELEPORTER, PHASE_CLOAK, DISPLACER, DRONE_LAUNCHER, EMP, CUSTOM

**Difficulty:** Intermediate (using built-in types) to Advanced (custom scripted systems)

**Source:** [Starsector Wiki - Modding Ship Systems](https://starsector.wiki.gg/wiki/Modding_ship_systems_overview)

---

## 6. AI Behavior

### 6.1 Combat AI

**What it controls:**
- Ship movement and positioning
- Target selection and prioritization
- Weapon autofire behavior
- System usage timing
- Shield management
- Flux management

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `settings.json` | `data/config/` | JSON (AI parameters) |
| Custom AI plugins | `data/scripts/` | Java |

**Plugin interfaces:**
- `ShipAIPlugin` - Full ship AI replacement
- `AutofireAIPlugin` - Weapon targeting AI
- `MissileAIPlugin` - Missile guidance AI

**Registration:** Via `BaseModPlugin.pickShipAI()`, `pickWeaponAutofireAI()`, etc.

**Difficulty:** Advanced (requires Java and understanding of combat mechanics)

**Example mods:**
- [AI Tweaks](https://github.com/Halke1986/starsector-ai-tweaks) - Improved autofire, target selection, fleet cohesion; weapons prefer ship's main target
- [Truly Automated Ships](https://github.com/TobiaFi/TrulyAutomatedShips) - AI cores make ships more autonomous

### 6.2 Fleet AI

**What it controls:**
- Battle formation and positioning
- Ship deployment priorities
- Retreat behaviors
- Admiral-level tactics

**Key issue addressed by mods:** Vanilla AI has cruisers/capitals chase lone frigates to map edges.

**Difficulty:** Advanced

**Source:** [AI Tweaks - Fleet Cohesion](https://fractalsoftworks.com/forum/index.php?topic=28428.0)

---

## 7. Events and Storyline/Missions

### 7.1 Rules.csv (Dialog System)

**What it controls:**
- All NPC conversations
- Interaction dialogs (planets, derelicts, stations)
- Mission briefings and rewards
- Story events and choices
- Bar encounters

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `rules.csv` | `data/campaign/` | CSV |
| Dialog scripts | `data/scripts/` | Java |

**How rules.csv works:**
Each row defines: trigger condition, test condition, script to execute, display text, and dialog options. It's essentially a custom scripting language.

**Key features:**
- Auto-reloads in dev mode (no restart needed)
- Variables stored in entity memory
- `FireBest` picks highest-scoring rule; `FireAll` executes all matching rules
- Custom scripts must be in packages defined in `settings.json` under `ruleCommandPackages`

**Difficulty:** Intermediate (simple dialogs) to Advanced (complex branching quests)

**Resources:**
- [Official Tutorial PDF](https://s3.amazonaws.com/fractalsoftworks/doc/StarsectorRuleScripting.pdf)
- [Rules.csv Wiki](https://starsector.wiki.gg/wiki/Rules.csv)
- [Developer Blog Post](https://fractalsoftworks.com/2023/11/13/you-merely-adopted-rules-csv-i-was-born-into-it/)

### 7.2 Bar Events

**What it controls:**
- Random encounters in station bars
- Mission offerings
- Intel and rumors
- Faction-specific events
- Story triggers

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| Bar event scripts | `data/scripts/bar/` | Java |
| `rules.csv` | `data/campaign/` | CSV |

**Implementation classes:**
- `BaseBarEvent` - Bare-bones implementation
- `BaseBarEventWithPerson` - Adds NPC with customizable attributes
- `BarEventCreator` - Spawns bar events periodically

**Difficulty:** Advanced (requires Java)

**Example mods:**
- [More Bar Missions](https://fractalsoftworks.com/forum/index.php?topic=26974.0) - Additional bar content

### 7.3 Quests

**What it controls:**
- Multi-stage mission sequences
- Intel screen tracking
- Rewards and consequences
- Custom interactions

**Components:**
1. `BarEvent` - Quest initiation
2. `BaseIntelPlugin` - Progress tracking in Intel screen
3. `InteractionDialogPlugin` - Mid-quest dialogs

**Difficulty:** Advanced (requires Java and understanding of multiple systems)

**Source:** [Starsector Wiki - Modding Quests](https://starsector.wiki.gg/wiki/Modding_Quests)

---

## 8. Settings and Configuration

### 8.1 settings.json

**Location:** `starsector-core/data/config/settings.json`

**What it controls:**
- Combat speed multiplier (`combatSpeedMult`) - WARNING: affects game balance
- Dev mode toggle (`devMode`)
- Iron mode settings (`allowForceQuitInIronMode`)
- Maximum zoom levels
- Various global parameters

**How to modify:**
1. Copy settings.json to your mod's `data/config/` folder
2. Include only the values you want to change
3. Your mod's values will override vanilla

**Difficulty:** Beginner

**Source:** [Forum Discussion on settings.json](https://fractalsoftworks.com/forum/index.php?topic=16792.0)

### 8.2 Dev Mode

**What it enables:**
- Variant Editor from main menu
- Extended zoom range
- Removes surveying/salvaging costs
- Edit other factions' colonies
- Instant industry construction
- F8 hot-reload of JSON/CSV in simulator
- Combat exit shortcuts (Backspace = instant victory)
- CTRL+Click to control any ship

**How to enable:**
1. Set `"devMode": true` in settings.json
2. Or use Console Commands mod: `devmode` command

**Difficulty:** Beginner

**Source:** [Dev Mode Wiki](https://starsector.wiki.gg/wiki/Dev_mode)

### 8.3 Star System Generation

**What it controls:**
- System size and age parameters
- Planet types and conditions
- Procedural content distribution
- Core Worlds configuration

**Methods:**
1. **CSV-based:** Simple configuration through data files
2. **Java-based:** Full control via generator classes

**Files to modify:**
| File | Location | Format |
|------|----------|--------|
| `star_gen_data.csv` | `data/campaign/procgen/` | CSV |
| `salvage_entity_gen_data.csv` | `data/campaign/procgen/` | CSV |
| System generator scripts | `data/scripts/world/` | Java |

**Difficulty:** Intermediate (CSV) to Advanced (Java generators)

**Example mods:**
- [CustomizableStarSystems](https://github.com/Tranquiliti/CustomizableStarSystems) - JSON-configured custom star systems

---

## 9. Popular QOL Mods Reference

### Essential QOL Mods

| Mod | Features | Files Modified |
|-----|----------|----------------|
| [Console Commands](https://fractalsoftworks.com/forum/index.php?topic=4106.0) | In-game command console, spawning, debugging | Plugin-based |
| [QoL Pack](https://fractalsoftworks.com/forum/index.php?topic=23652.0) | Collection of quality-of-life improvements | Various |
| [SSMSQoL](https://github.com/razuhl/SSMSQoL) | In-game settings UI, auto-drop excess cargo | Plugin + settings |
| [Version Checker](https://fractalsoftworks.com/forum/) | Monitors mod updates | Plugin-based |

### Gameplay Enhancement Mods

| Mod | Type | What It Changes |
|-----|------|-----------------|
| [Nexerelin](https://github.com/Histidine91/Nexerelin) | Overhaul | Adds 4X gameplay: diplomacy, invasions, faction wars, player faction |
| [Industrial.Evolution](https://github.com/SirHartley/Industrial.Evolution) | Content | New industries, exploration content, courier systems |
| [AI Tweaks](https://github.com/Halke1986/starsector-ai-tweaks) | AI | Improved weapon targeting, fleet cohesion, shield management |
| [Officer Extension](https://github.com/qcwxezda/Starsector-Officer-Extension) | Officers | Unlimited officer management, suspension, demotion |
| [Starpocalypse](https://github.com/jaghaimo/starpocalypse) | Economy | Scarce weapons/ships, Military Market restrictions |

### What QOL Mods Typically Modify

1. **UI Improvements:** Better zoom, radar, fleet management screens
2. **Auto-save/Quick-save:** Convenience features
3. **Cargo Management:** Auto-drop excess, smart stacking
4. **Fleet Operations:** Easier mothballing, officer assignment
5. **Information Display:** Better tooltips, combat analytics

---

## 10. Essential Modding Libraries

### Core Libraries

| Library | Purpose | Required By |
|---------|---------|-------------|
| [LazyLib](https://github.com/LazyWizard/lazylib) | Utility functions, geometry, targeting helpers | Many mods |
| [MagicLib](https://github.com/MagicLibStarsector/MagicLib) | Scripting functions, subsystems, achievements, cross-mod settings | Nexerelin, Industrial.Evolution |
| [LunaLib](https://github.com/Lukas22041/LunaLib) | In-game mod settings UI, utility classes, Codex integration | Many recent mods |
| [GraphicsLib](https://fractalsoftworks.com/forum/) | Visual effects (shaders, lighting) | Visual mods |

### MagicLib Features

- **MagicSettings:** Unified cross-mod settings via `data/config/modSettings.json`
- **MagicSubsystems:** Ship subsystem framework
- **Achievements:** Steam-like achievement system stored in `saves/common`
- [MagicLib Javadoc](https://magiclibstarsector.github.io/MagicLib/)

### LunaLib Features

- **In-game settings menu** for mod configuration
- **LunaCodex:** Adds "Mods" section to Codex
- Fully written in Kotlin
- Performance-optimized caching

---

## Difficulty Ratings Summary

| System | Data-Only | With Java |
|--------|-----------|-----------|
| Weapons | Beginner | Intermediate |
| Ships/Hulls | Beginner | Intermediate |
| Hullmods | Intermediate | Advanced |
| Ship Systems | Intermediate | Advanced |
| Skills | N/A | Advanced |
| Combat AI | N/A | Advanced |
| Markets/Economy | Intermediate | Advanced |
| Industries | Intermediate | Advanced |
| Factions | Intermediate | Intermediate |
| Events/Quests | Intermediate | Advanced |
| Star Systems | Intermediate | Advanced |
| Settings | Beginner | N/A |

---

## Sources

### Official Resources
- [Starsector Wiki - Modding](https://starsector.wiki.gg/wiki/Modding)
- [Starsector Wiki - Intro to Modding](https://starsector.wiki.gg/wiki/Intro_to_Modding)
- [Starfarer API Documentation](https://fractalsoftworks.com/starfarer.api/)
- [Fractal Softworks Forum - Modding](https://fractalsoftworks.com/forum/)

### Wiki References
- [Modding Hullmods](https://starsector.wiki.gg/wiki/Modding_hullmods)
- [Modding Ship Systems](https://starsector.wiki.gg/wiki/Modding_ship_systems_overview)
- [Modding Quests](https://starsector.wiki.gg/wiki/Modding_Quests)
- [Modding Bar Events](https://starsector.wiki.gg/wiki/Modding_Bar_Event)
- [Rules.csv](https://starsector.wiki.gg/wiki/Rules.csv)
- [Weapon_data.csv](https://starsector.wiki.gg/wiki/Weapon_data.csv)
- [Settings.json](https://starsector.wiki.gg/wiki/Settings.json)
- [Dev Mode](https://starsector.wiki.gg/wiki/Dev_mode)

### Developer Blog Posts
- [Rules.csv Deep Dive](https://fractalsoftworks.com/2023/11/13/you-merely-adopted-rules-csv-i-was-born-into-it/)
- [Skill Changes](https://fractalsoftworks.com/2021/07/02/skill-changes-part-1/)
- [Logistics & Fleet Management](https://fractalsoftworks.com/2013/05/25/logistics-fleet-management/)

### Community Resources
- [Starsector Mods](https://starsectormods.com/)
- [Rules.csv Scripting PDF](https://s3.amazonaws.com/fractalsoftworks/doc/StarsectorRuleScripting.pdf)

### GitHub Repositories
- [MagicLib](https://github.com/MagicLibStarsector/MagicLib)
- [LunaLib](https://github.com/Lukas22041/LunaLib)
- [LazyLib](https://github.com/LazyWizard/lazylib)
- [Nexerelin](https://github.com/Histidine91/Nexerelin)
- [Industrial.Evolution](https://github.com/SirHartley/Industrial.Evolution)
- [AI Tweaks](https://github.com/Halke1986/starsector-ai-tweaks)
- [Console Commands](https://github.com/LazyWizard/console-commands)
- [Officer Extension](https://github.com/qcwxezda/Starsector-Officer-Extension)
- [Starpocalypse](https://github.com/jaghaimo/starpocalypse)
- [CustomizableStarSystems](https://github.com/Tranquiliti/CustomizableStarSystems)
