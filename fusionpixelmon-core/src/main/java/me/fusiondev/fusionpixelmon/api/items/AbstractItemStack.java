package me.fusiondev.fusionpixelmon.api.items;

import me.fusiondev.fusionpixelmon.api.Raw;

import java.util.List;

public abstract class AbstractItemStack implements Raw {
    public abstract AbstractItemStack from(AbstractItemType itemType);

    public abstract AbstractItemStack setName(String name);

    public abstract List<String> getLore();

    public abstract AbstractItemStack setLore(List<String> itemLore);

    public AbstractItemStack setColour(Object colour) {
        return this;
    }
}
