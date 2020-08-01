package me.fusiondev.fusionpixelmon.modules.antifall;

import me.fusiondev.fusionpixelmon.modules.antifall.listeners.AntifallEvents;
import net.minecraftforge.common.MinecraftForge;

public class AntifallModule {

    public static void init() {
        AntifallEvents events = new AntifallEvents();
        MinecraftForge.EVENT_BUS.register(events);
        //Pixelmon.EVENT_BUS.register(events);
    }
}
