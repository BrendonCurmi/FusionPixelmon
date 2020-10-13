package me.fusiondev.fusionpixelmon;

public class FusionPixelmon {
    //todo should make form, pokeball shops autopage

    public static final String VERSION = "1.11";

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

    private static String module;

    public static String getModule() {
        if (module == null)
            module = plugin.getClass().getSimpleName().replace("FusionPixelmon", "").toLowerCase();
        return module;
    }
}
