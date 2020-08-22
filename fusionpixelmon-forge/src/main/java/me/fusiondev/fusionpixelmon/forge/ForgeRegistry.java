package me.fusiondev.fusionpixelmon.forge;

import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;
import me.fusiondev.fusionpixelmon.forge.api.pixelmon.PixelmonUtils;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeInvInventory;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeItemTypes;

public class ForgeRegistry extends Registry {
    @Override
    public AbstractItemStack getItemStack() {
        return null;
    }

    @Override
    public InvInventory getInvInventory() {
        return new ForgeInvInventory();
    }

    @Override
    public AbstractItemTypes getItemTypesRegistry() {
        return new ForgeItemTypes();
    }

    @Override
    public AbstractPixelmonUtils getPixelmonUtils() {
        return new PixelmonUtils();
    }
}
