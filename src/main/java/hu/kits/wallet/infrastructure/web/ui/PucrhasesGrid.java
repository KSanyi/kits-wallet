package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.data.renderer.ComponentRenderer;

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
        
        addColumn(new ComponentRenderer<>(this::createEditButton))
            .setFlexGrow(0);
        
        setSelectionMode(SelectionMode.NONE);
        
        // sort
    }
    
    private Button createEditButton(Purchase purchase) {
        Button edit = new Button("Szer", click -> getUI().ifPresent(ui -> ui.navigate(PurchaseView.class, purchase.id)));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return edit;
    }

}
