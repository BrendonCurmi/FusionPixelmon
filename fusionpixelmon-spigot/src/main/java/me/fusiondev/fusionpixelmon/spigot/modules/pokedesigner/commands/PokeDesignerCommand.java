package io.github.brendoncurmi.fusionpixelmon.spigot.modules.pokedesigner.commands;

import io.github.brendoncurmi.fusionpixelmon.spigot.gui.PokeSelectorUI;
import io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PokeDesignerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            ItemStack diamond = new ItemStack(Material.DIAMOND);
            ItemStack bricks = new ItemStack(Material.BRICK);
            bricks.setAmount(20);
            player.getInventory().addItem(bricks, diamond);

            //player.openInventory(new SpigotInvInventory().getInv());

            new PokeSelectorUI(player, "Name", "id", pokemon -> {
                System.out.println(pokemon.getDisplayName());
            });
        }
        return false;
    }
}
