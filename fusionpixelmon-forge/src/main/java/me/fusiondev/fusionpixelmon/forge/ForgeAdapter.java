package me.fusiondev.fusionpixelmon.forge;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.forge.impl.ForgePlayer;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeItemStack;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeItemType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.google.common.base.Preconditions.checkNotNull;

public class ForgeAdapter {
    public static AbstractPlayer adapt(EntityPlayerMP player) {
        checkNotNull(player);
        return new ForgePlayer(player);
    }

    public static AbstractItemStack adapt(ItemStack itemStack) {
        checkNotNull(itemStack);
        return new ForgeItemStack(itemStack);
    }

    public static AbstractItemType adapt(Item itemStack) {
        checkNotNull(itemStack);
        return new ForgeItemType(itemStack);
    }
}
