package hu.kits.wallet.infrastructure.web.ui;

import java.time.LocalDate;
import java.util.List;

import hu.kits.wallet.domain.File;
import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchase.Category;

public class PurchaseData {

    private Account account;
    private LocalDate date;
    private int amount;
    private Category category;
    private String shop;
    private String subject;
    private String comment;
    private List<File> files;
    
    public PurchaseData() {
    }
    
    public PurchaseData(Purchase purchase) {
        this(purchase.account(), purchase.date(), purchase.amount(), purchase.category(), purchase.shop(), purchase.subject(), purchase.comment(), purchase.files());
    }
    
    public PurchaseData(Account account, LocalDate date, int amount, Category category, String shop, String subject, String comment, List<File> files) {
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.shop = shop;
        this.subject = subject;
        this.comment = comment;
        this.files = files;
    }
    
    public Purchase toPurchase(Long id) {
        List<File> files = this.files != null ? this.files : List.of();
        return new Purchase(id, account, date, (int)amount, category, shop, subject, comment, files, null);
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
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
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

    public List<File> getFiles() {
        return files;
    }

    public void setPhotos(List<File> files) {
        this.files = files;
    }
    
}
