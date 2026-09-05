package dev.foksha.lynExp.items

import dev.foksha.lynExp.helpers.PendingInputManager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.Item.simple

/**
 * A clickable enchanted-book icon shown in the buttons GUI.
 *
 * When clicked it merges the stored enchantment(s) into [baseItem] (keeping any
 * that were already present) and places the new item in the specified
 * [outputSlot] of [upperGui].
 */
class BookSelectItem(
    private val player: Player,
    private val book: ItemStack,
    private val baseItem: ItemStack,
    private val upperGui: Gui,
    private val outputSlot: Int
) : AbstractItem() {

    // just show the book itself
    override fun getItemProvider(viewer: Player): ItemProvider = simple(book).getItemProvider(player)

    override fun handleClick(clickType: ClickType, player: Player, click: Click) {
        // left or right click → apply enchant
        if (!clickType.isLeftClick && !clickType.isRightClick) return

        val result = baseItem.clone()
        val storageMeta = book.itemMeta as EnchantmentStorageMeta
        for ((ench, lvl) in storageMeta.storedEnchants) {

            // prevents illegal combinations (e.g. Sharpness - Smite)
            // but still allows upgrading an already present enchant
            if (result.enchantments.keys
                    .filter { it != ench }
                    .any   { it.conflictsWith(ench) }) {
                player.sendMessage(
                    "§c${ench.key.key.replace('_', ' ').replaceFirstChar { it.uppercase() }} " +
                    "conflicts with an enchantment already on the item."
                )
                return
            }

            // merge while keeping any existing, upgrading if higher
            val current = result.getEnchantmentLevel(ench)
            if (lvl > current) {
                result.addUnsafeEnchantment(ench, lvl)
            }
        }
        val bookMeta = book.itemMeta as EnchantmentStorageMeta
        val (bookEnchantment, selectedLevel) =
            bookMeta.storedEnchants.entries.first().let { it.key to it.value }

        // replaces the output slot content with the newly enchanted item
        val max = bookEnchantment.maxLevel
        val lvl = selectedLevel
            upperGui.setItem(outputSlot, OutputReadyItem(player, result, max, lvl))

    }
}