package hu.kits.wallet.infrastructure.web.ui;

import static java.util.Comparator.comparing;

import java.util.List;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import hu.kits.wallet.common.Formatters;
import hu.kits.wallet.domain.Purchase;

public class PucrhasesGrid extends Grid<Purchase> {
    
    public PucrhasesGrid() {
        setSizeFull();
        
        addColumn(p -> Formatters.formatDate(p.date()))
            .setKey("date")
            .setHeader("Dátum")
            .setWidth("120px")
            .setFlexGrow(2)
            .setSortable(true)
            .setFrozen(true);
        
        final String amountTemplate = "<div style='text-align: right'>[[item.amount]]</div>";
        addColumn(TemplateRenderer.<Purchase>of(amountTemplate).withProperty("amount", purchase -> Formatters.formatAmount(purchase.amount()) + " Ft"))
            .setHeader("Összeg")
            .setFlexGrow(2)
            .setComparator(comparing(Purchase::amount))
            .setTextAlign(ColumnTextAlign.END);
        
        addColumn(Purchase::shop)
            .setHeader("Bolt")
            .setFlexGrow(3)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setSortable(true);
        
        addColumn(Purchase::subject)
            .setFlexGrow(3)
            .setHeader("Name");
        
        addColumn(p -> p.category().name())
            .setHeader("Kategória")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setFlexGrow(3)
            .setSortable(true);
        
        addColumn(Purchase::comment)
            .setFlexGrow(10)
            .setHeader("Megjegyzés");
        
        addColumn(Purchase::account)
            .setHeader("Acc.")
            .setFlexGrow(1);
        
        addColumn(Purchase::timestamp)
            .setKey("timestamp")
            .setHeader("TS")
            .setWidth("160px")
            .setSortable(true)
            .setFlexGrow(1);
        
        addSelectionListener(e -> getUI().ifPresent(ui -> e.getFirstSelectedItem().ifPresent(item -> ui.navigate(PurchaseView.class, item.id()))));
        
        sort(List.of(new GridSortOrder<>(getColumnByKey("date"), SortDirection.DESCENDING),new GridSortOrder<>(getColumnByKey("timestamp"), SortDirection.DESCENDING)));
    }

}
