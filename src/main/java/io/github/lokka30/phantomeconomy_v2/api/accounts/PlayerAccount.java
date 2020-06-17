package io.github.lokka30.phantomeconomy_v2.api.accounts;

import io.github.lokka30.phantomeconomy_v2.api.currencies.Currency;
import io.github.lokka30.phantomeconomy_v2.api.exceptions.NegativeAmountException;
import io.github.lokka30.phantomeconomy_v2.api.exceptions.OversizedWithdrawAmountException;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerAccount {

    private AccountManager accountManager;

    private OfflinePlayer offlinePlayer;

    public PlayerAccount(AccountManager accountManager, OfflinePlayer offlinePlayer) {
        this.accountManager = accountManager;
        this.offlinePlayer = offlinePlayer;
    }

    public OfflinePlayer getPlayer() {
        return offlinePlayer;
    }

    public UUID getUUID() {
        return offlinePlayer.getUniqueId();
    }

    public String getUUIDAsString() {
        return getUUID().toString();
    }

    public double getBalance(Currency currency) {
        if (!accountManager.cachedPlayerAccountBalances.containsKey(getUUID())) {
            //TODO get the balance from the database and put it into the cache
        }

        return accountManager.cachedPlayerAccountBalances.get(getUUID()).get(currency);
    }

    public void setBalance(Currency currency, double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Tried to set balance to PlayerAccount with uuid '" + getUUIDAsString() + "' and amount '" + amount + "' but the amount is lower than 0");
        } else {
            //TODO set tbe balance in the cache and in the database.
        }
    }

    public void deposit(Currency currency, double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Tried to set balance to PlayerAccount with uuid '" + getUUIDAsString() + "' and amount '" + amount + "' but the amount is lower than 0");
        } else {
            setBalance(currency, getBalance(currency) + amount);
        }
    }

    public void withdraw(Currency currency, double amount) throws NegativeAmountException, OversizedWithdrawAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Tried to set balance to PlayerAccount with uuid '" + getUUIDAsString() + "' and amount '" + amount + "' but the amount is lower than 0");
        } else {
            if (getBalance(currency) < amount) {
                throw new OversizedWithdrawAmountException(amount + " is too large for the PlayerAccount with uuid '" + getUUIDAsString() + "' which has a balance of '" + getBalance(currency) + "'.");
            } else {
                setBalance(currency, getBalance(currency) - amount);
            }
        }
    }

    public boolean has(Currency currency, double amount) {
        return getBalance(currency) >= amount;
    }
}
