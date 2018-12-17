package hu.kits.wallet.domain;

import java.util.List;

public interface PurchaseRepository {

    Purchases loadAll();
    
    void create(Purchase purchase);
    
}
