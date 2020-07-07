package me.fusiondev.fusionpixelmon.spigot;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.IPluginInfo;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.commands.PokeDesignerCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class SpigotFusionPixelmon extends JavaPlugin implements IPluginInfo {

    FileConfiguration config = getConfig();

    private static SpigotFusionPixelmon instance;

    @Override
    public void onEnable() {
        instance = this;
        FusionPixelmon.setPlugin(this);
        System.out.println("STARTED");

        config.options().copyDefaults(true);
        saveConfig();



        get("pd", new PokeDesignerCommand());


        getServer().getPluginManager().registerEvents(new SpigotInvInventory(), this);

        SpigotInvInventory.runUpdater();
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
}
