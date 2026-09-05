package dev.foksha.lynExp.gui

import dev.foksha.lynExp.helpers.PendingInputManager
import dev.foksha.lynExp.items.TakeOutputItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.StonecutterWindow
import dev.foksha.lynExp.items.BookSelectItem

object EnchantmentGui {

    fun openEnchantmentGui(player: Player) {

        val border = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
        val upperGui = Gui.builder()
            .setStructure(
                "i o")
            .addIngredient('i', dev.foksha.lynExp.items.PlaceInputItem(player))
            .addIngredient('o', TakeOutputItem(player))
            .build()


        val buttonsGui = Gui.builder()
            .setStructure(
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #"
            )
            .addIngredient('#', border)
            .build()

        val stoneCutterWindow = StonecutterWindow.builder()
            .setViewer(player)
            .setTitle("Enchantment Station")
            .setUpperGui(upperGui)
            .setButtonsGui(buttonsGui)
            .build()

        stoneCutterWindow.open()
    }

    /**
     * re opens the GUI with the selected [item] in the input slot and displays
     * every compatible enchanted book in the buttons area.
     */
    fun replaceItem(player: Player, item: ItemStack) {
        player.closeInventory()

        /* -------- upper GUI -------- */
        val upperGui = Gui.builder()
            .setStructure("i o")
            .addIngredient('i', item)
            .addIngredient('o', TakeOutputItem(player))
            .build()

        /* -------- buttons GUI -------- */
        val border = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
        val buttonsGui = Gui.builder()
            .setStructure(
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #",
                "# # # #"
            )
            .addIngredient('#', border)
            .build()

        // populates buttons with clickable books
        val books = EnchantmentUtil.getEnchantmentBooksFor(item)
        val outputSlot = 1            // 'o' is the 2nd character in "i o"
        val totalSlots = buttonsGui.size
        books.take(totalSlots).forEachIndexed { slot, book ->
            buttonsGui.setItem(
                slot,
                BookSelectItem(player, book, item, upperGui, outputSlot)
            )
        }

        /* -------- window -------- */
        StonecutterWindow.builder()
            .setViewer(player)
            .setTitle("Enchantment Station")
            .setUpperGui(upperGui)
            .setButtonsGui(buttonsGui)
            .build().apply {
                addCloseHandler {
                    val pending = PendingInputManager.take(player) ?: return@addCloseHandler

                    // give the item back (or drop it if the inventory is full)
                    val leftovers = player.inventory.addItem(pending).values
                    leftovers.forEach { item ->
                        player.world.dropItemNaturally(player.location, item)
                    }
                }
            }
            .open()
    }
}