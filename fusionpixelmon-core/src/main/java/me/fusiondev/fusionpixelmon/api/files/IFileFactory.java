package me.fusiondev.fusionpixelmon.api.files;

import java.io.Serializable;

public interface IFileFactory {

    void serialize(Serializable serializable, String path);

    Serializable deserialize(String path);
}
