package io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemType;
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
