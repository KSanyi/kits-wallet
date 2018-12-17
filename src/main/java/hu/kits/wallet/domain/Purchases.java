package hu.kits.wallet.domain;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
    
}
