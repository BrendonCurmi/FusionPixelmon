package me.FusionDev.FusionPixelmon.sponge;

import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeItemTypes;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;

public class SpongeRegistry extends Registry {
    @Override
    public AbstractItemStack getItemStack() {
        return null;
    }

    @Override
    public AbstractInventory getInventory() {
        return null;
    }

    @Override
    public AbstractItemTypes getItemTypesRegistry() {
        return new SpongeItemTypes();
    }
}
