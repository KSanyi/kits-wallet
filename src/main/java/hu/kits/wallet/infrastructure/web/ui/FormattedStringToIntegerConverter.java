package hu.kits.wallet.infrastructure.web.ui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.data.converter.StringToIntegerConverter;

public class FormattedStringToIntegerConverter extends StringToIntegerConverter {

    public static final char NUMBER_GROUPING_SEPARATOR = ' ';
    
    private final DecimalFormat format;
    
    public FormattedStringToIntegerConverter() {
        super("Nem szám");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(NUMBER_GROUPING_SEPARATOR);
        format = new DecimalFormat("###,###", decimalFormatSymbols);
        format.setMinimumIntegerDigits(1);
    }
    
    @Override
    protected NumberFormat getFormat(Locale locale) {
        return format;
    }

}
