package io.github.brendoncurmi.fusionpixelmon.spigot.impl;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.AbstractInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SpigotPlayer extends AbstractPlayer {

    Player player;

    public SpigotPlayer(Player player) {
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
        player.sendMessage(message.toString());
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
        player.openInventory((Inventory) inventory.getRaw());
    }
}
