package me.fusiondev.fusionpixelmon.data;

import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

public class PokeShrineData extends Data {

    public PokeShrineData(File file, String name) {
        super(file, name);
    }

    /**
     * Builds the default structure of the data.
     *
     * @return the data JSONObject.
     */
    @Override
    protected JSONObject build() {
        return new JSONObject();
    }

    private String getKey(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    public void lock(int x, int y, int z, UUID uuid) {
        String key = getKey(x, y, z);
        data.put(key, uuid);
    }

    public void unlock(int x, int y, int z) {
        String key = getKey(x, y, z);
        data.remove(key);
    }

    public boolean isLocked(int x, int y, int z) {
        String key = getKey(x, y, z);
        return data.has(key);
    }

    public boolean isLockedBy(int x, int y, int z, UUID uuid) {
        String key = getKey(x, y, z);
        return data.has(key) && data.get(key).toString().equals(uuid.toString());
    }
}
