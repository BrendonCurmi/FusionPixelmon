package me.FusionDev.FusionPixelmon.sponge.impl.inventory;

import me.FusionDev.FusionPixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public class SpongeItemTypes extends AbstractItemTypes {
    private AbstractItemType get(ItemType itemType) {
        return SpongeAdapter.adapt(itemType);
    }

    @Override
    public AbstractItemType BARRIER() {
        return get(ItemTypes.BARRIER);
    }

    @Override
    public AbstractItemType PAPER() {
        return get(ItemTypes.PAPER);
    }

    @Override
    public AbstractItemType GLASS_PANE() {
        return get(ItemTypes.GLASS_PANE);
    }

    @Override
    public AbstractItemType STAINED_GLASS_PANE() {
        return get(ItemTypes.STAINED_GLASS_PANE);
    }

    @Override
    public AbstractItemType EGG() {
        return get(ItemTypes.EGG);
    }

    @Override
    public AbstractItemType STAINED_HARDENED_CLAY() {
        return get(ItemTypes.STAINED_HARDENED_CLAY);
    }

    @Override
    public AbstractItemType CONCRETE() {
        return get(ItemTypes.CONCRETE);
    }

    @Override
    public AbstractItemType LAPIS_BLOCK() {
        return get(ItemTypes.LAPIS_BLOCK);
    }

    @Override
    public AbstractItemType REDSTONE_BLOCK() {
        return get(ItemTypes.REDSTONE_BLOCK);
    }

    @Override
    public AbstractItemType GOLD_BLOCK() {
        return get(ItemTypes.GOLD_BLOCK);
    }

    @Override
    public AbstractItemType QUARTZ_BLOCK() {
        return get(ItemTypes.QUARTZ_BLOCK);
    }

    @Override
    public AbstractItemType DYE() {
        return get(ItemTypes.DYE);
    }
}
