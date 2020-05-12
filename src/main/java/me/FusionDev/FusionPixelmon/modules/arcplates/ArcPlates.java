package me.FusionDev.FusionPixelmon.modules.arcplates;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import me.FusionDev.FusionPixelmon.api.data.FileFactory;
import me.FusionDev.FusionPixelmon.FusionPixelmon;
import me.FusionDev.FusionPixelmon.util.Grammar;
import me.FusionDev.FusionPixelmon.util.TimeUtil;
import me.FusionDev.FusionPixelmon.data.ArcStorageData;
import me.FusionDev.FusionPixelmon.api.inventory.InvInventory;
import me.FusionDev.FusionPixelmon.api.inventory.InvItem;
import me.FusionDev.FusionPixelmon.api.inventory.InvPage;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * An interface which provides a method of storing and quick switching between
 * Arceus Plates to change Arceus' type and form.
 * The data is persisted by serialising to {@link #path [config]/arcplates}.
 * Supports multiple Players and multiple separate Arceus pokemon per player.
 */
public class ArcPlates {

    /**
     * The config path to the serialised data files.
     */
    private static Path path = FusionPixelmon.getInstance().configDir.resolve("arcplates");

    private static final int ROWS = 5;
    private static final int[] BACKGROUND_SLOTS = {0, 1, 9, 10, 19, 27, 28, 36, 37};

    // Cooldown variable to prevent duping
    private boolean enabled = true;

    /**
     * Launches the Arc Plates Storage interface for the specified
     * Player and Pokemon. Only an Arceus Pokemon should be passed
     * as this feature is irrelevant for other Pokemon.
     * @param player the player.
     * @param pokemon the Arceus pokemon.
     */
    public void launch(Player player, Pokemon pokemon) {
        if (pokemon.getSpecies() != EnumSpecies.Arceus) return;

        File dataFile = new File(path.toFile(), player.getUniqueId() + " " + pokemon.getUUID().toString());
        FileFactory factory = new FileFactory();
        ArcStorageData data = dataFile.exists() ? (ArcStorageData) factory.deserialize(dataFile.getAbsolutePath()) : new ArcStorageData();

        InvPage page = new InvPage("§8Arc Plates", "arcplates", ROWS);

        // Save data to file upon closing GUI
        page.setInteractInventoryEventListener(event -> {
            if (event instanceof InteractInventoryEvent.Close) {
                if (!dataFile.exists()) {
                    try {
                        boolean created = dataFile.getParentFile().mkdirs() && dataFile.createNewFile();
                        if (!created && !dataFile.exists())
                            throw new FileNotFoundException("File " + dataFile.getAbsolutePath() + " could not be found or created!");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                factory.serialize(data, dataFile.getAbsolutePath());
            }
        });

        // Handle inventory action
        page.setClickInventoryEventListener(event1 -> {
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
                                                if (data.get(p.i) == null) {
                                                    data.set(p.i, ItemStack.builder().itemType(heldItemType).build());
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
                                data.set(getIDFromSlot(slot), null);
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
                                data.set(getIDFromSlot(slot), null);
                            } else player.sendMessage(Text.of(TextColors.RED, "Your inventory is full!"));
                        }
                    }
                }
            }
        });

        // GUI page runnable task
        page.setRunnable(() -> {
            if (data == null) return;
            for (Plate plate : Plate.values()) {
                ItemStack stack;
                String name = "";
                if (data.get(plate.i) == null) {
                    stack = ItemStack.builder().itemType(plate.type).build();
                    if (plate.colour != null) stack.offer(Keys.DYE_COLOR, plate.colour);
                } else {
                    ItemPlate plateItem = (ItemPlate) plate.plate.getItem();
                    stack = ItemStack.builder().itemType(getType(Objects.requireNonNull(plateItem.getRegistryName()))).build();
                    name = "§a" + Grammar.cap(plate.name()) + " Plate";
                }
                page.setItem(plate.slot, new InvItem(stack, name));
            }
        });

        // Create the GUI
        InvItem infoItem = new InvItem(ItemTypes.PAPER, "§b§lStorage Info").setLore(
                "",
                "In Storage:",
                "  Left Click: §aEquip Plate",
                "  Right Click: §aRemove Plate from Storage",
                "",
                "In Inventory:",
                "  Left Click: §aAdd Plate to Storage"
        );
        page.setItem(18, infoItem);

        InvItem backgroundItem = new InvItem(ItemTypes.STAINED_GLASS_PANE, "").setKey(Keys.DYE_COLOR, DyeColors.BLACK);
        for (int backSlot : BACKGROUND_SLOTS) page.setItem(backSlot, backgroundItem);

        new InvInventory().openPage(player, page);
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
    private boolean checkItem(ItemStack item, ArcStorageData data) {
        for (Plate p : Plate.values()) {
            if (getType(Objects.requireNonNull(p.plate.getItem().getRegistryName())) == item.getType()) {
                if (data.get(p.i) != null) return false;
                data.set(p.i, item);
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

    /**
     * The Plate entries.
     * Note: The slot values should be in ascending order.
     */
    enum Plate {
        DRACO(0, 3, EnumPlate.DRACO, DyeColors.RED),
        DREAD(1, 5, EnumPlate.DREAD, DyeColors.CYAN),
        EARTH(2, 7, EnumPlate.EARTH, DyeColors.ORANGE),
        FIST(3, 11, EnumPlate.FIST, DyeColors.GRAY),
        FLAME(4, 13, EnumPlate.FLAME, DyeColors.PINK),
        ICICLE(5, 15, EnumPlate.ICICLE, DyeColors.SILVER),
        INSECT(6, 17, EnumPlate.INSECT, DyeColors.LIME),
        IRON(7, 21, EnumPlate.IRON, DyeColors.WHITE),
        MEADOW(8, 23, EnumPlate.MEADOW, DyeColors.GREEN),
        MIND(9, 25, EnumPlate.MIND, ItemTypes.GLASS_PANE),
        PIXIE(10, 29, EnumPlate.PIXIE, DyeColors.PURPLE),
        SKY(11, 31, EnumPlate.SKY, DyeColors.LIGHT_BLUE),
        SPLASH(12, 33, EnumPlate.SPLASH, DyeColors.BLUE),
        SPOOKY(13, 35, EnumPlate.SPOOKY, DyeColors.BLACK),
        STONE(14, 39, EnumPlate.STONE, DyeColors.BROWN),
        TOXIC(15, 41, EnumPlate.TOXIC, DyeColors.MAGENTA),
        ZAP(16, 43, EnumPlate.ZAP, DyeColors.YELLOW);

        int i;
        int slot;
        EnumPlate plate;
        DyeColor colour;
        ItemType type;

        Plate(int i, int slot, EnumPlate plate, DyeColor colour) {
            this.i = i;
            this.slot = slot;
            this.plate = plate;
            this.colour = colour;
            this.type = ItemTypes.STAINED_GLASS_PANE;
        }

        Plate(int i, int slot, EnumPlate plate, ItemType type) {
            this.i = i;
            this.slot = slot;
            this.plate = plate;
            this.type = type;
        }
    }
}
