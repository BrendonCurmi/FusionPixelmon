package me.FusionDev.FusionPixelmon.sponge;

import me.FusionDev.FusionPixelmon.sponge.api.pixelmon.PixelmonUtils;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeInvInventory;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeItemTypes;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.pixelmon.AbstractPixelmonUtils;

public class SpongeRegistry extends Registry {
    @Override
    public AbstractItemStack getItemStack() {
        return null;
    }

    @Override
    public InvInventory getInvInventory() {
        return new SpongeInvInventory();
    }

    @Override
    public AbstractItemTypes getItemTypesRegistry() {
        return new SpongeItemTypes();
    }

    @Override
    public AbstractPixelmonUtils getPixelmonUtils() {
        return new PixelmonUtils();
    }
}
