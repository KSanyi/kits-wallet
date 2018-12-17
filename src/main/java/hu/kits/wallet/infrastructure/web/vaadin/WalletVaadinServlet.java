package hu.kits.wallet.infrastructure.web.vaadin;

import com.vaadin.server.VaadinServlet;

import hu.kits.wallet.domain.PurchaseRepository;

@SuppressWarnings("serial")
public class WalletVaadinServlet extends VaadinServlet {
    
    public final PurchaseRepository purchaseRepository;
    
    WalletVaadinServlet(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

}