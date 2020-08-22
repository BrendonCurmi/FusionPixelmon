package me.fusiondev.fusionpixelmon.forge;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        logger = event.getModLog();
        FusionPixelmon.setInstance(this);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void log(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP p = (EntityPlayerMP) event.player;
        System.out.println(p);
        System.out.println(p.getName());
        p.sendMessage(new TextComponentString(p.getName()));
    }
}
