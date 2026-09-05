package dev.foksha.lynExp.listeners

import dev.foksha.lynExp.gui.EnchantmentGui
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.StonecutterInventory

class InventoryOpenListener : Listener {

    @EventHandler
    fun onOpenInventory(event: InventoryOpenEvent) {
        val player = event.player
        val inventory = event.inventory
        if (inventory.type == InventoryType.ENCHANTING) {
            EnchantmentGui.openEnchantmentGui(player as Player)
            event.isCancelled = true
        }
    }
}