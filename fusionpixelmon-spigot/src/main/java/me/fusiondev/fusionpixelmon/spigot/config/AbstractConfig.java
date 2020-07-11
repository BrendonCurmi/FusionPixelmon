package me.fusiondev.fusionpixelmon.spigot.config;

import me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.config.PokeDesignerConfig;

import java.util.List;

public abstract class AbstractConfig {
    public abstract boolean isAntiFallDamageEnabled();
    public abstract boolean isMasterballCraftingEnabled();
    public abstract boolean isArcPlateEnabled();
    public abstract List<String> getPickableShrines();
    public abstract PokeDesignerConfig getPokeDesignerConfig();
}
