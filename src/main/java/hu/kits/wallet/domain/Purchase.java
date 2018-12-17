package hu.kits.wallet.domain;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Purchase {

    public final Account account;
    
    public final LocalDate date;
    
    public final int amount;
    
    public final Category category;
    
    public final String shop;
    
    public final String subject;
    
    public final String comment;
    
    public Purchase(Account account, LocalDate date, int amount, Category category, String shop, String subject, String comment) {
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
        
        RESTAURANT, FOOD, SPORT, PRESENT, SERVICE, HOUSEHOLD, CLOTHES, CAR, SOFTWARE, TRAVEL, HEALTH, HARDWARE, OTHER
        
    }
    
}
