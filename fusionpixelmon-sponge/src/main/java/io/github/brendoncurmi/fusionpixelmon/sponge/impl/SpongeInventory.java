package io.github.brendoncurmi.fusionpixelmon.sponge.impl;

import io.github.brendoncurmi.fusionpixelmon.api.Raw;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.AbstractInventory;
import org.spongepowered.api.item.inventory.Inventory;

public class SpongeInventory extends AbstractInventory implements Raw<Inventory> {
    Inventory inventory;

    public SpongeInventory(org.spongepowered.api.item.inventory.Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Object get() {
        return inventory;
    }

    @Override
    public Inventory getRaw() {
        return null;
    }
}
