package me.fusiondev.fusionpixelmon.config;

import me.fusiondev.fusionpixelmon.modules.arcplates.config.ArcPlatesConfig;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;

import java.util.HashMap;

public abstract class AbstractConfig {
    public abstract boolean isAntiFallDamageEnabled();
    public abstract boolean isMasterballCraftingEnabled();
    public abstract boolean hasModifiers();
    public abstract HashMap<String, String> getPickableShrines();
    public abstract ArcPlatesConfig getArcPlates();
    public abstract PokeDesignerConfig getPokeDesignerConfig();
}
