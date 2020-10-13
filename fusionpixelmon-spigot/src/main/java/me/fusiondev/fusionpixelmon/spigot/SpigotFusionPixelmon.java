package me.fusiondev.fusionpixelmon.spigot;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.IPluginInfo;
import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.config.Config;
import me.fusiondev.fusionpixelmon.data.PokeShrineData;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.spigot.api.updater.UpdateChecker;
import me.fusiondev.fusionpixelmon.spigot.impl.SpigotConfigManager;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import me.fusiondev.fusionpixelmon.spigot.modules.arcplates.SpigotArcPlatesModule;
import me.fusiondev.fusionpixelmon.spigot.modules.arcplates.commands.ArcPlatesCommand;
import me.fusiondev.fusionpixelmon.spigot.modules.masterball.SpigotMasterballModule;
import me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.commands.PokeDesignerCommand;
import me.fusiondev.fusionpixelmon.spigot.modules.pokemodifiers.PokeModifiersListeners;
import me.fusiondev.fusionpixelmon.spigot.modules.pokemodifiers.commands.PokeModifierCommand;
import me.fusiondev.fusionpixelmon.spigot.modules.pokeshrines.SpigotPokeShrines;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

public class SpigotFusionPixelmon extends JavaPlugin implements IPluginInfo {

    private static SpigotFusionPixelmon instance;

    private PokeShrineData pokeShrineData;

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onEnable() {
        instance = this;
        FusionPixelmon.setInstance(this);
        FusionPixelmon.setRegistry(new SpigotRegistry());
        System.out.println("Successfully running FusionPixelmon v" + VERSION + "!");

        new Metrics(this, BSTATS_ID);

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

        if (getConfiguration().getPokeDesignerConfig().isEnabled()) {
            get("pd", new PokeDesignerCommand());
        }

        if (getConfiguration().getArcPlates().isEnabled()) {
            get("arc", new ArcPlatesCommand());
        }

        getServer().getPluginManager().registerEvents(new SpigotInvInventory(), this);

        if (!getConfiguration().getPickableShrines().isEmpty()) {
            this.pokeShrineData = new PokeShrineData(SpigotFusionPixelmon.getInstance().getDataFolder(), "pokeshrines");
            getServer().getPluginManager().registerEvents(new SpigotPokeShrines(pokeShrineData), this);
        }

        if (getConfiguration().hasModifiers()) {
            PokeModifiers.init();

            getServer().getPluginManager().registerEvents(new PokeModifiersListeners(), this);

            get("pokemodifier", new PokeModifierCommand());
        }

        // Add Master Ball crafting recipe back
        if (getConfiguration().isMasterballCraftingEnabled()) {
            new SpigotMasterballModule(this);
        }

        SpigotInvInventory.runUpdater();

        new UpdateChecker(getLogger(), this).check(VERSIONS_ENDPOINT, DOWNLOAD_URL);
    }

    private void createConfigFile(File file, boolean alwaysCreate) {
        if (!file.exists() || alwaysCreate) {
            try {
                Files.createDirectories(file.getParentFile().toPath());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try (InputStream in = getInstance().getResource("assets/" + getId() + "/default.conf");
                 OutputStream out = new FileOutputStream(file)) {
                copy(in, out);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void get(String name, CommandExecutor e) {
        Optional<PluginCommand> pluginCommand = Optional.ofNullable(this.getCommand(name));
        pluginCommand.ifPresent(cmd -> cmd.setExecutor(e));
    }

    @Override
    public void onDisable() {
        if (getConfiguration().getArcPlates().getHovering().isEnabled()) {
            SpigotArcPlatesModule.getArcPlates().cleanup();
        }
        if (!getConfiguration().getPickableShrines().isEmpty()) {
            pokeShrineData.save();
        }
    }

    public static SpigotFusionPixelmon getInstance() {
        return SpigotFusionPixelmon.instance;
    }

    public PokeShrineData getPokeShrineData() {
        return pokeShrineData;
    }


    public static final String ID = "fusionpixelmon";
    public static final String VERSION = "1.10";

    public static final int BSTATS_ID = 8277;

    private static final int RESOURCE_ID = 84753;
    public static final String DOWNLOAD_URL = "https://www.spigotmc.org/resources/fusionpixelmon.84753/";
    public static final String VERSIONS_ENDPOINT = "https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID;


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
        return DOWNLOAD_URL;
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
