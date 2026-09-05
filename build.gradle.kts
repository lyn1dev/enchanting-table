plugins {
    kotlin("jvm") version "2.4.10" apply false
    id("com.gradleup.shadow") version "9.6.1" apply false
}

allprojects {
    group = "dev.foksha"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
        maven("https://repo.xenondevs.xyz/releases") { name = "xenondevs" }
    }
}

// Builds every variant jar into build/libs at the repository root.
val variantPaths = listOf(
    ":variants:mc-1.21.5-1.21.10",
    ":variants:mc-1.21.11",
    ":variants:mc-26.1",
    ":variants:mc-26.2",
)

val collectJars by tasks.registering(Copy::class) {
    group = "build"
    description = "Builds all Minecraft variant jars and collects them in build/libs."
    variantPaths.forEach { path ->
        dependsOn("$path:shadowJar")
        from(project(path).layout.buildDirectory.dir("libs")) { include("*.jar") }
    }
    into(layout.buildDirectory.dir("libs"))
}
