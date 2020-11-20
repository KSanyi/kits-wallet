package hu.kits.wallet.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public record Purchase(Long id,
        Account account, 
        LocalDate date, 
        int amount, 
        Category category, 
        String shop, 
        String subject,
        String comment, 
        LocalDateTime timestamp) {

    public static enum Account {
        S, KITS
    }
    
    public static enum Category {
        RESTAURANT, FOOD, SPORT, PRESENT, SERVICE, HOUSEHOLD, CLOTHES, CAR, SOFTWARE, TRAVEL, HEALTH, HARDWARE, TRANSPORT, KID, OTHER
    }

    public boolean dataContains(String filterText) {
        String[] filterParts = cleanString(filterText).split(" ");
        return Stream.of(filterParts).allMatch(filterPart -> 
            cleanString(category.name()).contains(filterPart) ||
            cleanString(shop).contains(filterPart) ||
            cleanString(subject).contains(filterPart) ||
            cleanString(comment).contains(filterPart));
    }
    
    private static String cleanString(String value) {
        return value.toLowerCase()
                .replace("ő", "o").replace("ö", "o").replace("ó", "o")
                .replace("ű", "u").replace("ü", "u").replace("ú", "u")
                .replace("á", "a").replace("í", "i").replace("é", "e");
    }
    
}
