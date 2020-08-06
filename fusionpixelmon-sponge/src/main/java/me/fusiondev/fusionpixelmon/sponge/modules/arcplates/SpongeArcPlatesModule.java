package me.fusiondev.fusionpixelmon.sponge.modules.arcplates;

import me.fusiondev.fusionpixelmon.modules.arcplates.AbstractArcPlatesUI;

public class SpongeArcPlatesModule {

    private static AbstractArcPlatesUI arcPlates;

    public static AbstractArcPlatesUI getArcPlates() {
        return (arcPlates == null) ? arcPlates = new SpongeArcPlates() : arcPlates;
    }
}
