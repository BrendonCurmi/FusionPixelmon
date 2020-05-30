package io.github.brendoncurmi.fusionpixelmon.api.items;

public abstract class AbstractItems<S, T> {
    S itemStack;
    T itemType;

    public S getItemStack() {
        return itemStack;
    }

    public T getItemType() {
        return itemType;
    }
}
