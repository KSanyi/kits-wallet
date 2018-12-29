package hu.kits.wallet.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatters {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat AMOUNT_FORMAT;
    private final static DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.0%");
    private final static DecimalFormat PERCENT_FORMAT2 = new DecimalFormat("0.00%");
    
    public static final Locale HU_LOCALE = new Locale("HU");
    
    static {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator((char) 160);
        AMOUNT_FORMAT = new DecimalFormat("###,###", decimalFormatSymbols);
    }
    
    public static String formatDate(LocalDate date) {
        return DATE_FORMAT.format(date);
    }
    
    public static String formatAmount(double amount) {
        return AMOUNT_FORMAT.format(amount);
    }
    
    public static String formatPercent(double amount) {
        return PERCENT_FORMAT.format(amount);
    }
    
    public static String formatPercent2(double amount) {
        return PERCENT_FORMAT2.format(amount);
    }
    
    public static String formatDecimal(double amount) {
        return DECIMAL_FORMAT.format(amount);
    }
    
}
