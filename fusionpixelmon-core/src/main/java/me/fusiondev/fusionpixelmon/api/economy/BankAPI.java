package me.fusiondev.fusionpixelmon.api.economy;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.impl.MathUtils;

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

    public BankAPI(AbstractPlayer player) {
        account = Pixelmon.moneyManager.getBankAccount(player.getUniqueId()).orElseThrow(() -> new NullPointerException("No bank account for " + player.getName()));
    }

    @Override
    public Optional<Object> getService() {
        return Optional.empty();
    }

    @Override
    public Object getCurrency() {
        return null;
    }

    @Override
    public String getCurrencyName(double amount) {
        String currency = amount != 1 ? "PokeDollars" : "PokeDollar";
        return amount + " " + currency;
    }

    @Override
    public BigDecimal balance(AbstractPlayer player) {
        return BigDecimal.valueOf(account.getMoney());
    }

    @Override
    public boolean canAfford(AbstractPlayer player, double amount) {
        return account.getMoney() >= amount;
    }

    @Override
    public boolean deposit(AbstractPlayer player, double amount, boolean message) {
        return change(MathUtils.clamp((int) amount, 0, MAX)) != -1;
    }

    @Override
    public boolean withdraw(AbstractPlayer player, double amount, boolean message) {
        return change(-MathUtils.clamp((int) amount, 0, MAX)) != -1;
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
        amount = MathUtils.clamp(amount, MIN, MAX);
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
