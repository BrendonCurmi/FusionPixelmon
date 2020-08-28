package me.fusiondev.fusionpixelmon.forge.modules.arcplates;

import ca.landonjw.gooeylibs.inventory.api.Button;
import ca.landonjw.gooeylibs.inventory.api.ButtonAction;
import ca.landonjw.gooeylibs.inventory.api.Page;
import ca.landonjw.gooeylibs.inventory.api.Template;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.pixelmon.ArcPlates;
import me.fusiondev.fusionpixelmon.data.ArcPlateData;
import me.fusiondev.fusionpixelmon.forge.ForgeFusionPixelmon;
import me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeInvInventory;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;
import me.fusiondev.fusionpixelmon.impl.TimeUtils;
import me.fusiondev.fusionpixelmon.modules.arcplates.AbstractArcPlatesUI;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ForgeArcPlates extends AbstractArcPlatesUI {
    public ForgeArcPlates() {
        super(ForgeFusionPixelmon.getInstance().getDataFolder().toPath().resolve("arcplates").toFile());
    }

    private static final List<UUID> GRACE = new ArrayList<>();
    private static final int[] INFO_BTN_COORDS = {1, 0};
    private static final int[] HOVERING_BTN_COORDS = {3, 0};

    @Override
    protected void create() {
        Template.Builder builder = Template.builder(page.rows);

        // Info
        AbstractItemStack infoStack = reg.getItemTypesRegistry().PAPER().to()
                .setName("§b§lStorage Info")
                .setLore(
                        "",
                        InvItem.DEFAULT_LORE_COLOUR + "In Storage:",
                        InvItem.DEFAULT_LORE_COLOUR + "  Click: §aAdd/Remove Plate from Storage",
                        InvItem.DEFAULT_LORE_COLOUR + "  Shift Click: §aEquip Plate"
                );

        Button infoBtn = Button.builder()
                .item((ItemStack) infoStack.getRaw())
                .build();

        builder.set(INFO_BTN_COORDS[0], INFO_BTN_COORDS[1], infoBtn);

        // Plates
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            AbstractItemStack stack = reg.getPixelmonUtils().getPixelmonItemStack(plate.plate.name().toLowerCase() + "_plate");
            String name = "§" + (data.hasPlate(plate.i) ? 'a' : '7') + GrammarUtils.cap(plate.name()) + " Plate";

            Button plateBtn = Button.builder()
                    .item((ItemStack) stack.getRaw())
                    .displayName(name)
                    .onClick(action -> {
                        //page.getEventHandler().call(Event.CLICK_INVENTORY, action, player);
                        clickInventory(action, player);
                    })
                    .build();

            int[] coords = ForgeInvInventory.getUICoords(plate.slot);
            builder.set(coords[0], coords[1], plateBtn);
        }

        // Hovering
        EntityPixelmon entityPixelmon = pokemon.getPixelmonIfExists();

        AbstractItemStack hoveringStack = reg.getItemTypesRegistry().DYE().to()
                .setColour(isActive(entityPixelmon) ? DyeColor.LIME : DyeColor.RED)
                .setName("§b§lArcPlates Hovering")
                .setLore(InvItem.DEFAULT_LORE_COLOUR + "Hover the plates around your Arceus");

        Button.Builder hoveringBtnBuilder = Button.builder().item((ItemStack) hoveringStack.getRaw());
        hoveringBtnBuilder.onClick(action -> {
            if (entityPixelmon != null) {
                deactivateForPlayer(entityPixelmon);
                if (!isActive(entityPixelmon)) {
                    createRing(entityPixelmon);
                } else {
                    deleteRing(entityPixelmon);
                    deactivate(entityPixelmon);
                }
            }

            hoveringStack.setColour(isActive(entityPixelmon) ? DyeColor.LIME : DyeColor.RED);
            builder.set(HOVERING_BTN_COORDS[0], HOVERING_BTN_COORDS[1], hoveringBtnBuilder.item((ItemStack) hoveringStack.getRaw()).build());
        });
        builder.set(HOVERING_BTN_COORDS[0], HOVERING_BTN_COORDS[1], hoveringBtnBuilder.build());

        // Background
        AbstractItemStack backgroundStack = reg.getItemTypesRegistry().STAINED_GLASS_PANE().to()
                .setColour(DyeColor.BLACK)
                .setName("");
        for (int backSlot : BACKGROUND_SLOTS) {
            int[] coords = ForgeInvInventory.getUICoords(backSlot);
            builder.set(coords[0], coords[1], Button.of((ItemStack) backgroundStack.getRaw()));
        }

        Page page1 = Page.builder()
                .title(page.title)
                .template(builder.build())
                .onOpen(() -> {
                    GRACE.add(player.getUniqueId());
                    TimeUtils.setTimeout(() -> GRACE.remove(player.getUniqueId()), 500);
                })
                .onClose(() -> {
                    if (!GRACE.contains(player.getUniqueId())) {
                        data.save();
                    }
                })
                .build();

        page1.forceOpenPage((EntityPlayerMP) player.get());
    }

    @Override
    protected void clickInventory(Object event, AbstractPlayer player) {
        ButtonAction action = (ButtonAction) event;
        EntityPlayerMP entityPlayer = (EntityPlayerMP) player.get();

        ItemStack stack = action.getButton().getDisplay();
        ItemStack newStack = new ItemStack(stack.getItem());
        int slot = entityPlayer.inventory.findSlotMatchingUnusedItem(newStack);

        ArcPlates.Plate plate = getPlate(stack);

        /*
         * If Plate is clicked and isn't in storage, check if player has one in inventory.
         * If player does, remove it and add it to storage. Otherwise notify they dont have it.
         */
        if (action.getClickType() == ClickType.PICKUP && !data.hasPlate(plate.i)) {
            if (entityPlayer.inventory.hasItemStack(newStack) && slot != -1) {
                entityPlayer.inventory.decrStackSize(slot, 1);
                data.add(plate.i);
            } else {
                player.sendMessage(Color.RED + "No plate in inventory");
            }
        } else if (enabled && data.hasPlate(plate.i)) {
            // Delay to prevent duping
            enabled = false;
            TimeUtils.setTimeout(() -> enabled = true, 900);

            if (action.getClickType() == ClickType.QUICK_MOVE) {

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
                    EnumPlate selectedEnumPlate = plate.plate;
                    if (selectedEnumPlate == null) {
                        player.sendMessage(Color.RED + "Cannot equip Plate");
                        return;
                    }

                    if (pokemon.getHeldItemAsItemHeld() instanceof ItemPlate) {
                        ItemPlate heldItemPlate = (ItemPlate) pokemon.getHeldItemAsItemHeld();

                        if (newStack.getItem() == heldItemPlate) {
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
                    pokemon.setHeldItem(new ItemStack(selectedEnumPlate.getItem()));
                    data.remove(plate.i);
                    player.sendMessage(Color.GREEN + "Plate equipped!");
                } else
                    player.sendMessage(Color.RED + "Cannot equip Plate because Pokemon is currently holding something!");
            } else if (action.getClickType() == ClickType.PICKUP) {
                /*
                 * Take the plate that is right clicked in the GUI and give it to the player if there is free
                 * inventory space.
                 */
                entityPlayer.inventory.addItemStackToInventory(newStack);
                data.remove(plate.i);
            }
        }
    }

    private ArcPlates.Plate getPlate(ItemStack itemStack) {
        for (ArcPlates.Plate plate : ArcPlates.Plate.values())
            if (itemStack.getItem() == plate.plate.getItem())
                return plate;
        throw new IllegalArgumentException("Item is not a plate!");
    }


    /**
     * ArcPlates hovering.
     */
    private EntityPixelmon entityPixelmon;

    @Override
    public void createRing(EntityPixelmon entityPixelmon) {
        if (isActive(entityPixelmon)) return;
        this.entityPixelmon = entityPixelmon;
        this.data = new ArcPlateData(ForgeFusionPixelmon.getInstance().getDataFolder().toPath().resolve("arcplates").toFile(), entityPixelmon.getPokemonData().getUUID());

        EntityArmorStand[] ARMORS = new EntityArmorStand[17];
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            EntityArmorStand armor = new EntityArmorStand(entityPixelmon.getEntityWorld());
            armor.setCustomNameTag(AbstractArcPlatesUI.ARMOR_STAND_NAME);
            armor.setNoGravity(true);
            armor.getEntityData().setBoolean("NoBasePlate", false);
            armor.setInvisible(true);
            armor.setPosition(entityPixelmon.getPosition().getX(), entityPixelmon.getPosition().getY(), entityPixelmon.getPosition().getZ());
            entityPixelmon.getEntityWorld().spawnEntity(armor);
            ARMORS[plate.i] = armor;
        }

        activate(entityPixelmon);
        get(entityPixelmon).setArmorStands(ARMORS);
    }

    @Override
    public void deleteRing(EntityPixelmon entityPixelmon) {
        if (!isActive(entityPixelmon)) return;
        EntityArmorStand[] ARMORS = (EntityArmorStand[]) get(entityPixelmon).getArmorStands();
        for (ArcPlates.Plate plate : ArcPlates.Plate.values()) {
            entityPixelmon.getEntityWorld().removeEntity(ARMORS[plate.i]);
        }
    }

    private int ticks = 0;

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
            if (ticks % 5 == 0) run();
            if (ticks == 20) ticks = 0;
        }
    }

    public void run() {
        if (entityPixelmon == null || data == null) {
            return;
        }

        if (!entityPixelmon.isAddedToWorld() || !isActive(entityPixelmon)) {
            deleteRing(entityPixelmon);
            deactivate(entityPixelmon);
            return;
        }

        BlockPos location = entityPixelmon.getPosition();
        double x0 = location.getX(), y0 = location.getY();

        EntityArmorStand[] ARMORS = (EntityArmorStand[]) get(entityPixelmon).getArmorStands();
        loop(x0, y0, (x, y, plate) -> {
            EntityArmorStand armor = ARMORS[plate.i];
            AbstractItemStack itemStack = reg.getPixelmonUtils().getPixelmonItemStack(plate.name().toLowerCase() + "_plate");
            armor.setItemStackToSlot(EntityEquipmentSlot.HEAD, (ItemStack) itemStack.getRaw());
            armor.setPosition(x, y, location.getZ());
        });
    }
}
