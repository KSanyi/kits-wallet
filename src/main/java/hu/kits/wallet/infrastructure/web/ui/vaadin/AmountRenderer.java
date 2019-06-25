package hu.kits.wallet.infrastructure.web.ui.vaadin;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

import hu.kits.wallet.common.Formatters;

public class AmountRenderer <SOURCE> extends BasicRenderer<SOURCE, Number> {

    public AmountRenderer(ValueProvider<SOURCE, Number> valueProvider) {
        super(valueProvider);
    }
    
    @Override
    protected String getFormattedValue(Number value) {
        
        return Formatters.formatAmount(value.doubleValue());
    }

}
