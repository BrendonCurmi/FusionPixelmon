package me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.commands;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.ui.SpigotShops;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PokeDesignerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Shops shops = new SpigotShops(SpigotAdapter.adapt(player));
            PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
            new PokeSelectorUI(SpigotAdapter.adapt(player), config.getPokeSelectorGuiTitle(), "pokeselector", pokemon -> {
                if (!config.containsBlackListedPokemon(pokemon.getSpecies())) {
                    shops.launch(pokemon, config.getGuiTitle());
                } else player.sendMessage(Color.RED + "That Pokemon cant use the PokeDesigner!");
            });
        }
        return true;
    }
}
