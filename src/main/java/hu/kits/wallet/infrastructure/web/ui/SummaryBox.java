package hu.kits.wallet.infrastructure.web.ui;

import java.util.List;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.common.Formatters;
import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.Purchases;

@SuppressWarnings("serial")
public class SummaryBox extends FormLayout {

    public void setItems(List<Purchase> filteredItems) {
        
        removeAllComponents();
        
        Purchases purchases = new Purchases(filteredItems);

        addComponent(createTextField("SUM", purchases.sum()));
        addComponent(createTextField("Current month", purchases.currentMonthSum()));
        addComponent(createTextField("Monthly avg", purchases.monthlyAverage()));
        addComponent(createTextField("Daily avg", purchases.dailyAverage()));
    }
    
    private TextField createTextField(String caption, int value) {
        
        TextField textField = new TextField(caption, Formatters.formatAmount(value));
        textField.setReadOnly(true);
        textField.setWidth("120px");
        textField.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
        
        return textField;
    }

}
