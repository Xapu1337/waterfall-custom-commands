package eu.xaru.commands.other.customcommands.core;

import eu.xaru.commands.other.customcommands.commands.CustomCommandBase;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CCCore extends Plugin {
        public CCCore() {}

        public void onEnable() {
            try {
                this.makeConfig();
            } catch (IOException var4) {
                this.getLogger().warning("could not create config file");
                this.getLogger().warning(var4.getMessage());
            }

            Configuration configuration = null;

            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
            } catch (IOException var3) {
                this.getLogger().warning("could not load config file");
                this.getLogger().warning(var3.getMessage());
            }

            Configuration finalConfiguration = configuration;
            configuration.getKeys().forEach((key) -> {
                if (key.equals("custom-commands")) {
                    finalConfiguration.getSection(key).getKeys().forEach((key1) -> {
                        if (key1.equals("commands")) {
                            finalConfiguration.getSection(key + "." + key1).getKeys().forEach((key2) -> {
                                try {
                                    ProxyServer.getInstance().getPluginManager().registerCommand(this, new CustomCommandBase(key2));
                                    this.getLogger().info("registered command " + key2);
                                } catch (IOException var3) {
                                    this.getLogger().warning("could not register command " + key2);
                                    this.getLogger().warning(var3.getMessage());
                                }

                            });
                        }

                    });
                }

            });
        }

        public void onDisable() {
            this.getProxy().getPluginManager().unregisterCommands(this);
            this.getLogger().info("unregistered all commands");
        }

        public void makeConfig() throws IOException {
            if (!this.getDataFolder().exists()) {
                this.getLogger().info("Created config folder: " + this.getDataFolder().mkdir());
            }

            File configFile = new File(this.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(configFile);
                InputStream in = this.getResourceAsStream("config.yml");
                in.transferTo(outputStream);
            }

        }
}
