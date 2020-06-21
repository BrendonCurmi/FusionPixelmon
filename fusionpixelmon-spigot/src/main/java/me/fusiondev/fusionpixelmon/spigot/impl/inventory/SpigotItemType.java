package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpigotItemType extends AbstractItemType {

    Material material;

    public SpigotItemType(Material material) {
        this.material = material;
    }

    @Override
    public AbstractItemStack to() {
        return new SpigotItemStack(new ItemStack(material));
    }

    @Override
    public Material getRaw() {
        return material;
    }
}
