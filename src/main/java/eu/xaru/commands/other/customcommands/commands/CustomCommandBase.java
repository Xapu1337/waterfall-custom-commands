package eu.xaru.commands.other.customcommands.commands;

import eu.xaru.commands.other.customcommands.core.CCCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomCommandBase extends Command {



    public CustomCommandBase(String commandEntry) throws IOException {
        super(commandEntry);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            TextComponent configMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', CCCore.instance.getConfig().getString("custom-commands.commands." + this.getName() + ".content")));
            if (CCCore.instance.getConfig().getString("custom-commands.commands." + this.getName() + ".link") != null) {
                configMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, CCCore.instance.getConfig().getString("custom-commands.commands." + this.getName() + ".link")));
            }

            p.sendMessage(configMessage);
        }

    }
}
