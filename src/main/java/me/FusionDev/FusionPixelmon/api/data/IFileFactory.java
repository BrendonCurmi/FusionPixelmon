package me.FusionDev.FusionPixelmon.api.data;

import java.io.Serializable;

public interface IFileFactory {

    void serialize(Serializable serializable, String path);

    Serializable deserialize(String path);
}
