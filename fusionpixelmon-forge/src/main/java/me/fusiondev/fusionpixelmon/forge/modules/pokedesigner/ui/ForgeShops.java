package me.fusiondev.fusionpixelmon.forge.modules.pokedesigner.ui;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.economy.BankAPI;
import me.fusiondev.fusionpixelmon.api.economy.IEconomyProvider;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;

public class ForgeShops extends Shops {
    public ForgeShops(AbstractPlayer player) {
        super(player);
    }

    @Override
    public IEconomyProvider<?, ?> getBank(PokeDesignerConfig config) {
        return new BankAPI(player);
    }
}
