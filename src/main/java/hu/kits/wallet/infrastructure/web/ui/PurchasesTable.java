package hu.kits.wallet.infrastructure.web.ui;

import java.util.List;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.PurchaseRepository;

@SuppressWarnings("serial")
class PurchasesTable extends Grid<Purchase> {

    private final PurchaseRepository repository;
    
    PurchasesTable(PurchaseRepository repository) {
        
        this.repository = repository;
        
        addColumn(p -> p.account)
            .setCaption("A")
            .setWidth(70)
            .setStyleGenerator(i -> "v-align-center");
        
        addColumn(p -> p.date)
            .setId("date")
            .setCaption("Dátuma")
            .setWidth(130)
            .setStyleGenerator(i -> "v-align-center");
        
        addColumn(p -> p.amount)
            .setCaption("Összege")
            .setWidth(120)
            .setStyleGenerator(i -> "v-align-right");
        
        addColumn(p -> p.shop)
            .setCaption("Bolt")
            .setWidth(200);
        
        addColumn(p -> p.subject)
            .setCaption("Tárgy")
            .setWidth(300);
        
        addColumn(p -> p.category)
            .setCaption("Kategória")
            .setWidth(150);
        
        addColumn(p -> p.comment)
            .setCaption("Megjegyzés")
            .setWidth(300)
            .setDescriptionGenerator(i -> i.comment);
        
        addColumn(i -> createDelButtonCell(i), new ComponentRenderer())
            .setCaption("")
            .setWidth(70)
            .setStyleGenerator(i -> "v-align-center");
        
        setWidth(getColumns().stream().mapToDouble(c -> c.getWidth()).sum() + 20 + "px");
        setHeightByRows(18);
        
        setHeaderRowHeight(35);
        
        setSelectionMode(SelectionMode.SINGLE);
        
        sort("date", SortDirection.DESCENDING);
    }
    
    private Component createDelButtonCell(Purchase purchase) {
        Button button = new Button(VaadinIcons.TRASH);
        button.addStyleNames(ValoTheme.BUTTON_TINY, ValoTheme.BUTTON_DANGER);
        button.addClickListener(click -> ConfirmationDialog.open("Biztos, hogy törlöd a " + purchase.subject + " vásárlást?", () -> deletePurchase(purchase)));
        return button;
    }
    
    private void deletePurchase(Purchase purchase) {
        repository.delete(purchase.id);
        Notification.show("Vásárlás törölve");
        List<Purchase> entries = repository.loadAll().entries();
        setItems(entries);
    }
    
}
