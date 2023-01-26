package eu.xaru.commands.other.customcommands.core;

import eu.xaru.commands.other.customcommands.commands.CCSettings;
import eu.xaru.commands.other.customcommands.commands.CustomCommandBase;
import eu.xaru.commands.other.customcommands.singletons.Singleton;
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
        public Configuration configuration = null;

        public static CCCore instance;

        public CCCore() {
            if(instance == null)
                instance = this;
        }
        public void onEnable() {
            try {
                this.makeConfig();
            } catch (IOException var4) {
                this.getLogger().warning("could not create config file");
                this.getLogger().warning(var4.getMessage());
            }

            configuration = getConfig();

            getProxy().getPluginManager().registerCommand(this, new CCSettings());

            loadCustomCommands();


        }

        public void onDisable() {
            this.getProxy().getPluginManager().unregisterCommands(this);
            Singleton.getSingleInstance().customCommands.clear();
            this.getLogger().info("unregistered all commands");
            configuration = null;
        }

        public void loadCustomCommands() {
            configuration.getKeys().forEach((key) -> {
                if (key.equals("custom-commands")) {
                    configuration.getSection(key).getKeys().forEach((key1) -> {
                        if (key1.equals("commands")) {
                            configuration.getSection(key + "." + key1).getKeys().forEach((key2) -> {
                                try {
                                    CustomCommandBase constructedCommand = new CustomCommandBase(key2);
                                    ProxyServer.getInstance().getPluginManager().registerCommand(this, constructedCommand);
                                    this.getLogger().info("registered command " + key2);
                                    Singleton.getSingleInstance().customCommands.put(key2, constructedCommand);
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

        public Configuration getConfig() {
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(ProxyServer.getInstance().getPluginManager().getPlugin("customcommands").getDataFolder(), "config.yml"));
            } catch (IOException var2) {
                ProxyServer.getInstance().getLogger().warning("could not load config file");
                ProxyServer.getInstance().getLogger().warning(var2.getMessage());
            }

            return configuration;
        }

        public void reloadConfig() {
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(ProxyServer.getInstance().getPluginManager().getPlugin("customcommands").getDataFolder(), "config.yml"));
            } catch (IOException var2) {
                ProxyServer.getInstance().getLogger().warning("could not reload config file");
                ProxyServer.getInstance().getLogger().warning(var2.getMessage());
            }

            // 2nd step, save the config
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(ProxyServer.getInstance().getPluginManager().getPlugin("customcommands").getDataFolder(), "config.yml"));
            } catch (IOException var2) {
                ProxyServer.getInstance().getLogger().warning("could not save config file");
                ProxyServer.getInstance().getLogger().warning(var2.getMessage());
            }

            // 3rd step is a depth check that ensures the config has all keys from the default config
            // this is done by comparing the config with the default config
            // if the config is missing a key, it will be added with the default value
            // if the config has a key that is not in the default config, it will be removed
            Configuration defaultConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ProxyServer.getInstance().getPluginManager().getPlugin("customcommands").getResourceAsStream("config.yml"));
            defaultConfig.getKeys().forEach((key) -> {
                if (!configuration.contains(key)) {
                    configuration.set(key, defaultConfig.get(key));
                }
            });
            configuration.getKeys().forEach((key) -> {
                if (!defaultConfig.contains(key)) {
                    configuration.set(key, null);
                }
            });
        }
}
