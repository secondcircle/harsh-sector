# Starpocalypse Mod Reference

A comprehensive reference document for the Starpocalypse mod, documenting its features and implementation for potential recreation.

## Overview

**Starpocalypse** is a Starsector mod that creates "more apocalyptic settings" for the campaign layer. It focuses on making the game harder by restricting access to military equipment, adding d-mods to all ships, strengthening faction defenses, and introducing consequence systems for player actions.

**Status**: Archived (no longer maintained as of May 6, 2023)
**Last Version**: 2.3.2 (for Starsector 0.95.1a)
**License**: GPL-3.0

## Source Repositories

| Repository | Description | URL |
|------------|-------------|-----|
| Original (jaghaimo) | Main repository, archived | https://github.com/jaghaimo/starpocalypse |
| Economy Edition (ThomasRahm) | Fork with standing system | https://github.com/ThomasRahm/starpocalypse |
| Forum Thread | Fractalsoftworks forum | https://fractalsoftworks.com/forum/index.php?topic=21812.0 |

## Feature List

### 1. Military Market Regulations

**What it does**: Restricts high-tier weapons, LPCs (Limited Production Chips), modspecs, and combat ships to Military Markets and Black Markets only. Open Markets only sell civilian-grade ships and low-tier combat items.

**Configuration Options**:
- `militaryRegulations`: Master toggle for the system
- `regulationMaxLegalTier`: Tier threshold for legal equipment (default: 0)
- `regulationMaxLegalFP`: Fleet Point limit for legal combat ships (default: 0)
- `removeMilitaryEndgameCargo`: Remove tier 3 items from military markets
- `removeMilitaryEndgameShips`: Remove capital ships from military markets

**Exceptions**:
- Lawless factions (pirates, Luddic Path) bypass regulations
- Independents do not submit to regulations
- Low stability triggers contraband mechanics (items become "legal")

**CSV Configuration Files**:
- `militaryRegulationFaction.csv` - Which factions enforce regulations
- `militaryRegulationsStability.csv` - Stability-based contraband rules

### 2. Ship D-Mod System

**What it does**: All ships in the game have d-mods (damage modifications). No pristine ships exist, including the player's starting fleet.

**Configuration Options**:
- `addDmodsToShipsInSubmarkets`: Apply d-mods to ships for sale
- `addDmodsToStartingFleet`: Apply d-mods to player's initial fleet
- `minimumDmods`: Minimum d-mods per ship (range: 2-4)
- `maximumDmods`: Maximum d-mods per ship (range: 2-4)

**CSV Configuration Files**:
- `shipDamage*.csv` - D-mod parameters by faction/submarket

### 3. Black Market Restrictions

**What it does**: Makes Black Market access more difficult and costly.

**Features**:
- Cannot access Black Market while legally docked (transponder on)
- Must use a "fence" who takes a cut of transactions
- Suspicion builds up even with transponder off (configurable)

**Configuration Options**:
- `shyBlackMarket`: Require transponder off for black market access
- `blackMarketFenceCut`: Tariff rate for fence (default: 0.5 / 50%)
- `transparentMarket`: Suspicion grows even with transponder off
- `transparentMarketMult`: Suspicion multiplier when transponder off (default: 0.5)

### 4. Enhanced Market Defenses

**What it does**: Adds defensive structures to all non-player markets, making raiding more difficult.

**Features**:
- Ground Defenses added to all markets (including raider bases)
- Orbital Stations added to undefended markets
- Patrol HQ added to core world markets
- Pirates receive megaports and heavy batteries

**Configuration Options**:
- `addStations`: Add tier 1 stations to undefended markets
- `addGroundDefenses`: Add ground defense structures
- `addPatrolHqs`: Add patrol HQ to core worlds

**CSV Configuration Files**:
- `station*.csv` - Station technology assignments by faction

### 5. Reputation Consequences

**What it does**: Defeating enemy fleets affects reputation with related factions. Raiding colonies triggers instant hostility.

**Features**:
- Defeating a fleet adjusts reputation with allied/hostile factions
- Stealing colony items sets reputation to hostile (-1)
- Maximum adjustments: +/-1 for standard factions, +/-3 for commissioned factions

**Configuration Options**:
- `combatAdjustedReputation`: Enable reputation shifts from combat
- `hostilityForSpecialItemRaid`: Enable raid consequences

**CSV Configuration Files**:
- `reputationBlacklist.csv` - Factions exempt from reputation adjustments

### 6. Salvage and Recovery Restrictions

**What it does**: Makes acquiring ships and blueprints more difficult.

**Features**:
- Blueprint packages cannot be looted as packages; must collect blueprints individually
- All ship recoveries require story points
- Salvage rates reduced based on hull size

**Configuration Options**:
- `blueprintPackageNoDrop`: Disable blueprint package drops
- `stingyRecoveries`: Require story points for all recoveries

### 7. S-Mod Restrictions

**What it does**: Limits the number of permanent modifications (s-mods) players can apply.

**Configuration Options**:
- `maxPermaMods`: Number of s-mods available (default: 0, requires skill for 1)

### 8. Standing System (Economy Edition Fork)

**What it does**: Adds a standing system that gates access to equipment based on multiple factors.

