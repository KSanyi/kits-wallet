package hu.kits.wallet.infrastructure.web.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class AppendableCombo extends ComboBox<String> {
    
    private List<String> items;
    
    public AppendableCombo(String caption, List<String> initialItems) {
        
        this.items = new ArrayList<>(initialItems);
        
        setItems(items);
        
        setCaption(caption);
        
        setNewItemProvider(newItem -> {
            items.add(newItem);
            setItems(items);
            return Optional.of(newItem);
        });
        
    }

}
