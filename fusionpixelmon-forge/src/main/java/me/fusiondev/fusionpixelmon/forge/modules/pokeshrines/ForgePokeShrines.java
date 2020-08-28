package me.fusiondev.fusionpixelmon.forge.modules.pokeshrines;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.modules.pokeshrines.PokeShrinesModule;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgePokeShrines extends PokeShrinesModule {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getEntityPlayer().getEntityWorld().isRemote && !event.getEntityPlayer().capabilities.isCreativeMode) {
            ResourceLocation clickedBlock = event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName();
            ResourceLocation item = event.getItemStack().getItem().getRegistryName();
            if (clickedBlock != null && item != null && BLOCKS.containsKey(clickedBlock.toString()) && isAllowedTool(BLOCKS.get(clickedBlock.toString()), item.toString())) {
                if (event.getEntityPlayer().inventory.getFirstEmptyStack() != -1) {
                    ItemStack selected = (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack(clickedBlock.getResourcePath()).getRaw();

                    // Some blocks may have a secondary block to make it bigger, so remove that too
                    event.getWorld().setBlockToAir(event.getPos());

                    BlockPos block2Loc = new BlockPos(event.getPos().getX(), event.getPos().getY() + 1, event.getPos().getZ());
                    ResourceLocation block2Name = event.getWorld().getBlockState(block2Loc).getBlock().getRegistryName();
                    if (block2Name != null && !block2Name.equals(clickedBlock)) {
                        block2Loc = new BlockPos(event.getPos().getX(), event.getPos().getY() - 1, event.getPos().getZ());
                    }
                    if (block2Name != null && block2Name.equals(clickedBlock)) {
                        event.getWorld().setBlockToAir(block2Loc);
                    }

                    event.getEntityPlayer().addItemStackToInventory(selected);
                } else event.getEntityPlayer().sendMessage(new TextComponentString(Color.RED + "Your inventory is full!"));
            }
        }
    }
}
