package me.fusiondev.fusionpixelmon.config;

import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;

import java.util.List;

public abstract class AbstractConfig {
    public abstract boolean isAntiFallDamageEnabled();
    public abstract boolean isMasterballCraftingEnabled();
    public abstract boolean isArcPlateEnabled();
    public abstract List<String> getPickableShrines();
    public abstract PokeDesignerConfig getPokeDesignerConfig();
}
