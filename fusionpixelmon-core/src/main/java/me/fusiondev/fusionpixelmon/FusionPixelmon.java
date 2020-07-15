package me.fusiondev.fusionpixelmon;

public class FusionPixelmon {
    public static final String VERSION = "1.9";

    public static Registry registry;

    public static Registry getRegistry() {
        return registry;
    }

    public static void setRegistry(Registry registry) {
        FusionPixelmon.registry = registry;
    }

    private static IPluginInfo plugin;

    public static void setInstance(IPluginInfo plugin) {
        FusionPixelmon.plugin = plugin;
    }

    public static IPluginInfo getInstance() {
        return FusionPixelmon.plugin;
    }
}
