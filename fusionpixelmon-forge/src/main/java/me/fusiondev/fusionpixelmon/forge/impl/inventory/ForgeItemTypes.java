package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class ForgeItemTypes extends AbstractItemTypes {
    private AbstractItemType get(Item item) {
        return ForgeAdapter.adapt(item);
    }

    private AbstractItemType get(Block block) {
        return ForgeAdapter.adapt(Item.getItemFromBlock(block));
    }

    @Override
    public AbstractItemType AIR() {
        return get(Items.AIR);
    }

    @Override
    public AbstractItemType BARRIER() {
        return get(Blocks.BARRIER);
    }

    @Override
    public AbstractItemType PAPER() {
        return get(Items.PAPER);
    }

    @Override
    public AbstractItemType GLASS_PANE() {
        return get(Blocks.GLASS_PANE);
    }

    @Override
    public AbstractItemType STAINED_GLASS_PANE() {
        return get(Blocks.STAINED_GLASS_PANE);
    }

    @Override
    public AbstractItemType EGG() {
        return get(Items.EGG);
    }

    @Override
    public AbstractItemType STAINED_HARDENED_CLAY() {
        return get(Blocks.STAINED_HARDENED_CLAY);
    }

    @Override
    public AbstractItemType CONCRETE() {
        return get(Blocks.CONCRETE);
    }

    @Override
    public AbstractItemType LAPIS_BLOCK() {
        return get(Blocks.LAPIS_BLOCK);
    }

    @Override
    public AbstractItemType REDSTONE_BLOCK() {
        return get(Blocks.REDSTONE_BLOCK);
    }

    @Override
    public AbstractItemType GOLD_BLOCK() {
        return get(Blocks.GOLD_BLOCK);
    }

    @Override
    public AbstractItemType QUARTZ_BLOCK() {
        return get(Blocks.QUARTZ_BLOCK);
    }

    @Override
    public AbstractItemType DYE() {
        return get(Items.DYE);
    }
}
