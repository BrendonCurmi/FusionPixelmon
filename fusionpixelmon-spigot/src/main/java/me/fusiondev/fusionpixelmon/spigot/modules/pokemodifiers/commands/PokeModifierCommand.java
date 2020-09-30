package me.fusiondev.fusionpixelmon.spigot.modules.pokemodifiers.commands;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.BaseModifier;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PokeModifierCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 2 && !(commandSender instanceof Player)) {
            commandSender.sendMessage(Color.RED + "This command can only be executed by a player unless one is specified");
            return true;
        }
        Player player = (strings.length == 2) ? Bukkit.getPlayer(strings[1]) : (Player) commandSender;
        if (strings.length > 0) {
            String modifierName = strings[0];
            if (PokeModifiers.hasModifier(modifierName, false)) {
                BaseModifier modifier = PokeModifiers.getModifier(modifierName, false);
                AbstractItemStack itemStack = FusionPixelmon.getRegistry()
                        .getPixelmonUtils()
                        .getPixelmonItemStack(modifier.getItemStack())
                        .setName(modifier.getName())
                        .setLore(modifier.getModifyWhat());
                player.getInventory().addItem((ItemStack) itemStack.getRaw());
                player.sendMessage(Color.GREEN + "Given " + modifierName + " modifier!");
            } else player.sendMessage(Color.RED + "That modifier doesn't exist");
        } else PokeModifiers.iterate(SpigotAdapter.adapt(player));

        return true;
    }
}
