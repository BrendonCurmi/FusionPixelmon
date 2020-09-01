package me.fusiondev.fusionpixelmon.forge.modules.pokedesigner.commands;

import mcp.MethodsReturnNonnullByDefault;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import me.fusiondev.fusionpixelmon.forge.modules.pokedesigner.ui.ForgeShops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PokeDesignerCommand extends CommandBase {
    @Override
    public String getName() {
        return "pokedesigner";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "fusionpixelmon.command.pd";
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> alias = new ArrayList<>();
        alias.add("pd");
        return alias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be executed by a player"));
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) sender;
        Shops shops = new ForgeShops(ForgeAdapter.adapt(player));
        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
        new PokeSelectorUI(ForgeAdapter.adapt(player), config.getPokeSelectorGuiTitle(), "pokeselector", pokemon -> {
            if (!config.containsBlackListedPokemon(pokemon.getSpecies())) {
                shops.launch(pokemon, config.getGuiTitle());
            } else player.sendMessage(new TextComponentString(Color.RED + "That Pokemon cant use the PokeDesigner!"));
        });
    }
}
