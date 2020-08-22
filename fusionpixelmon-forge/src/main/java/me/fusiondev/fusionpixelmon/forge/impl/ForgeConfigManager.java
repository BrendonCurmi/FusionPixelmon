package me.fusiondev.fusionpixelmon.forge.impl;

import me.fusiondev.fusionpixelmon.api.config.ConfigManager;
import me.fusiondev.fusionpixelmon.forge.ForgeFusionPixelmon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class ForgeConfigManager extends ConfigManager {
    public ForgeConfigManager(Path path) throws IOException {
        super(path);
    }

    @Override
    public URL getUrl() {
        try {
            return new File(ForgeFusionPixelmon.getInstance().getDataFolder(), "default.conf").toURI().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
