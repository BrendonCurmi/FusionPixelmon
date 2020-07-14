package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Material;

public class SpigotItemTypes extends AbstractItemTypes {
    private AbstractItemType get(Material material) {
        return SpigotAdapter.adapt(material);
    }

    @Override
    public AbstractItemType BARRIER() {
        return get(Material.BARRIER);
    }

    @Override
    public AbstractItemType PAPER() {
        return get(Material.PAPER);
    }

    @Override
    public AbstractItemType GLASS_PANE() {
        return get(Material.THIN_GLASS);
    }

    @Override
    public AbstractItemType STAINED_GLASS_PANE() {
        return get(Material.STAINED_GLASS_PANE);
    }

    @Override
    public AbstractItemType EGG() {
        return get(Material.EGG);
    }

    @Override
    public AbstractItemType STAINED_HARDENED_CLAY() {
        return get(Material.STAINED_CLAY);
    }

    @Override
    public AbstractItemType CONCRETE() {
        return get(Material.CONCRETE);
    }

    @Override
    public AbstractItemType LAPIS_BLOCK() {
        return get(Material.LAPIS_BLOCK);
    }

    @Override
    public AbstractItemType REDSTONE_BLOCK() {
        return get(Material.REDSTONE_BLOCK);
    }

    @Override
    public AbstractItemType GOLD_BLOCK() {
        return get(Material.GOLD_BLOCK);
    }

    @Override
    public AbstractItemType QUARTZ_BLOCK() {
        return get(Material.QUARTZ_BLOCK);
    }

    @Override
    public AbstractItemType DYE() {
        return get(Material.INK_SACK);
    }
}
