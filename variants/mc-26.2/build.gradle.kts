import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

// ---------------------------------------------------------------------------
// Minecraft 26.2
//
// InvUI 2.x links Minecraft internals directly and ships no runtime version
// abstraction, so one jar is needed per group of Minecraft versions that share
// an NMS surface. This variant pins the newest InvUI build that resolves
// cleanly against every Minecraft version in its range.
// ---------------------------------------------------------------------------
val mcRange = "26.2"
val mcSupported = "26.2"
val paperApi = "26.2.build.121-stable"
val invuiVersion = "2.3.1"
val jdkVersion = 25

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperApi")
    implementation("xyz.xenondevs.invui:invui:$invuiVersion")
}

// Every variant compiles the one shared source tree at the repository root.
kotlin {
    jvmToolchain(jdkVersion)
    sourceSets["main"].kotlin.setSrcDirs(listOf(rootProject.file("src/main/kotlin")))
    compilerOptions {
        // Matches the JDK each Minecraft line actually requires: 21 for the
        // 1.21.x jars, 25 for the 26.x jars (whose Paper API and InvUI builds
        // are themselves Java 25 bytecode).
        jvmTarget.set(JvmTarget.fromTarget(jdkVersion.toString()))
    }
}

// Keep javac on the same release Kotlin targets or Gradle rejects the mismatch.
tasks.withType<JavaCompile>().configureEach {
    options.release.set(jdkVersion)
}

sourceSets {
    main {
        resources.setSrcDirs(listOf(rootProject.file("src/main/resources")))
    }
}

tasks.processResources {
    val props = mapOf("version" to version, "mcSupported" to mcSupported)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    archiveBaseName.set("LynExp")
    archiveVersion.set("${project.version}-mc$mcRange")
    archiveClassifier.set("")

    // Let the Kotlin metadata transformer see every .kotlin_module before
    // duplicates are dropped, otherwise stdlib metadata is silently lost.
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    // The bundled InvUI calls Mojang-mapped internals. Without this attribute
    // Paper assumes a plugin.yml plugin is Spigot-mapped and runs its remapper
    // over those classes, which corrupts them.
    manifest {
        attributes("paperweight-mappings-namespace" to "mojang")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
