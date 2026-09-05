package dev.foksha.lynExp.items

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.inventory.event.InventoryClickEvent
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemBuilder
import xyz.xenondevs.invui.item.ItemProvider

class TakeOutputItem(private val player: Player) : AbstractItem() {

    override fun getItemProvider(viewer: Player): ItemProvider {

            val lore = listOf(
                "No output available",
                "Place an item in the",
                "input slot to start"
            )
            return ItemBuilder(Material.BARRIER)
                .setName("Output")
                .setLegacyLore(lore)
        }


    override fun handleClick(clickType: ClickType, player: Player, click: Click) {

    }
}