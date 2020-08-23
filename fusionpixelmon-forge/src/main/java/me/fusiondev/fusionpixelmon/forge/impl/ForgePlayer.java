package me.fusiondev.fusionpixelmon.forge.impl;

import ca.landonjw.gooeylibs.inventory.api.Page;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

public class ForgePlayer extends AbstractPlayer {

    private final EntityPlayerMP PLAYER;

    public ForgePlayer(EntityPlayerMP player) {
        this.PLAYER = player;
    }

    @Override
    public String getName() {
        return PLAYER.getName();
    }

    @Override
    public UUID getUniqueId() {
        return PLAYER.getUniqueID();
    }

    @Override
    public void sendMessage(Object message) {
        if (message instanceof ITextComponent)
            PLAYER.sendMessage((ITextComponent) message);
        else
            PLAYER.sendMessage(new TextComponentString(message.toString()));
    }

    @Override
    public Object get() {
        return PLAYER;
    }

    @Override
    public void closeInventory() {
        PLAYER.closeScreen();
    }

    @Override
    public void openInventory(AbstractInventory inventory) {
        ((Page) inventory.getRaw()).forceOpenPage(PLAYER);
    }

    @Override
    public Object getWorld() {
        return PLAYER.getServerWorld();
    }
}
