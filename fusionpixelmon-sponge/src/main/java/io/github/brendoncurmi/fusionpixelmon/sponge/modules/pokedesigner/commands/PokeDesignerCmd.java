package io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.commands;

import io.github.brendoncurmi.fusionpixelmon.sponge.FusionPixelmon;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import io.github.brendoncurmi.fusionpixelmon.sponge.gui.PokeSelectorUI;
import io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.config.PokeDesignerConfig;
import io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.gui.Shops;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

@NonnullByDefault
public class PokeDesignerCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextColors.RED, "This command can only be executed by a player"));
            return CommandResult.empty();
        }
        Player player = (Player) src;
        Shops shops = new Shops(SpongeAdapter.adapt(player));
        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
        new PokeSelectorUI(player, config.getPokeSelectorGuiTitle(), "pokeselector", pokemon -> {
            if (!config.containsBlackListedPokemon(pokemon.getSpecies())) {
                shops.launch(pokemon, config.getGuiTitle());
            } else player.sendMessage(Text.of(TextColors.RED, "That Pokemon cant use the PokeDesigner!"));
        });
        return CommandResult.success();
    }
}
