package me.fusiondev.fusionpixelmon.forge;

import me.fusiondev.fusionpixelmon.IPluginInfo;
import me.fusiondev.fusionpixelmon.config.Config;

public class PluginInfo implements IPluginInfo {

    public static final String ID = "fusionpixelmon";
    public static final String NAME = "FusionPixelmon";
    public static final String VERSION = "1.9";

    public static final String CMD_PERM = ID + ".command.";

    public static final String VERSIONS_ENDPOINT = "";
    public static final String ORE_VERSIONS = "";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String getDownloadUrl() {
        return null;
    }

    @Override
    public String getVersionsApiUrl() {
        return null;
    }

    private Config config;

    @Override
    public Config getConfiguration() {
        return config;
    }

    @Override
    public void setConfiguration(Config config) {
        this.config = config;
    }
}
