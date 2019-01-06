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
    
    public String findMostCommonSubjectFor(String shop) {
        
        Map<String , Long> frequencyMap = all.stream().filter(p -> shop.equals(p.shop)).collect(groupingBy(p -> p.subject, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? "" : list.get(list.size()-1).getKey();
    }
    
    public int findMostCommonAmountFor(String shop) {
        
        Map<Integer , Long> frequencyMap = all.stream().filter(p -> shop.equals(p.shop)).collect(groupingBy(p -> p.amount, counting()));
        List<Entry<Integer, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? 0 : list.get(list.size()-1).getKey();
    }

    public List<Purchase> entries() {
        return all;
    }

    public Purchases filter(String filterString, DateInterval dateInterval) {
        return new Purchases(all.stream()
                .filter(p -> dateInterval.contains(p.date))
                .filter(p -> p.dataContains(filterString)).collect(toList()));
    }
    
    public int sum() {
        return all.stream().mapToInt(p -> p.amount).sum();
    }

    public int monthlyAverage() {
        return average(ChronoUnit.MONTHS);
    }
    
    public int weeklyAverage() {
        return average(ChronoUnit.WEEKS);
    }

    public int dailyAverage() {
        return average(ChronoUnit.DAYS);
    }
    
    private int average(ChronoUnit unit) {
        if(all.isEmpty()) return 0;
        
        int days = Math.max(1, (int)unit.between(firstDate(), lastDate()));
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
