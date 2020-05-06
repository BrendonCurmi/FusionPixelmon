package me.FusionDev.FusionPixelmon.config;

import me.FusionDev.FusionPixelmon.FusionPixelmon;
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
        return HoconConfigurationLoader.builder().setURL(Sponge.getAssetManager().getAsset(FusionPixelmon.getInstance(), "default.conf").get().getUrl()).build().load(loader.getDefaultOptions());
    }
}
