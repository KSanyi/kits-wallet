package hu.kits.wallet.domain;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hu.kits.wallet.domain.Purchase.Category;

public class Purchases {

    private final List<Purchase> all;

    public Purchases(List<Purchase> purchases) {
        all = purchases;
    }
    
    public List<String> shops() {
        
        Map<String , Long> frequencyMap = all.stream().collect(groupingBy(p -> p.shop, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        return list.stream().map(Entry::getKey).collect(toList());
    }
    
    public Category findCategory(String shop) {
        
        return all.stream().filter(p -> shop.equals(p.shop)).findAny().map(p -> p.category).orElse(Category.OTHER);
    }
    
    public String findSubject(String shop) {
        
        Map<String , Long> frequencyMap = all.stream().filter(p -> shop.equals(p.shop)).collect(groupingBy(p -> p.subject, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? "" : list.get(list.size()-1).getKey();
    }

    public List<Purchase> entries() {
        return all;
    }

    public int sum() {
        return all.stream().mapToInt(p -> p.amount).sum();
    }

    public int monthlyAverage() {
        if(all.isEmpty()) return 0;
        
        int months = Math.max(1, (int)ChronoUnit.MONTHS.between(firstDate(), lastDate()));
        return sum() / months;
    }

    public int dailyAverage() {
        if(all.isEmpty()) return 0;
        
        int days = Math.max(1, (int)ChronoUnit.DAYS.between(firstDate(), lastDate()));
        return sum() / days;
    }
    
    private LocalDate firstDate() {
        return all.stream().map(p -> p.date).min(LocalDate::compareTo).get();
    }
    
    private LocalDate lastDate() {
        return all.stream().map(p -> p.date).max(LocalDate::compareTo).get();
    }

    public int currentMonthSum() {
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
        return all.stream().filter(p -> p.date.withDayOfMonth(1).equals(thisMonth)).mapToInt(p -> p.amount).sum();
    }
    
}
