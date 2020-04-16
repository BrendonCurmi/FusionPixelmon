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

    @Override
    protected void configUpdater(int newVersion) throws IOException {
        // Backwards compatibility to move data from pokedesigner -> pokedesigner.shop for previous versions
        CommentedConfigurationNode tmpNode = getNode().getNode("pokedesigner-shop-tmp");
        tmpNode.setValue(getNode().getNode("pokedesigner").getChildrenMap());
        getNode().removeChild("pokedesigner");

        super.configUpdater(newVersion);

        getNode().getNode("pokedesigner", "shops").setValue(tmpNode.getChildrenMap());
        getNode().removeChild("pokedesigner-shop-tmp");
    }
}
