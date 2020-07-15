package me.fusiondev.fusionpixelmon.spigot.impl;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SpigotPlayer extends AbstractPlayer {

    private final Player PLAYER;

    public SpigotPlayer(Player player) {
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
        PLAYER.sendMessage(message.toString());
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
