package me.FusionDev.FusionPixelmon.sponge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class SpongeItemType extends AbstractItemType {

    private final ItemType ITEMTYPE;

    public SpongeItemType(ItemType itemType) {
        this.ITEMTYPE = itemType;
    }

    @Override
    public AbstractItemStack to() {
        return new SpongeItemStack(ItemStack.builder().itemType(ITEMTYPE).build());
    }

    @Override
    public ItemType getRaw() {
        return ITEMTYPE;
    }
}
