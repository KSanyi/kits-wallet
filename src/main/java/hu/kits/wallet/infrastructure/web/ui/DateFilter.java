package hu.kits.wallet.infrastructure.web.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import hu.kits.wallet.common.Clock;
import hu.kits.wallet.domain.DateInterval;

class DateFilter extends VerticalLayout {

    private final List<Consumer<DateInterval>> listeners = new ArrayList<>();
    
    private List<Button> monthButtons = createMonthButtons();
    private List<Button> yearsButtons = createYearButtons();
    private final ComboBox<LocalDate> monthCombo = new ComboBox<>();
    
    private final DatePicker fromDateField = new DatePicker("");
    private final DatePicker toDateField = new DatePicker("");
    
    DateFilter() {
        
        setPadding(false);
        
        addClassName("date-filter");
        
        fromDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        fromDateField.setLocale(new Locale("HU"));
        fromDateField.setWidth("140px");
        toDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        toDateField.setLocale(new Locale("HU"));
        toDateField.setWidth("140px");
        
        add(fromDateField, toDateField, monthCombo);
        monthButtons.forEach(this::add);
        yearsButtons.forEach(this::add);
    }

    private List<Button> createYearButtons() {
        List<Button> buttons = new ArrayList<>();
        for(int i=Clock.today().getYear();i >=2018;i--) {
            int year = i;
            Button button = new Button(String.valueOf(year), click -> yearSelected(year));
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            buttons.add(button);
        }
        return buttons;
    }
    
    private List<Button> createMonthButtons() {
        List<Button> buttons = new ArrayList<>();
        
        for(int i=0;i<12;i++) {
            LocalDate date = Clock.today().minusMonths(i);    
            Button button = new Button(String.valueOf(date.getMonth()), click -> yearMonthSelected(date.getYear(), date.getMonthValue()));
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            buttons.add(button);
        }
        return buttons;
    }

    private void yearSelected(int year) {
        toDateField.setValue(LocalDate.MAX);
        fromDateField.setValue(LocalDate.of(year, 1, 1));
        toDateField.setValue(LocalDate.of(year, 12, 31));
    }
    
    private void yearMonthSelected(int year, int monthValue) {
        toDateField.setValue(LocalDate.MAX);
        fromDateField.setValue(LocalDate.of(year, monthValue, 1));
        toDateField.setValue(LocalDate.of(year, monthValue, 1).plusMonths(1).minusDays(1));
    }

    DateInterval getDateInterval() {
        return new DateInterval(fromDateField.getValue(), toDateField.getValue());
    }

    void addValueChangeListener(Consumer<DateInterval> listener) {
        listeners.add(listener);
    }
    
}
