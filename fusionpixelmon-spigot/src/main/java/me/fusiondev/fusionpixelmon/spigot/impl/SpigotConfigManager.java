package me.fusiondev.fusionpixelmon.spigot.impl;

import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.spigot.SpigotFusionPixelmon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class SpigotConfigManager extends ConfigManager {
    public SpigotConfigManager(Path path) throws IOException {
        super(path);
    }

    @Override
    public URL getUrl() {
        try {
            return new File(SpigotFusionPixelmon.getInstance().getDataFolder(), "default.conf").toURI().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
