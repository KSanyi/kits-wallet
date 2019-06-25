package hu.kits.wallet.infrastructure.web.ui;

import java.time.temporal.ChronoUnit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import hu.kits.wallet.Main;
import hu.kits.wallet.common.Clock;
import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchase.Category;
import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.domain.Purchases;

@Route(value = "purchase")
@PageTitle("Vásárlás")
public class PurchaseView extends VerticalLayout implements HasUrlParameter<Long> {

    private final DatePicker dateField = new DatePicker("Dátum", Clock.today());
    private final ComboBox<String> shopCombo = new ComboBox<>("Bolt");
    private final TextField subjectField = new TextField("Tárgy");
    private final ComboBox<Category> categoryCombo = new ComboBox<>("Kategória", Category.values());
    private final ComboBox<Account> accountCombo = new ComboBox<>("Account", Account.values());
    private final NumberField amountField = new NumberField("Összeg");
    private final TextArea commentField = new TextArea("Megjegyzés");
    private final Div lastPurchaseForShopLabel = new Div();
    
    private final Button saveButton = new Button("Mentés", click -> save());
    private final Button cancelButton = new Button("Mégsem", click -> cancel());
    private final Button deleteButton = new Button("Törlés", click -> delete());
    
    private final Purchases purchases;
    
    private Long purchaseId;
    
    private final Binder<PurchaseData> binder = new Binder<>(PurchaseData.class);;

    public PurchaseView() {
        
        purchases = purchaseRepository().loadAll();
        initView();

        shopCombo.setItems(purchases.shops());
        shopCombo.addValueChangeListener(e -> shopSelected(e.getValue()));
        
        accountCombo.setValue(Account.KITS);
        
        setupBinder();
    }
    
    private void setupBinder() {
        Binder<PurchaseData> binder = new Binder<>(PurchaseData.class);
        binder.forField(dateField).asRequired("Nem lehet üres").bind("date");
        binder.forField(shopCombo).asRequired("Nem lehet üres").bind("shop");
        binder.forField(subjectField).asRequired("Nem lehet üres").bind("subject");
        binder.forField(categoryCombo).asRequired("Nem lehet üres").bind("category");
        binder.forField(accountCombo).asRequired("Nem lehet üres").bind("account");
        binder.forField(amountField).asRequired("Nem lehet üres").bind("amount");
        binder.forField(commentField).bind("comment");
    }
    
    private void save() {
        PurchaseData purchaseData = new PurchaseData();
        if(binder.writeBeanIfValid(purchaseData)) {
            purchaseRepository().updateOrCreate(purchaseData.toPurchase(purchaseId));
            Notification.show("Vásárlás mentve", 3000, Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate("")); 
        } else {
            Notification.show("Javítsd a hibákat", 3000, Position.MIDDLE);
        }
    }
    
    private void cancel() {
        getUI().ifPresent(ui -> ui.navigate("")); 
    }
    
    private void delete() {
        purchaseRepository().delete(purchaseId);
        Notification.show("Vásárlás törölve", 3000, Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("")); 
    }
    
    private void shopSelected(String shop) {
        String lastPurchaseInfo = purchases.findLastPurchaseDate(shop).map(date -> ChronoUnit.DAYS.between(date, Clock.today()) + " napja (" + date + ")").orElse("");
        
        lastPurchaseForShopLabel.setText(lastPurchaseInfo);
        accountCombo.setValue(purchases.finAccount(shop));
        categoryCombo.setValue(purchases.findCategory(shop));
        subjectField.setValue(purchases.findMostCommonSubjectFor(shop));
        amountField.setValue((double)purchases.findMostCommonAmountFor(shop));
    }
    
    private static PurchaseRepository purchaseRepository() {
        return Main.purchaseRepository;
    }

    private void initView() {
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        setSpacing(false);
        
        amountField.setPattern("[0-9]*");
        amountField.setPreventInvalidInput(true);
        amountField.setSuffixComponent(new Span("Ft"));
        
        saveButton.setIcon(new Icon("lumo", "checkmark"));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancelButton.setIcon(new Icon("lumo", "cross"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        
        deleteButton.setIcon(new Icon("lumo", "cross"));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setVisible(false);
        
        add(dateField, shopCombo, lastPurchaseForShopLabel, subjectField, categoryCombo, accountCombo, amountField, commentField,
                new HorizontalLayout(saveButton, cancelButton, deleteButton));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if(parameter != null) {
            purchaseId = parameter;
            deleteButton.setVisible(true);
            purchaseRepository().loadAll()
                .find(parameter)
                .ifPresentOrElse(p -> binder.readBean(new PurchaseData(p)),
                    () -> Notification.show("Ismeretlen azonosító", 3000, Position.MIDDLE));
        }
    }

}
