package me.FusionDev.FusionPixelmon.sponge;

import me.FusionDev.FusionPixelmon.sponge.impl.SpongeInventory;
import me.FusionDev.FusionPixelmon.sponge.impl.SpongePlayer;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeItemStack;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeItemType;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import static com.google.common.base.Preconditions.checkNotNull;

public class SpongeAdapter {

    public static SpongePlayer adapt(Player player) {
        checkNotNull(player);
        return new SpongePlayer(player);
    }

    public static SpongeInventory adapt(Inventory inventory) {
        checkNotNull(inventory);
        return new SpongeInventory(inventory);
    }

    public static SpongeItemStack adapt(ItemStack itemStack) {
        checkNotNull(itemStack);
        return new SpongeItemStack(itemStack);
    }

    public static SpongeItemType adapt(ItemType itemType) {
        checkNotNull(itemType);
        return new SpongeItemType(itemType);
    }

    public static org.spongepowered.api.data.type.DyeColor adapt(DyeColor dyeColor) {
        checkNotNull(dyeColor);
        switch (dyeColor) {
            default:
            case WHITE:
                return DyeColors.WHITE;
            case ORANGE:
                return DyeColors.ORANGE;
            case MAGENTA:
                return DyeColors.MAGENTA;
            case LIGHT_BLUE:
                return DyeColors.LIGHT_BLUE;
            case YELLOW:
                return DyeColors.YELLOW;
            case LIME:
                return DyeColors.LIME;
            case PINK:
                return DyeColors.PINK;
            case GRAY:
                return DyeColors.GRAY;
            case SILVER:
                return DyeColors.SILVER;
            case CYAN:
                return DyeColors.CYAN;
            case PURPLE:
                return DyeColors.PURPLE;
            case BLUE:
                return DyeColors.BLUE;
            case BROWN:
                return DyeColors.BROWN;
            case GREEN:
                return DyeColors.GREEN;
            case RED:
                return DyeColors.RED;
            case BLACK:
                return DyeColors.BLACK;
        }
    }
}
