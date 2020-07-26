package me.fusiondev.fusionpixelmon.api.items;

public abstract class AbstractItems<S, T> {
    private S itemStack;
    private T itemType;

    public S getItemStack() {
        return itemStack;
    }

    public T getItemType() {
        return itemType;
    }
}
