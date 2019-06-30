package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;

import hu.kits.wallet.common.Formatters;
import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.infrastructure.web.ui.vaadin.AmountRenderer;

public class PucrhasesGrid extends Grid<Purchase> {
    
    public PucrhasesGrid() {
        
        addColumn(p -> Formatters.formatDate(p.date))
            .setHeader("Dátum")
            .setWidth("120px")
            .setFlexGrow(0)
            .setSortable(true)
            .setFrozen(true);
        
        addColumn(new AmountRenderer<>(p -> p.amount))
            .setHeader("Összeg")
            .setWidth("100px")
            .setFlexGrow(0)
            .setSortable(true) // sort is broken when using renderers
            .setTextAlign(ColumnTextAlign.END);
        
        addColumn(p -> p.shop)
            .setHeader("Bolt")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setSortable(true);
        
        addColumn(p -> p.subject)
            .setHeader("Name");
        
        addColumn(p -> p.category.name())
            .setHeader("Kategória")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setSortable(true);
        
        addColumn(p -> p.comment)
            .setHeader("Megjegyzés");
        
        addColumn(p -> p.account)
            .setHeader("Acc.")
            .setWidth("80px")
            .setFlexGrow(0);
        
        addSelectionListener(e -> getUI().ifPresent(ui -> e.getFirstSelectedItem().ifPresent(item -> ui.navigate(PurchaseView.class, item.id))));
    }

}
