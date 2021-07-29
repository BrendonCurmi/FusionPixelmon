package me.fusiondev.fusionpixelmon.sponge;

import com.google.inject.Inject;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import me.fusiondev.fusionpixelmon.data.PokeShrineData;
import me.fusiondev.fusionpixelmon.modules.antifall.AntifallModule;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.sponge.modules.arcplates.SpongeArcPlatesModule;
import me.fusiondev.fusionpixelmon.sponge.modules.arcplates.commands.ArcPlatesCommand;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.sponge.impl.SpongeConfigManager;
import me.fusiondev.fusionpixelmon.sponge.impl.inventory.SpongeInvInventory;
import me.fusiondev.fusionpixelmon.sponge.modules.masterball.SpongeMasterballModule;
import me.fusiondev.fusionpixelmon.sponge.modules.pokedesigner.commands.PokeDesignerCommand;
import me.fusiondev.fusionpixelmon.sponge.modules.pokemodifiers.PokeModifiersListeners;
import me.fusiondev.fusionpixelmon.sponge.modules.pokemodifiers.commands.PokeModifierCommand;
import me.fusiondev.fusionpixelmon.sponge.modules.pokeshrines.SpongePokeShrines;
import me.fusiondev.fusionpixelmon.api.updater.UpdateChecker;
import me.fusiondev.fusionpixelmon.config.Config;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Plugin(id = SpongeFusionPixelmon.ID,
        name = SpongeFusionPixelmon.NAME,
        version = SpongeFusionPixelmon.VERSION,
        authors = {"FusionDev"},
        description = "Add a little extra to your Pixelmon experience - PokeBuilder, ArcPlates, Modifier Tokens, and more!",
        dependencies = {
                @Dependency(id = "pixelmon", version = "8.1.2"),
                @Dependency(id = "spongeapi", version = "7.1.0")
        })
public class SpongeFusionPixelmon extends PluginInfo {

    // todo may need to update the lvl thing for nature as well

    private static SpongeFusionPixelmon instance;

    public Path configDir;
    private final Logger LOGGER;

    private PokeShrineData pokeShrineData;

    /**
     * Main class constructor that gets called by Sponge's classloader.
     */
    @Inject
    public SpongeFusionPixelmon(@ConfigDir(sharedRoot = false) Path configDir, Logger logger) {
        SpongeFusionPixelmon.instance = this;
        FusionPixelmon.setInstance(this);
        FusionPixelmon.setRegistry(new SpongeRegistry());
        this.configDir = configDir;
        this.LOGGER = logger;
    }

