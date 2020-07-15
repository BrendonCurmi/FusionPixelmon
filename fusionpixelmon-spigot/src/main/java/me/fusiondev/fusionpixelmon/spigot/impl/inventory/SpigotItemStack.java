package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class SpigotItemStack extends AbstractItemStack {

    private final ItemStack ITEMSTACK;

    public SpigotItemStack(ItemStack itemStack) {
        this.ITEMSTACK = itemStack;
    }

    @Override
    public AbstractItemStack from(AbstractItemType itemType) {
        return itemType.to();
    }

    @Override
    public AbstractItemStack setName(String name) {
        // If name is empty, set it to some colour to not be empty, otherwise spigot will revert display name sigh
        meta(meta -> meta.setDisplayName(!name.isEmpty() ? name : ChatColor.BLACK + ""));
        return this;
    }

    @Override
    public List<String> getLore() {
        return Objects.requireNonNull(ITEMSTACK.getItemMeta()).getLore();
    }

    @Override
    public AbstractItemStack setLore(List<String> itemLore) {
        meta(meta -> {
            meta.setLore(itemLore);
        });
        return this;
    }

    @Override
    public AbstractItemStack setColour(Object colour) {
        //Colorable cl = (Colorable) itemStack.getData();
        //cl.setColor((DyeColor) colour);
        //itemStack.setData((MaterialData) cl);
        if (colour instanceof DyeColor) {
            if (ITEMSTACK.getType() == Material.INK_SACK
                    || ITEMSTACK.getType() == Material.BANNER
                    || ITEMSTACK.getType() == Material.STANDING_BANNER
                    || ITEMSTACK.getType() == Material.WALL_BANNER) {
                ITEMSTACK.setDurability(((DyeColor) colour).getDyeData());
            } else {
                ITEMSTACK.setDurability(((DyeColor) colour).getBlockData());
            }
        } else ITEMSTACK.setDurability((byte) colour);
        return this;
    }

    private void meta(Runnable runnable) {
        if (ITEMSTACK.getData().getItemType() == Material.AIR) return;
        ItemMeta meta = ITEMSTACK.getItemMeta();
        runnable.run(meta);
        ITEMSTACK.setItemMeta(meta);
    }

    private interface Runnable {
        void run(ItemMeta meta);
    }

    @Override
    public ItemStack getRaw() {
        return ITEMSTACK;
    }
}
