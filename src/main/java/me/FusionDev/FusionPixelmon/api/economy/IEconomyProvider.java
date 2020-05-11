package me.FusionDev.FusionPixelmon.api.economy;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.math.BigDecimal;
import java.util.Optional;

public interface IEconomyProvider {

    Optional<EconomyService> getService();

    default boolean serviceExists() {
        return getService().isPresent();
    }

    Currency getCurrency();

    String getCurrencySymbol(double amount);

    BigDecimal balance(Player player);

    boolean canAfford(Player player, double amount);

    default boolean deposit(Player player, double amount){
        return deposit(player, amount, false);
    }

    boolean deposit(Player player, double amount, boolean message);

    default boolean withdraw(Player player, double amount) {
        return withdraw(player, amount, false);
    }

    boolean withdraw(Player player, double amount, boolean message);
}
