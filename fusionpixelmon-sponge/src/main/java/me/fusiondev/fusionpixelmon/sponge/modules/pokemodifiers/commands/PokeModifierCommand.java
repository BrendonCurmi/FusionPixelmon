package me.fusiondev.fusionpixelmon.sponge.modules.pokemodifiers.commands;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.BaseModifier;
import me.fusiondev.fusionpixelmon.sponge.SpongeAdapter;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.Optional;

@NonnullByDefault
public class PokeModifierCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(Color.RED + "This command can only be executed by a player"));
            return CommandResult.empty();
        }
        Player player = (Player) src;
        Optional<String> modifierName = args.getOne("modifier");
        if (modifierName.isPresent()) {
            if (PokeModifiers.hasModifier(modifierName.get(), false)) {
                BaseModifier modifier = PokeModifiers.getModifier(modifierName.get(), false);
                AbstractItemStack itemStack = FusionPixelmon.getRegistry()
                        .getPixelmonUtils()
                        .getPixelmonItemStack(modifier.getItemStack())
                        .setName(modifier.getName())
                        .setLore(modifier.getModifyWhat());
                player.getInventory().offer((ItemStack) itemStack.getRaw());
                player.sendMessage(Text.of(Color.GREEN + "Given " + modifierName.get() + " modifier!"));
            } else player.sendMessage(Text.of(Color.RED + "That modifier doesn't exist"));
        } else PokeModifiers.iterate(SpongeAdapter.adapt(player));
        return CommandResult.success();
    }
}
