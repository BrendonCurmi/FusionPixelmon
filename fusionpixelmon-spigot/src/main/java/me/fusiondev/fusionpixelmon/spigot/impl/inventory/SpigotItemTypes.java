package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Material;

public class SpigotItemTypes extends AbstractItemTypes {
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

    private AbstractItemType get(Material material) {
        return SpigotAdapter.adapt(material);
    }
}
