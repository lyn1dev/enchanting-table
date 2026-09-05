package dev.foksha.lynExp

import dev.foksha.lynExp.commands.LynExpCommand
import dev.foksha.lynExp.helpers.ConfigManager
import dev.foksha.lynExp.listeners.InventoryOpenListener
import dev.foksha.lynExp.listeners.PlayerInteractListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class LynExpPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: LynExpPlugin
            private set
        var commandPrefix: String = "${ChatColor.AQUA} [LYN] ${ChatColor.RESET} "
    }

    override fun onEnable() {
        instance = this
        LynExp.init(this)
        this.addDefaults()
        registerCommands()
        registerListeners()
        
        logger.info("LynExp plugin enabled successfully!")
    }

    override fun onDisable() {
        saveConfiguration()
        Bukkit.getScheduler().cancelTasks(this)
    }

    fun registerCommands() {
        logger.log(Level.INFO, "Registering commands for LynExp")
        
        getCommand("lynexp")?.let { command ->
            val commandHandler = LynExpCommand()
            command.setExecutor(commandHandler)
            command.tabCompleter = commandHandler
        }
    }

    fun registerListeners() {
        logger.log(Level.INFO, "Registering listeners for LynExp")
        val pm = server.pluginManager
        pm.registerEvents(InventoryOpenListener(), this)
        pm.registerEvents(PlayerInteractListener(), this)
    }

    fun saveConfiguration() {
        this.saveConfig()
    }

    fun addDefaults() {
        saveDefaultConfig()
        ConfigManager.saveDefaults()
        config.options().copyDefaults(true)
        saveConfig()
    }
}