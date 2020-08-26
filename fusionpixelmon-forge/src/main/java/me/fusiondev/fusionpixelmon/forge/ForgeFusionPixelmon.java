package me.fusiondev.fusionpixelmon.forge;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.config.Config;
import me.fusiondev.fusionpixelmon.forge.impl.ForgeConfigManager;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeInvInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

@Mod(
        modid = ForgeFusionPixelmon.ID,
        name = ForgeFusionPixelmon.NAME,
        version = ForgeFusionPixelmon.VERSION,
        dependencies = "required-after:pixelmon;"
)
public class ForgeFusionPixelmon extends PluginInfo {
//clientSideOnly

    private static ForgeFusionPixelmon instance;
    private Logger logger;
    private File dataFolder;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        logger = event.getModLog();
        FusionPixelmon.setInstance(this);
        FusionPixelmon.setRegistry(new ForgeRegistry());
        dataFolder = new File(event.getModConfigurationDirectory(), ID);
        System.out.println("CONFIG: " + event.getModConfigurationDirectory());

        createConfigFile(new File(getDataFolder(), "default.conf"), true);
        File configFile = new File(getDataFolder(), ID + ".conf");
        createConfigFile(configFile, false);

        // Load main config
        try {
            ConfigManager configManager = new ForgeConfigManager(configFile.toPath());
            setConfiguration(configManager.getNode().getValue(Config.type));

            // Load PokeDesigner config
            getConfiguration().getPokeDesignerConfig().loadPokeDesignerConfig(configManager.getLoader());
        } catch (IOException | ObjectMappingException ex) {
            ex.printStackTrace();
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void log(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP p = (EntityPlayerMP) event.player;
        System.out.println(p);
        System.out.println(p.getName());
        p.sendMessage(new TextComponentString(p.getName()));
    }

    @SubscribeEvent
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
