package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import hu.kits.wallet.domain.Purchases;
import hu.kits.wallet.infrastructure.web.ui.MainLayout.PurchasesChangedListener;

@Route(value = "stats", layout = MainLayout.class)
@PageTitle("Statisztikák")
public class StatsView extends Div implements PurchasesChangedListener {

    private final SummaryBox summaryBox = new SummaryBox();

    public StatsView() {
        
        setSizeFull();
        
        add(summaryBox);
        summaryBox.setWidth(null);
        summaryBox.setMinWidth("300px");
        
        add(summaryBox);
    }
    
    @Override
    public void purchasesChanged(Purchases purchases) {
        summaryBox.setItems(purchases);
        
    }

}
