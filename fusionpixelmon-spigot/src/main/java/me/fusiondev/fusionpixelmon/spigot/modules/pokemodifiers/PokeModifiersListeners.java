package me.fusiondev.fusionpixelmon.spigot.modules.pokemodifiers;

import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PokeModifiersListeners implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.hasItemMeta()) return;
        String name = itemStack.getItemMeta().getDisplayName();
        if (event.getHand() == EquipmentSlot.HAND && name != null && !name.isEmpty() && PokeModifiers.hasModifier(name, true) && name.startsWith(Color.GREEN.toString())) {
            PokeModifiers.getModifier(name, true).action(SpigotAdapter.adapt(player), () -> {
                if (itemStack.getAmount() > 1)
                    itemStack.setAmount(itemStack.getAmount() - 1);
                else
                    player.getInventory().remove(itemStack);
            });
        }
    }
}
