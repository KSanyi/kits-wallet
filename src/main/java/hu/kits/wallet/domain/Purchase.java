package hu.kits.wallet.domain;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public record Purchase(Long id,
        Account account,
        LocalDate date,
        int amount,  
        Category category, 
        String shop,
        String subject,
        String comment, 
        List<File> files,
        LocalDateTime timestamp) implements Comparable<Purchase> {

    public static enum Account {
        S, KITS
    }
    
    public static enum Category {
        RESTAURANT, FOOD, SPORT, PRESENT, SERVICE, HOUSEHOLD, CLOTHES, CAR, SOFTWARE, TRAVEL, HEALTH, HARDWARE, TRANSPORT, KID, OTHER
    }

    public boolean filter(String filterText) {
        String[] filterWords = cleanString(filterText).split(" ");
        return Stream.of(filterWords).allMatch(this::filterWord);
    }
    
    private static String cleanString(String value) {
        return value.toLowerCase()
                .replace("ő", "o").replace("ö", "o").replace("ó", "o")
                .replace("ű", "u").replace("ü", "u").replace("ú", "u")
                .replace("á", "a").replace("í", "i").replace("é", "e");
    }
    
    private boolean filterWord(String filterWord) {
        List<String> fieldsValues = List.of(category.name(), shop, subject, comment).stream()
                .map(Purchase::cleanString)
                .collect(toList());
        
        boolean negativeFilter = filterWord.startsWith("-");
        String word = negativeFilter ? filterWord.substring(1) : filterWord;
        
        if(negativeFilter) {
            return fieldsValues.stream().allMatch(fieldValue -> ! fieldValue.contains(word));
        } else {
            return fieldsValues.stream().anyMatch(fieldValue -> fieldValue.contains(word));
        }
    }
    
    @Override
    public int compareTo(Purchase other) {
        int dateCompare = other.date.compareTo(date);
        return dateCompare != 0 ? dateCompare : other.timestamp.compareTo(timestamp);
    }
    
}
