package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.domain.Purchases;
import hu.kits.wallet.infrastructure.web.vaadin.WalletVaadinServlet;

@SuppressWarnings("serial")
@Theme("wallet")
public class WalletUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        
        PurchaseRepository purchaseRepository = ((WalletVaadinServlet)VaadinServlet.getCurrent()).purchaseRepository;
        
        Purchases purchases = purchaseRepository.loadAll();
        
        setContent(new NewPurchaseForm(purchases, purchaseRepository::create));
        
    }
    
}
