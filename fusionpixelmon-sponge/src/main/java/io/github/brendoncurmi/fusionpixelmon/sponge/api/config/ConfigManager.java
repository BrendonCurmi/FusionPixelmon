package io.github.brendoncurmi.fusionpixelmon.sponge.api.config;

import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeFusionPixelmon;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigManager extends AbstractConfigManager<CommentedConfigurationNode, HoconConfigurationLoader> {

    public ConfigManager(Path path) throws IOException {
        super(path, true);
    }

    protected HoconConfigurationLoader getLoader(Path path) {
        return HoconConfigurationLoader.builder().setPath(path).build();
    }

    protected CommentedConfigurationNode getDefaultsFrom(HoconConfigurationLoader loader) throws IOException {
        return HoconConfigurationLoader.builder().setURL(Sponge.getAssetManager().getAsset(SpongeFusionPixelmon.getInstance(), "default.conf").get().getUrl()).build().load(loader.getDefaultOptions());
    }

    @Override
    protected void configUpdater(int newVersion) throws IOException {
        // Backwards compatibility to remove 1.8 "currency" field
        getNode().getNode("pokedesigner").removeChild("currency");
        super.configUpdater(newVersion);
    }
}
