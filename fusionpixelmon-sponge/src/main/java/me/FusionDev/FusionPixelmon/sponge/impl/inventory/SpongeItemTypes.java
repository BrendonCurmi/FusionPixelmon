package me.FusionDev.FusionPixelmon.sponge.impl.inventory;

import me.FusionDev.FusionPixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public class SpongeItemTypes extends AbstractItemTypes {
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

    private AbstractItemType get(ItemType itemType) {
        return SpongeAdapter.adapt(itemType);
    }
}
