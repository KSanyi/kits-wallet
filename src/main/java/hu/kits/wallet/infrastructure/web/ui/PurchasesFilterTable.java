package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.server.Page;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.DateInterval;
import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.domain.Purchases;

@SuppressWarnings("serial")
class PurchasesFilterTable extends VerticalLayout {

    private final TextField quickFilter = new TextField("Gyorskereső");
    
    private final DateFilter dateFilter = new DateFilter();
    
    private final PurchasesTable table;
    
    private final SummaryBox summaryBox = new SummaryBox();
    
    private final Purchases allPurchases;

    PurchasesFilterTable(PurchaseRepository repository) {
        
        table = new PurchasesTable(repository);
        allPurchases = repository.loadAll();
        table.setItems(allPurchases.entries());
        summaryBox.setItems(allPurchases);
        
        quickFilter.addValueChangeListener(e -> filter(e.getValue(), dateFilter.getDateInterval()));
        dateFilter.addValueChangeListener(e -> filter(quickFilter.getValue(), e));

        createLayout();
    }
    
    private void createLayout() {
        
        HorizontalLayout filters = new HorizontalLayout(quickFilter, dateFilter);
        
        HorizontalLayout header = new HorizontalLayout(filters);
        
        if(Page.getCurrent().getWebBrowser().getScreenWidth() > 1000) {
            addComponents(header, new HorizontalLayout(table, summaryBox));
        } else {
            addComponents(header, new VerticalLayout(table, summaryBox));
        }
        
        quickFilter.focus();
        quickFilter.addStyleName(ValoTheme.TEXTFIELD_LARGE);
        quickFilter.setWidth("300px");
        
        setMargin(false);
    }
    
    private void filter(String filterString, DateInterval dateInterval) {
        Purchases filteredPurchases = allPurchases.filter(filterString, dateInterval);
        table.setItems(filteredPurchases.entries());
        summaryBox.setItems(filteredPurchases);
    }
    
}
