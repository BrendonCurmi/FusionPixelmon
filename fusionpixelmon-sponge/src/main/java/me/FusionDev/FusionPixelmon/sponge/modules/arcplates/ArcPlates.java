package me.fusiondev.fusionpixelmon.sponge.modules.arcplates;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.pixelmon.ArcPlates.Plate;
import me.fusiondev.fusionpixelmon.api.ui.events.Event;
import me.fusiondev.fusionpixelmon.data.ArcPlateData;
import me.fusiondev.fusionpixelmon.impl.Grammar;
import me.fusiondev.fusionpixelmon.impl.TimeUtil;
import me.fusiondev.fusionpixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.sponge.SpongeFusionPixelmon;
import me.fusiondev.fusionpixelmon.sponge.impl.inventory.SpongeInvInventory;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Objects;
import java.util.Optional;

/**
 * An interface which provides a method of storing and quick switching between
 * Arceus Plates to change Arceus' type and form.
 * The data is persisted by serialising to [config]/arcplates.
 * Supports multiple Players and multiple separate Arceus pokemon per player.
 */
public class ArcPlates {

    private static final int ROWS = 5;
    private static final int[] BACKGROUND_SLOTS = {0, 1, 9, 10, 19, 27, 28, 36, 37};

    // Cooldown variable to prevent duping
    private boolean enabled = true;

