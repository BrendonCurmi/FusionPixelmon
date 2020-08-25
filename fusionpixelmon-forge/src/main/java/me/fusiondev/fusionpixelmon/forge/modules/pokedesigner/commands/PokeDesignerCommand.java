package me.fusiondev.fusionpixelmon.forge.modules.pokedesigner.commands;

import ca.landonjw.gooeylibs.inventory.api.Button;
import ca.landonjw.gooeylibs.inventory.api.Page;
import ca.landonjw.gooeylibs.inventory.api.Template;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import me.fusiondev.fusionpixelmon.forge.modules.pokedesigner.ui.ForgeShops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be executed by a player"));
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) sender;

        Shops shops = new ForgeShops(ForgeAdapter.adapt(player));
/*        //PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
        new PokeSelectorUI(ForgeAdapter.adapt(player), config.getPokeSelectorGuiTitle(), "pokeselector", pokemon -> {
            if (!config.containsBlackListedPokemon(pokemon.getSpecies())) {
                shops.launch(pokemon, config.getGuiTitle());
            } else player.sendMessage(Text.of(Color.RED + "That Pokemon cant use the PokeDesigner!"));
        });*/

        //PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
        new PokeSelectorUI(ForgeAdapter.adapt(player), "title", "pokeselector", pokemon -> {
            player.sendMessage(new TextComponentString("SELECTED POKEMAN"));
            shops.launch(pokemon, "gui title");
//            if (!config.containsBlackListedPokemon(pokemon.getSpecies())) {
//            } else player.sendMessage(new TextComponentString(Color.RED + "That Pokemon cant use the PokeDesigner!"));
        });
    }
}
