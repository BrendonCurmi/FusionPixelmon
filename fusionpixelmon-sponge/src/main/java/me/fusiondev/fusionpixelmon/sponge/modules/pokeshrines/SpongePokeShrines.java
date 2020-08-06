package me.fusiondev.fusionpixelmon.sponge.modules.pokeshrines;

import com.flowpowered.math.vector.Vector3i;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.modules.pokeshrines.PokeShrinesModule;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.common.block.SpongeBlockSnapshot;

import java.util.Map;

public class SpongePokeShrines extends PokeShrinesModule {

    /**
     * The block that the {@link #BLOCKS} will turn into upon valid interaction.
     */
    private static final BlockType EMPTY_BLOCK = BlockTypes.AIR;

    @Listener
    public void onInteractBlockEvent(InteractBlockEvent event) {
        if (!(event.getSource() instanceof Player)) return;
        Player player = (Player) event.getSource();
        Map<EventContextKey<?>, Object> context = event.getContext().asMap();
        ItemStackSnapshot itemStackSnapshot = (ItemStackSnapshot) context.get(EventContextKeys.USED_ITEM);
        if (player.get(Keys.GAME_MODE).orElse(GameModes.NOT_SET) == GameModes.SURVIVAL && itemStackSnapshot != null) {
            String block = event.getTargetBlock().getState().getType().getName();
            if (BLOCKS.containsKey(block) && isAllowedTool(BLOCKS.get(block), itemStackSnapshot.getType().getName())) {
                SpongeBlockSnapshot blockSnapshot = (SpongeBlockSnapshot) context.get(EventContextKeys.BLOCK_HIT);
                if (blockSnapshot.getLocation().isPresent()) {
                    PlayerInventory playerInv = (PlayerInventory) player.getInventory();
                    ItemStack selected = (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType(block.replace("pixelmon:", "")).to().getRaw();
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
