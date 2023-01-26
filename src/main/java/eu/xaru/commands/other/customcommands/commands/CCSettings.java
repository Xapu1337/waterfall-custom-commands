package eu.xaru.commands.other.customcommands.commands;

import eu.xaru.commands.other.customcommands.core.CCCore;
import eu.xaru.commands.other.customcommands.singletons.Singleton;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CCSettings extends Command {
    public CCSettings() {
        super("ccsettings", "customcommands.admin", "ccs", "ccsettings");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (args.length == 0) {
                p.sendMessage(new TextComponent("§cUsage: /ccsettings <ccs, ccsettings> <reload, list>"));
            } else if (args.length == 1) {
                switch (args[0]) {
                    case "reload" -> {
                        CCCore.instance.getProxy().getPluginManager().unregisterCommands(CCCore.instance);
                        CCCore.instance.reloadConfig();
                        CCCore.instance.loadCustomCommands();
                        CCCore.instance.getProxy().getPluginManager().registerCommand(CCCore.instance, new CCSettings());

                        p.sendMessage(new TextComponent("§aReloaded the config!"));
                    }
                    case "list" -> {
                        p.sendMessage(new TextComponent("§aList of all custom commands:"));
                        Singleton.getSingleInstance().customCommands.forEach((s, customCommandBase) -> p.sendMessage(new TextComponent("§7" + s + " §8 -> " + ChatColor.translateAlternateColorCodes('&', CCCore.instance.getConfig().getString("custom-commands.commands." + s + ".content")))));
                    }
                    default ->
                            p.sendMessage(new TextComponent("§cUsage: /ccsettings <ccs, ccsettings> <reload, list>"));
                }
            }
        }
    }
}
