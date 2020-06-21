package io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemType;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SpigotInvItem extends InvItem {
    public SpigotInvItem(AbstractItemStack itemStack, String name, List<String> itemLore) {
        super(itemStack, name, itemLore);
    }

    public SpigotInvItem(AbstractItemStack itemStack, String name) {
        super(itemStack, name);
    }

    public SpigotInvItem(AbstractItemType itemType, String name) {
        super(itemType, name);
    }

    public SpigotInvItem(ItemStack itemStack, String name, List<String> itemLore) {
        super(SpigotAdapter.adapt(itemStack), name, itemLore);
    }

    public SpigotInvItem(ItemStack itemStack, String name) {
        super(SpigotAdapter.adapt(itemStack), name);
    }

    public SpigotInvItem(Material itemType, String name) {
        super(SpigotAdapter.adapt(itemType), name);
    }
}
