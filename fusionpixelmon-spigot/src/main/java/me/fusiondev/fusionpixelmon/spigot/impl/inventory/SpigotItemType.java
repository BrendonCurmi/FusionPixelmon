package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpigotItemType extends AbstractItemType {

    private final Material MATERIAL;

    public SpigotItemType(Material material) {
        this.MATERIAL = material;
    }

    @Override
    public AbstractItemStack to() {
        return new SpigotItemStack(new ItemStack(MATERIAL));
    }

    @Override
    public Material getRaw() {
        return MATERIAL;
    }
}
