package dev.foksha.lynExp.helpers

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/** Stores the original (un-enchanted) item while a GUI is open. */
object PendingInputManager {
    private val pending: MutableMap<UUID, ItemStack> = HashMap()

    fun store(player: Player, item: ItemStack) {
        pending[player.uniqueId] = item
    }

    fun take(player: Player): ItemStack? = pending.remove(player.uniqueId)

    fun clear(player: Player) {
        pending.remove(player.uniqueId)
    }
    /** Returns `true` while the player has supplied an item to the
     *  enchantment GUI that has not yet been returned or replaced. */

    fun isPending(player: Player): Boolean =
                pending.containsKey(player.uniqueId)
}