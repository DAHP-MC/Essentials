package com.earth2me.essentials.utils;

import net.ess3.api.IEssentials;
import net.md_5.bungee.api.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    private static final DecimalFormat twoDPlaces = new DecimalFormat("#,###.##");
    private static final DecimalFormat currencyFormat = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

    // This field is likely to be modified in com.earth2me.essentials.Settings when loading currency format.
    // This ensures that we can supply a constant formatting.
    private static NumberFormat PRETTY_FORMAT = NumberFormat.getInstance(Locale.US);

    static {
        twoDPlaces.setRoundingMode(RoundingMode.HALF_UP);
        currencyFormat.setRoundingMode(RoundingMode.FLOOR);

        PRETTY_FORMAT.setRoundingMode(RoundingMode.FLOOR);
        PRETTY_FORMAT.setGroupingUsed(true);
        PRETTY_FORMAT.setMinimumFractionDigits(2);
        PRETTY_FORMAT.setMaximumFractionDigits(2);
    }

    // this method should only be called by Essentials
    public static void internalSetPrettyFormat(NumberFormat prettyFormat) {
        PRETTY_FORMAT = prettyFormat;
    }

    public static String shortCurrency(final BigDecimal value, final IEssentials ess) {
        return getRPMoney(value);
    }

    public static String formatDouble(final double value) {
        return getRPMoney(new BigDecimal(value));
    }

    public static String displayCurrency(final BigDecimal value, final IEssentials ess) {
        return getRPMoney(value);
    }

    public static String displayCurrencyExactly(final BigDecimal value, final IEssentials ess) {
        return getRPMoney(value);
    }

    public static String sanitizeCurrencyString(final String input, final IEssentials ess) {
        String symbol = ess.getSettings().getCurrencySymbol();
        boolean suffix = ess.getSettings().isCurrencySymbolSuffixed();
        if (input.contains(symbol)) {
            return suffix ? input.substring(0, input.indexOf(symbol)) : input.substring(symbol.length());
        }
        return getRPMoney(new BigDecimal(input));
    }

    public static boolean isInt(final String sInt) {
        try {
            Integer.parseInt(sInt);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isPositiveInt(final String sInt) {
        if (!isInt(sInt)) {
            return false;
        }
        return Integer.parseInt(sInt) > 0;
    }

    private static String getRPMoney(BigDecimal value) {
        /*
         * One Standard currency is equal to 100 knuts
         * Examples:
         * 1.00 = 0 Galleons, 3 Sickles and 13 Knuts
         * 10.00 = 2 Galleons, 0 Sickles and 14 Knuts
         */
        int tempKnuts = (int) (value.doubleValue() * 100d);
        int knuts = tempKnuts % 29;
        int tempSickles = tempKnuts / 29;
        int sickles = tempSickles % 17;
        int galleons = tempSickles / 17;
        return ChatColor.of("#FFD700") + "" + galleons + " Galleon" + (galleons == 1 ? "" : "s")
                + ChatColor.of("#A0A0A0") + ", "
                + ChatColor.of("#C0C0C0") + sickles + " Sickle" + (sickles == 1 ? "" : "s")
                + ChatColor.of("#A0A0A0") + " and "
                + ChatColor.of("#CD7F32") + knuts + " Knut" + (knuts == 1 ? "" : "s");
    }
}
