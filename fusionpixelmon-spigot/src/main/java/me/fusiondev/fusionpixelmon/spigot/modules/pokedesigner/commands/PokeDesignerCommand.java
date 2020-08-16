package me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.commands;

import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.ui.SpigotShops;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PokeDesignerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            ItemStack diamond = new ItemStack(Material.DIAMOND);
            ItemStack bricks = new ItemStack(Material.BRICK);
            bricks.setAmount(20);
            player.getInventory().addItem(bricks, diamond);

            //player.openInventory(new SpigotInvInventory().getInv());

            Shops shops = new SpigotShops(SpigotAdapter.adapt(player));

            new PokeSelectorUI(SpigotAdapter.adapt(player), "Name", "id", pokemon -> {
                shops.launch(pokemon, "Name");
            });
        }
        return true;
    }
}
