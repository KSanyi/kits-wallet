package hu.kits.wallet.infrastructure.web.ui;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.PurchaseRepository;

@SuppressWarnings("serial")
class PurchasesFilterTable extends VerticalLayout {

    private final TextField quickFilter = new TextField("Gyorskereső");
    
    private final PurchasesTable table = new PurchasesTable();
    
    private final SummaryBox summaryBox = new SummaryBox();
    
    private List<Purchase> allItems;

    PurchasesFilterTable(PurchaseRepository repository) {
        
        allItems = repository.loadAll().entries();
        table.setItems(allItems);
        summaryBox.setItems(allItems);
        
        quickFilter.addValueChangeListener(e -> filter(e.getValue()));

        createLayout();
    }
    
    private void createLayout() {
        
        HorizontalLayout filters = new HorizontalLayout(quickFilter);
        
        HorizontalLayout header = new HorizontalLayout(filters);
        
        addComponents(header, new HorizontalLayout(table, summaryBox));
        
        quickFilter.focus();
        quickFilter.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        quickFilter.setWidth("300px");
        
        setMargin(false);
    }
    
    private void filter(String filterText) {
        List<Purchase> filteredItems = allItems.stream().filter(p -> p.dataContains(filterText)).collect(Collectors.toList());
        table.setItems(filteredItems);
        summaryBox.setItems(filteredItems);
    }
    
}
