package me.fusiondev.fusionpixelmon.spigot;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
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

    public static org.bukkit.DyeColor adapt(DyeColor dyeColor) {
        checkNotNull(dyeColor);
        switch (dyeColor) {
            default:
            case WHITE:
                return org.bukkit.DyeColor.WHITE;
            case ORANGE:
                return org.bukkit.DyeColor.ORANGE;
            case MAGENTA:
                return org.bukkit.DyeColor.MAGENTA;
            case LIGHT_BLUE:
                return org.bukkit.DyeColor.LIGHT_BLUE;
            case YELLOW:
                return org.bukkit.DyeColor.YELLOW;
            case LIME:
                return org.bukkit.DyeColor.LIME;
            case PINK:
                return org.bukkit.DyeColor.PINK;
            case GRAY:
                return org.bukkit.DyeColor.GRAY;
            case SILVER:
                return org.bukkit.DyeColor.SILVER;
            case CYAN:
                return org.bukkit.DyeColor.CYAN;
            case PURPLE:
                return org.bukkit.DyeColor.PURPLE;
            case BLUE:
                return org.bukkit.DyeColor.BLUE;
            case BROWN:
                return org.bukkit.DyeColor.BROWN;
            case GREEN:
                return org.bukkit.DyeColor.GREEN;
            case RED:
                return org.bukkit.DyeColor.RED;
            case BLACK:
                return org.bukkit.DyeColor.BLACK;
        }
    }
}
