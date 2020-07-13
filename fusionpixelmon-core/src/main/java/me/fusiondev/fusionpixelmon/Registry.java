package me.fusiondev.fusionpixelmon;

import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;

public abstract class Registry {
    public abstract AbstractItemStack getItemStack();
    public abstract AbstractInventory getInventory();
    public abstract AbstractItemTypes getItemTypesRegistry();
    public abstract AbstractPixelmonUtils getPixelmonUtils();
}
