package me.FusionDev.FusionPixelmon.inventory;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class InvItem {
    private ItemStack itemStack;
    private String name;
    private List<Text> itemLore;

    private static final TextColor DEFAULT_LORE_COLOUR = TextColors.GRAY;

    public InvItem(ItemStack itemStack, String name, List<Text> itemLore) {
        this.itemStack = ItemStack.builder().fromItemStack(itemStack).add(Keys.DISPLAY_NAME, Text.of(name)).build();
        this.name = name;
        this.itemLore = itemLore;
    }

    public InvItem(ItemStack itemStack, String name) {
        this(itemStack, name, new ArrayList<>());
    }

    public InvItem(ItemType itemType, String name) {
        this(ItemStack.builder().itemType(itemType).build(), name);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(this.name = name));
    }

    public List<Text> getItemLore() {
        return itemLore;
    }

    public void setItemLore(List<Text> itemLore) {
        this.itemLore = itemLore;
        pushLore();
    }

    public List<Text> getLore() {
        if (itemStack.get(Keys.ITEM_LORE).isPresent())
            return itemStack.get(Keys.ITEM_LORE).get();
        return new ArrayList<>();
    }

    /**
     * Sets the specified lore as the item lore without pushing it to the item.
     * It is important to call {@link #pushLore()} some time after this method,
     * otherwise the lore won't be pushed to the item.
     *
     * @param lore the lore.
     * @return this instance.
     */
    public InvItem setLoreWait(Object... lore) {
        List<Text> itemLore = new ArrayList<>();
        for (Object line : lore) {
            if (line == null) continue;
            else if (line instanceof List)
                for (String l : (List<String>) line) itemLore.add(Text.of(DEFAULT_LORE_COLOUR, l));
            else if (line instanceof String[])
                for (String l : (String[]) line) itemLore.add(Text.of(DEFAULT_LORE_COLOUR, l));
            else itemLore.add(Text.of(DEFAULT_LORE_COLOUR, line));
        }
        this.itemLore = itemLore;
        return this;
    }

    public InvItem setLore(String... lore) {
        List<Text> itemLore = new ArrayList<>();
        for (String line : lore) {
            if (line == null) continue;
            itemLore.add(Text.of(DEFAULT_LORE_COLOUR, line));
        }
        this.itemLore = itemLore;
        pushLore();
        return this;
    }

    public InvItem setLore(Object... lore) {
        setLoreWait(lore);
        pushLore();
        return this;
    }

    public InvItem setLore(List<String> lore) {
        List<Text> itemLore = new ArrayList<>();
        for (String line : lore) itemLore.add(Text.of(DEFAULT_LORE_COLOUR, line));
        this.itemLore = itemLore;
        pushLore();
        return this;
    }

    /**
     * Adds the specified lore as a list to the beginning of the item lore.
     *
     * @param lore the lore to prepend.
     * @return this instance for chaining.todo
     */
    public InvItem prependLore(List<String> lore) {
        List<Text> list = new ArrayList<>();
        for (String line : lore) list.add(Text.of(DEFAULT_LORE_COLOUR, line));
        itemLore.addAll(0, list);
        return this;
    }

    /**
     * Adds the specified lore as an array to the beginning of the item lore.
     *
     * @param lore the lore to prepend.
     * @return this instance.
     */
    public InvItem prependLore(String... lore) {
        List<Text> list = new ArrayList<>();
        for (String line : lore) list.add(Text.of(DEFAULT_LORE_COLOUR, line));
        itemLore.addAll(0, list);
        return this;
    }

    /**
     * Adds the specified lore as a list to the end of the item lore.
     *
     * @param lore the lore to append.
     * @return this instance.
     */
    public InvItem appendLore(List<String> lore) {
        for (String line : lore) itemLore.add(Text.of(DEFAULT_LORE_COLOUR, line));
        return this;
    }

    /**
     * Adds the specified lore as an array to the end of the item lore.
     *
     * @param lore the lore to append.
     * @return this instance.
     */
    public InvItem appendLore(String... lore) {
        for (String line : lore) itemLore.add(Text.of(DEFAULT_LORE_COLOUR, line));
        return this;
    }

    public InvItem copyLoreFrom(InvItem item) {
        /*if (item.itemStack.get(Keys.ITEM_LORE).isPresent()) {
            itemStack.offer(Keys.ITEM_LORE, item.itemStack.get(Keys.ITEM_LORE).get());
        }*/
        if (item.itemLore != null && !item.itemLore.isEmpty()) {
            itemStack.offer(Keys.ITEM_LORE, item.itemLore);
        }
        return this;
    }

    /**
     * Pushes the saved item lore to the item.
     *
     * @return this instance.
     */
    public InvItem pushLore() {
        itemStack.offer(Keys.ITEM_LORE, itemLore);
        return this;
    }

    // todo actual copy this.lore=blalba

    /**
     * Creates a new instance as a copy of the current instance.
     *
     * @return the new copy instance.
     */
    public InvItem copy() {
        return new InvItem(itemStack, name, itemLore);
    }

    /**
     * Creates a new instance as a copy of the current instance,
     * but with a different name.
     *
     * @param name the new name of the copy.
     * @return the new copy instance with the new name.
     */
    public InvItem copy(String name) {
        return new InvItem(itemStack, name, itemLore);
    }

    /**
     * Creates a new instance as a copy of the current instance,
     * but only copies the primary item details. In other words,
     * only copies the item and the name.
     *
     * @return the new copy instance with item and name only.
     */
    public InvItem copyItem() {
        return new InvItem(itemStack, name);
    }

    /**
     * Offers a key-value pair to the item.
     *
     * @param key   the key.
     * @param value the value.
     * @return this instance.
     */
    public InvItem setKey(Key key, Object value) {
        itemStack.offer(key, value);
        return this;
    }
}
