package io.github.brendoncurmi.fusionpixelmon.sponge.impl;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.AbstractInventory;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public class SpongePlayer extends AbstractPlayer {

    Player player;

    public SpongePlayer(Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public void sendMessage(Object message) {
        player.sendMessage((Text) message);
    }

    @Override
    public Player get() {
        return player;
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void openInventory(AbstractInventory inventory) {
        player.openInventory((Inventory) inventory.get());
    }
}
