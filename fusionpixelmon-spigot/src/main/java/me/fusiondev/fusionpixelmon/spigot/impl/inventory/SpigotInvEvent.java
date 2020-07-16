package me.fusiondev.fusionpixelmon.spigot.impl.inventory;

import me.fusiondev.fusionpixelmon.api.ui.events.AbstractInvEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SpigotInvEvent extends AbstractInvEvent {

    private final boolean SHIFT;

    public SpigotInvEvent(InventoryClickEvent event) {
        SHIFT = event.isShiftClick();
    }

    @Override
    public boolean isShift() {
        return SHIFT;
    }
}
