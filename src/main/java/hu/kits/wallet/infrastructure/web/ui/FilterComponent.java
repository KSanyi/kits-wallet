package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import hu.kits.wallet.domain.Filter;

class FilterComponent extends VerticalLayout {

    private final TextField filterTextField = new TextField("", "Szűrés");
    
    private final DateFilter dateFilter = new DateFilter();
    
    FilterComponent() {
        filterTextField.setPrefixComponent(new Icon("lumo", "search"));
        filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterTextField.setClearButtonVisible(true);
        filterTextField.setWidth("220px");
        
        filterTextField.addValueChangeListener(e -> filterChanged());
        dateFilter.addValueChangeListener(e -> filterChanged());
        
        add(new H5("Szűrő"), filterTextField, dateFilter);
    }
    
    private void filterChanged() {
        MainLayout.get().filterChanged(new Filter(filterTextField.getValue(), dateFilter.getDateInterval()));
    }
    
}
