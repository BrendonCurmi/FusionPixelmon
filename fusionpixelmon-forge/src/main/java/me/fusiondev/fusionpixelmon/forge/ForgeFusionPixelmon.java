package me.fusiondev.fusionpixelmon.forge;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.config.Config;
import me.fusiondev.fusionpixelmon.forge.impl.ForgeConfigManager;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeInvInventory;
import me.fusiondev.fusionpixelmon.forge.modules.arcplates.commands.ArcPlatesCommand;
import me.fusiondev.fusionpixelmon.forge.modules.masterball.ForgeMasterballModule;
import me.fusiondev.fusionpixelmon.forge.modules.pokedesigner.commands.PokeDesignerCommand;
import me.fusiondev.fusionpixelmon.forge.modules.pokemodifiers.PokeModifiersListeners;
import me.fusiondev.fusionpixelmon.forge.modules.pokemodifiers.commands.PokeModifierCommand;
import me.fusiondev.fusionpixelmon.forge.modules.pokeshrines.ForgePokeShrines;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

@Mod(
        modid = ForgeFusionPixelmon.ID,
        name = ForgeFusionPixelmon.NAME,
        version = ForgeFusionPixelmon.VERSION,
        dependencies = "required-after:pixelmon;",
        updateJSON = ForgeFusionPixelmon.VERSIONS_ENDPOINT
)
public class ForgeFusionPixelmon extends PluginInfo {
//clientSideOnly

    private static ForgeFusionPixelmon instance;
    private File dataFolder;

    @Mod.EventHandler
    @SuppressWarnings({"UnstableApiUsage", "unused"})
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        FusionPixelmon.setInstance(this);
        FusionPixelmon.setRegistry(new ForgeRegistry());
        dataFolder = new File(event.getModConfigurationDirectory(), ID);

        createConfigFile(new File(getDataFolder(), "default.conf"), true);
        File configFile = new File(getDataFolder(), ID + ".conf");
        createConfigFile(configFile, false);

        // Load main config
        try {
            ConfigManager configManager = new ForgeConfigManager(configFile.toPath());
            setConfiguration(configManager.getNode().getValue(Config.type));

            // Load PokeDesigner config
            getConfiguration().getPokeDesignerConfig().loadPokeDesignerConfig(configManager.getLoader());

            getConfiguration().getPokeDesignerConfig().removeShop("ivev");
        } catch (IOException | ObjectMappingException ex) {
            ex.printStackTrace();
        }

        MinecraftForge.EVENT_BUS.register(this);

        /*if (getConfiguration().getArcPlates().getHovering().isEnabled()) {
            MinecraftForge.EVENT_BUS.register(ForgeArcPlatesModule.getArcPlates());
        }*/

        if (!getConfiguration().getPickableShrines().isEmpty()) {
            MinecraftForge.EVENT_BUS.register(new ForgePokeShrines());
        }

        if (getConfiguration().hasModifiers()) {
            PokeModifiers.init();
            MinecraftForge.EVENT_BUS.register(new PokeModifiersListeners());
        }
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        if (getConfiguration().isMasterballCraftingEnabled()) {
            new ForgeMasterballModule();
        }
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void starting(FMLServerStartingEvent event) {
        if (getConfiguration().getArcPlates().isEnabled()) {
            event.registerServerCommand(new ArcPlatesCommand());
        }
        if (getConfiguration().getPokeDesignerConfig().isEnabled()) {
            event.registerServerCommand(new PokeDesignerCommand());
        }
        if (getConfiguration().hasModifiers()) {
            event.registerServerCommand(new PokeModifierCommand());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void log(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        if (ForgeVersion.getResult(Loader.instance().activeModContainer()).status == ForgeVersion.Status.OUTDATED) {
            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "There is a newer version of FusionPixelmon available @ " + getDownloadUrl()));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void openInventory(GuiOpenEvent event) {
        ForgeInvInventory.runUpdater();
    }

    private void createConfigFile(File file, boolean alwaysCreate) {
        if (!file.exists() || alwaysCreate) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try (InputStream in = getResource("assets/" + getId() + "/default.conf");
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

    public InputStream getResource(String filename) {
        if (filename == null) throw new IllegalArgumentException("Filename cannot be null");
        else try {
            URL url = this.getClass().getClassLoader().getResource(filename);
            if (url == null) return null;
            else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException ex) {
            return null;
        }
    }

    public static ForgeFusionPixelmon getInstance() {
        return instance;
    }

    public File getDataFolder() {
        return dataFolder;
    }
}
