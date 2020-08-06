package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import org.bukkit.inventory.Inventory;

public class SpigotInventory extends AbstractInventory {
    private final Inventory INVENTORY;

    public SpigotInventory(Inventory inventory) {
        this.INVENTORY = inventory;
    }

    @Override
    public Inventory getRaw() {
        return INVENTORY;
    }
}
