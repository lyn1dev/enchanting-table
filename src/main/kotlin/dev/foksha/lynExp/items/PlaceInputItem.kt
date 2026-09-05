package dev.foksha.lynExp.items

import dev.foksha.lynExp.gui.EnchantmentGui
import dev.foksha.lynExp.helpers.PendingInputManager
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.inventory.event.InventoryClickEvent
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemBuilder
import xyz.xenondevs.invui.item.ItemProvider

class PlaceInputItem(private val player: Player) : AbstractItem() {

    override fun getItemProvider(viewer: Player): ItemProvider {

            val lore = listOf(
                "Click me while holding an",
                "enchantable item to insert it"
            )
            return ItemBuilder(Material.HOPPER)
                .setName("Input")
                .setLegacyLore(lore)
        }

    override fun handleClick(clickType: ClickType, player: Player, click: Click) {

        val cursor = player.itemOnCursor          // item on mouse cursor
        if (cursor.type == Material.AIR) return   // nothing dragged
 
         val enchantable = Enchantment.values().any { it.canEnchantItem(cursor) }
         if (!enchantable) {
             player.sendMessage("§cThat item cannot be enchanted here.")
             return
         }

         /* ---- take exactly ONE copy from the stack ---- */
         val single = cursor.clone()
         single.amount = 1

         if (cursor.amount == 1) {
             player.setItemOnCursor(ItemStack(Material.AIR))  // clear cursor
         } else {
             cursor.amount -= 1
             player.setItemOnCursor(cursor)
         }

         /* ---- remember it and open GUI ---- */
         EnchantmentGui.replaceItem(player, single)
         PendingInputManager.store(player, single)
    }
 }