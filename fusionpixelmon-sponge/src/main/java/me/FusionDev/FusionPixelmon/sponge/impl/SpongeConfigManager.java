package me.fusiondev.fusionpixelmon.sponge.impl;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
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
        return Sponge.getAssetManager().getAsset(FusionPixelmon.getInstance(), "default.conf").get().getUrl();
    }
}
