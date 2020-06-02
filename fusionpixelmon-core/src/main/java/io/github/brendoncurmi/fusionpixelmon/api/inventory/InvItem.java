package io.github.brendoncurmi.fusionpixelmon.api.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.colour.Colour;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemType;

import java.util.ArrayList;
import java.util.List;

public class InvItem {
    private AbstractItemStack itemStack;
    private String name;
    private List<String> itemLore;

    private static final String DEFAULT_LORE_COLOUR = "§" + Colour.GREY.getCode();

    public InvItem(AbstractItemStack itemStack, String name, List<String> itemLore) {
        this.itemStack = itemStack;
        setName(name);
        setLore(itemLore);
    }

    public InvItem(AbstractItemStack itemStack, String name) {
        this(itemStack, name, new ArrayList<>());
    }

    public InvItem(AbstractItemType itemType, String name) {
        this(itemType.to(), name);
    }

    public AbstractItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(AbstractItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
//        itemStack.offer(Keys.DISPLAY_NAME, Text.of(this.name = name));
        getItemStack().setName(name);
    }

    public List<String> getLore() {
        return itemStack.getLore();
    }

    public InvItem setLore(List<String> lore) {
        List<String> itemLore = new ArrayList<>();
        for (String line : lore) itemLore.add(DEFAULT_LORE_COLOUR + line);
        this.itemLore = itemLore;
        pushLore();
        return this;
    }

    public InvItem setLore(String... lore) {
        List<String> itemLore = new ArrayList<>();
        for (String line : lore) {
            if (line == null) continue;
            itemLore.add(DEFAULT_LORE_COLOUR + line);
        }
        this.itemLore = itemLore;
        pushLore();
        return this;
    }

    public InvItem setLore(Object... lore) {
        setLoreWait(lore);//todo check why this is wait - actually is it due to the parsing of object lore?
        pushLore();
        return this;
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
        List<String> itemLore = new ArrayList<>();
        for (Object line : lore) {
            if (line == null) continue;
            else if (line instanceof List)
                for (String l : (List<String>) line) itemLore.add(DEFAULT_LORE_COLOUR + l);
            else if (line instanceof String[])
                for (String l : (String[]) line) itemLore.add(DEFAULT_LORE_COLOUR +l);
            else itemLore.add(DEFAULT_LORE_COLOUR + line.toString());
        }
        this.itemLore = itemLore;
        return this;
    }

    /**
     * Adds the specified lore as a list to the beginning of the item lore.
     *
     * @param lore the lore to prepend.
     * @return this instance for chaining.todo
     */
    public InvItem prependLore(List<String> lore) {
        List<String> list = new ArrayList<>();
        for (String line : lore) list.add(DEFAULT_LORE_COLOUR + line);
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
        List<String> list = new ArrayList<>();
        for (String line : lore) list.add(DEFAULT_LORE_COLOUR + line);
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
        for (String line : lore) itemLore.add(DEFAULT_LORE_COLOUR + line);
        return this;
    }

    /**
     * Adds the specified lore as an array to the end of the item lore.
     *
     * @param lore the lore to append.
     * @return this instance.
     */
    public InvItem appendLore(String... lore) {
        for (String line : lore) itemLore.add(DEFAULT_LORE_COLOUR + line);
        return this;
    }

    /**
     * Pushes the saved item lore to the item, without clearing the empty trailing lines.
     *
     * @return this instance.
     */
    public InvItem pushLore() {
        return pushLore(false);
    }

    /**
     * Pushes the saved item lore to the item, possibly after clearing the empty trailing lines.
     *
     * @param clearTrailing if the empty trailing lines should be cleared.
     * @return this instance
     */
    public InvItem pushLore(boolean clearTrailing) {
        if (clearTrailing) {
            String text;
            for (int i = 0; i < itemLore.size() - 1; i++) {
                text = itemLore.get((itemLore.size() - 1) - i);
                if (text.isEmpty() || text.equals(DEFAULT_LORE_COLOUR)) itemLore.remove((itemLore.size() - 1) - i);
                else break;
            }
        }
        itemStack.setLore(itemLore);
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
    /*public InvItem setKey(Object key, Object value) {
        itemStack.offer(key, value);
        return this;
    }*/
}
