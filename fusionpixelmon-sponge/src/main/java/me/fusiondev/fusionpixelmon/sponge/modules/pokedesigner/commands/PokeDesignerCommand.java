package me.fusiondev.fusionpixelmon.sponge.modules.pokedesigner.commands;

import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.sponge.modules.pokedesigner.ui.SpongeShops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

@NonnullByDefault
public class PokeDesignerCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(Color.RED + "This command can only be executed by a player"));
            return CommandResult.empty();
        }
        Player player = (Player) src;
        Shops shops = new SpongeShops(SpongeAdapter.adapt(player));
        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
        new PokeSelectorUI(SpongeAdapter.adapt(player), config.getPokeSelectorGuiTitle(), "pokeselector", pokemon -> {
            if (!config.containsBlackListedPokemon(pokemon.getSpecies())) {
                shops.launch(pokemon, config.getGuiTitle());
            } else player.sendMessage(Text.of(Color.RED + "That Pokemon cant use the PokeDesigner!"));
        });
        return CommandResult.success();
    }
}
