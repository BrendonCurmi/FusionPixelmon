package me.FusionDev.FusionPixelmon.sponge.impl;

import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import org.spongepowered.api.item.inventory.Inventory;

public class SpongeInventory extends AbstractInventory {
    Inventory inventory;

    public SpongeInventory(org.spongepowered.api.item.inventory.Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getRaw() {
        return inventory;
    }
}
