package me.fusiondev.fusionpixelmon.spigot.modules.arcplates;

import me.fusiondev.fusionpixelmon.modules.arcplates.AbstractArcPlatesUI;

public class SpigotArcPlatesModule {
    private static AbstractArcPlatesUI arcPlates;

    public static AbstractArcPlatesUI getArcPlates() {
        return (arcPlates == null) ? arcPlates = new SpigotArcPlates() : arcPlates;
    }
}
