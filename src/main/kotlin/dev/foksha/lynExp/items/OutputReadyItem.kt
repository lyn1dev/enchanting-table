package dev.foksha.lynExp.items

import dev.foksha.lynExp.helpers.ExperienceManager
import dev.foksha.lynExp.helpers.PendingInputManager
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider

/**
 * Shown in the output slot once an enchantment has been applied.
 * Clicking it puts the item in the player's inventory (or drops it if full)
 * and closes the GUI.
 */
class OutputReadyItem(
    private val player: Player,
    private val result: ItemStack,
    private val maxLevel: Int,
    private val desiredLevel: Int,
    ) : AbstractItem() {

    override fun getItemProvider(viewer: Player): ItemProvider =
        Item.simple(result).getItemProvider(player)

    override fun handleClick(clickType: ClickType, player: Player, click: Click) {
        if (!clickType.isLeftClick && !clickType.isRightClick && !clickType.isShiftClick) return
        val cost = ExperienceManager.levelsRequired(maxLevel, desiredLevel)

        if (ExperienceManager.consumeLevels(player, cost)) {
            // try to add to inventory, otherwise drop at feet
            val leftovers = player.inventory.addItem(result).values
            leftovers.forEach { player.world.dropItemNaturally(player.location, it) }
            PendingInputManager.clear(player)
            player.closeInventory()
            player.spawnParticle(Particle.ENCHANT, player.location, 10, 0.5, 0.5, 0.5)
            player.sendMessage("§aEnchanted for §e$cost §alevels. The item was placed in your inventory.")
        } else {
            player.sendMessage("§cYou need §e$cost §clevels for that enchantment.")
        }
    }
}