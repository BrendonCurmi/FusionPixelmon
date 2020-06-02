package io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        meta(meta -> meta.setDisplayName(name));
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
