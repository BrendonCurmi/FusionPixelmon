package me.fusiondev.fusionpixelmon.forge.modules.arcplates;

import me.fusiondev.fusionpixelmon.modules.arcplates.AbstractArcPlatesUI;

public class ForgeArcPlatesModule {
    private static AbstractArcPlatesUI arcPlates;

    public static AbstractArcPlatesUI getArcPlates() {
        return (arcPlates == null) ? arcPlates = new ForgeArcPlates() : arcPlates;
    }
}
