package hu.kits.wallet.infrastructure.web.ui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.kits.wallet.common.Clock;
import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchases;

@SuppressWarnings("serial")
public class NewPurchaseForm extends VerticalLayout {

    private final ComboBox<Purchase.Account> accountCombo = new ComboBox<>("Account", Arrays.asList(Purchase.Account.values()));
    
    private final DateField dateField = new DateField("Dátum");
    
    private final AppendableCombo shopCombo;
    
    private final Label lastPurchaseForShopLabel = new Label();
    
    private final TextField subjectField = new TextField("Tárgy");
    
    private final TextField amountField = new TextField("Összeg (ezer Ft)");
    
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
        String lastPurchaseInfo = purchases.findLastPurchaseDate(shop).map(date -> ChronoUnit.DAYS.between(date, Clock.today()) + " napja (" + date + ")").orElse("");
        
        lastPurchaseForShopLabel.setValue(lastPurchaseInfo);
        accountCombo.setValue(purchases.finAccount(shop));
        categoryCombo.setValue(purchases.findCategory(shop));
        subjectField.setValue(purchases.findMostCommonSubjectFor(shop));
        amountField.setValue(String.valueOf(purchases.findMostCommonAmountFor(shop) / 1000));
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
    
    private static void cancel() {
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
        
        int amount = (int)Double.parseDouble(amountField.getValue()) * 1000;
        
        return new Purchase(null, accountCombo.getValue(), dateField.getValue(), amount, categoryCombo.getValue(), shopCombo.getValue(), subjectField.getValue(), commentTextArea.getValue());
    }

    private void createLayout() {

        Button button = new Button("X");
        button.addStyleName(ValoTheme.BUTTON_HUGE);
        button.setWidth("120px");
        button.addClickListener(e -> UI.getCurrent().addWindow(new AmountWindow(Integer.parseInt(amountField.getValue()), this::amountSet)));
        
        lastPurchaseForShopLabel.addStyleName(ValoTheme.LABEL_HUGE);
        
        accountCombo.addStyleName(ValoTheme.COMBOBOX_HUGE);
        dateField.addStyleName(ValoTheme.DATEFIELD_HUGE);
        amountField.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        shopCombo.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        subjectField.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        categoryCombo.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        commentTextArea.addStyleName(ValoTheme.TEXTAREA_HUGE);
        
        amountField.setWidth("200px");
        accountCombo.setWidth("300px");
        dateField.setWidth("350px");
        shopCombo.setWidth("600px");
        subjectField.setWidth("700px");
        categoryCombo.setWidth("600px");
        commentTextArea.setWidth("700px");
        commentTextArea.setRows(3);
        
        accountCombo.setEmptySelectionAllowed(false);
        categoryCombo.setEmptySelectionAllowed(false);
        shopCombo.setEmptySelectionAllowed(false);
        
        amountField.addStyleName(ValoTheme.TEXTAREA_ALIGN_RIGHT);
        
        saveButton.addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_HUGE);
        cancelButton.addStyleNames(ValoTheme.BUTTON_FRIENDLY, ValoTheme.BUTTON_HUGE);
        
        if(Page.getCurrent().getWebBrowser().getScreenWidth() >= 1000) {
            addComponents(new HorizontalLayout(accountCombo, dateField, shopCombo, lastPurchaseForShopLabel), new HorizontalLayout(subjectField, categoryCombo), new HorizontalLayout(amountField, button, commentTextArea), new HorizontalLayout(saveButton, cancelButton));
        } else {
            addComponents(new HorizontalLayout(accountCombo, dateField), shopCombo, lastPurchaseForShopLabel, subjectField, new HorizontalLayout(amountField, button), categoryCombo, commentTextArea, new HorizontalLayout(saveButton, cancelButton));
        }
        
    }
    
}
