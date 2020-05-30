package io.github.brendoncurmi.fusionpixelmon;

public class FusionPixelmon {
    public static final String VERSION = "1.9";

    public static Registry registry;

    public static Registry getRegistry() {
        return registry;
    }

    public static void setRegistry(Registry registry) {
        FusionPixelmon.registry = registry;
    }
}
