package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.PurchaseRepository;

@SuppressWarnings("serial")
public class PurchasesScreen extends VerticalLayout {
    
    public PurchasesScreen(PurchaseRepository purchaseRepository, WalletUI ui) {
        
        Button button = new Button("Új vásárlás");
        button.setIcon(VaadinIcons.PLUS);
        button.addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_HUGE);
        button.addClickListener(click -> ui.newPurchase());
        
        addComponents(button, new PurchasesFilterTable(purchaseRepository));
        
    }

}
