package me.fusiondev.fusionpixelmon.sponge.impl;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public class SpongePlayer extends AbstractPlayer {

    private final Player PLAYER;

    public SpongePlayer(Player player) {
        this.PLAYER = player;
    }

    @Override
    public String getName() {
        return PLAYER.getName();
    }

    @Override
    public UUID getUniqueId() {
        return PLAYER.getUniqueId();
    }

    @Override
    public void sendMessage(Object message) {
        PLAYER.sendMessage((Text) message);
    }

    @Override
    public Player get() {
        return PLAYER;
    }

    @Override
    public void closeInventory() {
        PLAYER.closeInventory();
    }

    @Override
    public void openInventory(AbstractInventory inventory) {
        PLAYER.openInventory((Inventory) inventory.getRaw());
    }
}
