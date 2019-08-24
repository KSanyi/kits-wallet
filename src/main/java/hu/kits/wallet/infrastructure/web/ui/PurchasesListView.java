package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import hu.kits.wallet.Main;
import hu.kits.wallet.domain.Purchases;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Vásárlások")
public class PurchasesListView extends HorizontalLayout {

    private final TextField searchField = new TextField("", "Szűrés");
    private final DateFilter dateFilter = new DateFilter();
    private final PucrhasesGrid grid = new PucrhasesGrid();
    private final SummaryBox summaryBox = new SummaryBox();

    public PurchasesListView() {
        
        setSizeFull();
        
        add(summaryBox);
        summaryBox.setWidth(null);
        summaryBox.setMinWidth("300px");
        
        Component tableWithSearchBar = createTableWithSearchBar();
        
        add(tableWithSearchBar);
        setAlignSelf(Alignment.END, summaryBox);
        setFlexGrow(1, summaryBox);
        setFlexGrow(3, tableWithSearchBar);

        updateView();
    }
    
    private Component createTableWithSearchBar() {
        VerticalLayout tableWithSearchBar = new VerticalLayout();
        Component searchBar = createSearchBar();
        tableWithSearchBar.add(searchBar);
        tableWithSearchBar.add(grid);
        tableWithSearchBar.setFlexGrow(0, searchBar);
        tableWithSearchBar.setFlexGrow(1, grid);
        tableWithSearchBar.setSizeFull();
        tableWithSearchBar.expand(grid);
        tableWithSearchBar.setPadding(false);
        tableWithSearchBar.getStyle().set("padding-bottom", "10px");
        
        return tableWithSearchBar;
    }
    
    private Component createSearchBar() {
        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.setClearButtonVisible(true);
        searchField.setWidth("250px");

        dateFilter.addClassName("date-filter");
        dateFilter.addValueChangeListener(e -> updateView());
        
        Button createNewPurchaseButton = createNewPurchaseButton();
        HorizontalLayout searchbar = new HorizontalLayout(searchField, dateFilter, createNewPurchaseButton);
        
        searchbar.setWidthFull();
        return searchbar;
    }
    
    private Button createNewPurchaseButton() {
        Button newPurchaseButton = new Button("Vásárlás", new Icon("lumo", "plus"));
        newPurchaseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newPurchaseButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(PurchaseView.class)));
        return newPurchaseButton;
    }

    private void updateView() {
        Purchases allPurchases = Main.purchaseRepository.loadAll();
        Purchases filteredPurchases = allPurchases.filter(searchField.getValue(), dateFilter.getDateInterval());
        grid.setItems(filteredPurchases.entries());
        summaryBox.setItems(filteredPurchases);
    }

}
