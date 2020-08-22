package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
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
    public AbstractItemStack copy() {
        return new ForgeItemStack(ITEMSTACK.copy());
    }

    @Override
    public Object getRaw() {
        return ITEMSTACK;
    }
}
