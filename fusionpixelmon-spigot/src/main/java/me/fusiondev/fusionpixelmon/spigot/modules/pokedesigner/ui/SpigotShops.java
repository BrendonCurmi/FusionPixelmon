package me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.ui;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.economy.BankAPI;
import me.fusiondev.fusionpixelmon.api.economy.IEconomyProvider;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;

public class SpigotShops extends Shops {

    public SpigotShops(AbstractPlayer player) {
        super(player);
    }

    @Override
    public IEconomyProvider<?, ?> getBank(PokeDesignerConfig config) {
        return new BankAPI(player);
    }
}
