package me.FusionDev.FusionPixelmon.api.pixelmon;

import java.util.List;

public interface IPokemonWrapper {

    String getTitle();

    String getIfShiny();

    String getSpeciesName();

    String getName();

    String getAbility();

    String getNature();

    String getGender();

    String getSize();

    String getForm();

    String getCaughtBall();

    boolean hasTexture();

    String getTexture();

    String getPokerus();

    List<String> getIVs();

    List<String> getEVs();
}
