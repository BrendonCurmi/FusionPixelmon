package io.github.brendoncurmi.fusionpixelmon.sponge.impl;

import io.github.brendoncurmi.fusionpixelmon.api.config.ConfigManager;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeFusionPixelmon;
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
