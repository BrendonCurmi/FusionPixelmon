package me.fusiondev.fusionpixelmon.sponge.modules.arcplates;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.pixelmon.ArcPlates;
import me.fusiondev.fusionpixelmon.data.ArcPlateData;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;
import me.fusiondev.fusionpixelmon.impl.TimeUtils;
import me.fusiondev.fusionpixelmon.modules.arcplates.AbstractArcPlatesUI;
import me.fusiondev.fusionpixelmon.sponge.SpongeFusionPixelmon;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SpongeArcPlates extends AbstractArcPlatesUI {

    public SpongeArcPlates() {
        super(SpongeFusionPixelmon.getInstance().getConfigDir().resolve("arcplates").toFile());
    }

    @Override
    protected void clickInventory(Object event1, AbstractPlayer player1) {
        ClickInventoryEvent event = (ClickInventoryEvent) event1;
        if (event instanceof ClickInventoryEvent.Shift) {
            player.sendMessage(Text.of(Color.RED + "Shifting in this inventory is not a function"));
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
                        openInv = ((Player) player.get()).getOpenInventory().get().query(SlotIndex.of(slot));
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
                    TimeUtils.setTimeout(() -> enabled = true, 900);

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
                                if (selectedItemPlate.equals(heldItemPlate)) {
                                    player.sendMessage(Text.of(Color.RED + "That Plate is already equipped!"));
                                    return;
                                } else {
                                    ItemType heldItemType = getType(Objects.requireNonNull(heldItemPlate.getRegistryName()));
                                    for (ArcPlates.Plate p : ArcPlates.Plate.values()) {
                                        if (getType(Objects.requireNonNull(p.plate.getItem().getRegistryName())) == heldItemType) {
                                            if (!data.hasPlate(p.i)) {
                                                data.add(p.i);
                                                break;
                                            } else {
                                                player.sendMessage(Text.of(Color.RED + "Cant unequip " + GrammarUtils.cap(p.name()) + " Plate because there is another in Storage! Please remove the one in Storage first before unequiping."));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                            pokemon.setHeldItem(new net.minecraft.item.ItemStack(selectedItemPlate));
                            data.remove(getIDFromSlot(slot));
                            player.sendMessage(Text.of(Color.GREEN + "Plate equipped!"));
                        } else
                            player.sendMessage(Text.of(Color.RED + "Cannot equip Plate because Pokemon is currently holding something!"));
                    }
                    // Right clicking plate in GUI
                    else if (event instanceof ClickInventoryEvent.Secondary) {
                        /*
                         * Take the plate that is right clicked in the GUI and give it to the player if there is free
                         * inventory space.
                         */
                        PlayerInventory playerInv = (PlayerInventory) ((Player) player.get()).getInventory();
                        if (playerInv.getMainGrid().canFit(selected)) {
                            ((Player) player.get()).getInventory().offer(selected);
                            data.remove(getIDFromSlot(slot));
                        } else player.sendMessage(Text.of(Color.RED + "Your inventory is full!"));
                    }
                }
            }
        }
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
        for (ArcPlates.Plate p : ArcPlates.Plate.values()) {
            if (getType(Objects.requireNonNull(p.plate.getItem().getRegistryName())) == item.getType()) {
                if (data.hasPlate(p.i)) return false;
                data.add(p.i);
                return true;
            }
        }
        return false;
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
     * ArcPlates hovering.
     */
    private EntityPixelmon entityPixelmon;
    private World world;

    @Override
    public void createRing(EntityPixelmon entityPixelmon) {
        if (isActive(entityPixelmon)) return;
        this.entityPixelmon = entityPixelmon;

        Location<World> location = ((Entity) entityPixelmon).getLocation();
        this.world = location.getExtent();

        this.data = new ArcPlateData(SpongeFusionPixelmon.getInstance().getConfigDir().resolve("arcplates").toFile(), entityPixelmon.getPokemonData().getUUID());

        ArmorStand[] ARMORS = new ArmorStand[17];
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            Entity armorStand = location.createEntity(EntityTypes.ARMOR_STAND);
            if (!world.spawnEntity(armorStand)) continue;
            ArmorStand armor = (ArmorStand) armorStand;
            armor.offer(Keys.DISPLAY_NAME, Text.of(AbstractArcPlatesUI.ARMOR_STAND_NAME));
            armor.offer(Keys.CUSTOM_NAME_VISIBLE, false);
            armor.offer(Keys.HAS_GRAVITY, false);
            armor.offer(Keys.ARMOR_STAND_MARKER, true);
            armor.offer(Keys.INVISIBLE, true);
            armor.setCreator(((Entity) entityPixelmon.getPokemonData().getOwnerPlayer()).getUniqueId());
            ARMORS[plate.i] = armor;
        }

        activate(entityPixelmon);
        get(entityPixelmon).setArmorStands(ARMORS);
        get(entityPixelmon).setTask(Task.builder()
                .execute(new MyTask())
                .delay(100, TimeUnit.MILLISECONDS)
                .interval(250, TimeUnit.MILLISECONDS)
                .name("ArcPlates")
                .submit(SpongeFusionPixelmon.getInstance()));
    }

    @Override
    public void deleteRing(EntityPixelmon entityPixelmon) {
        if (!isActive(entityPixelmon)) return;
        ((Task) get(entityPixelmon).getTask()).cancel();
        ArmorStand[] ARMORS = (ArmorStand[]) get(entityPixelmon).getArmorStands();
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            ARMORS[plate.i].remove();
        }
    }

    private class MyTask implements Runnable {
        public void run() {
            if (!entityPixelmon.isAddedToWorld() || !isActive(entityPixelmon)) {
                ((Task) get(entityPixelmon).getTask()).cancel();
                deleteRing(entityPixelmon);
                deactivate(entityPixelmon);
                return;
            }
            Entity pokemonEntity = ((Entity) entityPixelmon);
            //Vector3d playerLoc = ((Entity) entityPixelmon.getPokemonData().getOwnerPlayer()).getLocation().getPosition();

            Location<World> location = pokemonEntity.getLocation();
            double x0 = location.getX(), y0 = location.getY();

            ArmorStand[] ARMORS = (ArmorStand[]) get(entityPixelmon).getArmorStands();
            loop(x0, y0, (x, y, plate) -> {
                ArmorStand armor = ARMORS[plate.i];
                AbstractItemStack itemStack = FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack(plate.name().toLowerCase() + "_plate");
                armor.setHelmet((ItemStack) itemStack.getRaw());
                armor.setLocation(new Location<>(world, x, y, location.getZ()));
                //armor.lookAt(playerLoc);
            });
        }
    }
}
