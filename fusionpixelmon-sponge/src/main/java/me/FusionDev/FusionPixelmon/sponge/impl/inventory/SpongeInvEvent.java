package me.FusionDev.FusionPixelmon.sponge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.ui.events.AbstractInvEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class SpongeInvEvent extends AbstractInvEvent {

    private final boolean SHIFT;

    public SpongeInvEvent(ClickInventoryEvent event) {
        SHIFT = event instanceof ClickInventoryEvent.Shift;
    }

    @Override
    public boolean isShift() {
        return SHIFT;
    }
}
