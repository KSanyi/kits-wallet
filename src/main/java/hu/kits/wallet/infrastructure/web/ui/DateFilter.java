package hu.kits.wallet.infrastructure.web.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.common.Clock;
import hu.kits.wallet.domain.DateInterval;

@SuppressWarnings("serial")
public class DateFilter extends HorizontalLayout {

    private final List<Consumer<DateInterval>> listeners = new ArrayList<>();
    
    private Button thisMonthButton = new Button("This month", click -> thisMonthSelected());
    private Button thisYearButton = new Button("This year", click -> thisYearSelected());
    
    private final DateField fromDateField = new DateField("");
    private final DateField toDateField = new DateField("");
    
    DateFilter() {
        
        thisMonthButton.addStyleName(ValoTheme.BUTTON_LINK);
        thisYearButton.addStyleName(ValoTheme.BUTTON_LINK);
        fromDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        toDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        
        VerticalLayout buttonsLayout = new VerticalLayout(thisMonthButton, thisYearButton);
        buttonsLayout.setMargin(false);
        buttonsLayout.setSpacing(false);
        
        addComponents(fromDateField, toDateField, buttonsLayout);
    }

    private void thisMonthSelected() {
        fromDateField.setValue(Clock.today().withDayOfMonth(1));
        toDateField.setValue(Clock.today().plusMonths(1).withDayOfMonth(1).minusDays(1));
    }
    
    private void thisYearSelected() {
        fromDateField.setValue(Clock.today().withDayOfYear(1));
        toDateField.setValue(Clock.today().plusYears(1).withDayOfMonth(1).minusDays(1));
    }

    public DateInterval getDateInterval() {
        return new DateInterval(fromDateField.getValue(), toDateField.getValue());
    }

    public void addValueChangeListener(Consumer<DateInterval> listener) {
        listeners.add(listener);
    }
    
}
