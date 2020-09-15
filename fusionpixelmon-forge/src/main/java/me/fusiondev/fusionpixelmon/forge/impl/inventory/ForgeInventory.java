package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import ca.landonjw.gooeylibs.inventory.api.Page;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;

public class ForgeInventory extends AbstractInventory {
    private final Page INVENTORY;

    public ForgeInventory(Page inventory) {
        this.INVENTORY = inventory;
    }

    @Override
    public Page getRaw() {
        return INVENTORY;
    }
}
