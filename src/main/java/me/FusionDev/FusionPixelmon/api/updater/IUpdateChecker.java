package me.FusionDev.FusionPixelmon.api.updater;

import java.io.IOException;

public interface IUpdateChecker {
    void check(String endpoint, String url) throws IOException;
}