**Standing Factors**:
- Player reputation with faction
- Best contact's reputation and quality
- Commission status
- Contact importance (for black market tiers)

**Features**:
- Buying larger hulls/high-tier weapons requires good standing
- Very low standing prevents even civilian ship purchases
- Black market requires pirate contact with sufficient importance

## Technical Implementation

### Architecture

Starpocalypse is a Java-based mod using Gradle for builds. It uses:
- **Java 1.7** compatibility (Starsector requirement)
- **Lombok** for code generation
- **XStream** for XML serialization
- **Janino** for runtime compilation

### Key Game Systems Modified

| System | Modification Type | Implementation |
|--------|------------------|----------------|
| Submarkets | Custom submarket plugins | Java (listeners) |
| Ship spawning | D-mod injection | Java + CSV config |
| Economy/Markets | Item filtering | Java + CSV whitelist |
| Faction relations | Reputation listeners | Java |
| Salvage/Recovery | Drop table modification | Java (tags) |
| Market structures | Building injection | Java (transient listeners) |

### File Structure

```
starpocalypse/
├── assets/
│   ├── starpocalypse.json     # Main configuration
│   └── mod_info.json          # Mod metadata
├── data/
│   └── starpocalypse/         # CSV configuration files
│       ├── militaryRegulationFaction.csv
│       ├── militaryRegulationsStability.csv
│       ├── station*.csv
│       ├── shipDamage*.csv
│       ├── reputationBlacklist.csv
│       └── raidProtectorItem.csv
└── src/
    └── starpocalypse/         # Java source code
```

### Configuration System

**JSON Configuration** (`starpocalypse.json`):
- Master toggles for all features
- Numeric parameters (multipliers, limits)
- Boolean flags for enabling/disabling features

**CSV Configuration** (`data/starpocalypse/*.csv`):
- Faction-specific rules
- Whitelist/blacklist systems
- Supports negation syntax and "all" keyword
- Third-party mods can override via same folder structure

### API Hooks Used

Based on the implementation, the mod likely uses these Starsector API hooks:

1. **BaseModPlugin** - Entry point for mod initialization
2. **EconomyListener** - For market/economy changes
3. **CampaignPlugin** - For campaign-layer modifications
4. **SubmarketPlugin** - For custom submarket behavior
5. **FleetMemberAPI** - For d-mod injection
6. **MarketAPI** - For market structure modification

### Compatibility Notes

- Works with Nexerelin (handles autonomous colonies specially)
- Ignores player-owned markets
- Not safe to remove mid-save (as of 2.2.0)
- To remove: delete `starpocalypse/data` folder, load, then save

## Recreation Candidates

### High Priority (Good Candidates)

| Feature | Difficulty | Notes |
|---------|------------|-------|
| Military Regulations | Medium | Core feature, CSV-driven, well-documented |
| Ship D-Mods | Easy | Straightforward injection on ship spawn |
| Enhanced Market Defenses | Easy | Simple building addition via listeners |
| Black Market Restrictions | Medium | Requires submarket plugin work |

### Medium Priority

| Feature | Difficulty | Notes |
|---------|------------|-------|
| Reputation Consequences | Medium | Requires fleet/combat listeners |
| Salvage Restrictions | Easy | Tag manipulation on items |
| S-Mod Limits | Easy | Single setting modification |

### Lower Priority / Complex

| Feature | Difficulty | Notes |
|---------|------------|-------|
| Standing System (Fork) | High | Complex multi-factor calculations |
| Transparent Market | Medium | Requires understanding suspicion system |

## Implementation Notes for Recreation

### Military Regulations Approach

1. Create a SubmarketPlugin that filters items by tier/FP
2. Use CSV files to define which factions/submarkets are regulated
3. Hook into market refresh to apply filtering
4. Consider stability-based exceptions

### D-Mod System Approach

1. Listen for ship spawns in submarkets
2. Apply random d-mods based on configured range
3. Handle starting fleet separately on new game
4. Potentially use FleetMemberAPI.getVariant().addPermaMod()

### Market Defense Approach

1. Use transient listener to add buildings
2. Check for existing buildings to avoid duplicates
3. Use faction-specific station tech mappings
4. Exclude player markets and hidden markets

## Version History Highlights

| Version | Key Changes |
|---------|-------------|
| 2.3.2 | Nexerelin exploit fix, derelict script fix |
| 2.3.0 | Blueprint packages removed, all recoveries need story points, s-mods default 0 |
| 2.2.0 | New submarket implementation, no longer safe to remove mid-save |
| 2.1.0 | Raid protector, military blacklists, random d-mods |
| 2.0.0 | Updated for 0.95.1a, core world ship quality degradation |

## References

- [GitHub - jaghaimo/starpocalypse](https://github.com/jaghaimo/starpocalypse) - Original repository (archived)
- [GitHub - ThomasRahm/starpocalypse](https://github.com/ThomasRahm/starpocalypse) - Economy Edition fork
- [Fractalsoftworks Forum Thread](https://fractalsoftworks.com/forum/index.php?topic=21812.0) - Official forum thread
- [Starsector Modding Wiki](https://starsector.wiki.gg/wiki/Getting_started_with_mod_programming) - General modding reference
