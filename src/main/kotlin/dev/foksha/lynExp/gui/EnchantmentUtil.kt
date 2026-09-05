// Kotlin
package dev.foksha.lynExp.gui

import dev.foksha.lynExp.helpers.ExperienceManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EnchantmentUtil {

    /**
     * Build a list of enchanted books that could legally be applied to [item].
     *
     * • Only enchantments where Enchantment#canEnchantItem returns true are considered  
     * • One book is created for every level in the enchantment’s valid level-range  
     * • Display-name: e.g. "Sharpness V" (aqua)  
     * • Lore: single grey line with numeric level
     */
    fun getEnchantmentBooksFor(item: ItemStack): List<ItemStack> {
        val result = mutableListOf<ItemStack>()

        Enchantment.values()
            .filter { it.canEnchantItem(item) }      // natural compatibility
            .forEach { enchant ->
                val max = enchant.maxLevel
                val min = enchant.startLevel
                for (level in min..max) {
                    result += createBook(enchant, level)
                }
            }

        return result
    }

    /** creates a single enchanted book with a display name and lore. */
    private fun createBook(enchant: Enchantment, level: Int): ItemStack {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as EnchantmentStorageMeta
        meta.addStoredEnchant(enchant, level, /* ignoreLevelRestriction = */ true)

        // display name: proper cased enchantment key + level in Roman numerals
        val readableName = enchant.key.key
            .lowercase()
            .replace('_', ' ')
            .replaceFirstChar(Char::uppercase)

        meta.displayName(
            Component.text("$readableName ${roman(level)}", NamedTextColor.AQUA)
        )

        val price = ExperienceManager.levelsRequired(enchant.maxLevel, level)

        meta.lore(
            listOf(
                Component.text("Level $level", NamedTextColor.GOLD),
                Component.text("Price: $price", NamedTextColor.GREEN)
            )
        )

        book.itemMeta = meta
        return book
    }

    /** very small helper to convert 1-10 to Roman numerals. */
    private fun roman(level: Int): String = when (level) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        6 -> "VI"
        7 -> "VII"
        8 -> "VIII"
        9 -> "IX"
        10 -> "X"
        else -> level.toString()
    }
}