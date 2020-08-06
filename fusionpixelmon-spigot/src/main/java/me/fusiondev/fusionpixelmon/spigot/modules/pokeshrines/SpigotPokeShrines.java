package me.fusiondev.fusionpixelmon.spigot.modules.pokeshrines;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Colour;
import me.fusiondev.fusionpixelmon.modules.pokeshrines.PokeShrinesModule;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpigotPokeShrines extends PokeShrinesModule implements Listener {

    /**
     * The block that the {@link #BLOCKS} will turn into upon valid interaction.
     */
    private static final Material EMPTY_BLOCK = Material.AIR;

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
                && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            String block = event.getClickedBlock().getType().toString().toLowerCase().replace("pixelmon_", "pixelmon:");
            if (BLOCKS.containsKey(block) && isAllowedTool(BLOCKS.get(block), event.getMaterial().toString())) {
                if (event.getPlayer().getInventory().firstEmpty() != -1) {
                    block = event.getClickedBlock().getType().toString();
                    ItemStack selected = (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType(block.toLowerCase().replace("pixelmon_", "")).to().getRaw();

                    event.getClickedBlock().setType(EMPTY_BLOCK);

                    // Some blocks may have a secondary block to make it bigger, so remove that too
                    Block clickedBlock = event.getClickedBlock();
                    Location block2Loc = new Location(clickedBlock.getWorld(), clickedBlock.getX(), clickedBlock.getY() + 1, clickedBlock.getZ());
                    if (!block2Loc.getBlock().getType().name().equalsIgnoreCase(block)) {
                        block2Loc = new Location(clickedBlock.getWorld(), clickedBlock.getX(), clickedBlock.getY() - 1, clickedBlock.getZ());
                    }
                    if (block2Loc.getBlock().getType().name().equalsIgnoreCase(block)) {
                        block2Loc.getBlock().setType(EMPTY_BLOCK);
                    }

                    event.getPlayer().getInventory().addItem(selected);
                } else event.getPlayer().sendMessage("§" + Colour.RED.getCode() + "Your inventory is full!");
            }
        }
    }
}
