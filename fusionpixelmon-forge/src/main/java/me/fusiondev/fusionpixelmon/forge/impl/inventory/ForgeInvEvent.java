package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import me.fusiondev.fusionpixelmon.api.ui.events.AbstractInvEvent;

public class ForgeInvEvent extends AbstractInvEvent {

    @Override
    public boolean isShift() {
        return false;
    }
}
