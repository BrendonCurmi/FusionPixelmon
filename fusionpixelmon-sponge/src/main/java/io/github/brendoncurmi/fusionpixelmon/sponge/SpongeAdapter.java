package io.github.brendoncurmi.fusionpixelmon.sponge;

import io.github.brendoncurmi.fusionpixelmon.sponge.impl.SpongeInventory;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.SpongePlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;

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
}
