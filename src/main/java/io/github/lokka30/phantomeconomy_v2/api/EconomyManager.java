package io.github.lokka30.phantomeconomy_v2.api;

import io.github.lokka30.phantomeconomy_v2.PhantomEconomy;
import io.github.lokka30.phantomeconomy_v2.api.currencies.Currency;
import io.github.lokka30.phantomeconomy_v2.api.exceptions.InvalidCurrencyException;
import io.github.lokka30.phantomeconomy_v2.utils.LogLevel;

public class EconomyManager {

    private PhantomEconomy instance;

    public EconomyManager(final PhantomEconomy instance) {
        this.instance = instance;
    }

    // Other classes can get the instance if they need it
    public PhantomEconomy getInstance() {
        return instance;
    }

    // Returns new Currency class form the currency's name
    public Currency getCurrency(String name) throws InvalidCurrencyException {
        if (isCurrency(name)) {
            return new Currency(this, name);
        } else {
            throw new InvalidCurrencyException(name + " is not a valid currency.");
        }
    }

    // Check if the currency 'name' is enabled
    public boolean isCurrency(String name) {
        return instance.getFileCache().SETTINGS_CURRENCIES_ENABLED_CURRENCIES.contains(name);
    }

    // Returns the default currency
    @SuppressWarnings("unused")
    public Currency getDefaultCurrency() throws InvalidCurrencyException {
        String defaultCurrencyName = instance.getFileCache().SETTINGS_DEFAULT_CURRENCY;
        if (isCurrency(defaultCurrencyName)) {
            return getCurrency(defaultCurrencyName);
        } else {
            instance.getUtils().log(LogLevel.SEVERE, "&cSETTINGS ERROR! &7Unable to get currency '" + defaultCurrencyName + "' as was specified as the default currency. Check the default currency setting and make sure that the currency is enabled and defined. This should be fixed immediately as major issues could occur.");
            return null;
        }
    }

    // Returns the currency that Vault is used for
    public Currency getVaultCurrency() throws InvalidCurrencyException {
        String vaultCurrencyName = instance.getFileCache().SETTINGS_VAULT_CURRENCY;
        if (isCurrency(vaultCurrencyName)) {
            return getCurrency(vaultCurrencyName);
        } else {
            instance.getUtils().log(LogLevel.SEVERE, "&cSETTINGS ERROR! &7Unable to get currency '" + vaultCurrencyName + "' as was specified as the Vault currency. Check the vault currency setting and make sure that the currency is enabled and defined. This should be fixed immediately as it is likely to cause major issues with the plugins trying to use the Vault API.");
            return null;
        }
    }


}
