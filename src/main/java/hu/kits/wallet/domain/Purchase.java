package hu.kits.wallet.domain;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Purchase {

    public final Long id;
    
    public final Account account;
    
    public final LocalDate date;
    
    public final int amount;
    
    public final Category category;
    
    public final String shop;
    
    public final String subject;
    
    public final String comment;
    
    public Purchase(Long id, Account account, LocalDate date, int amount, Category category, String shop, String subject, String comment) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.shop = shop;
        this.subject = subject;
        this.comment = comment;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static enum Account {
        S, KITS
    }
    
    public static enum Category {
        
        RESTAURANT, FOOD, SPORT, PRESENT, SERVICE, HOUSEHOLD, CLOTHES, CAR, SOFTWARE, TRAVEL, HEALTH, HARDWARE, TRANSPORT, OTHER
        
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
