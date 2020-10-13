package me.fusiondev.fusionpixelmon.spigot.api.updater;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class UpdateChecker {

    private Logger logger;
    private JavaPlugin plugin;

    public UpdateChecker(Logger logger, JavaPlugin plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    public void check(String apiUrl, String downloadUrl) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL(apiUrl).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String version = scanner.next();
                    if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                        logger.info("There is a newer version of FusionPixelmon available! Version " + version + " @ " + downloadUrl);
                    }
                }
            } catch (IOException ignored) {
                // If an exception occurs, just don't check for newer versions
            }
        });
    }
}
