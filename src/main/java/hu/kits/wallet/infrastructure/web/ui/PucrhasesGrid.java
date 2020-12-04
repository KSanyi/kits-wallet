package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import hu.kits.wallet.common.Formatters;
import hu.kits.wallet.domain.Purchase;

class PucrhasesGrid extends Grid<Purchase> {
    
    PucrhasesGrid() {
        addSelectionListener(e -> getUI().ifPresent(ui -> e.getFirstSelectedItem().ifPresent(item -> ui.navigate(PurchaseView.class, item.id()))));
        
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        addComponentColumn(PucrhasesGrid::createCard);
    }
    
    private static HorizontalLayout createCard(Purchase purchase) {
        
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(purchase.subject());
        name.addClassName("name");
        header.add(name);

        Span comment = new Span(purchase.comment());
        comment.addClassName("post");

        HorizontalLayout footer = new HorizontalLayout();
        footer.addClassName("actions");
        footer.setSpacing(false);
        footer.getThemeList().add("spacing-s");

        IronIcon shopIcon = new IronIcon("vaadin", "heart");
        Span shop = new Span(purchase.shop());
        shop.addClassName("likes");
        
        Span date = new Span(Formatters.formatDate(purchase.date()));
        date.addClassName("comments");

        IronIcon categoryIcon = new IronIcon("vaadin", "heart");
        Span category = new Span(purchase.category().name());
        category.addClassName("likes");
        
        footer.add(shopIcon, shop, date, categoryIcon, category);

        description.add(header, comment, footer);
        
        H4 amount = new H4(Formatters.formatAmount(purchase.amount()) + " Ft");
        amount.setWidth("100px");
        
        card.add(amount, description);
        //card.setVerticalComponentAlignment(Alignment.CENTER, amount);
        return card;
    }

}
