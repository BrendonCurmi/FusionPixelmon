package me.fusiondev.fusionpixelmon.api.inventory;

import me.fusiondev.fusionpixelmon.api.ui.events.AbstractInvEvent;
import me.fusiondev.fusionpixelmon.api.ui.events.EventHandler;

import java.util.HashMap;
import java.util.Map;

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

    /*public InvPage(String title, String id, String parent, int rows) {
        this(title, id);
        this.rows = rows;
    }*/

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

    private EventHandler eventHandler = new EventHandler();

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    boolean isCancellable = true;

    public void setCancellable(boolean cancellable) {
        isCancellable = cancellable;
    }

    public static interface InvAction {
        void action(AbstractInvEvent event);
    }
}
