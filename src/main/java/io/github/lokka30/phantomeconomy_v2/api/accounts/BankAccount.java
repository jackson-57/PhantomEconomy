package io.github.lokka30.phantomeconomy_v2.api.accounts;

import io.github.lokka30.phantomeconomy_v2.api.currencies.Currency;
import io.github.lokka30.phantomeconomy_v2.api.exceptions.NegativeAmountException;
import io.github.lokka30.phantomeconomy_v2.api.exceptions.OversizedWithdrawAmountException;

import java.util.HashMap;

@SuppressWarnings("unused")
public class BankAccount {

    private AccountManager accountManager;

    private String name;

    public BankAccount(AccountManager accountManager, String name) {
        this.accountManager = accountManager;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getBalance(Currency currency) {
        if (!accountManager.cachedBankAccountBalances.containsKey(getName())) {
            HashMap<Currency, Double> balanceMap = new HashMap<>();
            balanceMap.put(currency, accountManager.getInstance().getDatabase().getBalance("BankAccount", getName(), currency.getName()));
            accountManager.cachedBankAccountBalances.put(getName(), balanceMap);
        } else if (!accountManager.cachedBankAccountBalances.get(getName()).containsKey(currency)) {
            HashMap<Currency, Double> balanceMap = accountManager.cachedBankAccountBalances.get(getName());
            balanceMap.put(currency, accountManager.getInstance().getDatabase().getBalance("BankAccount", getName(), currency.getName()));
            accountManager.cachedBankAccountBalances.put(getName(), balanceMap);
        }

        return accountManager.cachedBankAccountBalances.get(getName()).get(currency);
    }

    public void setBalance(Currency currency, double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Tried to set balance to BankAccount with name '" + getName() + "' and amount '" + amount + "' but the amount is lower than 0");
        } else {
            HashMap<Currency, Double> balanceMap = new HashMap<>();
            balanceMap.put(currency, amount);
            accountManager.cachedBankAccountBalances.put(getName(), balanceMap);
            accountManager.getInstance().getDatabase().setBalance("BankAccount", getName(), currency.getName(), amount);
        }
    }

    public void deposit(Currency currency, double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Tried to set balance to BankAccount with name '" + getName() + "' and amount '" + amount + "' but the amount is lower than 0");
        } else {
            setBalance(currency, getBalance(currency) + amount);
        }
    }

    public void withdraw(Currency currency, double amount) throws NegativeAmountException, OversizedWithdrawAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Tried to set balance to BankAccount with name '" + getName() + "' and amount '" + amount + "' but the amount is lower than 0");
        } else {
            if (getBalance(currency) < amount) {
                throw new OversizedWithdrawAmountException(amount + " is too large for the BankAccount named '" + getName() + "' which has a balance of '" + getBalance(currency) + "'.");
            } else {
                setBalance(currency, getBalance(currency) - amount);
            }
        }
    }

    public boolean has(Currency currency, double amount) {
        return getBalance(currency) >= amount;
    }
}
