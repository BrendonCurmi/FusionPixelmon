package me.fusiondev.fusionpixelmon.spigot.modules.pokeshrines;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.Colour;
import me.fusiondev.fusionpixelmon.data.PokeShrineData;
import me.fusiondev.fusionpixelmon.modules.pokeshrines.PokeShrinesModule;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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

    private final PokeShrineData DATA;

    public SpigotPokeShrines(PokeShrineData data) {
        this.DATA = data;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
                && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            String block = fix(event.getClickedBlock().getType().toString());
            Player player = event.getPlayer();
            if (BLOCKS.containsKey(block) && isAllowedTool(BLOCKS.get(block), event.getMaterial().toString())) {
                if (player.getInventory().firstEmpty() != -1) {
                    Location blockLoc = getLocation(event.getClickedBlock().getLocation(), block);
                    int x = blockLoc.getBlockX(), y = blockLoc.getBlockY(), z = blockLoc.getBlockZ();
                    if (DATA.isLocked(x, y, z)) {
                        if (!DATA.isLockedBy(x, y, z, player.getUniqueId())) {
                            player.sendMessage("§cThis shrine is locked for pickup by another player");
                            return;
                        }
                        DATA.unlock(x, y, z);
                    }

                    ItemStack selected = (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType(block.toLowerCase().replace("pixelmon:", "")).to().getRaw();

                    event.getClickedBlock().setType(EMPTY_BLOCK);

                    // Some blocks may have a secondary block to make it bigger, so remove that too
                    Block clickedBlock = event.getClickedBlock();
                    Location block2Loc = new Location(clickedBlock.getWorld(), clickedBlock.getX(), clickedBlock.getY() + 1, clickedBlock.getZ());
                    if (!fix(block2Loc.getBlock().getType().name()).equalsIgnoreCase(block)) {
                        block2Loc = new Location(clickedBlock.getWorld(), clickedBlock.getX(), clickedBlock.getY() - 1, clickedBlock.getZ());
                    }
                    if (fix(block2Loc.getBlock().getType().name()).equalsIgnoreCase(block)) {
                        block2Loc.getBlock().setType(EMPTY_BLOCK);
                    }

                    player.getInventory().addItem(selected);
                } else player.sendMessage("§" + Colour.RED.getCode() + "Your inventory is full!");
            } else if (BLOCKS.containsKey(block) && !event.hasItem()) {
                Location blockLoc = getLocation(event.getClickedBlock().getLocation(), block);
                int x = blockLoc.getBlockX(), y = blockLoc.getBlockY(), z = blockLoc.getBlockZ();
                if (DATA.isLocked(x, y, z)) {
                    if (DATA.isLockedBy(x, y, z, player.getUniqueId())) {
                        DATA.unlock(x, y, z);
                        player.sendMessage("§aThis shrine has been unlocked for pickup");
                    } else {
                        player.sendMessage("§cThis shrine is already locked for pickup by another player");
                    }
                } else {
                    DATA.lock(x, y, z, player.getUniqueId());
                    player.sendMessage("§aThis shrine has been locked for pickup");
                }
            }
        }
    }

    /**
     * Gets the lowest possible location for the specified shrine block,
     * as some shrine blocks may have a secondary block to make it bigger.
     *
     * @param location the clicked block location.
     * @param block    the shrine block.
     * @return the lowest location of the shrine block.
     */
    private Location getLocation(Location location, String block) {
        Location blockLoc = location;
        Location block2Loc = new Location(blockLoc.getWorld(), blockLoc.getBlockX(), blockLoc.getBlockY() - 1, blockLoc.getBlockZ());
        if (fix(block2Loc.getBlock().getType().name()).equalsIgnoreCase(block)) {
            blockLoc = block2Loc;
        }
        return blockLoc;
    }

    private static String fix(String s) {
        return s.toLowerCase().replace("pixelmon_", "pixelmon:");
    }
}
