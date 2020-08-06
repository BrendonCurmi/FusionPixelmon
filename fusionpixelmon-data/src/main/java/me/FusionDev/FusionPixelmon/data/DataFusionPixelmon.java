package me.FusionDev.FusionPixelmon.data;

import com.google.inject.Inject;
import me.FusionDev.FusionPixelmon.data.files.FileFactory;
import me.FusionDev.FusionPixelmon.data.files.IFileFactory;
import me.fusiondev.fusionpixelmon.data.ArcPlateData;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Plugin(id = "fusionpixelmon-data",
        name = "FusionPixelmonData",
        version = "1.0",
        authors = {"FusionDev"},
        description = "Data migrator for FusionPixelmon 1.9",
        dependencies = {
                @Dependency(id = "pixelmon", version = "7.0.8"),
                @Dependency(id = "spongeapi", version = "7.1.0"),
                @Dependency(id = DataFusionPixelmon.MAIN_ID)
        })
public class DataFusionPixelmon {

    public static final String MAIN_ID = "fusionpixelmon";

    private final Logger LOGGER;
    private final File ARCPLATES_DIR;

    @Inject
    public DataFusionPixelmon(@ConfigDir(sharedRoot = true) Path configDir, Logger logger) {
        this.LOGGER = logger;
        this.ARCPLATES_DIR = configDir.resolve(MAIN_ID).resolve("arcplates").toFile();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        LOGGER.info("Starting FusionPixelmon ArcPlateData migration...");

        IFileFactory fileFactory = new FileFactory();

        File[] files = ARCPLATES_DIR.listFiles();
        for (File file : files) {
            if (!file.getName().endsWith(".json")) {
                LOGGER.info("  Migrating " + file + "...");
                try {
                    ArcStorageData oldData = (ArcStorageData) fileFactory.deserialize(file.getAbsolutePath());
                    String uuid = file.getName().split(" ")[1];
                    ArcPlateData newData = new ArcPlateData(ARCPLATES_DIR, UUID.fromString(uuid));

                    for (int i = 0; i < 17; i++) {
                        if (oldData.get(i) != null)
                            newData.add(i);
                        else
                            newData.remove(i);
                    }

                    newData.save();
                    Files.delete(file.toPath());
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        LOGGER.info("Finished FusionPixelmon ArcPlateData migration");
    }
}
