package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Objects;

public class SpigotItemStack extends AbstractItemStack {

    ItemStack itemStack;

    public SpigotItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public AbstractItemStack from(AbstractItemType itemType) {
        return itemType.to();
    }

    @Override
    public void setName(String name) {
        // If name is empty, set it to some colour to not be empty, otherwise spigot will revert display name sigh
        meta(meta -> meta.setDisplayName(!name.isEmpty() ? name : ChatColor.BLACK + ""));
    }

    @Override
    public List<String> getLore() {
        return Objects.requireNonNull(itemStack.getItemMeta()).getLore();
    }

    @Override
    public void setLore(List<String> itemLore) {
        meta(meta -> {
            meta.setLore(itemLore);
        });
    }

    @Override
    public void setColour(Object colour) {
        Colorable cl = (Colorable) itemStack.getData();
        cl.setColor((DyeColor) colour);
        itemStack.setData((MaterialData) cl);
    }

    private void meta(Runnable runnable) {
        ItemMeta meta = itemStack.getItemMeta();
        runnable.run(meta);
        itemStack.setItemMeta(meta);
    }

    private interface Runnable {
        void run(ItemMeta meta);
    }

    @Override
    public ItemStack getRaw() {
        return itemStack;
    }
}
