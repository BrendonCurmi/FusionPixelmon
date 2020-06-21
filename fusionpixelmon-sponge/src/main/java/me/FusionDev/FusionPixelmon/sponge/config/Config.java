package me.FusionDev.FusionPixelmon.sponge.config;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import me.FusionDev.FusionPixelmon.sponge.modules.pokedesigner.config.PokeDesignerConfig;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Config extends AbstractConfig {
    @SuppressWarnings("UnstableApiUsage")
    public final static TypeToken<Config> type = TypeToken.of(Config.class);

    @Inject
    @Setting("anti-fall-damage")
    private boolean antiFallDamage;
    @Inject
    @Setting("craft-masterballs")
    private boolean craftMasterBalls;
    @Inject
    @Setting("arcplate")
    private boolean arcPlate;
    @Inject
    @Setting("shrine-pickup")
    private List<String> shrinePickup = ImmutableList.of();
    @Inject
    @Setting("pokedesigner")
    private PokeDesignerConfig pokeDesigner = new PokeDesignerConfig();

    @Override
    public boolean isAntiFallDamageEnabled() {
        return antiFallDamage;
    }

    @Override
    public boolean isMasterballCraftingEnabled() {
        return craftMasterBalls;
    }

    @Override
    public boolean isArcPlateEnabled() {
        return arcPlate;
    }

    @Override
    public List<String> getPickableShrines() {
        return shrinePickup;
    }

    @Override
    public PokeDesignerConfig getPokeDesignerConfig() {
        return pokeDesigner;
    }
}
