# Starsector Modding Knowledge Base

This folder contains comprehensive documentation for Starsector modding, focused on QOL enhancements, rebalancing, and new mechanics (not visual/sprite modding).

## Quick Start

1. **New to modding?** Start with [Official Resources](01-OFFICIAL-RESOURCES.md)
2. **Setting up your environment?** See [Development Workflow](02-DEVELOPMENT-WORKFLOW.md)
3. **Wondering if you need Java?** Check [Java vs JSON](03-JAVA-VS-JSON.md)
4. **Want to know what's moddable?** Browse [Moddable Systems](04-MODDABLE-SYSTEMS.md)

## Documentation Index

| Document | Description |
|----------|-------------|
| [01-OFFICIAL-RESOURCES.md](01-OFFICIAL-RESOURCES.md) | Official wiki, forums, API docs, mod structure, getting started |
| [02-DEVELOPMENT-WORKFLOW.md](02-DEVELOPMENT-WORKFLOW.md) | Hot reload, debug mode, console commands, logging, testing |
| [03-JAVA-VS-JSON.md](03-JAVA-VS-JSON.md) | What requires code vs data-only modding, API overview |
| [04-MODDABLE-SYSTEMS.md](04-MODDABLE-SYSTEMS.md) | Combat, economy, skills, AI, events - what can be modified |
| [STARPOCALYPSE-REFERENCE.md](STARPOCALYPSE-REFERENCE.md) | Archived mod analysis - features to potentially recreate |

## Key Takeaways

### Can I mod without Java?
**Yes, for many things.** JSON-only mods can:
- Rebalance ship/weapon stats
- Modify faction relationships
- Adjust economy/market settings
- Create new ship variants
- Change skill effects (values only)

**Java required for:**
- Custom hullmods with unique behaviors
- New weapon behaviors
- Campaign scripts and events
- AI modifications
- Entirely new mechanics

### Development Cycle
1. Edit files (JSON or Java)
2. **JSON changes**: Some hot-reload with `reloadcampaign` console command
3. **Java changes**: Requires game restart
4. Use DevMode (`devMode:true` in settings.json) for testing
5. Check `starsector.log` for errors

### Starpocalypse Features Worth Recreating
The [Starpocalypse reference](STARPOCALYPSE-REFERENCE.md) documents an archived mod with interesting mechanics:
- Military market regulations (restrict equipment by tier)
- D-mod system for all ships
- Black market restrictions
- Hostile activity consequences
- Dynamic war mechanics

Source code available at: https://github.com/jaghaimo/starpocalypse

## Sources

All documentation includes source URLs. Primary sources:
- [Fractal Softworks Forums](https://fractalsoftworks.com/forum/)
- [Starsector Wiki](https://starsector.fandom.com/wiki/Starsector_Wiki)
- [Unofficial Starsector Wiki](https://starsector-wiki.com/)
- Community tutorials and guides
