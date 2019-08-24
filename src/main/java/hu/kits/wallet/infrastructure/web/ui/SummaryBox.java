package hu.kits.wallet.infrastructure.web.ui;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import hu.kits.wallet.common.Clock;
import hu.kits.wallet.common.Formatters;
import hu.kits.wallet.common.Pair;
import hu.kits.wallet.domain.Purchases;

class SummaryBox extends VerticalLayout {

    // for the scrollbar to appear, see https://stackoverflow.com/questions/53849728/scrollable-layout-in-vaadin-flow
    private final VerticalLayout content = new VerticalLayout();
    
    SummaryBox() {
        addClassName("summary-box");
        setHeightFull();
        getStyle().set("overflow", "auto");
        
        content.setPadding(false);
        add(content);
    }
    
    void setItems(Purchases purchases) {
        content.removeAll();
        
        content.add(new H4("Átlagok"), createTable(asList(
                new Pair<>("SUM", purchases.sum()),
                new Pair<>(Clock.today().getMonth().name(), purchases.currentMonthSum()),
                new Pair<>("Monthly avg", purchases.monthlyAverage()),
                new Pair<>("Weekly avg", purchases.weeklyAverage()),
                new Pair<>("Daily avg", purchases.dailyAverage()))));
        
        List<Pair<String, Integer>> categoryEntries = purchases.categorySummary().entrySet().stream()
                .map(e -> new Pair<>(e.getKey().name().toLowerCase(), e.getValue()))
                .sorted(Comparator.comparing((Pair<String, Integer> p) -> p.second).reversed())
                .collect(toList());
    
        content.add(new H4("Kategóriák"), createTable(categoryEntries));
    }
    
    private static Grid<Pair<String, Integer>> createTable(List<Pair<String, Integer>> entries) {
        Grid<Pair<String, Integer>> grid = new Grid<>();
        
        grid.addColumn(Pair::first);
        final String amountTemplate = "<div style='text-align: right'>[[item.amount]]</div>";
        grid.addColumn(TemplateRenderer.<Pair<String, Integer>>of(amountTemplate).withProperty("amount", pair -> Formatters.formatAmount(pair.second) + " Ft"));
        
        grid.setItems(entries);
        grid.setHeightByRows(true);
        grid.setSelectionMode(SelectionMode.NONE);
        
        return grid;
    }
    
}
