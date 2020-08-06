package me.FusionDev.FusionPixelmon.data;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;

import java.io.Serializable;

/**
 * Stores the 17 {@link SerializableItem SerializableItems} to represent
 * the 17 different Type Plates Arceus can use.
 */
public class ArcStorageData implements Serializable {
    private static final long serialVersionUID = 5320075889998129568L;

    private SerializableItem[] items = new SerializableItem[17];

    public SerializableItem get(int i) {
        return items[i];
    }

    public void set(int i, ItemStack itemStack) {
        items[i] = (itemStack != null) ? new SerializableItem(itemStack) : null;
    }

    public static class SerializableItem implements Serializable {
        private static final long serialVersionUID = 1295351933776907588L;
        String name;

        SerializableItem(ItemStack itemStack) {
            itemStack.getValue(Keys.DISPLAY_NAME).ifPresent(textValue -> this.name = textValue.get().toPlain());
        }
    }
}
