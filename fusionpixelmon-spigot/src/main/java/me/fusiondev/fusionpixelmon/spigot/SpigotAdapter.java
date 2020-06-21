package me.fusiondev.fusionpixelmon.spigot;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.spigot.impl.SpigotPlayer;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotInventory;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotItemStack;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotItemType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.google.common.base.Preconditions.checkNotNull;

public class SpigotAdapter {


    public static AbstractPlayer adapt(Player player) {
        checkNotNull(player);
        return new SpigotPlayer(player);
    }

    public static SpigotInventory adapt(Inventory inventory) {
        checkNotNull(inventory);
        return new SpigotInventory(inventory);
    }

    public static AbstractItemStack adapt(ItemStack itemStack) {
        checkNotNull(itemStack);
        return new SpigotItemStack(itemStack);
    }
    public static AbstractItemType adapt(Material material) {
        checkNotNull(material);
        return new SpigotItemType(material);
    }

}
