package io.github.brendoncurmi.fusionpixelmon.api.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;

import java.util.*;

/**
 * Provides the ability to create customizable inventory GUI menus and control
 * the flow.
 */
public abstract class InvInventory {

    // todo multipage support, size of page, colours of names/invs, add players to inventory and store / anzi save inventory then assign to player

    /**
     * Stores the ID/object pairs for the different pages in the inventory.
     */
    private final Map<String, InvPage> states;

    public InvInventory() {
        this.states = new HashMap<>();
    }

    /**
     * Adds the specified page to this inventory's list of pages. If a page with
     * the same ID has already been added, it will be overridden with the new page.<br/>
     * This method is only useful if you intend to open the page with its ID instead
     * of the instantiated object, using {@link #openPage(AbstractPlayer, String)}. If you
     * intend on opening the page with the object instead, you can avoid using this.
     * @param pages the list of inventory pages to add.
     */
    public void add(List<InvPage> pages) {
        if (pages == null) throw new InventoryException("There was an error adding the page to the inventory");
        for (InvPage page : pages) add(page);
    }

    /**
     * Adds the specified page to this inventory's list of pages. If a page with
     * the same ID has already been added, it will be overridden with the new page.<br/>
     * This method is only useful if you intend to open the page with its ID instead
     * of the instantiated object, using {@link #openPage(AbstractPlayer, String)}. If you
     * intend on opening the page with the object instead, you can avoid using this.
     * @param page the inventory page to add.
     */
    public void add(InvPage page) {
        if (states == null || page == null || page.id == null || page.id.isEmpty())
            throw new InventoryException("There was an error adding the page to the inventory");
        states.put(page.id, page);
    }

    // todo openPages may not remove previous pages if navigating forward since inventory doesn't close, but maybe all close in the end?
    public AbstractInventory inventory;

    public void openPage(AbstractPlayer player, String id) {
        openPage(player, states.get(id));
    }

    public abstract void openPage(AbstractPlayer player, InvPage page);

    /**
     * Stores every player and their inventory page if they have one open.
     */
    public static HashMap<UUID, InvPage> openPages = new HashMap<>();

    //todo maybe change these to take UUID as an argument, instead of player
    /**
     * Gets the inventory page that the specified player has open.
     * @param player the player to check.
     * @return the page that the player has open; or null if none open.
     */
    public static InvPage getPlayerOpened(AbstractPlayer player) {
        if (player == null) throw new InventoryException("Player can't be null");
        return openPages.get(player.getUniqueId());
    }

    /**
     * Adds the specified player and page to the {@link #openPages} cache.
     * If the player has opened another page already, there is no need to
     * call {@link #playerClosed(AbstractPlayer)} before this again as this method
     * will replace previous opened pages with the new specified one.
     * @param player the player who opened the page.
     * @param invPage the page the player opened.
     */
    public static void playerOpened(AbstractPlayer player, InvPage invPage) {
        openPages.put(player.getUniqueId(), invPage);
    }

    /**
     * Clears the specified player from the {@link #openPages} cache.
     * This is intended to be used when the player closes the inventory.
     * @param player the player closing the inventory.
     */
    public static void playerClosed(AbstractPlayer player) {
        openPages.remove(player.getUniqueId());
    }
}
