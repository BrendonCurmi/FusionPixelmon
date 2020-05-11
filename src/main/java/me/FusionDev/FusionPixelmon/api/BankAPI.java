package me.FusionDev.FusionPixelmon.api;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import me.FusionDev.FusionPixelmon.api.economy.IEconomyProvider;
import me.FusionDev.FusionPixelmon.util.MathUtil;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * This Bank API contains the player's bank account and transaction operations.
 * The bank currency is the PokéDollar ($), which is the main currency of the Pixelmon mod.<br/>
 * The maximum amount of PokéDollars a player can have is $999,999. If the player attempts
 * to gain any more PokéDollars, the extra PokéDollars will be lost.<br/>
 * PokéDollars can be earned by defeating NPC Trainers and selling items to shopkeepers.<br/>
 * PokéDollars can be transferred to other players by using the <code>/transfer</code> command:
 * <pre><code>
 *     /transfer &lt;player&gt; &lt;amount&gt;
 * </code></pre>
 * PokéDollars can be spawned in by using the <code>/givemoney</code> command:
 * <pre><code>
 *     /givemoney &lt;player&gt; &lt;amount&gt;
 * </code></pre>
 */
public class BankAPI implements IEconomyProvider {
    private IPixelmonBankAccount account;

    public BankAPI(Player player) {
        account = Pixelmon.moneyManager.getBankAccount(player.getUniqueId()).orElseThrow(() -> new NullPointerException("No bank account for " + player.getName()));
    }

    @Override
    public Optional<EconomyService> getService() {
        return Optional.empty();
    }

    @Override
    public Currency getCurrency() {
        return null;
    }

    @Override
    public String getCurrencySymbol(double amount) {
        String currency = amount != 1 ? "PokeDollars" : "PokeDollar";
        return amount + " " + currency;
    }

    @Override
    public BigDecimal balance(Player player) {
        return BigDecimal.valueOf(account.getMoney());
    }

    @Override
    public boolean canAfford(Player player, double amount) {
        return account.getMoney() >= amount;
    }

    @Override
    public boolean deposit(Player player, double amount, boolean message) {
        return change(MathUtil.clamp((int) amount, 0, MAX)) != -1;
    }

    @Override
    public boolean withdraw(Player player, double amount, boolean message) {
        return change(-MathUtil.clamp((int) amount, 0, MAX)) != -1;
    }

    private final int MAX = 999999;
    private final int MIN = -999999;

    /**
     * Changes the player's balance by the specified amount.
     * The amount is clamped between {@link #MIN} and {@link #MAX},
     * so negative values (< 0) can be used for deductions/withdrawals.
     *
     * @param amount the amount to change by.
     * @return the player's balance after the change.
     */
    private int change(int amount) {
        amount = MathUtil.clamp(amount, MIN, MAX);
        int before = account.getMoney();
        int change = account.changeMoney(amount);
        int diff = change - before;
        if (diff == 0 && amount != 0) {
            // if amount > 0 - limit of money reached
            // else - no more money left
            return -1;
        }
        return change;
    }
}
