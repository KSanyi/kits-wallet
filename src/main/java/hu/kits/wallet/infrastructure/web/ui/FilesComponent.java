package hu.kits.wallet.infrastructure.web.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import hu.kits.wallet.Main;
import hu.kits.wallet.domain.FileStorage;
import hu.kits.wallet.domain.File;

public class FilesComponent extends CustomField<List<File>> {

    private final List<File> files = new ArrayList<>();
    
    private final HorizontalLayout mainLayout = new HorizontalLayout();
    
    private final FileStorage fileStorage = Main.fileStorage;
    
    public FilesComponent(String title) {
        add(new H4(title), mainLayout);
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        //upload.setAcceptedFileTypes("image/*");
        upload.getElement().setAttribute("capture", "environment");
        
        upload.addSucceededListener(event -> {
            String fileId = UUID.randomUUID().toString().substring(0, 8) + "_" + event.getFileName();
            addFile(new File(fileId));
            changeHappened();
            
            byte[] bytes;
            try {
                bytes = IOUtils.toByteArray(buffer.getInputStream());
            } catch (IOException ex) {
                throw new RuntimeException("Error uploading file", ex);
            }
            
            fileStorage.saveFile(fileId, bytes);
        });
                
        mainLayout.add(upload);
    }

    @Override
    protected List<File> generateModelValue() {
        return List.copyOf(files);
    }

    @Override
    protected void setPresentationValue(List<File> files) {
        for(File file : files) {
            addFile(file);
        }
    }
    
    private void addFile(File file) {
        files.add(file);
        StreamResource resource = new StreamResource(file.id(), () -> new ByteArrayInputStream(fileStorage.getFile(file.id())));
        
        Component fileComponent;
        if(file.isPhoto()) {
            Image image = new Image(resource, file.id());
            image.setWidth("200px");
            fileComponent = image;
        } else {
            fileComponent = new Anchor(resource, file.id());
        }
        
        Button removeButton = new Button(VaadinIcon.CLOSE.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON);
        
        VerticalLayout layout = new VerticalLayout(fileComponent, removeButton);
        layout.setWidth("200px");
        layout.setPadding(false);
        layout.setSpacing(false);
        removeButton.setWidthFull();
        removeButton.addClickListener(click -> {
            mainLayout.remove(layout);
            files.remove(file);
            changeHappened();
            fileStorage.delete(file.id());
        });
        
        mainLayout.add(layout);
    }
    
    void changeHappened() {
        this.updateValue();
    }

}
