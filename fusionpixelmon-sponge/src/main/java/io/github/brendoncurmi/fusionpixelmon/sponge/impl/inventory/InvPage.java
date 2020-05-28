package io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * can only have 1 action/item per slot
 */
public class InvPage {

    public String title;
    public String id;
    int rows = 4;
    public Map<Integer, InvItem> elements;
    public HashMap<Integer, InvAction> actions;

    public InvItem backgroundItem = null;

    public Inventory inventory;

    public Runnable runnable;

    public InvPage(String title, String id) {
        this.title = title;
        this.id = id;
        elements = new HashMap<>();
        actions = new HashMap<>();
    }

    public InvPage(String title, String id, int rows) {
        this(title, id);
        this.rows = rows;
    }

    public InvPage(String title, String id, String parent, int rows) {
        this(title, id);
        this.rows = rows;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setItem(int slot, InvItem element) {
        setItem(slot, element, null);
    }

//    public void setItem(int slot, InvItem element) {
//        elements.put(slot, element);
//    }

    public void setItem(int slot, InvItem element, InvAction invAction) {
        elements.put(slot, element);
        if (invAction != null) actions.put(slot, invAction);
    }

    public void setItemRange(int slotFrom, int slotTo, InvItem element) {
        if (slotFrom > slotTo) {
            // todo bad exception!
            return;
        }
        for (int slot = slotFrom; slot <= slotTo; slot++) {
            elements.put(slot, element);
        }
    }

    public void setBackground(InvItem backgroundItem) {
        this.backgroundItem = backgroundItem;
    }

    public Consumer<Event> interactInventoryEventListener;

    public void setInteractInventoryEventListener(Consumer<Event> listener) {
        this.interactInventoryEventListener = listener;
    }


    public Consumer<Event> clickInventoryEventListener;

    public void setClickInventoryEventListener(Consumer<Event> listener) {
        this.clickInventoryEventListener = listener;
    }

    boolean isCancellable = true;

    public void setCancellable(boolean cancellable) {
        isCancellable = cancellable;
    }

    public static interface InvAction {
        void action(ClickInventoryEvent event);
    }
}