    @Listener
    @SuppressWarnings({"UnstableApiUsage", "unused"})
    public void preInit(GamePreInitializationEvent event) {
        // Configs
        try {
            Files.createDirectories(this.configDir);
        } catch (IOException ex) {
            LOGGER.error("Error loading '" + configDir.toString() + "' directory", ex);
        }

        try {
            Path path = Paths.get(this.configDir.toString(), ID + ".conf");

            if (!Files.exists(path)) {
                Optional<Asset> asset = Sponge.getAssetManager().getAsset(this, "default.conf");
                if (asset.isPresent()) asset.get().copyToFile(path);
            }

            // Load main config
            ConfigManager configManager = new SpongeConfigManager(path);
            setConfiguration(configManager.getNode().getValue(Config.type));

            // Load PokeDesigner config
            getConfiguration().getPokeDesignerConfig().loadPokeDesignerConfig(configManager.getLoader());
        } catch (IOException | ObjectMappingException ex) {
            LOGGER.error("Config file could not be loaded", ex);
        }

        // Register commands through Sponge
        if (getConfiguration().getArcPlates().isEnabled()) {
            Sponge.getCommandManager().register(instance, CommandSpec.builder()
                    .description(Text.of("Opens the ArcPlates GUI to store your Type Plates for your Arceus"))
                    .permission(CMD_PERM + "arc")
                    .executor(new ArcPlatesCommand())
                    .build(), "arc");
        }

        if (getConfiguration().getPokeDesignerConfig().isEnabled()) {
            Sponge.getCommandManager().register(instance, CommandSpec.builder()
                    .description(Text.of("Opens the PokeDesigner GUI to design your Pokemon"))
                    .permission(CMD_PERM + "pokedesigner")
                    .executor(new PokeDesignerCommand())
                    .build(), "pokedesigner", "pd");
        }

        // Register event listeners through Sponge
        if (!getConfiguration().getPickableShrines().isEmpty()) {
            this.pokeShrineData = new PokeShrineData(SpongeFusionPixelmon.getInstance().getConfigDir().toFile(), "pokeshrines");
            Sponge.getEventManager().registerListeners(this, new SpongePokeShrines(pokeShrineData));
        }

        // Set up PokeDesigner Modifiers
        if (getConfiguration().hasModifiers()) {
            PokeModifiers.init();

            Sponge.getEventManager().registerListeners(this, new PokeModifiersListeners());

            Sponge.getCommandManager().register(instance, CommandSpec.builder()
                    .description(Text.of("Gives a Pokemon modifier to the player"))
                    .permission(CMD_PERM + "admin.pokemodifier")
                    .arguments(
                            GenericArguments.optionalWeak(GenericArguments.string(Text.of("modifier"))),
                            GenericArguments.optionalWeak(GenericArguments.player(Text.of("target")))
                    )
                    .executor(new PokeModifierCommand())
                    .build(), "pokemodifier");
        }

        // Register pixelmon events through Forge
        if (getConfiguration().isAntiFallDamageEnabled()) {
            AntifallModule.init();
        }
    }

    @Listener
    @SuppressWarnings("unused")
    public void init(GameInitializationEvent event) {
        // Add Master Ball crafting recipe back
        if (getConfiguration().isMasterballCraftingEnabled()) {
            new SpongeMasterballModule(this);
        }
    }

    @Listener
    @SuppressWarnings("unused")
    public void postInit(GamePostInitializationEvent event) {
        SpongeInvInventory.runUpdater();
    }

    @Listener
    @SuppressWarnings("unused")
    public void onServerStart(GameStartedServerEvent event) {
        LOGGER.info("Successfully running FusionPixelmon v" + VERSION + "!");

        if (!Sponge.getServiceManager().isRegistered(EconomyService.class) && getConfiguration().getPokeDesignerConfig().useCurrency()) {
            LOGGER.warn("No economy plugin detected, so using PokeDollars as currency instead");
        }

        try {
            new UpdateChecker(LOGGER).check(VERSIONS_ENDPOINT, DOWNLOAD_URL);
        } catch (IOException ignored) {
            // If an exception occurs, just don't check for newer versions
        }

        if (hasDeprecatedDataFiles()) {
            LOGGER.warn("Old ArcStorageData files found! ");
            LOGGER.warn("Join our discord (https://discord.gg/VFNTycm) to download the data migrator plugin so players can keep their Arceus plates");
        }
    }

    @Listener
    @SuppressWarnings("unused")
    public void onServerStop(GameStoppingServerEvent event) {
        if (getConfiguration().getArcPlates().getHovering().isEnabled()) {
            SpongeArcPlatesModule.getArcPlates().cleanup();
        }
        if (!getConfiguration().getPickableShrines().isEmpty()) {
            pokeShrineData.save();
        }
    }

    @Listener
    @SuppressWarnings("unused")
    public void onServerStop(GameStoppedServerEvent event) {
        LOGGER.info("Successfully stopped FusionPixelmon!");
    }

    public Path getConfigDir() {
        return configDir;
    }

    public static SpongeFusionPixelmon getInstance() {
        return SpongeFusionPixelmon.instance;
    }

    public PokeShrineData getPokeShrineData() {
        return pokeShrineData;
    }

    private static boolean hasDeprecatedDataFiles() {
        File arcplate = SpongeFusionPixelmon.getInstance().getConfigDir().resolve("arcplates").toFile();
        if (arcplate.exists()) {
            File[] files = arcplate.listFiles();
            for (File file : files) if (!file.getName().endsWith(".json")) return true;
        }
        return false;
    }
}
