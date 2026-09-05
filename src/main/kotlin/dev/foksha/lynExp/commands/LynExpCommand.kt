package dev.foksha.lynExp.commands

import dev.foksha.lynExp.LynExpPlugin
import dev.foksha.lynExp.helpers.ConfigManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class LynExpCommand : CommandExecutor, TabCompleter {
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            showHelp(sender)
            return true
        }
        
        when (args[0].lowercase()) {
            "reload" -> {
                if (!sender.hasPermission("lynexp.reload")) {
                    sender.sendMessage("${LynExpPlugin.commandPrefix}${ChatColor.RED}You don't have permission to use this command!")
                    return true
                }
                
                try {
                    ConfigManager.reloadConfig()
                    sender.sendMessage("${LynExpPlugin.commandPrefix}${ChatColor.GREEN}Configuration reloaded successfully!")
                    LynExpPlugin.instance.logger.info("Configuration reloaded by ${sender.name}")
                } catch (e: Exception) {
                    sender.sendMessage("${LynExpPlugin.commandPrefix}${ChatColor.RED}Failed to reload configuration: ${e.message}")
                    LynExpPlugin.instance.logger.severe("Failed to reload configuration: ${e.message}")
                }
            }
            
            "help" -> showHelp(sender)
            
            else -> {
                sender.sendMessage("${LynExpPlugin.commandPrefix}${ChatColor.RED}Unknown subcommand. Use '/lynexp help' for available commands.")
            }
        }
        
        return true
    }
    
    private fun showHelp(sender: CommandSender) {
        sender.sendMessage("${LynExpPlugin.commandPrefix}${ChatColor.YELLOW}Available commands:")
        
        if (sender.hasPermission("lynexp.reload")) {
            sender.sendMessage("${ChatColor.AQUA}/lynexp reload ${ChatColor.WHITE}- Reload the plugin configuration")
        }
        
        sender.sendMessage("${ChatColor.AQUA}/lynexp help ${ChatColor.WHITE}- Show this help message")
    }
    
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        if (args.size == 1) {
            val subcommands = mutableListOf("help")
            
            if (sender.hasPermission("lynexp.reload")) {
                subcommands.add("reload")
            }
            
            return subcommands.filter { it.startsWith(args[0].lowercase()) }
        }
        
        return emptyList()
    }
}