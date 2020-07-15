package me.fusiondev.fusionpixelmon;

import me.fusiondev.fusionpixelmon.config.Config;

public interface IPluginInfo {
    String getId();

    String getName();

    String getVersion();

    String getDownloadUrl();

    String getVersionsApiUrl();

    Config getConfiguration();

    void setConfiguration(Config config);
}
