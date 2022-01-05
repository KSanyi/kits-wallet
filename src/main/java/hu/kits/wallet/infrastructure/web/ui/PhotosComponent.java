package hu.kits.wallet.infrastructure.web.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
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
import hu.kits.wallet.domain.Photo;

public class PhotosComponent extends CustomField<List<Photo>> {

    private final List<Photo> photos = new ArrayList<>();
    
    private final HorizontalLayout mainLayout = new HorizontalLayout();
    
    private final FileStorage fileStorage = Main.fileStorage;
    
    public PhotosComponent(String title) {
        add(new H4(title), mainLayout);
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/*");
        upload.getElement().setAttribute("capture", "environment");
        
        upload.addSucceededListener(event -> {
            String photoId = UUID.randomUUID().toString().substring(0, 8) + "_" + event.getFileName();
            addPhoto(new Photo(photoId));
            changeHappened();
            
            byte[] bytes;
            try {
                bytes = IOUtils.toByteArray(buffer.getInputStream());
            } catch (IOException ex) {
                throw new RuntimeException("Error uploading photo", ex);
            }
            
            fileStorage.saveFile(photoId, bytes);
        });
                
        mainLayout.add(upload);
    }

    @Override
    protected List<Photo> generateModelValue() {
        return List.copyOf(photos);
    }

    @Override
    protected void setPresentationValue(List<Photo> photos) {
        for(Photo photo : photos) {
            addPhoto(photo);
        }
    }
    
    private void addPhoto(Photo photo) {
        photos.add(photo);
        StreamResource resource = new StreamResource(photo.id(), () -> new ByteArrayInputStream(fileStorage.getFile(photo.id())));
        Image image = new Image(resource, photo.id());
        image.setWidth("200px");
        Button removeButton = new Button(VaadinIcon.CLOSE.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON);
        
        VerticalLayout layout = new VerticalLayout(image, removeButton);
        layout.setWidth("200px");
        layout.setPadding(false);
        layout.setSpacing(false);
        removeButton.setWidthFull();
        removeButton.addClickListener(click -> {
            mainLayout.remove(layout);
            photos.remove(photo);
            changeHappened();
            fileStorage.delete(photo.id());
        });
        
        mainLayout.add(layout);
    }
    
    void changeHappened() {
        this.updateValue();
    }

}
