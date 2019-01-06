package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.infrastructure.web.vaadin.WalletVaadinServlet;

@SuppressWarnings("serial")
@Theme("wallet")
public class WalletUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        String token = request.getParameter("token");
        
        if("364b6227-52b7-4934-9bea-7d9978ecd893".equals(token)) {
            newPurchase();            
        }
    }

    public void newPurchase() {
        PurchaseRepository purchaseRepository = ((WalletVaadinServlet)VaadinServlet.getCurrent()).purchaseRepository;
        setContent(new NewPurchaseForm(purchaseRepository.loadAll(), purchaseRepository::create));
    }

    public void showPurchases() {
        PurchaseRepository purchaseRepository = ((WalletVaadinServlet)VaadinServlet.getCurrent()).purchaseRepository;
        setContent(new PurchasesScreen(purchaseRepository, this));
    }
    
}
