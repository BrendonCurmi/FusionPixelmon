package me.fusiondev.fusionpixelmon.data;

import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

public class ArcPlateData extends Data {

    public ArcPlateData(File file, UUID uuid) {
        super(file, uuid);
    }

    /**
     * Builds the default structure of the data.
     *
     * @return the data JSONObject.
     */
    @Override
    protected JSONObject build() {
        JSONObject data = new JSONObject();

        JSONObject plates = new JSONObject();

        for (int i = 0; i < 17; i++) {
            plates.put(i + "", 0);
        }

        data.put("plates", plates);
        return data;
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
