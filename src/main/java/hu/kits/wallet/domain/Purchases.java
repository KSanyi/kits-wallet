package hu.kits.wallet.domain;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchase.Category;

public class Purchases {

    private final List<Purchase> entries;

    public Purchases(List<Purchase> purchases) {
        entries = purchases;
    }
    
    public List<String> shops() {
        
        Map<String , Long> frequencyMap = entries.stream().collect(groupingBy(p -> p.shop, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        return list.stream().map(Entry::getKey).collect(toList());
    }
    
    public Optional<LocalDate> findLastPurchaseDate(String shop) {
        
        return entries.stream().filter(p -> shop.equals(p.shop)).map(p -> p.date).sorted().reduce((first, second) -> second);
    }
    
    public Category findCategory(String shop) {
        
        return entries.stream().filter(p -> shop.equals(p.shop)).findAny().map(p -> p.category).orElse(Category.OTHER);
    }
    
    public Account finAccount(String shop) {
        
        return entries.stream().filter(p -> shop.equals(p.shop)).findAny().map(p -> p.account).orElse(Account.S);
    }

    public String findMostCommonSubjectFor(String shop) {
        
        Map<String , Long> frequencyMap = entries.stream().filter(p -> shop.equals(p.shop)).collect(groupingBy(p -> p.subject, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? "" : list.get(list.size()-1).getKey();
    }
    
    public int findMostCommonAmountFor(String shop) {
        
        Map<Integer , Long> frequencyMap = entries.stream().filter(p -> shop.equals(p.shop)).collect(groupingBy(p -> p.amount, counting()));
        List<Entry<Integer, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? 0 : list.get(list.size()-1).getKey();
    }

    public List<Purchase> entries() {
        return entries;
    }

    public Purchases filter(String filterString, DateInterval dateInterval) {
        return new Purchases(entries.stream()
                .filter(p -> dateInterval.contains(p.date))
                .filter(p -> p.dataContains(filterString)).collect(toList()));
    }
    
    public int sum() {
        return entries.stream().mapToInt(p -> p.amount).sum();
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
        if(entries.isEmpty()) return 0;
        
        int days = Math.max(1, (int)unit.between(firstDate(), lastDate()));
        return sum() / days;
    }
    
    private LocalDate firstDate() {
        return entries.stream().map(p -> p.date).min(LocalDate::compareTo).get();
    }
    
    private LocalDate lastDate() {
        return entries.stream().map(p -> p.date).max(LocalDate::compareTo).get();
    }

    public int currentMonthSum() {
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
        return entries.stream().filter(p -> p.date.withDayOfMonth(1).equals(thisMonth)).mapToInt(p -> p.amount).sum();
    }
    
    public Map<Category, Integer> categorySummary() {
        
        return entries.stream().collect(groupingBy(p -> p.category, summingInt(p -> p.amount)));
    }

}
