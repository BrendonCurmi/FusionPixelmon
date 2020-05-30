package io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemType;
import org.spongepowered.api.item.ItemType;

public class SpongeItemType extends AbstractItemType {

    ItemType itemType;

    public SpongeItemType(ItemType itemType) {
        this.itemType = itemType;
    }



    @Override
    public AbstractItemStack to() {
        return null;
    }

    @Override
    public ItemType getRaw() {
        return itemType;
    }
}
