package io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;
import io.github.brendoncurmi.fusionpixelmon.sponge.FusionPixelmon;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.StringProperty;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.*;

/**
 * Provides the ability to create customizable inventory GUI menus and control
 * the flow.
 */
public class InvInventory {

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
    public Inventory inventory;

    public void openPage(AbstractPlayer player, String id) {
        openPage(player, states.get(id));
    }

    public void openPage(AbstractPlayer player, InvPage page) {
        final Inventory inventory = Inventory.builder()
                .property("type", new StringProperty("fusionpixelmon-page"))
                .property("id", new StringProperty(page.id))
                .property(InventoryDimension.PROPERTY_NAME, InventoryDimension.of(9, page.rows))
                .of(InventoryArchetypes.CHEST)
                .listener(InteractInventoryEvent.class, event -> {
                    // Execute InteractInventoryEvent if the page has one defined
                    if (page.interactInventoryEventListener != null) {
                        page.interactInventoryEventListener.accept(event);
                    }

                    // Register player has closed inventory upon inventory close (duh)
                    if (event instanceof InteractInventoryEvent.Close) {
                        playerClosed(player);
                        return;
                    }

                    /*if (!(event instanceof InteractInventoryEvent.Open) && !(event instanceof InteractInventoryEvent.Close)) {
                        if (page.isCancellable) event.setCancelled(true);
                    } else */
                    if (event instanceof InteractInventoryEvent.Open) {
                        event.getCursorTransaction().setCustom(ItemStackSnapshot.NONE);
                        event.getCursorTransaction().setValid(true);
                    }
                })
                .listener(ClickInventoryEvent.class, event -> {
                    event.setCancelled(true);

                    // Execute ClickInventoryEvent if the page has one defined
                    if (page.clickInventoryEventListener != null) {
                        page.clickInventoryEventListener.accept(event);
                    }

                    // Check which slot is clicked and run action if one is defined for the slot
                    try {
                        int slot = -1;

                        Optional<SlotIndex> slotIndex = event.getTransactions().get(0).getSlot().getInventoryProperty(SlotIndex.class);
                        if (slotIndex.isPresent()) {
                            if (slotIndex.get().getValue() != null) {
                                slot = slotIndex.get().getValue();
                            }
                        }

                        InvPage invPage;
                        if ((invPage = getPlayerOpened(player)) != null) {
                            if (invPage.actions.containsKey(slot)) {
                                invPage.actions.get(slot).action(event);
                            }
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                })
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of(page.title)))
                .build(FusionPixelmon.getInstance());
        this.inventory = inventory;

        // Add items to inventory
        int num = 0;
        for (final Inventory slot : inventory.slots()) {
            if (page.elements.containsKey(num)) {
                slot.set(ItemStack.builder()
                        .fromContainer(page.elements.get(num).getItemStack().toContainer().set(DataQuery.of("UnsafeData", "slotnum"), num))
                        .build());
            } else if (page.backgroundItem != null) {
                slot.set(ItemStack.builder()
                        .fromItemStack(page.backgroundItem.getItemStack())
                        .build());
            }
            num++;
        }
        page.inventory = inventory;

        // Open page for player
        Task.builder().execute(() -> {
            player.openInventory(SpongeAdapter.adapt(inventory));
        }).delayTicks(1).submit(FusionPixelmon.getInstance());
        playerOpened(player, page);
    }

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


    /**
     * The number of ticks between each execution.
     */
    private static final int UPDATE_TICK_RATE = 10;

    /**
     * Starts executing the inventory updater task.
     */
    public static void runUpdater() {
        Task.builder().intervalTicks(UPDATE_TICK_RATE).execute(() -> {
            // ticks / updateTickRate gives total number of executions
            // todo but number of executions is not important o track. could make 'ticks' to int and reset once ticks == updateTickRate
            for (InvPage page : openPages.values()) {
                // Execute every open page's runnable, if one is defined
                if (page.runnable != null) {
                    page.runnable.run();
                }

                // todo check this
                int num = 0;
                for (final Inventory slot : page.inventory.slots()) {
                    if (page.elements.get(num) != null) {
                        slot.set(page.elements.get(num).getItemStack());
                    }
                    num++;
                }
            }
        }).submit(FusionPixelmon.getInstance());
    }
}
