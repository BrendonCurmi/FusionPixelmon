package me.fusiondev.fusionpixelmon.modules.pokeshrines;

import me.fusiondev.fusionpixelmon.FusionPixelmon;

import java.util.List;

/**
 * Allows the "breaking" and obtaining of the shrines, altars, and chalices that naturally
 * spawn throughout the world of Pixelmon, since they are made to be unbreakable.<br/>
 * If the player interacts with one of these blocks while holding any of the allowed
 * pickaxes whilst in Survival Mode (and with inventory space), then the block will
 * drop into the player's inventory.
 */
public class PokeShrinesModule {

    /**
     * The blocks which can be obtained using this method.
     */
    protected static final List<String> BLOCKS = FusionPixelmon.getInstance().getConfiguration().getPickableShrines();
}