    /**
     * Launches the Arc Plates Storage interface for the specified
     * Player and Pokemon. Only an Arceus Pokemon should be passed
     * as this feature is irrelevant for other Pokemon.
     *
     * @param player  the player.
     * @param pokemon the Arceus pokemon.
     */
    public void launch(Player player, Pokemon pokemon) {
        if (pokemon.getSpecies() != EnumSpecies.Arceus) return;

        ArcPlateData data = new ArcPlateData(SpongeFusionPixelmon.getInstance().getConfigDir().resolve("arcplates").toFile(), pokemon.getUUID());

        InvPage page = new InvPage("§8Arc Plates", "arcplates", ROWS);

        // Save data to file upon closing GUI
        page.getEventHandler().add(Event.CLOSE_INVENTORY, (event, player1) -> data.save());

        // Handle inventory action
        page.getEventHandler().add(Event.CLICK_INVENTORY, (event1, player1) -> {
            ClickInventoryEvent event = (ClickInventoryEvent) event1;
            if (event instanceof ClickInventoryEvent.Shift) {
                player.sendMessage(Text.of(TextColors.RED, "Shifting in this inventory is not a function"));
                return;
            }

            // If clicked outside bounds
            if (event.getTransactions().isEmpty()) return;

            ItemStack selected = event.getTransactions().get(0).getOriginal().createStack();

            Optional<SlotIndex> slotIndex = event.getTransactions().get(0).getSlot().getInventoryProperty(SlotIndex.class);
            if (slotIndex.isPresent() && slotIndex.get().getValue() != null) {
                // When shift clicking, seems slot is taken as the value of the slot where the item ends up, not
                // where clicked. Hence disabling shift clicking in the inventory.
                int slot = slotIndex.get().getValue();

                // In Player Inventory
                if (slot > (ROWS * 9) - 1 && selected.getType() instanceof ItemPlate) {
                    new Thread(() -> {
                        // When the item is clicked, it isn't found in the inventory as it would be on the cursor
                        // even with setCancelled(true). So wait until the item is returned to the inventory upon
                        // cancellation, and then remove it.
                        Inventory openInv;
                        Optional<ItemStack> itemStack;
                        do {
                            // todo might not work if player quickly closes inventory. can do ifPresent() and remove plate update due to no payment
                            openInv = player.getOpenInventory().get().query(SlotIndex.of(slot));
                            itemStack = openInv.first().peek(1);
                        } while (!itemStack.isPresent());
                        if (checkItem(itemStack.get(), data)) {
                            openInv.first().poll(1);
                        }
                    }, player.getName() + "'s " + slot + pokemon.getUUID()).start();
                }
                // In GUI Page
                else if (selected.getType() instanceof ItemPlate) {
                    ItemPlate selectedItemPlate = (ItemPlate) selected.getType();

                    if (enabled) {
                        // Delay to prevent duping
                        enabled = false;
                        TimeUtil.setTimeout(() -> enabled = true, 900);

                        // Left clicking plate in GUI
                        if (event instanceof ClickInventoryEvent.Primary) {
                            /*
                             * Give the pokemon the plate that is left clicked in the GUI.
                             * If the pokemon is already holding a plate (but different type), put it in storage and give
                             * the pokemon the new clicked plate.
                             * If the pokemon is already holding a plate but there is another one of the same type in
                             * storage, do nothing as player must remove the one in storage first.
                             * If the pokemon is already holding something which isnt a plate, do nothing as player must
                             * remove that first.
                             */
                            if (pokemon.getHeldItemAsItemHeld() instanceof NoItem || pokemon.getHeldItemAsItemHeld() instanceof ItemPlate) {
                                if (pokemon.getHeldItemAsItemHeld() instanceof ItemPlate) {
                                    ItemPlate heldItemPlate = (ItemPlate) pokemon.getHeldItemAsItemHeld();
                                    if (selectedItemPlate == heldItemPlate) {
                                        player.sendMessage(Text.of(TextColors.RED, "That Plate is already equipped!"));
                                        return;
                                    } else {
                                        ItemType heldItemType = getType(Objects.requireNonNull(heldItemPlate.getRegistryName()));
                                        for (Plate p : Plate.values()) {
                                            if (getType(Objects.requireNonNull(p.plate.getItem().getRegistryName())) == heldItemType) {
                                                if (!data.hasPlate(p.i)) {
                                                    data.add(p.i);
                                                    break;
                                                } else {
                                                    player.sendMessage(Text.of(TextColors.RED, "Cant unequip " + Grammar.cap(p.name()) + " Plate because there is another in Storage! Please remove the one in Storage first before unequiping."));
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                                pokemon.setHeldItem(new net.minecraft.item.ItemStack(selectedItemPlate));
                                data.remove(getIDFromSlot(slot));
                                player.sendMessage(Text.of(TextColors.GREEN, "Plate equipped!"));
                            } else
                                player.sendMessage(Text.of(TextColors.RED, "Cannot equip Plate because Pokemon is currently holding something!"));
                        }
                        // Right clicking plate in GUI
                        else if (event instanceof ClickInventoryEvent.Secondary) {
                            /*
                             * Take the plate that is right clicked in the GUI and give it to the player if there is free
                             * inventory space.
                             */
                            PlayerInventory playerInv = (PlayerInventory) player.getInventory();
                            if (playerInv.getMainGrid().canFit(selected)) {
                                player.getInventory().offer(selected);
                                data.remove(getIDFromSlot(slot));
                            } else player.sendMessage(Text.of(TextColors.RED, "Your inventory is full!"));
                        }
                    }
                }
            }
        });

        // GUI page runnable task
        page.setRunnable(() -> {
            for (Plate plate : Plate.values()) {
                ItemStack stack;
                String name = "";
                if (!data.hasPlate(plate.i)) {
                    stack = ItemStack.builder().itemType((ItemType) plate.type.getRaw()).build();
                    if (plate.colour != null) stack.offer(Keys.DYE_COLOR, SpongeAdapter.adapt(plate.colour));
                } else {
                    ItemPlate plateItem = (ItemPlate) plate.plate.getItem();
                    stack = ItemStack.builder().itemType(getType(Objects.requireNonNull(plateItem.getRegistryName()))).build();
                    name = "§a" + Grammar.cap(plate.name()) + " Plate";
                }
                page.setItem(plate.slot, new InvItem(SpongeAdapter.adapt(stack), name));
            }
        });

        // Create the GUI
        InvItem infoItem = new InvItem(SpongeAdapter.adapt(ItemTypes.PAPER), "§b§lStorage Info").setLore(
                "",
                "In Storage:",
                "  Left Click: §aEquip Plate",
                "  Right Click: §aRemove Plate from Storage",
                "",
                "In Inventory:",
                "  Left Click: §aAdd Plate to Storage"
        );
        page.setItem(18, infoItem);

        ItemStack backgroundStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        backgroundStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
        InvItem backgroundItem = new InvItem(SpongeAdapter.adapt(backgroundStack), "");
        for (int backSlot : BACKGROUND_SLOTS) page.setItem(backSlot, backgroundItem);

        new SpongeInvInventory().openPage(SpongeAdapter.adapt(player), page);
    }

    /**
     * Checks if the specified item is a plate that can be
     * added to the data.<br/>
     * The item is successful if the specified item is a plate
     * which isn't already in the data.<br/>
     * The item is unsuccessful if the specified item is not a
     * plate or if it is a plate but already exists in the data.
     *
     * @param item the selected item.
     * @param data the current data stack.
     * @return true if the item is a plate and doesn't exist in
     * the data; false if the item isn't a plate or if it's a
     * plate but already in the data.
     */
    private boolean checkItem(ItemStack item, ArcPlateData data) {
        for (Plate p : Plate.values()) {
            if (getType(Objects.requireNonNull(p.plate.getItem().getRegistryName())) == item.getType()) {
                if (data.hasPlate(p.i)) return false;
                data.add(p.i);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the plate ID from the slot where the plate is.
     *
     * @param slot the current slot location.
     * @return the ID of the plate at the specified slot.
     */
    private int getIDFromSlot(int slot) {
        // todo plate slots are in ascending order, can break out of loop with plates.slot > slot if not found
        for (Plate plate : Plate.values()) if (plate.slot == slot) return plate.i;
        return -1;
    }

    /**
     * Gets the Sponge {@link ItemType} from the specified item id.
     * This is intended for use in converting from a modded item.
     *
     * @param name the registry item name.
     * @return the Sponge {@link ItemType}.
     */
    private ItemType getType(ResourceLocation name) {
        return Sponge.getRegistry().getType(ItemType.class, name.toString()).orElse(ItemTypes.AIR);
    }
}
