package me.fusiondev.fusionpixelmon.forge.modules.pokemodifiers;

import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.forge.ForgeAdapter;
import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PokeModifiersListeners {
    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        ItemStack itemStack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
        if (PokeModifiers.hasModifier(itemStack.getDisplayName(), true) && itemStack.getDisplayName().startsWith(Color.GREEN.toString())) {
            EntityPlayerMP player = (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(event.getEntityPlayer().getUniqueID());
            PokeModifiers.getModifier(itemStack.getDisplayName(), true).action(ForgeAdapter.adapt(player), () -> itemStack.setCount(itemStack.getCount() - 1));
        }
    }
}
