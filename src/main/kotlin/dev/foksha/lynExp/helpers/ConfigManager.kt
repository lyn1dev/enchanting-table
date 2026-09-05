package dev.foksha.lynExp.helpers

import dev.foksha.lynExp.LynExpPlugin
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration

object ConfigManager {

    private val plugin: LynExpPlugin get() = LynExpPlugin.instance
    private val config: FileConfiguration get() = plugin.config

    // Experience Bottle Configuration
    fun getExpBottleCost(): Int = config.getInt("exp-bottle.exp-cost", 5)

    fun getExpBottleLevelCost(): Int = config.getInt("exp-bottle.level-cost", 5)

    // Enchantment gui configuration
    fun getEnchantmentExpPerEnchant(): Int = config.getInt("enchantment-gui.exp-per-enchant", 1395)

    fun shouldShowParticles(): Boolean = config.getBoolean("enchantment-gui.show-particles", true)

    fun getItemExpValue(material: Material): Int {
        val path = "item-exp-values.${material.name.lowercase()}"
        return config.getInt(path, 0)
    }

    fun reloadConfig() {
        plugin.reloadConfig()
    }

    // meth to save default configuration values
    fun saveDefaults() {
        // exp bottle defaults
        if (!config.contains("exp-bottle.exp-cost")) {
            config.set("exp-bottle.exp-cost", 5)
        }
        if (!config.contains("exp-bottle.level-cost")) {
            config.set("exp-bottle.level-cost", 5)
        }

        // ench GUI defaults
        if (!config.contains("enchantment-gui.exp-per-enchant")) {
            config.set("enchantment-gui.exp-per-enchant", 1395)
        }
        if (!config.contains("enchantment-gui.show-particles")) {
            config.set("enchantment-gui.show-particles", true)
        }

        val itemExpDefaults = mapOf(
            "rotten_flesh" to 2,
            "bone" to 3,
            "spider_eye" to 4,
            "string" to 1,
            "gunpowder" to 5,
            "blaze_rod" to 10,
            "ender_pearl" to 8,
            "ghast_tear" to 12,
            "slime_ball" to 6
        )

        for ((key, value) in itemExpDefaults) {
            val path = "item-exp-values.$key"
            if (!config.contains(path)) {
                config.set(path, value)
            }
        }

        plugin.saveConfig()
    }
}