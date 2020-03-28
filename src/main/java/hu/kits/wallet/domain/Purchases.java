package hu.kits.wallet.domain;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchase.Category;

public class Purchases {

    private final List<Purchase> entries;

    public Purchases(List<Purchase> purchases) {
        entries = purchases.stream()
                .sorted(Comparator.comparing(Purchase::date).reversed())
                .collect(toList());
    }
    
    public List<String> commonShops() {
        
        Map<String , Long> frequencyMap = entries.stream().collect(groupingBy(Purchase::shop, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet().stream().filter(e -> e.getValue() > 5).collect(toList()));
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        return list.stream().map(Entry::getKey).collect(toList());
    }
    
    public List<String> commonSubjects() {
        
        Map<String , Long> frequencyMap = entries.stream().collect(groupingBy(Purchase::subject, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet().stream().filter(e -> e.getValue() > 5).collect(toList()));
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        return list.stream().map(Entry::getKey).collect(toList());
    }
    
    public Optional<LocalDate> findLastPurchaseDate(String shop) {
        if(shop != null) {
            return entries.stream().filter(p -> shop.equals(p.shop())).map(Purchase::date).sorted().reduce((first, second) -> second);
        } else {
            return Optional.empty();
        }
    }
    
    public Category findCategory(String shop) {
        
        return entries.stream().filter(p -> shop.equals(p.shop())).findAny().map(Purchase::category).orElse(Category.OTHER);
    }
    
    public Account finAccount(String shop) {
        
        return entries.stream().filter(p -> shop.equals(p.shop())).findAny().map(Purchase::account).orElse(Account.S);
    }

    public String findMostCommonSubjectFor(String shop) {
        
        Map<String , Long> frequencyMap = entries.stream().filter(p -> shop.equals(p.shop())).collect(groupingBy(Purchase::subject, counting()));
        List<Entry<String, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? "" : list.get(list.size()-1).getKey();
    }
    
    public int findMostCommonAmountFor(String shop) {
        
        Map<Integer , Long> frequencyMap = entries.stream().filter(p -> shop.equals(p.shop())).collect(groupingBy(Purchase::amount, counting()));
        List<Entry<Integer, Long>> list = new ArrayList<>(frequencyMap.entrySet());
        list.sort(Entry.comparingByValue());
        
        return list.isEmpty() ? 0 : list.get(list.size()-1).getKey();
    }

    public List<Purchase> entries() {
        return entries;
    }

    public Purchases filter(String filterString, DateInterval dateInterval) {
        return new Purchases(entries.stream()
                .filter(p -> dateInterval.contains(p.date()))
                .filter(p -> p.dataContains(filterString)).collect(toList()));
    }
    
    public int sum() {
        return entries.stream().mapToInt(Purchase::amount).sum();
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
        return entries.stream().map(Purchase::date).min(LocalDate::compareTo).get();
    }
    
    private LocalDate lastDate() {
        return entries.stream().map(Purchase::date).max(LocalDate::compareTo).get();
    }

    public int currentMonthSum() {
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
        return entries.stream().filter(p -> p.date().withDayOfMonth(1).equals(thisMonth)).mapToInt(Purchase::amount).sum();
    }
    
    public Map<Category, Integer> categorySummary() {
        
        return entries.stream().collect(groupingBy(Purchase::category, summingInt(Purchase::amount)));
    }

    public Optional<Purchase> find(long id) {
        
        return entries.stream().filter(e -> e.id() == id).findFirst();
    }

}
