package me.fusiondev.fusionpixelmon.api.config;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.loader.ConfigurationLoader;
import me.fusiondev.fusionpixelmon.FusionPixelmon;

import java.io.IOException;
import java.nio.file.Path;

public abstract class AbstractConfigManager<T extends ConfigurationNode, L extends ConfigurationLoader<T>> {
    private final L loader;
    private T node;

    /**
     * Constructor to load the config file at the specified file. Config version is not checked or updated.
     *
     * @param path the path of the config file.
     * @throws IOException if an I/O exception occurs.
     */
    protected AbstractConfigManager(Path path) throws IOException {
        this(path, false);
    }

    /**
     * Constructor to load the config file at the specified file. If <code>checkVersion</code> is true, the file
     * version is checked and updated with new entries if it is old.
     *
     * @param path         the path of the config file.
     * @param checkVersion whether the version should be checked and config updated.
     * @throws IOException if an I/O exception occurs.
     */
    protected AbstractConfigManager(Path path, boolean checkVersion) throws IOException {
        loader = getLoader(path);
        load(checkVersion);
    }

    /**
     * Loads and saves the config file from the loader, assigning it to this class' node. If <code>checkVersion</code>
     * is true, the file version is checked and updated with new entries if it is old.
     *
     * @param checkVersion whether the version should be checked and config updated.
     * @throws IOException if an I/O exception occurs.
     */
    protected void load(boolean checkVersion) throws IOException {
        node = loader.load();
        if (checkVersion) {
            String versionStr = FusionPixelmon.getPlugin().getVersion();
            int version = Integer.parseInt(versionStr.substring(versionStr.indexOf(".") + 1));
            if (node.getNode("version").getInt() < version) {
                configUpdater(version);
            }
        }
        save();
    }

    /**
     * Updates the config file from the default config.
     * @param newVersion the new version of the config file.
     * @throws IOException if an I/O exception occurs.
     */
    protected void configUpdater(int newVersion) throws IOException {
        node.mergeValuesFrom(getDefaultsFrom(loader));
        node.getNode("version").setValue(newVersion);
    }

    /**
     * Saves the class' node through the loader.
     *
     * @throws IOException if an I/O exception occurs.
     */
    protected void save() throws IOException {
        loader.save(node);
    }

    /**
     * Gets the loader of the config.
     *
     * @return the config loader.
     */
    public L getLoader() {
        return loader;
    }

    /**
     * Gets the class' node.
     *
     * @return the node.
     */
    public T getNode() {
        return node;
    }

    /**
     * Builds and gets the loader for the config at the specified file.
     *
     * @param path the path of the config file.
     * @return the built loader.
     */
    protected abstract L getLoader(Path path);

    /**
     * The default node to merge values from.
     *
     * @param loader the config loader.
     * @return the default node to merge from.
     * @throws IOException if an I/O exception occurs.
     */
    protected abstract T getDefaultsFrom(L loader) throws IOException;
}
