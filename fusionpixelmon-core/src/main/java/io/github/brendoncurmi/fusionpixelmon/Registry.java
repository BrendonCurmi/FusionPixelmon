package io.github.brendoncurmi.fusionpixelmon;

import io.github.brendoncurmi.fusionpixelmon.api.inventory.AbstractInventory;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;

public abstract class Registry {
    public abstract AbstractItemStack getItemStack();
    public abstract AbstractInventory getInventory();
}
