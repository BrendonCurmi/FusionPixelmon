package io.github.brendoncurmi.fusionpixelmon.sponge;

import io.github.brendoncurmi.fusionpixelmon.sponge.impl.SpongeInventory;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.SpongePlayer;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.SpongeItemStack;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.SpongeItemType;
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
}
