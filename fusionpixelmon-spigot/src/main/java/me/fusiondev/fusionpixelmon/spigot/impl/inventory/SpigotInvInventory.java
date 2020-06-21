package io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvInventory;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.api.ui.Event;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

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

        this.inventory = new SpigotInventory(inventory);
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
}
