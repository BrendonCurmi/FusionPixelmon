package me.fusiondev.fusionpixelmon.forge.modules.arcplates.commands;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import mcp.MethodsReturnNonnullByDefault;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import me.fusiondev.fusionpixelmon.forge.modules.arcplates.ForgeArcPlates;
import me.fusiondev.fusionpixelmon.ui.PokeSelectorUI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ArcPlatesCommand extends CommandBase {
    @Override
    public String getName() {
        return "arc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "fusionpixelmon.command.arc";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(Color.RED + "This command can only be executed by a player"));
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) sender;
        AbstractPlayer abstractPlayer = ForgeAdapter.adapt(player);
        new PokeSelectorUI(abstractPlayer, "Arceus Selector", "arceusselector", pokemon -> {
            if (pokemon.getSpecies() == EnumSpecies.Arceus) new ForgeArcPlates().launch(abstractPlayer, pokemon);
            else abstractPlayer.sendMessage(Color.RED + "Please only select an Arceus!");
        });
    }
}
