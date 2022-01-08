package hu.kits.wallet.infrastructure.web.ui;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;

import hu.kits.wallet.Main;
import hu.kits.wallet.domain.Filter;
import hu.kits.wallet.domain.Purchases;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@PreserveOnRefresh
public class MainLayout extends AppLayout {

    private final FilterComponent filterComponent = new FilterComponent();
    
    private final Set<PurchasesChangedListener> purchasesChangedListeners = new HashSet<>();
    
    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(filterComponent);
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        //layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        layout.add(createNewPurchaseButton(), createStatsButton(), createPurchasesButton());
        return layout;
    }
    
    private Button createNewPurchaseButton() {
        Button newPurchaseButton = new Button("Vásárlás", new Icon("lumo", "plus"));
        newPurchaseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newPurchaseButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(PurchaseView.class)));
        return newPurchaseButton;
    }
    
    private Button createStatsButton() {
        Button statsButton = new Button("Stat");
        statsButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(StatsView.class)));
        return statsButton;
    }
    
    private Button createPurchasesButton() {
        Button statsButton = new Button("Vásárlások");
        statsButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate(PurchasesListView.class)));
        return statsButton;
    }

    public static MainLayout get() {
        return (MainLayout) UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == MainLayout.class)
                .findFirst().get();
    }

    public void filterChanged(Filter filter) {
        Purchases allPurchases = Main.purchaseRepository.loadAll();
        Purchases filteredPurchases = allPurchases.filter(filter);
        purchasesChangedListeners.forEach(listener -> listener.purchasesChanged(filteredPurchases));
    }
    
    static interface PurchasesChangedListener {
        void purchasesChanged(Purchases purchases);
    }

    public void addPurchasesChangedListener(PurchasesChangedListener purchasesChangedListener) {
        if(purchasesChangedListeners.add(purchasesChangedListener)) {
            purchasesChangedListener.purchasesChanged(Main.purchaseRepository.loadAll());
        }
    }

}
