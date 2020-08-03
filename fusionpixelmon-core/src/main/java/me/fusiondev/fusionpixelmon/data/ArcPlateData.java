package me.fusiondev.fusionpixelmon.data;

import me.fusiondev.fusionpixelmon.api.files.FileUtils;
import me.fusiondev.fusionpixelmon.api.files.IFileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ArcPlateData {

    private static final IFileUtils UTILS = new FileUtils();

    private final File FILE;
    private JSONObject data;

    public ArcPlateData(File file, UUID uuid) {
        if (!file.exists()) file.mkdirs();
        this.FILE = new File(file, uuid + ".json");
        get();
    }

    /**
     * Gets the data from the saved file and populates the {@link #data} variable.
     * If the data file doesn't exist, the data is built per default {@link #build()}.
     */
    public void get() {
        if (!FILE.exists()) data = build();
        else
            try {
                data = UTILS.read(FILE.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    /**
     * Builds the default structure of the data.
     *
     * @return the data JSONObject.
     */
    private JSONObject build() {
        JSONObject data = new JSONObject();

        JSONObject plates = new JSONObject();

        for (int i = 0; i < 17; i++) {
            plates.put(i + "", 0);
        }

        data.put("plates", plates);
        return data;
    }

    /**
     * Saves the data to the file.
     */
    public void save() {
        UTILS.write(FILE.getAbsolutePath(), data);
    }

    /**
     * Checks if the plate in the specified index is saved in the data.
     *
     * @param i the index of the plate.
     * @return true if the plate is saved; otherwise false.
     */
    public boolean hasPlate(int i) {
        return data.getJSONObject("plates").getInt(i + "") == 1;
    }

    /**
     * Adds the plate at the specified index.
     *
     * @param i the index of the plate.
     */
    public void add(int i) {
        data.getJSONObject("plates").put(i + "", 1);
    }

    /**
     * Removes the plate at the specified index.
     *
     * @param i the index of the plate.
     */
    public void remove(int i) {
        data.getJSONObject("plates").put(i + "", 0);
    }
}
