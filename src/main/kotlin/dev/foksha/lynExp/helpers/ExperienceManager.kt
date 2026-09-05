package dev.foksha.lynExp.helpers

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.math.pow

object ExperienceManager {
    
    /**
     * Attempts to consume experience for an exp bottle
     * @param player The player to consume experience from
     * @return true if successful, false if not enough experience
     */
    fun consumeExpForBottle(player: Player): Boolean {
        val expCost = ConfigManager.getExpBottleCost()
        val levelCost = ConfigManager.getExpBottleLevelCost()
        
        // checks if the player has enough experience (prioritize exp cost over level cost)
        return if (expCost > 0) {
            if (player.totalExperience >= expCost) {
                player.giveExp(-expCost)
                true
            } else {
                false
            }
        } else if (levelCost > 0) {
            if (player.level >= levelCost) {
                player.level -= levelCost
                true
            } else {
                false
            }
        } else {
            true // no cost configured
        }
    }
    
    /**
     * Attempts to consume experience levels for enchanting
     * @param player The player to consume levels from
     * @return true if successful, false if not enough levels
     */
    fun consumeLevelsForEnchanting(player: Player): Boolean {
        val expRequired = ConfigManager.getEnchantmentExpPerEnchant()
        return if (getPlayerExp(player) >= expRequired) {
                changePlayerExp(player, -expRequired)
            true
        } else {
            false
        }
    }
    
    /**
     * Gives an experience bottle to the player
     */
    fun giveExpBottle(player: Player) {
        val expBottle = ItemStack(Material.EXPERIENCE_BOTTLE)
        player.inventory.addItem(expBottle)
    }
    /**
     * Fetches avaialble enchants of an item
     */
    fun getAvailableEnchantments(item: ItemStack): List<Enchantment> {
        return Enchantment.values().filter { it.canEnchantItem(item) }
    }

    /**
     * Grants experience to player
     */
    fun addExp(player: Player, amount: Int) {
        player.giveExp(amount)
    }

    /**
     * Number of levels the player has to pay for the given enchantment level.
     *
     * Example (maxLevel = 3):
     *   level 1 → 10 level 2 → 20 level 3 → 30
     */
    fun levelsRequired(maxLevel: Int, desiredLevel: Int): Int {
            val step = 30.0 / maxLevel
            return ceil(step * desiredLevel).toInt()
        }

    /**
     * Tries to subtract exactly [levels] from the player.
     * Returns true when successful.
     */
   fun consumeLevels(player: Player, levels: Int): Boolean {
       val expLevels = getExpAtLevel(levels)

        return if (getPlayerExp(player) >= expLevels) {
            changePlayerExp(player, -expLevels)
                    true
                } else false
        }


    /**
       code following is jewed from essentials apparently
       source: https://www.spigotmc.org/threads/how-to-get-players-exp-points.239171/
     */

    fun getExpToLevelUp(level: Int): Int {
        if (level <= 15) {
            return 2 * level + 7
        } else if (level <= 30) {
            return 5 * level - 38
        } else {
            return 9 * level - 158
        }
    }

    // Calculate total experience up to a level
    fun getExpAtLevel(level: Int): Int {
        if (level <= 16) {
            return (level.toDouble().pow(2.0) + 6 * level).toInt()
        } else if (level <= 31) {
            return (2.5 * level.toDouble().pow(2.0) - 40.5 * level + 360.0).toInt()
        } else {
            return (4.5 * level.toDouble().pow(2.0) - 162.5 * level + 2220.0).toInt()
        }
    }

    // Calculate player's current EXP amount
    fun getPlayerExp(player: Player): Int {
        var exp = 0
        val level = player.getLevel()


        // Get the amount of XP in past levels
        exp += getExpAtLevel(level)


        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp())

        return exp
    }

    // Give or take EXP
    fun changePlayerExp(player: Player, exp: Int): Int {
        // Get player's current exp
        val currentExp = getPlayerExp(player)


        // Reset player's current exp to 0
        player.exp = 0f
        player.level = 0

        // Give the player their exp back, with the difference
        val newExp = currentExp + exp
        player.giveExp(newExp)

        // Return the player's new exp amount
        return newExp
    }

}