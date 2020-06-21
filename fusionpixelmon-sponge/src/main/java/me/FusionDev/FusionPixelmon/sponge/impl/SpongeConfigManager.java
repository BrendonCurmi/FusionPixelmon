package me.FusionDev.FusionPixelmon.sponge.impl;

import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.FusionDev.FusionPixelmon.sponge.SpongeFusionPixelmon;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class SpongeConfigManager extends ConfigManager {

    public SpongeConfigManager(Path path) throws IOException {
        super(path);
    }

    @Override
    public URL getUrl() {
        return Sponge.getAssetManager().getAsset(SpongeFusionPixelmon.getInstance(), "default.conf").get().getUrl();
    }
}
