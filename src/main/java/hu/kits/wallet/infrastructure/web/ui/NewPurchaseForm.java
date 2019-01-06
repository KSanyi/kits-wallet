package hu.kits.wallet.infrastructure.web.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchases;

@SuppressWarnings("serial")
public class NewPurchaseForm extends VerticalLayout {

    private final ComboBox<Purchase.Account> accountCombo = new ComboBox<>("Account", Arrays.asList(Purchase.Account.values()));
    
    private final DateField dateField = new DateField("Dátum");
    
    private final AppendableCombo shopCombo;
    
    private final TextField subjectField = new TextField("Tárgy");
    
    private final TextField amountField = new TextField("Összeg");
    
    private final ComboBox<Purchase.Category> categoryCombo = new ComboBox<>("Kategória", Arrays.asList(Purchase.Category.values()));
    
    private final TextArea commentTextArea = new TextArea("Megjegyzés");
    
    private final Button saveButton = new Button("Mentés");
    
    private final Button cancelButton = new Button("Mégsem");
    
    private final Purchases purchases;
    
    private final Consumer<Purchase> saveAction;
    
    NewPurchaseForm(Purchases purchases, Consumer<Purchase> saveAction) {
        
        this.purchases = purchases;
        this.saveAction = saveAction;
        shopCombo = new AppendableCombo("Bolt", purchases.shops());
        
        createLayout();
        
        saveButton.addClickListener(click -> save());
        cancelButton.addClickListener(click -> cancel());
        shopCombo.addValueChangeListener(e -> shopSelected(e.getValue()));
        
        init();
    }
    
    private void shopSelected(String shop) {
        categoryCombo.setValue(purchases.findCategory(shop));
        subjectField.setValue(purchases.findMostCommonSubjectFor(shop));
        amountField.setValue(String.valueOf(purchases.findMostCommonAmountFor(shop)));
    }
    
    private void amountSet(int amount) {
        amountField.setValue(String.valueOf(amount));
        shopCombo.focus();
    }
    
    private void init() {
        accountCombo.setValue(Account.S);
        dateField.setValue(LocalDate.now());
        subjectField.clear();
        commentTextArea.clear();
        shopCombo.setValue(purchases.shops().get(1));
        shopCombo.setValue(purchases.shops().get(0));
    }
    
    private void save() {
        
        if(validate()) {
            saveAction.accept(createPurchase());
            Notification.show("Sikeres mentés");
            ((WalletUI)UI.getCurrent()).showPurchases();
        } else {
            Notification.show("Sikertelen mentés", "Javitsd a hibákat!", Notification.Type.ERROR_MESSAGE);
        }
    }
    
    private void cancel() {
        ((WalletUI)UI.getCurrent()).showPurchases();
    }
    
    boolean validate() {
        
        List<Boolean> validationResults = new ArrayList<>(Arrays.asList(
                validateMandatoryField(dateField),
                validateMandatoryField(amountField),
                validateMandatoryField(subjectField)));
        
        if(!amountField.isEmpty()) {
            validationResults.add(validateAmountField());
        }
        
        return validationResults.stream().allMatch(b -> b);
    }
    
    private boolean validateAmountField() {
        try {
            Integer.parseInt(amountField.getValue());
            return true;
        } catch (NumberFormatException ex) {
            amountField.setComponentError(new UserError("Hibás számformátum"));
            return false;
        }
    }

    private static boolean validateMandatoryField(AbstractField<?> field) {
        if(field.isEmpty()) {
            field.setComponentError(new UserError(field.getCaption() + " nem lehet üres"));
            return false;
        } else {
            field.setComponentError(null);
            return true;
        }
    }
    
    private Purchase createPurchase() {
        return new Purchase(accountCombo.getValue(), dateField.getValue(), Integer.parseInt(amountField.getValue()), categoryCombo.getValue(), shopCombo.getValue(), subjectField.getValue(), commentTextArea.getValue());
    }

    private void createLayout() {

        Button button = new Button("X");
        button.addClickListener(e -> UI.getCurrent().addWindow(new AmountWindow(Integer.parseInt(amountField.getValue()), this::amountSet)));
        
        addComponents(new HorizontalLayout(accountCombo, dateField), shopCombo, subjectField, new HorizontalLayout(amountField, button), categoryCombo, commentTextArea, new HorizontalLayout(saveButton, cancelButton));
        
        accountCombo.addStyleName(ValoTheme.COMBOBOX_HUGE);
        dateField.addStyleName(ValoTheme.DATEFIELD_HUGE);
        amountField.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        shopCombo.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        subjectField.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        categoryCombo.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        commentTextArea.addStyleName(ValoTheme.TEXTAREA_HUGE);
        
        accountCombo.setWidth("140px");
        dateField.setWidth("230px");
        shopCombo.setWidth("300px");
        subjectField.setWidth("300px");
        categoryCombo.setWidth("300px");
        commentTextArea.setWidth("400px");
        commentTextArea.setRows(3);
        
        categoryCombo.setEmptySelectionAllowed(false);
        shopCombo.setEmptySelectionAllowed(false);
        
        amountField.addStyleName(ValoTheme.TEXTAREA_ALIGN_RIGHT);
        
        saveButton.addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_HUGE);
        cancelButton.addStyleNames(ValoTheme.BUTTON_FRIENDLY, ValoTheme.BUTTON_HUGE);
    }
    
}
