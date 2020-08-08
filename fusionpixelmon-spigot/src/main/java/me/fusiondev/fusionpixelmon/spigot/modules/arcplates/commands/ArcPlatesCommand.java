package me.fusiondev.fusionpixelmon.spigot.modules.arcplates.commands;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import me.fusiondev.fusionpixelmon.spigot.modules.arcplates.SpigotArcPlates;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArcPlatesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Color.RED + "This command can only be executed by a player");
            return true;
        }
        Player player = (Player) commandSender;
        AbstractPlayer abstractPlayer = SpigotAdapter.adapt(player);
        new PokeSelectorUI(abstractPlayer, "Arceus Selector", "arceusselector", pokemon -> {
            if (pokemon.getSpecies() == EnumSpecies.Arceus) new SpigotArcPlates().launch(abstractPlayer, pokemon);
            else player.sendMessage(Color.RED + "Please only select an Arceus!");
        });
        return true;
    }
}
