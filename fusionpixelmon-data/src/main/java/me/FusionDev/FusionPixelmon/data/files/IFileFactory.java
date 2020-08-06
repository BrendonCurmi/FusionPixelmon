package me.FusionDev.FusionPixelmon.data.files;

import java.io.IOException;
import java.io.Serializable;

public interface IFileFactory {

    void serialize(Serializable serializable, String path) throws IOException;

    Serializable deserialize(String path) throws IOException, ClassNotFoundException;
}
