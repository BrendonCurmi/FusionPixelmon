package me.fusiondev.fusionpixelmon.spigot.modules.arcplates;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
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
import me.fusiondev.fusionpixelmon.spigot.SpigotFusionPixelmon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SpigotArcPlates extends AbstractArcPlatesUI {

    public SpigotArcPlates() {
        super(SpigotFusionPixelmon.getInstance().getDataFolder().toPath().resolve("arcplates").toFile());
    }

    @Override
    protected void clickInventory(Object event1, AbstractPlayer player1) {
        InventoryClickEvent event = (InventoryClickEvent) event1;
        if (event.isShiftClick()) {
            player.sendMessage(Color.RED + "Shifting in this inventory is not a function");
            //event.setCancelled(true);
            return;
        }

        int slot = event.getRawSlot();

        // If clicked outside bounds
        if (slot < 0) return;

        ItemStack selected = event.getCurrentItem();

        // In Player Inventory
        if (slot > (ROWS * 9) - 1 && isItemPlate(selected) && checkItem(selected, data)) {
            PlayerInventory playerInv = ((Player) player.get()).getInventory();
            // remove() removes everything in the stack.
            // If 1 amount in stack, remove it. If more than 1, remove 1.
            if (selected.getAmount() == 1) playerInv.remove(selected);
            else selected.setAmount(selected.getAmount() - 1);
        }
        // In GUI Page
        else if (isItemPlate(selected) && enabled) {
            // Delay to prevent duping
            enabled = false;
            TimeUtils.setTimeout(() -> enabled = true, 900);

            // Left clicking plate in GUI
            if (event.isLeftClick()) {
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
                    EnumPlate selectedEnumPlate = getPlate(selected.getType());
                    if (selectedEnumPlate == null) {
                        player.sendMessage(Color.RED + "Cannot equip Plate");
                        return;
                    }

                    if (pokemon.getHeldItemAsItemHeld() instanceof ItemPlate) {
                        ItemPlate heldItemPlate = (ItemPlate) pokemon.getHeldItemAsItemHeld();
                        if (selected.getType().name().toLowerCase().contains(heldItemPlate.getType().name().toLowerCase())) {
                            player.sendMessage(Color.RED + "That Plate is already equipped!");
                            return;
                        } else {
                            for (ArcPlates.Plate p : ArcPlates.Plate.values()) {
                                if (p.plate.getItem().getRegistryName() == heldItemPlate.getRegistryName()) {
                                    if (!data.hasPlate(p.i)) {
                                        data.add(p.i);
                                        break;
                                    } else {
                                        player.sendMessage(Color.RED + "Cant unequip " + GrammarUtils.cap(p.name()) + " Plate because there is another in Storage! Please remove the one in Storage first before unequiping.");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    pokemon.setHeldItem(new net.minecraft.item.ItemStack(selectedEnumPlate.getItem()));
                    data.remove(getIDFromSlot(slot));
                    player.sendMessage(Color.GREEN + "Plate equipped!");
                } else
                    player.sendMessage(Color.RED + "Cannot equip Plate because Pokemon is currently holding something!");
            }
            // Right clicking plate in GUI
            else if (event.isRightClick()) {
                /*
                 * Take the plate that is right clicked in the GUI and give it to the player if there is free
                 * inventory space.
                 */
                PlayerInventory playerInv = ((Player) player.get()).getInventory();
                playerInv.addItem(selected);
                data.remove(getIDFromSlot(slot));
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
            if (item.getType().toString().toUpperCase().contains(p.plate.name().toUpperCase())) {
                if (data.hasPlate(p.i)) return false;
                data.add(p.i);
                return true;
            }
        }
        return false;
    }

    private EnumPlate getPlate(Material material) {
        for (ArcPlates.Plate plate : ArcPlates.Plate.values())
            if (material.toString().contains(plate.plate.name()))
                return plate.plate;
        return null;
    }

    private boolean isItemPlate(ItemStack itemStack) {
        String s = itemStack.getType().toString();
        return s.startsWith("PIXELMON_") && s.endsWith("_PLATE");
    }


    /**
     * ArcPlates hovering.
     */
    private EntityPixelmon entityPixelmon;
    private World world;
    private Entity entity;

    @Override
    public void createRing(EntityPixelmon entityPixelmon) {
        if (isActive(entityPixelmon)) return;
        this.entityPixelmon = entityPixelmon;
        this.world = (World) player.getWorld();
        this.entity = Bukkit.getEntity(entityPixelmon.getUniqueID());

        Location loc = entity.getLocation();

        ArmorStand[] ARMORS = new ArmorStand[17];
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            ArmorStand armor = world.spawn(loc, ArmorStand.class);
            armor.setCustomName(ARMOR_STAND_NAME);
            armor.setCustomNameVisible(false);
            armor.setGravity(false);
            armor.setMarker(true);
            armor.setVisible(false);
            armor.setInvulnerable(true);
            ARMORS[plate.i] = armor;
        }

        activate(entityPixelmon);
        get(entityPixelmon).setArmorStands(ARMORS);
        get(entityPixelmon).setTask(Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(SpigotFusionPixelmon.getInstance(), new MyTask(), 0L, 5L));
    }

    @Override
    public void deleteRing(EntityPixelmon entityPixelmon) {
        if (!isActive(entityPixelmon)) return;
        Bukkit.getScheduler().cancelTask((int) get(entityPixelmon).getTask());
        ArmorStand[] ARMORS = (ArmorStand[]) get(entityPixelmon).getArmorStands();
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            ARMORS[plate.i].remove();
        }
    }

    private class MyTask implements Runnable {
        public void run() {
            if (!entityPixelmon.isAddedToWorld() || !isActive(entityPixelmon)) {
                Bukkit.getScheduler().cancelTask((int) get(entityPixelmon).getTask());
                deleteRing(entityPixelmon);
                deactivate(entityPixelmon);
                return;
            }
            Location pos = entity.getLocation();

            double x0 = pos.getX(), y0 = pos.getY();
            ArmorStand[] ARMORS = (ArmorStand[]) get(entityPixelmon).getArmorStands();
            loop(x0, y0, (x, y, plate) -> {
                ArmorStand armor = ARMORS[plate.i];

                AbstractItemStack itemStack = FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack(plate.name().toLowerCase() + "_plate");
                armor.setHelmet((ItemStack) itemStack.getRaw());
                armor.teleport(new Location(world, x, y, pos.getZ()));
            });
        }
    }
}
