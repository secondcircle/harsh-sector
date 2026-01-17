# Harsh Sector

A Starsector mod adding difficulty/realism features inspired by Starpocalypse.

## Tech Stack
- Java 17 (compiles with `--release 7` for Starsector compatibility)
- Starsector 0.98a-RC8

## Key Paths
- **Starsector**: `/Applications/Starsector.app`
- **API JAR**: `/Applications/Starsector.app/Contents/Resources/Java/starfarer.api.jar`
- **Mods folder**: `/Applications/Starsector.app/mods/`
- **Logs**: `/Applications/Starsector.app/starsector-core/starsector.log`
- **Mod symlink**: `harsh_sector/` → mods folder

## Build & Test
```bash
cd harsh_sector && ./build.sh   # Compile and package JAR
# Then restart Starsector (Java changes require full restart)
```

## Structure
- `harsh_sector/` - The mod itself
- `docs/modding/` - Starsector modding reference docs
