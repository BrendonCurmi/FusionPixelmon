package me.fusiondev.fusionpixelmon.api.data;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils implements IFileUtils{

    @Override
    public void write(String name, JSONObject json) {
        try (FileWriter file = new FileWriter(name)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public JSONObject read(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(name)), StandardCharsets.UTF_8));
        return new JSONObject(build(reader));
    }

    private static String build(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) builder.append((char) cp);
        return builder.toString();
    }
}
