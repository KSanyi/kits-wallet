package hu.kits.wallet.infrastructure.web.ui;

import static hu.kits.wallet.common.Clock.today;
import static java.time.Period.ofMonths;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import hu.kits.wallet.common.Clock;
import hu.kits.wallet.domain.DateInterval;

public class DateFilter extends HorizontalLayout {

    private final List<Consumer<DateInterval>> listeners = new ArrayList<>();
    
    private final Button thisMonthButton = new Button(Clock.today().getMonth().name(), click -> thisMonthSelected());
    private final Button thisYearButton = new Button(String.valueOf(Clock.today().getYear()), click -> thisYearSelected());
    private final ComboBox<LocalDate> monthCombo = new ComboBox<>();
    
    private final DatePicker fromDateField = new DatePicker("");
    private final DatePicker toDateField = new DatePicker("");
    
    DateFilter() {
        
        fromDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        fromDateField.setLocale(new Locale("HU"));
        fromDateField.setWidth("160px");
        toDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        toDateField.setLocale(new Locale("HU"));
        toDateField.setWidth("160px");
        
        monthCombo.setPlaceholder("Hónap");
        monthCombo.setItemLabelGenerator(d -> d.getYear() + " " + d.getMonth().toString().toLowerCase());
        monthCombo.setItems(createMonths());
        monthCombo.addValueChangeListener(e -> monthSelected(e.getValue()));
        
        //VerticalLayout buttonsBar = new VerticalLayout(thisMonthButton, thisYearButton);
        //buttonsBar.setMargin(false);
        //buttonsBar.setSpacing(false);
        
        thisMonthButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        thisYearButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        
        add(fromDateField, toDateField, monthCombo, thisMonthButton, thisYearButton);
    }

    private static List<LocalDate> createMonths() {
        LocalDate start = LocalDate.of(2018,7,1);
        return start.datesUntil(today(), ofMonths(1))
                .sorted(reverseOrder())
                .collect(toList());
    }

    private void thisMonthSelected() {
        monthSelected(today());
        monthCombo.clear();
    }
    
    private void monthSelected(LocalDate month) {
        if(month != null) {
            toDateField.setValue(LocalDate.MAX);
            fromDateField.setValue(month.withDayOfMonth(1));
            toDateField.setValue(month.plusMonths(1).withDayOfMonth(1).minusDays(1));            
        }
    }
    
    private void thisYearSelected() {
        toDateField.setValue(LocalDate.MAX);
        fromDateField.setValue(today().withDayOfYear(1));
        toDateField.setValue(today().plusYears(1).withDayOfMonth(1).minusDays(1));
    }

    public DateInterval getDateInterval() {
        return new DateInterval(fromDateField.getValue(), toDateField.getValue());
    }

    public void addValueChangeListener(Consumer<DateInterval> listener) {
        listeners.add(listener);
    }
    
}
