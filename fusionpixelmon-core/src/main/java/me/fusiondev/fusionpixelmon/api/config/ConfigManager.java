package me.fusiondev.fusionpixelmon.api.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public abstract class ConfigManager extends AbstractConfigManager<CommentedConfigurationNode, HoconConfigurationLoader> {

    public ConfigManager(Path path) throws IOException {
        super(path, true);
    }

    protected HoconConfigurationLoader getLoader(Path path) {
        return HoconConfigurationLoader.builder().setPath(path).build();
    }

    protected CommentedConfigurationNode getDefaultsFrom(HoconConfigurationLoader loader) throws IOException {
        return HoconConfigurationLoader.builder().setURL(getUrl()).build().load(loader.getDefaultOptions());
    }

    @Override
    protected void configUpdater(int newVersion) throws IOException {
        // Backwards compatibility to remove 1.8 "currency" field
        getNode().getNode("pokedesigner").removeChild("currency");
        super.configUpdater(newVersion);
    }

    public abstract URL getUrl();
}
