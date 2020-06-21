package io.github.brendoncurmi.fusionpixelmon.api;

import io.github.brendoncurmi.fusionpixelmon.api.inventory.AbstractInventory;

import java.util.UUID;

public abstract class AbstractPlayer {

    public abstract String getName();

    public abstract UUID getUniqueId();

    public abstract void sendMessage(Object message);

    public abstract Object get();

    public abstract void closeInventory();

    //todo need to remove this inventory object
    public abstract void openInventory(AbstractInventory inventory);
}
