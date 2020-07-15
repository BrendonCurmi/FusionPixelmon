package me.FusionDev.FusionPixelmon.sponge.api.economy;

import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.economy.IEconomyProvider;
import me.FusionDev.FusionPixelmon.sponge.util.CauseStackUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;
import java.util.Optional;

public class EconomyProvider implements IEconomyProvider<EconomyService, Currency> {

    private final Currency CURRENCY;

    public EconomyProvider(String currencyName) {
        this.CURRENCY = getCurrency(currencyName);
    }

    @Override
    public Optional<EconomyService> getService() {
        return Sponge.getServiceManager().provide(EconomyService.class);
    }

    @Override
    public Currency getCurrency() {
        return CURRENCY != null ? CURRENCY : getCurrency("");
    }

    @Override
    public Currency getCurrency(String name) {
        Optional<EconomyService> economyService = getService();
        if (economyService.isPresent() && name != null && !name.isEmpty())
            for (Currency currency : economyService.get().getCurrencies())
                if (currency.getName().equalsIgnoreCase(name))
                    return currency;
        return economyService.map(EconomyService::getDefaultCurrency).orElse(null);
    }

    @Override
    public String getCurrencySymbol(double amount) {
        return getService().map(economyService -> {
            Text text = amount != 1 ? getCurrency().getPluralDisplayName() : getCurrency().getDisplayName();
            return amount + " " + text.toPlain();
        }).orElseGet(() -> String.valueOf(amount));
    }

    @Override
    public BigDecimal balance(AbstractPlayer player) {
        Optional<EconomyService> economyService = getService();
        if (economyService.isPresent()) {
            Optional<UniqueAccount> account = economyService.get().getOrCreateAccount(player.getUniqueId());
            if (account.isPresent())
                return account.get().getBalance(CURRENCY);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean canAfford(AbstractPlayer player, double amount) {
        Optional<EconomyService> economyService = getService();
        if (economyService.isPresent()) {
            Optional<UniqueAccount> account = economyService.get().getOrCreateAccount(player.getUniqueId());
            return account.isPresent() && balance(player).doubleValue() >= amount;
        }
        return false;
    }

    @Override
    public boolean deposit(AbstractPlayer player, double amount, boolean message) {
        Optional<EconomyService> economyService = getService();
        if (economyService.isPresent()) {
            EconomyService service = economyService.get();
            Optional<UniqueAccount> account = service.getOrCreateAccount(player.getUniqueId());
            if (!account.isPresent()) {
                player.sendMessage(Text.of(TextColors.RED, "Cannot find an account for player '" + player.getName() + "'"));
                return false;
            }

            BigDecimal cost = BigDecimal.valueOf(amount);
            TransactionResult result = account.get().deposit(getCurrency(), cost, CauseStackUtil.createCause(player));
            if (result.getResult() == ResultType.SUCCESS) {
                if (message) {
                    player.sendMessage(Text.of(TextColors.GREEN, "Successfully deposited " + getCurrencySymbol(amount) + " into your account"));
                }
                return true;
            } else {
                if (message) {
                    player.sendMessage(Text.of(TextColors.RED, "Unable to deposit " + getCurrencySymbol(amount) + " into your account"));
                }
            }
        }
        return false;
    }

    @Override
    public boolean withdraw(AbstractPlayer player, double amount, boolean message) {
        Optional<EconomyService> economyService = getService();
        if (economyService.isPresent()) {
            EconomyService service = economyService.get();
            Optional<UniqueAccount> account = service.getOrCreateAccount(player.getUniqueId());
            if (!account.isPresent()) {
                player.sendMessage(Text.of(TextColors.RED, "Cannot find an account for player '" + player.getName() + "'"));
                return false;
            }

            BigDecimal cost = BigDecimal.valueOf(amount);
            TransactionResult result = account.get().withdraw(getCurrency(), cost, CauseStackUtil.createCause(player));
            if (result.getResult() == ResultType.SUCCESS) {
                if (message) {
                    player.sendMessage(Text.of(TextColors.GREEN, "Successfully withdrew " + getCurrencySymbol(amount) + " from your account"));
                }
                return true;
            } else if (result.getResult() == ResultType.ACCOUNT_NO_FUNDS) {
                if (message) {
                    player.sendMessage(Text.of(TextColors.RED, "You do not have enough " + getCurrency().getPluralDisplayName() + " to perform this transaction"));
                }
            } else {
                player.sendMessage(Text.of(TextColors.RED, "Unable to withdraw " + getCurrencySymbol(amount) + " from your account"));
            }
        }
        return false;
    }
}
