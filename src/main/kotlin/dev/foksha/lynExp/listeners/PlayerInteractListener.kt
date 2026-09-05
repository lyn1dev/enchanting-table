package dev.foksha.lynExp.listeners

import dev.foksha.lynExp.helpers.ConfigManager
import dev.foksha.lynExp.helpers.ExperienceManager
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val playerInventory = player.inventory
        val block = event.clickedBlock
        if (block == null) return
        if (block.type == Material.ENCHANTING_TABLE && playerInventory.itemInMainHand.type == Material.GLASS_BOTTLE) {
            event.isCancelled = true
            if (ExperienceManager.consumeExpForBottle(player)) {
                playerInventory.itemInMainHand.amount -= 1
                ExperienceManager.giveExpBottle(player)
                player.sendMessage("§aYou bottled your experience.")
            } else {
                player.sendMessage("§cYou don’t have enough experience to bottle.")
            }
        }
    }

    @EventHandler
    fun onMobItemConvert(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock ?: return
        if (block.type != Material.CONDUIT) return

        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) return

        val expValue = ConfigManager.getItemExpValue(item.type)
        if (expValue <= 0) {
            player.sendMessage("§cThat item cannot be converted into experience.")
            return
        }

        event.isCancelled = true
        item.amount -= 1
        ExperienceManager.addExp(player, expValue)
        player.playSound(player.location, Sound.BLOCK_CONDUIT_AMBIENT, 1f, 1f)
        player.sendMessage("§aConverted ${item.type.name.lowercase()} into $expValue experience.")
    }
}