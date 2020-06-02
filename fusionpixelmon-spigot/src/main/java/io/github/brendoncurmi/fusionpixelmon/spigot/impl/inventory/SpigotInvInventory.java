package io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.AbstractPlayer;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvInventory;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

public class SpigotInvInventory extends InvInventory implements Listener {
    Inventory inv;

    public Inventory getInv() {
        return inv;
    }

    public SpigotInvInventory() {
        inv = Bukkit.createInventory(null, 9, "Example");
    }

    @Override
    public void openPage(AbstractPlayer player, InvPage page) {

        Inventory inventory = Bukkit.createInventory(null, 9 * page.rows, page.title);
        inv = inventory;
//        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "Example Sword", "§aFirst line of the lore", "§bSecond line of the lore"));
//        inv.addItem(createGuiItem(Material.IRON_HELMET, "§bExample Helmet", "§aFirst line of the lore", "§bSecond line of the lore"));


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
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));


        item.setItemMeta(meta);

        return item;
    }


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();


        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }
}
