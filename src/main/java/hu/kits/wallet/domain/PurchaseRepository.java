package hu.kits.wallet.domain;

public interface PurchaseRepository {

    Purchases loadAll();
    
    void create(Purchase purchase);
    
}
