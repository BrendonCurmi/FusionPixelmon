package me.fusiondev.fusionpixelmon.sponge.impl.inventory;

import me.fusiondev.fusionpixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.ui.events.Event;
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

import java.util.Optional;

public class SpongeInvInventory extends InvInventory {

    @Override
    public void openPage(AbstractPlayer player, InvPage page) {
        final Inventory inventory = Inventory.builder()
                .property("type", new StringProperty("fusionpixelmon-page"))
                .property("id", new StringProperty(page.id))
                .property(InventoryDimension.PROPERTY_NAME, InventoryDimension.of(9, page.rows))
                .of(InventoryArchetypes.CHEST)
                .listener(InteractInventoryEvent.class, event -> {
                    // Execute InteractInventoryEvent if the page has one defined
                    /*if (page.interactInventoryEventListener != null) {
                        page.interactInventoryEventListener.accept(event);
                    }*/
                    page.getEventHandler().call(Event.INTERACT_INVENTORY, event, player);

                    // Register player has closed inventory upon inventory close (duh)
                    // todo maybe pull this first so that if InteractInventoryEvent doesnt run if InteractInventoryEvent.close
                    if (event instanceof InteractInventoryEvent.Close) {
                        page.getEventHandler().call(Event.CLOSE_INVENTORY, event, player);
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
                    /*if (page.clickInventoryEventListener != null) {
                        page.clickInventoryEventListener.accept(event);
                    }*/
                    page.getEventHandler().call(Event.CLICK_INVENTORY, event, player);

                    // Check which slot is clicked and run action if one is defined for the slot
                    try {
                        int slot = -1;

                        Optional<SlotIndex> slotIndex = event.getTransactions().get(0).getSlot().getInventoryProperty(SlotIndex.class);
                        if (slotIndex.isPresent() && slotIndex.get().getValue() != null) {
                            slot = slotIndex.get().getValue();
                        }

                        InvPage invPage;
                        if ((invPage = getPlayerOpened(player)) != null && invPage.actions.containsKey(slot)) {
                            invPage.actions.get(slot).action(SpongeAdapter.adapt(event));
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                })
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of(page.title)))
                .build(FusionPixelmon.getInstance());

        // Add items to inventory
        int num = 0;
        for (final Inventory slot : inventory.slots()) {
            if (page.elements.containsKey(num)) {
                slot.set(ItemStack.builder()
                        .fromContainer(((ItemStack) page.elements.get(num).getItemStack().getRaw()).toContainer().set(DataQuery.of("UnsafeData", "slotnum"), num))
                        .build());
            } else if (page.backgroundItem != null) {
                slot.set(ItemStack.builder()
                        .fromItemStack((ItemStack) page.backgroundItem.getItemStack().getRaw())
                        .build());
            }
            num++;
        }
        AbstractInventory abstractInventory = new SpongeInventory(inventory);
        this.inventory = abstractInventory;
        page.inventory = abstractInventory;

        // Open page for player
        Task.builder().execute(() -> player.openInventory(this.inventory)).delayTicks(1).submit(FusionPixelmon.getInstance());
        playerOpened(player, page);
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

                // This populates the inventory again, which also handles elements added/updated through runnable
                //todo optimize this
                if (page.dynamicElements != null) {
                    int num = 0;
                    for (final Inventory slot : ((Inventory) page.inventory.getRaw()).slots()) {
                        if (page.dynamicElements.get(num) != null) {
                            slot.set((ItemStack) page.dynamicElements.get(num).getItemStack().getRaw());
                        }
                        num++;
                    }
                }
            }
        }).submit(FusionPixelmon.getInstance());
    }
}
