package hu.kits.wallet.infrastructure.web.ui;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
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

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final DatePicker dateField = new DatePicker("Dátum", Clock.today());
    private final ComboBox<String> shopCombo = new ComboBox<>("Bolt");
    private final ComboBox<String> subjectCombo = new ComboBox<>("Tárgy");
    private final ComboBox<Category> categoryCombo = new ComboBox<>("Kategória", Category.values());
    private final ComboBox<Account> accountCombo = new ComboBox<>("Account", Account.values());
    private final NumberField amountField = new NumberField("Összeg");
    private final TextArea commentField = new TextArea("Megjegyzés");
    private final PhotosComponent photosComponent = new PhotosComponent("Fényképek");
    private final Div lastPurchaseForShopLabel = new Div();
    
    private final Button saveButton = new Button("Mentés", click -> save());
    private final Button duplicateButton = new Button("Másolás", click -> duplicate());
    private final Button cancelButton = new Button("Mégsem", click -> cancel());
    private final Button deleteButton = new Button("Törlés", click -> delete());
    
    private final Purchases purchases;
    
    private Long purchaseId;
    
    private PurchaseData purchaseData;
    
    private final Binder<PurchaseData> binder = new Binder<>(PurchaseData.class);

    public PurchaseView() {
        
        purchases = purchaseRepository().loadAll();
        initView();

        shopCombo.setItems(purchases.commonShops());
        shopCombo.addValueChangeListener(e -> shopSelected(e.getValue()));
        
        shopCombo.setAllowCustomValue(true);
        shopCombo.addCustomValueSetListener(e -> {
            if(!e.getDetail().equals(shopCombo.getValue())) {
                shopCombo.setValue(e.getDetail());
            }
        });
        
        subjectCombo.setItems(purchases.commonSubjects());
        
        subjectCombo.setAllowCustomValue(true);
        subjectCombo.addCustomValueSetListener(e -> {
            if(!e.getDetail().equals(subjectCombo.getValue())) {
                subjectCombo.setValue(e.getDetail());
            }
        });
        
        accountCombo.setValue(Account.S);
        
        setupBinder();
    }
    
    private void setupBinder() {
        binder.forField(dateField).asRequired("Nem lehet üres").bind("date");
        binder.forField(shopCombo).asRequired("Nem lehet üres").bind("shop");
        binder.forField(subjectCombo).asRequired("Nem lehet üres").bind("subject");
        binder.forField(categoryCombo).asRequired("Nem lehet üres").bind("category");
        binder.forField(accountCombo).asRequired("Nem lehet üres").bind("account");
        binder.forField(amountField).asRequired("Nem lehet üres").bind("amount");
        binder.forField(commentField).bind("comment");
        binder.forField(photosComponent).bind("photos");
        
    }
    
    private void save() {
        PurchaseData purchaseData = new PurchaseData();
        if(binder.writeBeanIfValid(purchaseData)) {
            purchaseRepository().updateOrCreate(purchaseData.toPurchase(purchaseId));
            Notification.show("Vásárlás mentve", 3000, Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(PurchasesListView.class)); 
        } else {
            Notification.show("Javítsd a hibákat", 3000, Position.MIDDLE);
        }
    }
    
    private void duplicate() {
        PurchaseData purchaseData = new PurchaseData();
        if(binder.writeBeanIfValid(purchaseData)) {
            purchaseData.setDate(Clock.today());
            purchaseRepository().updateOrCreate(purchaseData.toPurchase(null));
            Notification.show("Vásárlás mentve", 3000, Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(PurchasesListView.class)); 
        } else {
            Notification.show("Javítsd a hibákat", 3000, Position.MIDDLE);
        }
    }
    
    private void cancel() {
        getUI().ifPresent(ui -> ui.navigate(PurchasesListView.class)); 
    }
    
    private void delete() {
        purchaseRepository().delete(purchaseId);
        Notification.show("Vásárlás törölve", 3000, Position.MIDDLE);
        log.info("Purchase deleted: {}", purchaseData);
        getUI().ifPresent(ui -> ui.navigate(PurchasesListView.class)); 
    }
    
    private void shopSelected(String shop) {
        if(shop == null) return;
        
        String lastPurchaseInfo = purchases.findLastPurchaseDate(shop).map(date -> ChronoUnit.DAYS.between(date, Clock.today()) + " napja (" + date + ")").orElse("");
        
        if(lastPurchaseInfo.isBlank()) return;
        
        lastPurchaseForShopLabel.setText(lastPurchaseInfo);
        accountCombo.setValue(purchases.finAccount(shop));
        categoryCombo.setValue(purchases.findCategory(shop));
        subjectCombo.setValue(purchases.findMostCommonSubjectFor(shop));
        amountField.setValue((double)purchases.findMostCommonAmountFor(shop));
    }
    
    private static PurchaseRepository purchaseRepository() {
        return Main.purchaseRepository;
    }

    private void initView() {
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        setSpacing(false);
        
        amountField.setWidth("150px");
        amountField.setPattern("[0-9]*");
        amountField.setPreventInvalidInput(true);
        amountField.setSuffixComponent(new Span("Ft"));
        amountField.setClearButtonVisible(true);
        amountField.addFocusListener(e -> amountField.clear());
        
        saveButton.setIcon(new IronIcon("lumo", "checkmark"));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setHeight("50px");
        
        cancelButton.setIcon(new IronIcon("lumo", "cross"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        
        duplicateButton.setIcon(new IronIcon("lumo", "reload"));
        duplicateButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        duplicateButton.setVisible(false);
        duplicateButton.setHeight("50px");
        
        deleteButton.setIcon(new IronIcon("lumo", "cross"));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setVisible(false);
        
        add(cancelButton, dateField, shopCombo, lastPurchaseForShopLabel, subjectCombo, categoryCombo, accountCombo, amountField, commentField, photosComponent,
                saveButton, duplicateButton, deleteButton);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if(parameter != null) {
            purchaseId = parameter;
            deleteButton.setVisible(true);
            duplicateButton.setVisible(true);
            purchaseRepository().loadAll()
                .find(parameter)
                .ifPresentOrElse(p -> {
                        purchaseData = new PurchaseData(p);
                        binder.readBean(purchaseData);
                        log.info("Purchase loaded: {}", p);
                    },
                    () -> Notification.show("Ismeretlen azonosító", 3000, Position.MIDDLE));
        }
    }

}
