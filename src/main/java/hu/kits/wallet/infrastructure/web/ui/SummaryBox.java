package hu.kits.wallet.infrastructure.web.ui;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;

import hu.kits.wallet.common.Pair;
import hu.kits.wallet.domain.Purchases;
import hu.kits.wallet.infrastructure.web.ui.vaadin.AmountRenderer;

public class SummaryBox extends Div {

    public void setItems(Purchases purchases) {
        
        removeAll();
        
        add(new H4("Átlagok"), createTable(asList(
                new Pair<>("SUM", purchases.sum()),
                new Pair<>("Current month", purchases.currentMonthSum()),
                new Pair<>("Monthly avg", purchases.monthlyAverage()),
                new Pair<>("Weekly avg", purchases.weeklyAverage()),
                new Pair<>("Daily avg", purchases.dailyAverage()))));
        
        List<Pair<String, Integer>> categoryEntries = purchases.categorySummary().entrySet().stream()
                .map(e -> new Pair<>(e.getKey().name().toLowerCase(), e.getValue()))
                .sorted(Comparator.comparing((Pair<String, Integer> p) -> p.second).reversed())
                .collect(toList());
    
        add(new H4("Kategóriák"), createTable(categoryEntries));
    }
    
    private static Grid<Pair<String, Integer>> createTable(List<Pair<String, Integer>> entries) {
        
        Grid<Pair<String, Integer>> grid = new Grid<>();
        
        grid.addColumn(Pair::first);
        
        grid.addColumn(new AmountRenderer<>(Pair::second))
            .setTextAlign(ColumnTextAlign.END);
        
        grid.setItems(entries);
        
        grid.setHeightByRows(true);
        
        return grid;
    }
    
}
