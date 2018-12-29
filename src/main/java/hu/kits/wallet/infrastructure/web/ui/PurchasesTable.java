package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;

import hu.kits.wallet.domain.Purchase;

@SuppressWarnings("serial")
class PurchasesTable extends Grid<Purchase> {

    PurchasesTable() {
        
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
        
        setWidth(getColumns().stream().mapToDouble(c -> c.getWidth()).sum() + 20 + "px");
        setHeightByRows(18);
        
        setHeaderRowHeight(35);
        
        setSelectionMode(SelectionMode.SINGLE);
        
        sort("date", SortDirection.DESCENDING);
    }
    
}
