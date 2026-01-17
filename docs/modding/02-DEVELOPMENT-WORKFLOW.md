# Starsector Modding Development Workflow

This guide covers the practical day-to-day development workflow for Starsector modding, including hot reloading, debugging, testing, and iteration.

## Table of Contents

- [Development Environment Setup](#development-environment-setup)
- [Hot Reload Capabilities](#hot-reload-capabilities)
- [DevMode](#devmode)
- [Console Commands Mod](#console-commands-mod)
- [Testing and Iteration Workflow](#testing-and-iteration-workflow)
- [Logging and Debugging](#logging-and-debugging)
- [Memory Settings and JVM Arguments](#memory-settings-and-jvm-arguments)
- [Recommended Tools](#recommended-tools)

---

## Development Environment Setup

### Required Software

**Text Editors / IDEs:**
- **VS Code** - Cross-platform, full-featured with thousands of extensions (recommended for general editing)
- **Notepad++** - Lightweight, fast, Windows-only (lacks JSON validation)
- **IntelliJ IDEA Community Edition** - Recommended for Java mod development with debugging support

**Specialized Tools:**
- **Spreadsheet Editor** - For CSV files (Microsoft Excel, LibreOffice Calc, or Ron's CSV Editor)
- **Graphics Editor** - Must support transparency (Krita, GIMP, or Photoshop)
- **Starsector Ship & Weapon Editor** - For creating ship hulls and weapon offsets

> **Important:** Do not use Windows Notepad for editing code or data files.

### IDE Setup (IntelliJ IDEA)

For Java scripting, IntelliJ IDEA is the recommended IDE. The setup provides:
- Full in-editor documentation for Starsector API
- One-click build and debug configuration
- Breakpoint debugging with game attached
- Hot code reload within methods

#### Quick Setup Steps:

1. Download [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/) (free)
2. Clone or download the [Starsector IntelliJ Template](https://github.com/wispborne/Starsector-IntelliJ-Template)
3. Extract template into your Starsector `mods/` folder
4. Open the project folder in IntelliJ (select folder containing `mod_info.json`)
5. Configure JDK 17 (Azul Zulu recommended - same as Starsector uses)
6. Add dependencies via Project Structure > Libraries:
   - All `.jar` files from `starsector-core/`
   - `starfarer.api.zip`
   - LazyLib, MagicLib jars if using those libraries

#### Running with Debugger:

1. Select "Run Starsector" configuration from dropdown
2. Click the green Debug button (bug icon)
3. Enable your mod in the launcher
4. Set breakpoints by clicking left of line numbers in code files

**Hot Reload of Java Code:**
When debugging, you can modify code within methods without restarting:
1. Make code changes
2. Click the Build (hammer) icon
3. IntelliJ will reload classes automatically
4. Quickload your save to see changes (prevents crashes)

**Sources:**
- [IntelliJ IDEA Setup - Starsector Wiki](https://starsector.wiki.gg/wiki/IntelliJ_IDEA_Setup)
- [Starsector IntelliJ Template - GitHub](https://github.com/wispborne/Starsector-IntelliJ-Template)
- [Getting Started with Mod Programming - Starsector Wiki](https://starsector.wiki.gg/wiki/Getting_started_with_mod_programming)

---

## Hot Reload Capabilities

Starsector supports limited hot reloading during development, which significantly speeds up iteration.

### What CAN Be Hot Reloaded

**Using F8 in Simulator (requires DevMode):**
- `.json` files (ship files, weapon files, configuration)
- `.csv` files (ship_data.csv, weapon_data.csv, etc.)
- Ship stats and weapon stats
- MagicLib settings files

**Using IntelliJ Debugger:**
- Java code changes within existing methods
- Requires rebuild (hammer icon) and quickload

### What CANNOT Be Hot Reloaded

The following require a full game restart:
- New Java classes or new methods
- Changes to class structure or method signatures
- `mod_info.json` changes
- New ships/weapons (files must exist on startup)
- Plugin initialization code

### Hot Reload Workflow

1. Enable DevMode (see below)
2. Launch game and enter the Simulator
3. Make changes to `.json` or `.csv` files externally
4. Press **F8** in the Simulator to reload data files
5. Changes take effect immediately - no restart needed

> **Tip:** This is a massive timesaver for balancing ship stats, weapon damage, and other numerical tweaks.

**Sources:**
- [Dev Mode - Starsector Wiki](https://starsector.wiki.gg/wiki/Dev_mode)
- [Intro to Modding - Starsector Wiki](https://starsector.wiki.gg/wiki/Intro_to_Modding)

---

## DevMode

DevMode is Starsector's built-in developer mode that enables testing and debugging features.

### Enabling DevMode

**Method 1: Edit settings.json**
```json
{
  "devMode": true
}
```
Location: `<Starsector installation>/starsector-core/settings.json`

**Method 2: Console Commands Mod**
1. Install Console Commands mod
2. Open console with `~` or `Ctrl+Backspace`
3. Type `devmode` and press Enter

> **Note:** Toggling via console won't fully activate all features (e.g., modifying other factions' colonies).

### DevMode Features

**General:**
- Enables Variant Editor from main menu
- Massively increased zoom range (campaign and combat)
- Removes surveying, salvaging, and colony creation costs
- Allows modifying other factions' colonies
- Speeds up industry construction time
- Unpause while viewing TAB map in campaign
- Exit combat with player victory at any time

**Data Reloading:**
- **F8 in Simulator** - Reloads `.json` and `.csv` files without restart

### DevMode Keyboard Shortcuts

#### Campaign Mode

| Shortcut | Function |
|----------|----------|
| `K` | Add 25,000 credits |
| `L` | Add 50,000 XP (4 levels for you, some for officers) |
| `G` | Trigger pirate raids, expeditions, or inspections |
| `N` | Spawn large pirate fleet |
| `Ctrl+N` | Spawn Remnant fleet with alpha cores |
| `M` | Spawn random derelict ship with loot |
| `Ctrl+Z` | Toggle sensors (color-codes systems by faction) |
| `Ctrl+Click` | Teleport anywhere on map |
| `ESC` | Force-close interaction dialogs |

#### Combat Mode

| Shortcut | Function |
|----------|----------|
| `Backspace` | End combat with player victory |
| `Ctrl+Click` | Switch control to targeted ship/fighter |
| `F10` | Cycle camera modes |
| `F12` | Deal random damage to enemies |
| `I` | Destroy currently controlled ship |
| `P` | Toggle travel drive |
| `N` | Cycle through friendly ships |

#### TAB Map (Combat)

| Shortcut | Function |
|----------|----------|
| `F1` | Toggle AI for selected ship |
| `F2` | Switch ship allegiance |
| `F3` | Switch control to selected ship |
| `F4` | Damage selected ship |

#### D-Mod Overlay (Combat)

| Key | Effect |
|-----|--------|
| `7`, `8`, `9` | Modify d-mod overlay level |
| `0` | Pristine condition |

**Sources:**
- [Dev Mode - Starsector Wiki](https://starsector.wiki.gg/wiki/Dev_mode)
- [Dev Mode - StarSector Fandom Wiki](https://starsector.fandom.com/wiki/Dev_mode)

---

## Console Commands Mod

The Console Commands mod is an essential tool for mod development and testing.

### Installation

1. Download from [Official Forum Thread](https://fractalsoftworks.com/forum/index.php?topic=4106.0)
2. Extract to `<Starsector>/mods/` folder
3. Enable in launcher

### Opening the Console

- **Default:** Press `~` (tilde) key
- **Alternative:** Press `Ctrl+Backspace`
- Key binding can be changed in mod settings

### Essential Commands for Modders

| Command | Description |
|---------|-------------|
| `help` | List all available commands |
| `help <command>` | Get detailed help for a specific command |
| `devmode` | Toggle DevMode |
| `addship <hull_id>` | Add a ship to your fleet |
| `findship <hull_id>` | Check if ship spawns in faction markets |
| `addspecial <blueprint_id>` | Add blueprint package to inventory |
| `addcredits <amount>` | Add credits |
| `addxp <amount>` | Add experience points |
| `god` | Toggle invincibility |
| `infiniteammo` | Toggle infinite ammo |
| `forcemarketupdate` | Force market inventory refresh |

### Testing Your Mod Content

**Testing Ships:**
```
findship my_ship_hull_id
```
If not appearing, increase spawn weights in CSV and start new campaign.

**Adding Ships for Testing:**
```
addship my_ship_hull_id
```

**Testing Blueprints:**
```
addspecial my_blueprint_package_id
```

### Additional Console Mods

- [Additional Console Commands](https://fractalsoftworks.com/forum/index.php?topic=30955.0) - Extends base functionality
- [Additional Search Commands](https://fractalsoftworks.com/forum/index.php?topic=23024.0) - Enhanced search capabilities

**Sources:**
- [Console Commands - GitHub](https://github.com/LazyWizard/console-commands)
- [Console Commands - Forum Thread](https://fractalsoftworks.com/forum/index.php?topic=4106.0)
- [Console Commands Mod Overview](https://starsectormods.com/console-commands-mod-for-starsector/)

---

## Testing and Iteration Workflow

### Recommended Development Cycle

```
1. Edit files (code, JSON, CSV)
        |
        v
2. Build/Compile (if Java)
        |
        v
3. Test in-game
   - F8 reload for data files (Simulator)
   - Quickload for code changes (with debugger)
   - Full restart for structural changes
        |
        v
4. Debug issues
   - Check starsector.log
   - Use breakpoints (IntelliJ)
   - Console commands for quick tests
        |
        v
5. Iterate
```

### Fast Iteration Tips

1. **Use the Simulator for balance testing**
   - Enter simulator, enable DevMode
   - Press F8 to reload data after external edits
   - Test weapon/ship stats without campaign load

2. **Keep a test save**
   - Create a save with your mod content accessible
   - Use quicksave/quickload for rapid testing
   - Console commands to add missing items

3. **Avoid Janino**
   - Janino runs Java source without compilation
   - Seems convenient but catches fewer errors
   - Use proper IDE compilation instead

4. **Use console commands liberally**
   - Spawn ships/items instantly
   - Teleport around the sector
   - Force market updates

### Ship & Weapon Editor Workflow

1. **First launch:** Select Starsector installation root folder
2. **Load mod:** Press `M`, navigate to `Starsector/mods/your_mod/`
3. **Create ship:**
   - Press `I` to load sprite
   - Press `T` to edit details
   - Press `C` to set center of mass
   - Press `Ctrl+drag` to set collision circle
4. **Save:** Press `V`, save to `your_mod/data/hulls/`

**Editor Shortcuts:**
- `F5` - Toggle weapon render opacity
- `F6/F7/F8` - Play/Stop/Reset weapon animations
- `Ctrl+Click` - Insert bound vertex

**Sources:**
- [Intro to Modding - Starsector Wiki](https://starsector.wiki.gg/wiki/Intro_to_Modding)
- [Ship & Weapon Editor - Forum Thread](https://fractalsoftworks.com/forum/index.php?topic=11491.0)
- [Ship Editor - GitHub](https://github.com/Ontheheavens/Ship-Editor/)

---

## Logging and Debugging

### Log File Location

**All Platforms:**
```
<Starsector installation>/starsector-core/starsector.log
```

The file may appear as just "starsector" if file extensions are hidden.

**Log Rotation:**
When log exceeds 50 MB, numbered backups are created:
- `starsector.log.1`
- `starsector.log.2`
- etc.

### Reading Crash Logs

**Critical Tips:**
1. **Read from bottom to top** - Crash info is at the end
2. **Look for "ERROR"** entries with stack traces
3. **Report crashes immediately** - Before restarting the game
4. **Identify the thread** - Look for the thread name in the error

**Example Stack Trace Pattern:**
```
[Thread-X] ERROR com.fs.starfarer.SomeClass - Error message here
    at com.example.MyMod.myMethod(MyMod.java:123)
    at com.fs.starfarer.SomeOtherClass.method(Unknown Source)
    ...
```

**Debugging Steps:**
1. Find the first ERROR entry from the bottom
2. Note which thread generated it
3. Scroll up to find INFO entries from same thread
4. Trace the sequence of events leading to crash

### Using IntelliJ Debugger

1. Set breakpoints by clicking left of line numbers
2. Run with Debug configuration (green bug icon)
3. Game will pause at breakpoints
4. Inspect variables, step through code
5. Modify code, rebuild, quickload to continue

### Common Debug Techniques

1. **Add logging to your code:**
   ```java
   import org.apache.log4j.Logger;

   public class MyPlugin {
       private static final Logger log = Logger.getLogger(MyPlugin.class);

       public void myMethod() {
           log.info("myMethod called with...");
       }
   }
   ```

2. **Check file loading:** Log entries show which files load

3. **Infinite loop detection:** If game freezes (not crashes), suspect infinite loops

4. **Validate JSON:** Use VS Code or online validators before testing

**Sources:**
- [Starsector Log File - Starsector Wiki](https://starsector.wiki.gg/wiki/Starsector_log_file)
- [Mod Troubleshooting Guide - Forum](https://fractalsoftworks.com/forum/index.php?topic=10931.30)

---

## Memory Settings and JVM Arguments

### vmparams File Location

```
<Starsector installation>/starsector.vmparams
```

Edit with any text editor (may require admin access on Windows).

### Key Memory Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| `-Xms` | Minimum heap size | `-Xms4096m` |
| `-Xmx` | Maximum heap size | `-Xmx6144m` |
| `-Xss` | Thread stack size | Don't modify unless advised |

### Recommended Settings

**Based on your system RAM:**

| System RAM | Recommended -Xmx | Notes |
|------------|------------------|-------|
| 8 GB | 4096m (4 GB) | Conservative, stable |
| 16 GB | 6144m (6 GB) | Good for most mod lists |
| 16+ GB | 8192m (8 GB) | Large mod lists; may be unstable |

**Best Practices:**
- Set `-Xms` and `-Xmx` to the same value for consistent performance
- Never allocate more than half your total system RAM
- 6 GB is the sweet spot for stability and mod support
- 8 GB can be unstable; roll back if issues occur

### Example vmparams

```
-XX:+UseG1GC
-XX:MaxGCPauseMillis=50
-Xms6144m
-Xmx6144m
-Xss2048k
-Djava.library.path=native/windows
-Dorg.lwjgl.librarypath=native/windows
```

### Troubleshooting Memory Issues

1. **Game won't launch:** Lower -Xmx value
2. **Out of memory errors:** Increase -Xmx (within limits)
3. **Long pauses/stutters:** Try adjusting GC settings
4. **Permission denied:** Run editor as admin, or save elsewhere and move file

**Sources:**
- [Modded System Requirements - Forum](https://fractalsoftworks.com/forum/index.php?topic=8726.0)
- [vmparams File Question - Forum](https://fractalsoftworks.com/forum/index.php?topic=22084.0)
- [Changing Available RAM - Forum](https://fractalsoftworks.com/forum/index.php?topic=19801.0)

---

## Recommended Tools

### Essential Mods for Development

| Mod | Purpose | Link |
|-----|---------|------|
| **Console Commands** | Developer console, testing | [Forum](https://fractalsoftworks.com/forum/index.php?topic=4106.0) |
| **LazyLib** | Utility library for mods | [Forum](https://fractalsoftworks.com/forum/index.php?topic=5444.0) |
| **MagicLib** | Extended modding utilities | [Wiki](https://starsector.wiki.gg/wiki/MagicLib) |

### Development Software

| Tool | Purpose | Link |
|------|---------|------|
| **IntelliJ IDEA Community** | Java IDE with debugging | [JetBrains](https://www.jetbrains.com/idea/download/) |
| **VS Code** | General text/code editing | [Microsoft](https://code.visualstudio.com/) |
| **Ship & Weapon Editor** | Create ships and weapons | [Forum](https://fractalsoftworks.com/forum/index.php?topic=11491.0) |
| **Chipper** | Log viewer utility | [GitHub](https://github.com/wispborne/chipper) |

### Project Templates

| Template | Description | Link |
|----------|-------------|------|
| **Starsector IntelliJ Template** | Pre-configured IntelliJ project | [GitHub](https://github.com/wispborne/Starsector-IntelliJ-Template) |
| **Starsector Mod Template** | Gradle-based template | [GitHub](https://github.com/wispborne/starsector-mod-template) |

### Useful Resources

- [Starsector Wiki - Modding Section](https://starsector.wiki.gg/wiki/Modding)
- [Fractal Softworks Forum - Modding](https://fractalsoftworks.com/forum/index.php?board=8.0)
- [Starsector Mods Directory](https://starsectormods.com/)
- [Starsector Discord](https://discord.gg/starsector) - Active modding community

---

## Quick Reference Card

### DevMode Essentials
- Enable: `"devMode": true` in settings.json
- F8 in Simulator = reload data files
- K = 25k credits, L = 50k XP
- Ctrl+Click = teleport
- Backspace = win combat

### Console Commands Essentials
- Open: `~` or `Ctrl+Backspace`
- `help` = list commands
- `addship <id>` = add ship
- `devmode` = toggle devmode

### Log File
- Location: `starsector-core/starsector.log`
- Read from bottom for crashes
- Look for ERROR with stack trace

### Memory
- File: `starsector.vmparams`
- Set: `-Xms6144m -Xmx6144m`
- Never exceed 50% of system RAM

---

*Document compiled from official Starsector wiki, Fractal Softworks forums, and community resources. Information current as of Starsector 0.98a.*
