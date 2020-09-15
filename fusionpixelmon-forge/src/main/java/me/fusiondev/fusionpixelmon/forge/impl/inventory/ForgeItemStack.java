package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;

public class ForgeItemStack extends AbstractItemStack {

    private final ItemStack ITEMSTACK;

    public ForgeItemStack(ItemStack itemStack) {
        this.ITEMSTACK = itemStack;
    }

    @Override
    public AbstractItemStack from(AbstractItemType itemType) {
        return itemType.to();
    }

    @Override
    public AbstractItemStack setName(String name) {
        ITEMSTACK.setStackDisplayName(name);
        return this;
    }

    @Override
    public List<String> getLore() {
        return null;
    }

    @Override
    public AbstractItemStack setLore(List<String> itemLore) {
        NBTTagList nbtLore = new NBTTagList();
        for (String line : itemLore) {
            if (line != null) {
                nbtLore.appendTag(new NBTTagString(line));
            }
        }
        ITEMSTACK.getOrCreateSubCompound("display").setTag("Lore", nbtLore);
        return this;
    }

    @Override
    public AbstractItemStack setColour(Object colour) {
        if (colour instanceof DyeColor) {
            if (ITEMSTACK.getItem() == Items.DYE
                    || ITEMSTACK.getItem() == Items.BANNER
                    || ITEMSTACK.getItem() == Item.getItemFromBlock(Blocks.STANDING_BANNER)
                    || ITEMSTACK.getItem() == Item.getItemFromBlock(Blocks.WALL_BANNER)) {
                ITEMSTACK.setItemDamage(((DyeColor) colour).getDyeData());
            } else {
                ITEMSTACK.setItemDamage(((DyeColor) colour).getBlockData());
            }
        } else ITEMSTACK.setItemDamage((byte) colour);
        return this;
    }

    @Override
    public AbstractItemStack copy() {
        return new ForgeItemStack(ITEMSTACK.copy());
    }

    @Override
    public Object getRaw() {
        return ITEMSTACK;
    }
}
