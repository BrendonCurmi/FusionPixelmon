package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.ui.Event;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import me.fusiondev.fusionpixelmon.spigot.SpigotFusionPixelmon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpigotInvInventory extends InvInventory implements Listener {

    @Override
    public void openPage(AbstractPlayer player, InvPage page) {
        Inventory inventory = Bukkit.createInventory(null, 9 * page.rows, page.title);

        // Add items to inventory
        for (int i = 0; i < inventory.getSize(); i++) {
            if (page.elements.containsKey(i)) {
                inventory.setItem(i, (ItemStack) page.elements.get(i).getItemStack().getRaw());
            } else if (page.backgroundItem != null) {
                inventory.setItem(i, (ItemStack) page.backgroundItem.getItemStack().getRaw());
            }
        }

        AbstractInventory abstractInventory = new SpigotInventory(inventory);
        this.inventory = abstractInventory;
        page.inventory = abstractInventory;
        player.openInventory(this.inventory);
        playerOpened(player, page);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openPages.containsKey(player.getUniqueId())) {
            InvPage invPage = getPlayerOpened(player.getUniqueId());
            event.setCancelled(true);

            invPage.getEventHandler().call(Event.CLICK_INVENTORY, event, SpigotAdapter.adapt(player));

            /*if (invPage.clickInventoryEventListener != null) {
                invPage.clickInventoryEventListener.accept(event);
            }*/

            // Check which slot is clicked and run action if one is defined for the slot
            try {
                int slot = event.getRawSlot();

                if ((invPage.rows * 9) - 1 > slot) {
                    if (invPage.actions.containsKey(slot)) {
                        invPage.actions.get(slot).action(event);
                    }
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            //ItemStack clickedItem = event.getCurrentItem();
            //if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openPages.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            InvPage invPage = getPlayerOpened(player.getUniqueId());
            invPage.getEventHandler().call(Event.INTERACT_INVENTORY, event, SpigotAdapter.adapt(player));
            /*if (invPage.interactInventoryEventListener != null) {
                invPage.interactInventoryEventListener.accept(event);
            }*/
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (openPages.containsKey(player.getUniqueId())) {
            playerClosed(player.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openPages.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private static final int UPDATE_TICK_RATE = 10;

    public static void runUpdater() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SpigotFusionPixelmon.getInstance(), () -> {
            for (InvPage page : openPages.values()) {
                // Execute every open page's runnable, if one is defined
                if (page.runnable != null) {
                    page.runnable.run();
                }

                for (Map.Entry<Integer, InvItem> element : page.elements.entrySet()) {
                    if (element.getValue() != null) {
                        ((Inventory) page.inventory.getRaw()).setItem(element.getKey(), (ItemStack) element.getValue().getItemStack().getRaw());
                        //slot.set((ItemStack) page.elements.get(num).getItemStack().getRaw());
                    }
                }
                /*int num = 0;
                for (final Inventory slot : ((Inventory) page.inventory.getRaw()).slots()) {
                    if (page.elements.get(num) != null) {
                        slot.set((ItemStack) page.elements.get(num).getItemStack().getRaw());
                    }
                    num++;
                }*/
            }
        }, 0L, (long) UPDATE_TICK_RATE);
    }
}
