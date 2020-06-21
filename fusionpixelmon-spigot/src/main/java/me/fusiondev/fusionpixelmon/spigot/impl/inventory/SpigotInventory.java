package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import org.bukkit.inventory.Inventory;

public class SpigotInventory extends AbstractInventory {
    Inventory inventory;

    public SpigotInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getRaw() {
        return inventory;
    }
}
