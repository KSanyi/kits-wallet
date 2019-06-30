package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import hu.kits.wallet.Main;
import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.domain.Purchases;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Vásárlások")
public class PurchasesListView extends VerticalLayout {

    private final TextField searchField = new TextField("", "Szűrés");
    private final DateFilter dateFilter = new DateFilter();
    private final PucrhasesGrid grid = new PucrhasesGrid();
    private final SummaryBox summaryBox = new SummaryBox();

    //private final CategoryEditorDialog form = new CategoryEditorDialog(this::saveCategory, this::deleteCategory);

    public PurchasesListView() {
        
        initView();

        add(createSearchBar());
        add(createContent());

        updateView();
    }
    
    private static PurchaseRepository purchaseRepository() {
        return Main.purchaseRepository;
    }

    private void initView() {
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

    private Component createSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        dateFilter.addClassName("nomobile");
        dateFilter.addValueChangeListener(e -> updateView());
        
        viewToolbar.add(searchField, dateFilter, createNewPurchaseButton());
        return viewToolbar;
    }
    
    private Button createNewPurchaseButton() {
        Button newPurchaseButton = new Button("Vásárlás", new Icon("lumo", "plus"));
        newPurchaseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newPurchaseButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(PurchaseView.class)));
        
        /*
         * This is a fall-back method: '+' is not a event.code (DOM events), so as a
         * fall-back shortcuts will perform a character-based comparison. Since Key.ADD
         * changes locations frequently based on the keyboard language, we opted to use
         * a character instead.
         */
        newPurchaseButton.addClickShortcut(Key.of("+"));
        
        return newPurchaseButton;
    }

    private Component createContent() {
        HorizontalLayout container = new HorizontalLayout();
        container.setClassName("view-container");

        summaryBox.setMinWidth("250px");
        summaryBox.addClassName("nomobile");
        
        VerticalLayout gridHolder = new VerticalLayout(grid);
        gridHolder.setMargin(false);
        gridHolder.setPadding(false);
        
        container.add(summaryBox, gridHolder);
        return container;
    }

    private void updateView() {
        Purchases allPurchases = purchaseRepository().loadAll();
        Purchases filteredPurchases = allPurchases.filter(searchField.getValue(), dateFilter.getDateInterval());
        grid.setItems(filteredPurchases.entries());
        summaryBox.setItems(filteredPurchases);
    }

}
