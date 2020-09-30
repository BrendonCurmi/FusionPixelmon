package me.fusiondev.fusionpixelmon.forge.modules.pokemodifiers.commands;

import mcp.MethodsReturnNonnullByDefault;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.types.BaseModifier;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PokeModifierCommand extends CommandBase {
    @Override
    public String getName() {
        return "pokemodifier";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/pokemodifier <modifier> [player]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be executed by a player unless one is specified"));
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) sender;
        if (args.length > 0) {
            String modifierName = args[0];
            if (PokeModifiers.hasModifier(modifierName, false)) {
                BaseModifier modifier = PokeModifiers.getModifier(modifierName, false);
                AbstractItemStack itemStack = FusionPixelmon.getRegistry()
                        .getPixelmonUtils()
                        .getPixelmonItemStack(modifier.getItemStack())
                        .setName(modifier.getName())
                        .setLore(modifier.getModifyWhat());
                player.addItemStackToInventory((ItemStack) itemStack.getRaw());
                player.sendMessage(new TextComponentString(Color.GREEN + "Given " + modifierName + " modifier!"));
            } else player.sendMessage(new TextComponentString(Color.RED + "That modifier doesn't exist"));
        } else PokeModifiers.iterate(ForgeAdapter.adapt(player));
    }
}
