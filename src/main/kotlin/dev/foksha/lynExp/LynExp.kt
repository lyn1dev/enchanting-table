package dev.foksha.lynExp

import org.bukkit.plugin.Plugin
import java.util.logging.Logger
import dev.foksha.lynExp.LynExpPlugin

object LynExp {
    var plugin: Plugin? = null
    var logger: Logger? = null

    fun init(plugin: Plugin) {
        LynExp.plugin = plugin
        LynExp.logger = plugin.getLogger()
    }
}