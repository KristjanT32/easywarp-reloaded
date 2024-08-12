package krisapps.easywarpreloaded;

import krisapps.easywarpreloaded.commands.SetLanguage;
import krisapps.easywarpreloaded.commands.legacy.*;
import krisapps.easywarpreloaded.commands.tabcompletion.*;
import krisapps.easywarpreloaded.util.DataUtility;
import krisapps.easywarpreloaded.util.WarpUtility;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.krisapps.PluginEssentials.LocalizationUtility;
import org.krisapps.PluginEssentials.LoggingUtility;
import org.krisapps.PluginEssentials.MessageUtility;
import org.krisapps.PluginEssentials.PluginEssentials;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public final class EasyWarpReloaded extends JavaPlugin {


    /* TODO: Work on WarpUtility, finish implementing the basic methods.
     *   Also make sure to rewrite all methods in /legacy/*/

    public static EasyWarpReloaded instance;

    private final PluginEssentials essentials = new PluginEssentials(this, "en-US");
    public MessageUtility messageUtility = essentials.messages;
    public LocalizationUtility localizationUtility = essentials.localization;
    public LoggingUtility logging = essentials.logging;
    public WarpUtility warpUtility = new WarpUtility();
    public DataUtility dataUtility = new DataUtility(this);

    public FileConfiguration pluginConfig;
    public File configFile = new File(getDataFolder(), "config.yml");

    public FileConfiguration pluginData;
    public File dataFile = new File(getDataFolder(), "warps.yml");

    @Override
    public void onEnable() {
        loadFiles();
        registerCommands();
        registerEvents();
        localizationUtility.initialize();
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadFiles() {
        if (!configFile.getParentFile().exists() || !configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", true);
        }
        if (!dataFile.getParentFile().exists() || !dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!Files.exists(Path.of(getDataFolder() + "/localization/en-US.yml"))) {
            saveResource("localization/en-US.yml", true);
            try {
                Files.move(Path.of(getDataFolder() + "/localization/en-US.yml"),
                        Path.of(getDataFolder().toPath() + "/localization/en-US.yml"),
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        pluginConfig = new YamlConfiguration();
        pluginData = new YamlConfiguration();

        try {
            pluginConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().warning("Failed to load the config file: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            pluginData.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Failed to load the data file: " + e.getMessage());
            e.printStackTrace();
        }

        getLogger().info("Starting localization discovery...");
    }

    private void registerCommands() {
        // Commands
        getCommand("warp").setExecutor(new Warp(this));
        getCommand("warpman").setExecutor(new WarpManCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("invite").setExecutor(new InviteCommand(this));
        getCommand("setlanguage").setExecutor(new SetLanguage(this));
        getCommand("invites").setExecutor(new ViewInvites(this));


        // Tab Completion
        getCommand("warp").setTabCompleter(new WarpAC(this));
        getCommand("warpman").setTabCompleter(new WarpManagerAC(this));
        getCommand("invite").setTabCompleter(new InviteAC(this));
        getCommand("setlanguage").setTabCompleter(new LanguageAC(this));
        getCommand("invites").setTabCompleter(new ViewInvitesAC(this));
    }
    public int resetDefaultLanguageFile() {
        saveResource("localization/en-US.yml", true);
        try {
            Files.move(Path.of(getDataFolder() + "/localization/en-US.yml"),
                    Path.of(getDataFolder().toPath() + "/localization/en-US.yml"),
                    StandardCopyOption.REPLACE_EXISTING
            );
            return 200;
        } catch (IOException e) {
            e.printStackTrace();
            return 500;
        }
    }

    public void reloadCurrentLanguageFile() {
        localizationUtility.loadCurrentLanguage();
    }

    @Override
    public void saveConfig() {
        try {
            pluginConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().warning("An error occurred while trying to save the Configuration File.\nReason: " + e.getMessage());
        }
    }
    public boolean saveData() {
        try {
            pluginData.save(dataFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logging.log("An error occurred while trying to save the data file.\nReason: " + e.getMessage(),
                    "Data",
                    Level.SEVERE
            );
            return false;
        }
    }
}
