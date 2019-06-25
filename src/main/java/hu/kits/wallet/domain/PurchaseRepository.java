package hu.kits.wallet.domain;

public interface PurchaseRepository {

    Purchases loadAll();
    
    void updateOrCreate(Purchase purchase);
    
    void delete(long id);
    
}
