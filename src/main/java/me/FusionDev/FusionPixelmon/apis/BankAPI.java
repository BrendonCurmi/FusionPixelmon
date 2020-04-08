package me.FusionDev.FusionPixelmon.apis;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import org.spongepowered.api.entity.living.player.Player;

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
public class BankAPI {
    private IPixelmonBankAccount account;

    public BankAPI(Player player) {
        account = Pixelmon.moneyManager.getBankAccount(player.getUniqueId()).orElseThrow(() -> new NullPointerException("No bank account for " + player.getName()));
    }

    /**
     * Checks the amount of money the Player has.
     *
     * @return the amount of money the player has.
     */
    public int balance() {
        return account.getMoney();
    }

    /**
     * Checks if the player can afford the specified amount.
     * For a player to afford an amount, they must have more or
     * equal to the specified amount.
     *
     * @param amount the amount to check.
     * @return true if the player can afford the amount; otherwise false.
     */
    public boolean canAfford(int amount) {
        return account.getMoney() >= amount;
    }

    /**
     * Deposits the specified amount into the player's account.
     *
     * @param amount the amount to deposit.
     * @return the new balance after the deposit.
     */
    public int deposit(int amount) {
        return change(MathHelper.clamp(amount, 0, MAX));
    }

    /**
     * Withdraws the specified amount from the player's account.
     *
     * @param amount the amount to withdraw.
     * @return the new balance after the withdrawal.
     */
    public int withdraw(int amount) {
        return change(-MathHelper.clamp(amount, 0, MAX));
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
    public int change(int amount) {
        amount = MathHelper.clamp(amount, MIN, MAX);
        int before = balance();
        int change = account.changeMoney(amount);
        int diff = change - before;
        if (diff == 0) {
            if (amount > 0) {}//limit of money reached
            else {}//no more money left
        }
        return change;
    }
}
