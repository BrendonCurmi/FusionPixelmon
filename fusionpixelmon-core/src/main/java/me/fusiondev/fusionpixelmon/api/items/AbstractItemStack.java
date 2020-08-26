package me.fusiondev.fusionpixelmon.api.items;

import me.fusiondev.fusionpixelmon.api.Raw;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItemStack implements Raw {
    public abstract AbstractItemStack from(AbstractItemType itemType);

    public abstract AbstractItemStack setName(String name);

    public abstract List<String> getLore();

    public abstract AbstractItemStack setLore(List<String> itemLore);

    public AbstractItemStack setLore(String... lore) {
        List<String> itemLore = new ArrayList<>();
        for (String line : lore) {
            if (line == null) continue;
            itemLore.add(line);
        }
        return setLore(itemLore);
    }

    public abstract AbstractItemStack copy();

    public AbstractItemStack setColour(Object colour) {
        return this;
    }
}
