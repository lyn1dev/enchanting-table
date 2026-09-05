plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "LynExp"

// One subproject per Minecraft compatibility group. All four share the single
// source tree in src/main; only the InvUI build, the Paper API level and the
// JDK differ. See README.md for how the groups were determined.
include(
    "variants:mc-1.21.5-1.21.10",
    "variants:mc-1.21.11",
    "variants:mc-26.1",
    "variants:mc-26.2",
)
