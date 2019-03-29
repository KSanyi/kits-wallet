package hu.kits.wallet.infrastructure.web.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ConfirmationDialog extends Window {

    private final Button yesButton = createButton("Igen", VaadinIcons.CHECK_CIRCLE_O, ValoTheme.BUTTON_SMALL, ValoTheme.BUTTON_PRIMARY);
    private final Button noButton = createButton("Nem", VaadinIcons.CLOSE_CIRCLE_O, ValoTheme.BUTTON_SMALL, ValoTheme.BUTTON_FRIENDLY);
    
    public static void open(String question, Runnable yesAction) {
        UI.getCurrent().addWindow(new ConfirmationDialog(question, yesAction));
    }
    
    public ConfirmationDialog(String question, Runnable yesAction) {
        this(question, yesAction, ()->{});
    }
    
    public ConfirmationDialog(String question, Runnable yesAction, Runnable noAction) {
        setCaption("Megerősítés");
        setModal(true);
        setClosable(false);
        setResizable(false);

        yesButton.addClickListener(click -> {
            yesAction.run();
            close();
        });
        noButton.addClickListener(click -> {
            noAction.run();
            close();
        });
        
        Label questionLabel = new Label(question, ContentMode.HTML);
        
        HorizontalLayout buttonBar = new HorizontalLayout(yesButton, noButton);
        VerticalLayout layout = new VerticalLayout(questionLabel, buttonBar);
        layout.setComponentAlignment(buttonBar, Alignment.BOTTOM_CENTER);
        
        setContent(layout);
    }
    
    private static Button createButton(String caption, VaadinIcons icon, String ... styleNames) {
        Button button = new Button(caption, icon);
        button.addStyleNames(styleNames);
        return button;
    }
    
}
