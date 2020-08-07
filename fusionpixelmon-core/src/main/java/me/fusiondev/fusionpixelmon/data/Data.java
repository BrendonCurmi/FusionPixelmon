package me.fusiondev.fusionpixelmon.data;

import me.fusiondev.fusionpixelmon.api.files.FileUtils;
import me.fusiondev.fusionpixelmon.api.files.IFileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public abstract class Data {

    private static final IFileUtils UTILS = new FileUtils();

    private final File FILE;
    protected JSONObject data;

    public Data(File file, Object name) {
        if (!file.exists()) file.mkdirs();
        this.FILE = new File(file, name + ".json");
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
    protected abstract JSONObject build();

    /**
     * Saves the data to the file.
     */
    public void save() {
        UTILS.write(FILE.getAbsolutePath(), data);
    }
}
