package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import hu.kits.wallet.common.Clock;
import hu.kits.wallet.domain.Purchases;
import hu.kits.wallet.infrastructure.web.ui.MainLayout.PurchasesChangedListener;

@Route(value = "purchases", layout = MainLayout.class)
@PageTitle("Vásárlások")
@CssImport(value = "./styles/purchases-list-view.css", include = "lumo-badge")
@PreserveOnRefresh
public class PurchasesListView extends Div implements PurchasesChangedListener {

    private final PucrhasesGrid grid = new PucrhasesGrid();

    public PurchasesListView() {
        
        setId("purchases-list-view");
        addClassName("purchases-list-view");
        
        setSizeFull();
        
        add(grid);
    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        MainLayout.get().addPurchasesChangedListener(this);
    }
    
    @Override
    public void purchasesChanged(Purchases purchases) {
        var items = purchases.entries();
        int numberOfFutureItems = (int)items.stream().filter(i -> i.date().isAfter(Clock.today())).count();
        grid.setItems(items);
        grid.scrollToIndex(numberOfFutureItems);
    }

}
