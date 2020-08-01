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

    /**
     * The pickaxes that are allowed to validate the interaction.
     */
    private static final Material[] ALLOWED_PICKS = {Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE};

    /**
     * Checks if the specified itemtype is an allowed pickaxe.
     *
     * @param itemType the itemtype.
     * @return true if the item is an allowed pickaxe; false otherwise.
     */
    private boolean isAllowedPickaxe(Material itemType) {
        for (Material pick : ALLOWED_PICKS) if (itemType.equals(pick)) return true;
        return false;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
                && event.getPlayer().getGameMode() == GameMode.SURVIVAL
                && isAllowedPickaxe(event.getMaterial())) {
            for (String block : BLOCKS) {
                block = block.replace(":", "_");
                if (event.getClickedBlock().getType().toString().equalsIgnoreCase(block.replace(":", "_"))) {
                    if (event.getPlayer().getInventory().firstEmpty() != -1) {
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

                        ItemStack selected = (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType(block.replace("pixelmon_", "")).to().getRaw();
                        event.getPlayer().getInventory().addItem(selected);
                    } else event.getPlayer().sendMessage("§" + Colour.RED.getCode() + "Your inventory is full!");
                }
            }
        }
    }
}
