package me.fusiondev.fusionpixelmon.sponge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import org.spongepowered.api.item.inventory.Inventory;

public class SpongeInventory extends AbstractInventory {
    private final Inventory INVENTORY;

    public SpongeInventory(Inventory inventory) {
        this.INVENTORY = inventory;
    }

    @Override
    public Inventory getRaw() {
        return INVENTORY;
    }
}
