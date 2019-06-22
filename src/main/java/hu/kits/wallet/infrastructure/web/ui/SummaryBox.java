package hu.kits.wallet.infrastructure.web.ui;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;

import hu.kits.wallet.common.Formatters;
import hu.kits.wallet.common.Pair;
import hu.kits.wallet.domain.Purchases;

@SuppressWarnings("serial")
public class SummaryBox extends FormLayout {

    public void setItems(Purchases purchases) {
        
        removeAllComponents();
        
        addComponent(createTable(asList(
                new Pair<>("SUM", purchases.sum()),
                new Pair<>("Current month", purchases.currentMonthSum()),
                new Pair<>("Monthly avg", purchases.monthlyAverage()),
                new Pair<>("Weekly avg", purchases.weeklyAverage()),
                new Pair<>("Daily avg", purchases.dailyAverage()))));
        
        List<Pair<String, Integer>> categoryEntries = purchases.categorySummary().entrySet().stream()
                .map(e -> new Pair<>(e.getKey().name().toLowerCase(), e.getValue()))
                .collect(toList());
    
        addComponent(createTable(categoryEntries));
    }
    
    private static Grid<Pair<String, Integer>> createTable(List<Pair<String, Integer>> entries) {
        
        Grid<Pair<String, Integer>> table = new Grid<>();
        
        table.addColumn(Pair::first)
            .setWidth(150);
        
        table.addColumn(Pair::second, e -> Formatters.formatAmount(e))
            .setWidth(120)
            .setId("amount")
            .setStyleGenerator(item -> "v-align-right");
        
        table.setWidth("270px");
        table.setHeightByRows(Math.max(1, entries.size()));
        table.setHeaderRowHeight(30);
        
        table.sort("amount", SortDirection.DESCENDING);
        
        table.setItems(entries);
        
        return table;
    }
    
}
