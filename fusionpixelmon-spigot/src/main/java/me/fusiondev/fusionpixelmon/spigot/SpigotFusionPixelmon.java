package me.fusiondev.fusionpixelmon.spigot;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.IPluginInfo;
import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.config.Config;
import me.fusiondev.fusionpixelmon.spigot.impl.SpigotConfigManager;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.commands.PokeDesignerCommand;
import org.apache.commons.io.IOUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

public class SpigotFusionPixelmon extends JavaPlugin implements IPluginInfo {

    private static SpigotFusionPixelmon instance;

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onEnable() {
        instance = this;
        FusionPixelmon.setInstance(this);
        FusionPixelmon.setRegistry(new SpigotRegistry());
        System.out.println("STARTED");

        createConfigFile(new File(getDataFolder(), "default.conf"), true);
        File configFile = new File(getDataFolder(), ID + ".conf");
        createConfigFile(configFile, false);

        // Load main config
        try {
            ConfigManager configManager = new SpigotConfigManager(configFile.toPath());
            setConfiguration(configManager.getNode().getValue(Config.type));

            // Load PokeDesigner config
            getConfiguration().getPokeDesignerConfig().loadPokeDesignerConfig(configManager.getLoader());
        } catch (IOException | ObjectMappingException ex) {
            ex.printStackTrace();
        }

        get("pd", new PokeDesignerCommand());

        getServer().getPluginManager().registerEvents(new SpigotInvInventory(), this);

        SpigotInvInventory.runUpdater();
    }

    private void createConfigFile(File file, boolean alwaysCreate) {
        if (!file.exists() || alwaysCreate) {
            try {
                Files.createDirectories(file.getParentFile().toPath());

                InputStream in = getInstance().getResource(getId() + "/default.conf");
                OutputStream out = new FileOutputStream(file);
                IOUtils.copy(in, out);
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void get(String name, CommandExecutor e) {
        Optional<PluginCommand> pluginCommand = Optional.ofNullable(this.getCommand(name));
        pluginCommand.ifPresent(cmd -> cmd.setExecutor(e));
    }

    @Override
    public void onDisable() {
        System.out.println("ENDED");
    }

    public static SpigotFusionPixelmon getInstance() {
        return SpigotFusionPixelmon.instance;
    }


    public static final String ID = "fusionpixelmon";
    public static final String NAME = "FusionPixelmon";
    public static final String VERSION = "1.9";

    public static final String CMD_PERM = ID + ".command.";

    public static final String VERSIONS_ENDPOINT = "https://ore.spongepowered.org/api/v1/projects/" + ID + "/versions";
    public static final String ORE_VERSIONS = "https://ore.spongepowered.org/FusionDev/FusionPixelmon/versions";


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String getDownloadUrl() {
        return ORE_VERSIONS;
    }

    @Override
    public String getVersionsApiUrl() {
        return VERSIONS_ENDPOINT;
    }

    private Config config;

    @Override
    public Config getConfiguration() {
        return config;
    }

    @Override
    public void setConfiguration(Config config) {
        this.config = config;
    }
}
