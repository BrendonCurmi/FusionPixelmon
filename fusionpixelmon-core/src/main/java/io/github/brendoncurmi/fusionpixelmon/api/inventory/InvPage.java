package io.github.brendoncurmi.fusionpixelmon.api.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * can only have 1 action/item per slot
 */
public class InvPage {

    public String title;
    public String id;
    public int rows = 4;
    public Map<Integer, InvItem> elements;
    public HashMap<Integer, InvAction> actions;

    public InvItem backgroundItem = null;

    public AbstractInventory inventory;

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

    public Consumer<Object> interactInventoryEventListener;

    public void setInteractInventoryEventListener(Consumer<Object> listener) {
        this.interactInventoryEventListener = listener;
    }


    public Consumer<Object> clickInventoryEventListener;

    public void setClickInventoryEventListener(Consumer<Object> listener) {
        this.clickInventoryEventListener = listener;
    }

    boolean isCancellable = true;

    public void setCancellable(boolean cancellable) {
        isCancellable = cancellable;
    }

    public static interface InvAction {
        void action(Object event);
    }
}
