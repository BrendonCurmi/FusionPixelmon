package me.fusiondev.fusionpixelmon.api.updater;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class UpdateChecker {

    private Logger logger;

    public UpdateChecker(Logger logger) {
        this.logger = logger;
    }

    /**
     * Checks if there is a newer version of the plugin and reports to the logger if so.
     *
     * @param apiUrl      the url of the version API endpoint.
     * @param downloadUrl the url of the download page.
     * @throws IOException if an I/O exception occurs.
     */
    public void check(String apiUrl, String downloadUrl) throws IOException {
        JSONArray payload = readJsonFromUrl(apiUrl);
        // Get 0th element as versions are in order of latest first
        String name = ((JSONObject) payload.get(0)).getString("name");
        if (name != null && !name.equals(FusionPixelmon.VERSION)) {
            logger.info("There is a newer version of FusionPixelmon available! Version " + name + " @ " + downloadUrl);
        }
    }

    /**
     * Reads the data from the specified url and extracts it as a {@link JSONObject}.
     *
     * @param url the url of the data.
     * @return the data from the url as a JSONObject.
     * @throws IOException if an I/O error occurs.
     */
    private static JSONArray readJsonFromUrl(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return new JSONArray(read(reader));
    }

    /**
     * Reads the data from the specified reader and returns it as a string.
     *
     * @param reader the reader.
     * @return the contents of the reader.
     * @throws IOException if an I/O error occurs.
     */
    private static String read(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) builder.append((char) cp);
        //reader.close(); todo close
        return builder.toString();
    }
}
