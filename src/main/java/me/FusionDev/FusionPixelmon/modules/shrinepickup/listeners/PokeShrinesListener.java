package me.FusionDev.FusionPixelmon.modules.shrinepickup.listeners;

import com.flowpowered.math.vector.Vector3i;
import me.FusionDev.FusionPixelmon.FusionPixelmon;
import me.FusionDev.FusionPixelmon.api.pixelmon.PixelmonAPI;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.common.block.SpongeBlockSnapshot;

import java.util.List;
import java.util.Map;

/**
 * Handles the feature which allows the "breaking" and obtaining of the
 * shrines, altars, and chalices that naturally spawn throughout the world
 * of Pixelmon, since they are made to be unbreakable.<br/>
 * If the player interacts with one of these blocks while holding any of
 * the {@link #ALLOWED_PICKS allowed pickaxes} whilst in Survival Mode (and
 * with inventory space), then the block will drop into the player's inventory.
 */
public class PokeShrinesListener {

    /**
     * The blocks which can be obtained using this method.
     */
    private static final List<String> BLOCKS = FusionPixelmon.getInstance().getConfig().getPickableShrines();

    /**
     * The block that the {@link #BLOCKS} will turn into upon valid interaction.
     */
    private static final BlockType EMPTY_BLOCK = BlockTypes.AIR;

    /**
     * The pickaxes that are allowed to validate the interaction.
     */
    private static final ItemType[] ALLOWED_PICKS = {ItemTypes.IRON_PICKAXE, ItemTypes.DIAMOND_PICKAXE};

    /**
     * Checks if the specified itemtype is an allowed pickaxe.
     *
     * @param itemType the itemtype.
     * @return true if the item is an allowed pickaxe; false otherwise.
     */
    private boolean isAllowedPickaxe(ItemType itemType) {
        for (ItemType pick : ALLOWED_PICKS) if (itemType == pick) return true;
        return false;
    }

    @Listener
    public void onInteractBlockEvent(InteractBlockEvent event) {
        for (String block : BLOCKS) {
            if (event.getTargetBlock().getState().getType().getName().equals(block)) {
                Player player = (Player) event.getSource();
                Map<EventContextKey<?>, Object> context = event.getContext().asMap();
                ItemStackSnapshot itemStackSnapshot = (ItemStackSnapshot) context.get(EventContextKeys.USED_ITEM);
                if (player.get(Keys.GAME_MODE).orElse(GameModes.NOT_SET) == GameModes.SURVIVAL && itemStackSnapshot != null && isAllowedPickaxe(itemStackSnapshot.getType())) {
                    SpongeBlockSnapshot blockSnapshot = (SpongeBlockSnapshot) context.get(EventContextKeys.BLOCK_HIT);
                    if (blockSnapshot.getLocation().isPresent()) {
                        PlayerInventory playerInv = (PlayerInventory) player.getInventory();
                        ItemType type = PixelmonAPI.getPixelmonItemType(block.replace("pixelmon:", ""));
                        ItemStack selected = ItemStack.builder().itemType(type).build();
                        if (playerInv.getMain().canFit(selected)) {
                            Location<World> blockLoc = blockSnapshot.getLocation().get();
                            blockLoc.setBlockType(EMPTY_BLOCK);

                            // Some blocks may have a secondary block to make it bigger, so remove that too
                            Location<World> block2Loc = blockLoc.setBlockPosition(new Vector3i(blockLoc.getBlockX(), blockLoc.getBlockY() + 1, blockLoc.getBlockZ()));
                            if (!block2Loc.getBlockType().getName().equals(block)) {
                                block2Loc = blockLoc.setBlockPosition(new Vector3i(blockLoc.getBlockX(), blockLoc.getBlockY() - 1, blockLoc.getBlockZ()));
                            }
                            if (block2Loc.getBlockType().getName().equals(block)) {
                                block2Loc.setBlockType(EMPTY_BLOCK);
                            }

                            playerInv.offer(selected);
                        } else player.sendMessage(Text.of(TextColors.RED, "Your inventory is full!"));
                    }
                }
            }
        }
    }
}
