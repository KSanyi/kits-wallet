package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.PurchaseRepository;

@SuppressWarnings("serial")
class PurchasesFilterTable extends VerticalLayout {

    private final TextField quickFilter = new TextField("Gyorskereső");
    
    private final PurchasesTable table;
    
    private PurchaseRepository repository;

    PurchasesFilterTable(PurchaseRepository repository) {
        
        this.repository = repository;
        
        this.table = new PurchasesTable();
        
        refresh();
        
        quickFilter.addValueChangeListener(e -> table.filter(e.getValue()));

        createLayout();
    }
    
    private void createLayout() {
        
        HorizontalLayout filters = new HorizontalLayout(quickFilter);
        
        HorizontalLayout header = new HorizontalLayout(filters);
        
        addComponents(header, table);
        
        quickFilter.focus();
        quickFilter.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        quickFilter.setWidth("300px");
        
        setMargin(false);
    }
    
    void refresh() {
        table.setInvoices(repository.loadAll().entries());
    }
    
}
