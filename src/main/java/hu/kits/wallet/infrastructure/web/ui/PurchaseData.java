package hu.kits.wallet.infrastructure.web.ui;

import java.time.LocalDate;
import java.util.List;

import hu.kits.wallet.domain.Photo;
import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchase.Category;

public class PurchaseData {

    private Account account;
    private LocalDate date;
    private double amount;
    private Category category;
    private String shop;
    private String subject;
    private String comment;
    private List<Photo> photos;
    
    public PurchaseData() {
    }
    
    public PurchaseData(Purchase purchase) {
        this(purchase.account(), purchase.date(), purchase.amount(), purchase.category(), purchase.shop(), purchase.subject(), purchase.comment(), purchase.photos());
    }
    
    public PurchaseData(Account account, LocalDate date, int amount, Category category, String shop, String subject, String comment, List<Photo> photos) {
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.shop = shop;
        this.subject = subject;
        this.comment = comment;
        this.photos = photos;
    }
    
    public Purchase toPurchase(Long id) {
        List<Photo> photos = this.photos != null ? this.photos : List.of();
        return new Purchase(id, account, date, (int)amount, category, shop, subject, comment, photos, null);
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public String getShop() {
        return shop;
    }
    
    public void setShop(String shop) {
        this.shop = shop;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    
}
