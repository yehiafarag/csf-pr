/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.IdentificationDataset;
import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataUploader extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener, Serializable {

    private final MainHandler handler;
    private final User user;
    private final ProgressBar pi = new ProgressBar();
    private Map<Integer, IdentificationDataset> expList;
    private final GeneralUtil util = new GeneralUtil();
    private Upload upload;
    private File file;
    private VerticalLayout selectRemoveExpLayout;

    /**
     *
     * @param handler main application handler
     * @param user authenticated user
     */
    public QuantDataUploader(MainHandler handler, User user) {
        this.handler = handler;
        this.user = user;
        this.pi.setValue(0f);
        this.pi.setCaption("Loading...");
        this.pi.setVisible(false);
        this.setWidth("100%");
        this.setSpacing(true);
        this.initLayouts();

    }

    /**
     * initialize the upload unit layout
     */
    private void initLayouts() {
       
 Label selectFileTitle = new Label("<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Upload New File</strong></h4>");
        selectFileTitle.setContentMode(ContentMode.HTML);
        selectFileTitle.setHeight("45px");
        selectFileTitle.setWidth("100%");
        this.addComponent(selectFileTitle);
        this.setComponentAlignment(selectFileTitle, Alignment.TOP_CENTER);

        upload = new Upload(null, this);
        upload.setStyleName("small");
        upload.setVisible(true);
        upload.setHeight("30px");
        upload.setWidth("100%");
        upload.setButtonCaption("UPLOAD ADD / EDIT EXPERIMENT");
        upload.addSucceededListener(this);
        this.addComponent(upload);    


    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null; // Output stream to write to
        file = new File(filename);
        System.out.println("file type  "+ mimeType);
        try {
            if (mimeType.equalsIgnoreCase("text/csv") || mimeType.trim().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".trim()) || mimeType.equalsIgnoreCase("application/octet-stream")) {
                fos = new FileOutputStream(file);
            }
        } catch (final java.io.FileNotFoundException e) {
            Label l = new Label("<h4 style='color:red'>" + e.getMessage() + "</h4>");
            l.setContentMode(ContentMode.HTML);
            this.addComponent(l);
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (final Exception e) {
            // Error while opening the file. Not reported here.
            System.err.println(e.getLocalizedMessage());
            return null;
        }

        return fos; // Return the output stream to write to
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        boolean validData = true;
        try {
            
            if (validData) {
                // send the file to the reader to extract the information 
                boolean test = handler.handelQuantDataFile(file, event.getMIMEType());//Getting data from the uploaded file..here we assume that the experiment id is 1
                if (!test) {
                    Notification.show("Failed !  Please Check Your Uploaded File !", Notification.Type.TRAY_NOTIFICATION);  //file didn't store in db  
                } else {
                    // Display the uploaded file in the file panel.
                    Notification.show("Successful !", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * validate the dataset form before start uploading and processing the files
     */
   
}

