# LynExp

This plugin adds a vanilla+ enchanting solution, long gone the days of re-rolling the enchantment table for a chance of your desired enchantment. Pick your desired enchantment from a list of enchantments!

## Downloads

Grab the jar that matches your server from the [latest release](https://github.com/lyn1dev/enchanting-table/releases/latest).

| Minecraft | Jar | Java |
|---|---|---|
| 1.21.5 – 1.21.10 | `LynExp-1.0.0-mc1.21.5-1.21.10.jar` | 21+ |
| 1.21.11 | `LynExp-1.0.0-mc1.21.11.jar` | 21+ |
| 26.1 – 26.1.2 | `LynExp-1.0.0-mc26.1-26.1.2.jar` | 25+ |
| 26.2 | `LynExp-1.0.0-mc26.2.jar` | 25+ |

Paper (or a Paper fork) is required — InvUI 2.x does not support Spigot. Minecraft 1.21 through 1.21.4 cannot be supported: the GUI is built on InvUI's `StonecutterWindow`, which first appeared in the InvUI build targeting 1.21.5.

## Why four jars

The plugin's own code is version-agnostic — every Bukkit API it touches is unchanged from 1.21 through 26.2. The split comes entirely from InvUI, which since v2 links Minecraft internals directly and ships no runtime version abstraction, so a given InvUI build only loads on the Minecraft versions whose internals it was compiled against.

Each group below is one contiguous run of Minecraft versions over which the bundled InvUI build's entire referenced surface — every class, method, field and constructor — resolves identically. The three boundaries are real breaks:

- **1.21.10 → 1.21.11** — `ResourceLocation` was renamed to `Identifier`, `npc.VillagerData` moved to `npc.villager.VillagerData`, and `ResourceKey.location()` became `identifier()`.
- **26.1.2 → 26.2** — candles were restructured: `Items.GREEN_CANDLE` became `Items.DYED_CANDLE.green()` via the new `ColorCollection` type. These references live in `CustomContainerMenu`, the direct superclass of the stonecutter menu this plugin's GUI is built on.
- **1.21.5 vs 1.21.6+** — `ItemStack.parse`/`save` changed signature. The pinned InvUI build for the first group avoids that API entirely, which is why one jar covers 1.21.5 through 1.21.10.

## Building

```bash
./gradlew collectJars
```

All four jars land in `build/libs/`. Build a single variant with e.g. `./gradlew :variants:mc-26.2:shadowJar`.

The four subprojects under `variants/` share the one source tree in `src/main`; they differ only in the InvUI build, the Paper API level and the JDK. The JDK 25 toolchain for the 26.x variants is provisioned automatically.

Each jar's manifest sets `paperweight-mappings-namespace: mojang`. Without it Paper assumes a `plugin.yml` plugin is Spigot-mapped and runs its remapper across the bundled, Mojang-mapped InvUI classes.
