package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import ca.landonjw.gooeylibs.inventory.api.Button;
import ca.landonjw.gooeylibs.inventory.api.Page;
import ca.landonjw.gooeylibs.inventory.api.Template;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.AbstractInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.ui.events.Event;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ForgeInvInventory extends InvInventory {
    private static final int SLOT_SIZE = 9;

    @Override
    public void openPage(AbstractPlayer player, InvPage page) {
        Template.Builder b = Template.builder(page.rows);

        int size = page.rows * SLOT_SIZE;
        int x = 0, y = 0;
        for (int i = 0; i < size; i++) {
            if (page.elements.containsKey(i)) {
                final int SLOT = i;
                Button btn = Button.builder()
                        .item((ItemStack) page.elements.get(i).getItemStack().getRaw())
                        .onClick(action -> {
                            page.getEventHandler().call(Event.CLICK_INVENTORY, action, player);

                            if (page.actions.containsKey(SLOT)) {
                                page.actions.get(SLOT).action(new ForgeInvEvent());
                            }
                        })
                        .build();

                b.set(y, x, btn);
            } else if (page.backgroundItem != null) {
                b.set(y, x, Button.of((ItemStack) page.backgroundItem.getItemStack().getRaw()));
            }
            if (++x == 9) {
                x = 0;
                y++;
            }
        }

        Page page1 = Page.builder()
                .title(page.title)
                .template(b.build())
                .onOpen(() -> addGrace(player.getUniqueId()))
                .onClose(() -> {
                    if (noGrace(player.getUniqueId())) {
                        onInventoryClose(player);
                    }
                })
                .build();

        AbstractInventory inventory = new ForgeInventory(page1);
        this.inventory = inventory;
        page.inventory = inventory;
        player.openInventory(inventory);
        InvInventory.playerOpened(player, page);
    }

    private void onInventoryClose(AbstractPlayer player) {
        if (openPages.containsKey(player.getUniqueId())) {
            openPages.get(player.getUniqueId()).getEventHandler().call(Event.CLOSE_INVENTORY, null, player);
            playerClosed(player.getUniqueId());
        }
    }

    /**
     * Converts the specified slot number into inventory coordinates,
     * in the form of (row, column).
     * @param slot the slot number.
     * @return the inventory slot coords.
     */
    public static int[] getUICoords(int slot) {
        int y = slot / SLOT_SIZE, x = slot % SLOT_SIZE;
        return new int[]{y, x};
    }

    public static void runUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 0, 500);
    }

    private static class Task extends TimerTask {
        @Override
        public void run() {
            for (InvPage page : openPages.values()) {
                // Execute every open page's runnable, if one is defined
                if (page.runnable != null) {
                    page.runnable.run();
                }

                Template.Builder b = ((Page) page.inventory.getRaw()).getTemplate().toBuilder();

                if (page.dynamicElements != null) {
                    for (Map.Entry<Integer, InvItem> element : page.dynamicElements.entrySet()) {
                        if (element.getValue() != null) {
                            Button btn = Button.builder()
                                    .item((ItemStack) element.getValue().getItemStack().getRaw())
                                    //todo dynamic elements these shouldnt do anything
                                    /*.onClick(action -> {
                                        page.getEventHandler().call(Event.CLICK_INVENTORY, action, ForgeAdapter.adapt(action.getPlayer()));

                                        if (page.actions.containsKey(element.getKey())) {
                                            page.actions.get(element.getKey()).action(new ForgeInvEvent());//SpigotAdapter.adapt(event)
                                        }
                                    })*/
                                    .build();

                            int[] coords = getUICoords(element.getKey());
                            b.set(coords[0], coords[1], btn);
                        }
                    }
                }
            }
        }
    }
}
