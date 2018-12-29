package hu.kits.wallet.infrastructure.web.ui;

import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AmountWindow extends Window {

    private final TextField textField = new TextField();
    
    private final Consumer<Integer> action;
    
    public AmountWindow(int amount, Consumer<Integer> action) {
        
        this.action = action;
        
        setSizeFull();
        
        GridLayout layout = new GridLayout(3, 5);
        layout.setSizeFull();
        
        textField.setValue(String.valueOf(amount));
        textField.setSizeFull();
        textField.addStyleName(ValoTheme.TEXTFIELD_HUGE);
        
        layout.addComponent(textField, 0, 0, 2, 0);
        for(int i=1;i<=9;i++) {
            layout.addComponent(new NumberButton(i));
        }

        Button clearButton = new Button("Clear", click -> clearClicked());
        clearButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        clearButton.setSizeFull();
        
        layout.addComponent(clearButton);
        
        layout.addComponent(new NumberButton(0));
        
        Button doneButton = new Button("Done", click -> doneClicked());
        doneButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        
        layout.addComponent(doneButton);
        doneButton.setSizeFull();
        
        setContent(layout);
    }

    private void doneClicked() {
        if(textField.getValue().isBlank()) {
            action.accept(0);
        } else {
            action.accept(Integer.parseInt(textField.getValue()));
        }
        close();
    }
    
    private void clearClicked() {
        textField.clear();
    }
    
    private void buttonClicked(int number) {
        textField.setValue(textField.getValue() + number);
    }
    
    private class NumberButton extends Button {
        
        NumberButton(int number) {
            setCaption(String.valueOf(number));
            setSizeFull();
            addClickListener(click -> buttonClicked(number));
            addStyleName(ValoTheme.BUTTON_HUGE);
        }
    }
}
