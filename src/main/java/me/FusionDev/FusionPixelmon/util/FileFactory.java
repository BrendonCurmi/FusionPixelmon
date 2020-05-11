package me.FusionDev.FusionPixelmon.util;

import java.io.*;

/**
 * This factory handles the serialization and deserialization of serializable objects.
 */
public class FileFactory {

    /**
     * Serializes the specified serializable object to the specified file.
     *
     * @param serializable the object to serialize.
     * @param path         the path to write the file to.
     */
    public static void serialize(Serializable serializable, String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(serializable);
            out.close();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deserializes the specified file path to the data object.
     *
     * @param path the path to read the file from.
     * @return the deserialized data object.
     */
    public static Serializable deserialize(String path) {
        Serializable serializable = null;
        try {
            FileInputStream inputStream = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            serializable = (Serializable) in.readObject();
            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return serializable;
    }
}
