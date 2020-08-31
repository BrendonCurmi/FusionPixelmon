package me.fusiondev.fusionpixelmon.modules.arcplates.config;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ArcPlatesConfig {
    @Setting("enabled")
    private boolean enabled;
    @Setting("hovering")
    private Hovering hovering;

    public boolean isEnabled() {
        return enabled;
    }

    public Hovering getHovering() {
        return hovering;
    }

    @ConfigSerializable
    public static class Hovering {
        @Setting("enabled")
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }
    }
}
