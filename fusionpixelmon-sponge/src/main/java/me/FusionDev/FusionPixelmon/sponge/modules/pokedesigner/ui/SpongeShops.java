package me.FusionDev.FusionPixelmon.sponge.modules.pokedesigner.ui;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.economy.IEconomyProvider;
import me.fusiondev.fusionpixelmon.api.economy.BankAPI;
import me.FusionDev.FusionPixelmon.sponge.api.economy.EconomyProvider;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.EconomyService;

public class SpongeShops extends Shops {

    public SpongeShops(AbstractPlayer player) {
        super(player);
    }

    public IEconomyProvider<?, ?> getBank(PokeDesignerConfig config) {
        return (Sponge.getServiceManager().isRegistered(EconomyService.class) && config.useCurrency()) ?
                new EconomyProvider(config.getCurrency())
                : new BankAPI(player);
    }
}
