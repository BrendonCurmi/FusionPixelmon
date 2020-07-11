package me.FusionDev.FusionPixelmon.sponge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class SpongeItemType extends AbstractItemType {

    private ItemType itemType;

    public SpongeItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public AbstractItemStack to() {
        return new SpongeItemStack(ItemStack.builder().itemType(itemType).build());
    }

    @Override
    public ItemType getRaw() {
        return itemType;
    }
}
