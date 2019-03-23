package hu.kits.wallet.infrastructure.web.ui;

import static hu.kits.wallet.common.Clock.today;
import static java.time.Period.ofMonths;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.DateInterval;

@SuppressWarnings("serial")
public class DateFilter extends HorizontalLayout {

    private final List<Consumer<DateInterval>> listeners = new ArrayList<>();
    
    private final Button thisMonthButton = new Button("This month", click -> thisMonthSelected());
    private final Button thisYearButton = new Button("This year", click -> thisYearSelected());
    private final ComboBox<LocalDate> monthCombo = new ComboBox<>("Hónap");
    
    private final DateField fromDateField = new DateField("");
    private final DateField toDateField = new DateField("");
    
    DateFilter() {
        
        thisMonthButton.addStyleName(ValoTheme.BUTTON_LINK);
        thisYearButton.addStyleName(ValoTheme.BUTTON_LINK);
        fromDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        fromDateField.setWidth("140px");
        fromDateField.setDateFormat("yyyy.MM.dd");
        toDateField.addValueChangeListener(e -> listeners.stream().forEach(l -> l.accept(getDateInterval())));
        toDateField.setWidth("140px");
        toDateField.setDateFormat("yyyy.MM.dd");
        
        monthCombo.setItemCaptionGenerator(d -> d.getYear() + " " + d.getMonth().toString().toLowerCase());
        monthCombo.setWidth("160px");
        monthCombo.setItems(createMonths());
        monthCombo.addValueChangeListener(e -> monthSelected(e.getValue()));
        
        VerticalLayout buttonsLayout = new VerticalLayout(thisMonthButton, thisYearButton);
        buttonsLayout.setMargin(false);
        buttonsLayout.setSpacing(false);
        
        addComponents(fromDateField, toDateField, monthCombo, buttonsLayout);
    }

    private List<LocalDate> createMonths() {
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
