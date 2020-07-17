package me.fusiondev.fusionpixelmon.api.data;

import org.json.JSONObject;

import java.io.IOException;

public interface IFileUtils {
    void write(String name, JSONObject json);

    JSONObject read(String name) throws IOException;
}
