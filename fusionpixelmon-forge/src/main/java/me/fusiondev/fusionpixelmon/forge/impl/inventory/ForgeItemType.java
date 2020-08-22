package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ForgeItemType extends AbstractItemType {

    private final Item ITEM;

    public ForgeItemType(Item item) {
        this.ITEM = item;
    }

    public ForgeItemType(Block block) {
        this.ITEM = Item.getItemFromBlock(block);
    }

    @Override
    public AbstractItemStack to() {
        return new ForgeItemStack(new ItemStack(ITEM));
    }

    @Override
    public Item getRaw() {
        return ITEM;
    }
}
