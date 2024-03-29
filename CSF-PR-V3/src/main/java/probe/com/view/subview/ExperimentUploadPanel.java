/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
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
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.User;
import probe.com.view.subview.util.GeneralUtil;
import probe.com.view.subview.util.Help;

/**
 *
 * @author Yehia Farag
 */
public class ExperimentUploadPanel extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener, Serializable {

    private ExperimentHandler handler;
    private User user;
    private ProgressIndicator pi = new ProgressIndicator();
    private VerticalLayout newExpLayout = new VerticalLayout();
    private VerticalLayout oldExpLayout = new VerticalLayout();
    private VerticalLayout uploaderLayout = new VerticalLayout();
    private TextField expNameField, speciesField, sampleTypeField, sampleProcessingField, instrumentTypeField, fragModeField, UploadedByNameField, emailField, publicationLinkField;
    private TextArea descriptionField;
    private HorizontalLayout bodyLayout = new HorizontalLayout();
    private Map<Integer, ExperimentBean> expList;
    private GeneralUtil util = new GeneralUtil();
    private Select select;
    private VerticalLayout expDetails = new VerticalLayout();
    private Upload upload;
    private Help help = new Help();
    private HorizontalLayout helpNote;
    private File file;
    private VerticalLayout selectLayout = new VerticalLayout();
    private Property.ValueChangeListener selectListener;
    private Form newExpForm;
    private Select selectRemoveExp;
    private VerticalLayout removeExpLayout;
    private VerticalLayout selectRemoveExpLayout;

    public ExperimentUploadPanel(ExperimentHandler handler, User user) {
        this.handler = handler;
        this.user = user;
        this.pi.setValue(0f);
        this.pi.setCaption("Loading...");
        this.pi.setVisible(false);
        this.setWidth("100%");
        this.setSpacing(true);
        this.addComponent(bodyLayout);
        this.bodyLayout.setWidth("100%");

        bodyLayout.addComponent(newExpLayout);

        bodyLayout.addComponent(oldExpLayout);
        this.oldExpLayout.setWidth("100%");
        this.addComponent(uploaderLayout);
        this.uploaderLayout.setWidth("100%");
        this.initLayouts();

    }

    private void initLayouts() {
        Label newExpLable = new Label("<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>New Experiment:</strong></h4>");
        newExpLable.setContentMode(Label.CONTENT_XHTML);
        newExpLable.setHeight("45px");
        newExpLable.setWidth("100%");

        newExpLayout.setMargin(true);
        newExpLayout.addComponent(newExpLable);
        newExpLayout.setComponentAlignment(newExpLable, Alignment.TOP_CENTER);

        newExpForm = new Form();
        newExpForm.setInvalidCommitted(false); // no invalid values in data model
        newExpLayout.addComponent(newExpForm);

        expNameField = new TextField("Experiment Name:");
        expNameField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        expNameField.setRequired(true);
        expNameField.setRequiredError("EXPERIMENT NAME CAN NOT BE EMPTY!");
        expNameField.setWidth("350px");
        expNameField.setMaxLength(70);


        speciesField = new TextField("Species:");
        speciesField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        speciesField.setRequired(true);
        speciesField.setRequiredError("EXPERIMENT SPECIES CAN NOT BE EMPTY!");
        speciesField.setWidth("350px");
        speciesField.setMaxLength(70);
//        newExpLayout.addComponent(speciesField);

        sampleTypeField = new TextField("Sample Type:");
        sampleTypeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        sampleTypeField.setRequired(true);
        sampleTypeField.setRequiredError("EXPERIMENT SAMPLE TYPE CAN NOT BE EMPTY!");
        sampleTypeField.setWidth("350px");
        sampleTypeField.setMaxLength(70);
//        newExpLayout.addComponent(sampleTypeField);

        sampleProcessingField = new TextField("Sample Processing:");
        sampleProcessingField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        sampleProcessingField.setRequired(true);
        sampleProcessingField.setRequiredError("EXPERIMENT SAMPLE PROCESSING CAN NOT BE EMPTY!");
        sampleProcessingField.setWidth("350px");
        sampleProcessingField.setMaxLength(70);
//        newExpLayout.addComponent(sampleProcessingField);



        instrumentTypeField = new TextField("Instrument Type:");
        instrumentTypeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        instrumentTypeField.setRequired(true);
        instrumentTypeField.setRequiredError("EXPERIMENT INSTURMENT TYPE CAN NOT BE EMPTY!");
        instrumentTypeField.setWidth("350px");
        instrumentTypeField.setMaxLength(70);
//        newExpLayout.addComponent(instrumentTypeField);

        fragModeField = new TextField("Frag Mode:");
        fragModeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        fragModeField.setRequired(true);
        fragModeField.setRequiredError("EXPERIMENT FRAG MODE CAN NOT BE EMPTY!");
        fragModeField.setWidth("350px");
        fragModeField.setMaxLength(70);
//        newExpLayout.addComponent(fragModeField);

        UploadedByNameField = new TextField("Uploaded By:");
        UploadedByNameField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        UploadedByNameField.setRequired(true);
        UploadedByNameField.setRequiredError("EXPERIMENT UPLOADED BY CAN NOT BE EMPTY!");
        UploadedByNameField.setValue(user.getUsername());
        UploadedByNameField.setEnabled(false);
        UploadedByNameField.setWidth("350px");
        UploadedByNameField.setMaxLength(70);
//        newExpLayout.addComponent(UploadedByNameField);


        emailField = new TextField("Email:");
        emailField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        emailField.setRequired(true);
        emailField.setValue(user.getEmail());
        emailField.setEnabled(false);
        emailField.setRequiredError("EXPERIMENT EMAIL CAN NOT BE EMPTY!");
        emailField.setWidth("350px");
        emailField.setMaxLength(70);
//        newExpLayout.addComponent(emailField);


        descriptionField = new TextArea("Description:");
        descriptionField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        descriptionField.setRequired(true);
        descriptionField.setRequiredError("EXPERIMENT Description CAN NOT BE EMPTY!");
        descriptionField.setWidth("350px");
        descriptionField.setMaxLength(950);
//        newExpLayout.addComponent(descriptionField);

        publicationLinkField = new TextField("Publication Link:");
        publicationLinkField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        publicationLinkField.setWidth("350px");
        publicationLinkField.setMaxLength(300);
//        newExpLayout.addComponent(publicationLinkField);

        newExpForm.addField(Integer.valueOf(1), expNameField);
        newExpForm.addField(Integer.valueOf(2), descriptionField);

        newExpForm.addField(Integer.valueOf(3), speciesField);
        newExpForm.addField(Integer.valueOf(4), sampleTypeField);
        newExpForm.addField(Integer.valueOf(5), sampleProcessingField);
        newExpForm.addField(Integer.valueOf(6), instrumentTypeField);
        newExpForm.addField(Integer.valueOf(7), fragModeField);
        newExpForm.addField(Integer.valueOf(8), UploadedByNameField);
        newExpForm.addField(Integer.valueOf(9), emailField);
        newExpForm.addField(Integer.valueOf(10), publicationLinkField);


        /**
         * ***************************************************************************************************************
         */
        oldExpLayout.setMargin(true);
        oldExpLayout.setSpacing(true);
        Label oldExpLable = new Label("<h4 style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Or Update Existing Experiments !</strong></h4><h4>For New Experiment Please Leave Experiment ID Blank!</h4><h4 style='color:blue'><strong style='color:red'>* </strong> For New Experiment Please Remember to Upload Protein file first!</h4>");
        oldExpLable.setContentMode(Label.CONTENT_XHTML);
        oldExpLable.setHeight("45px");
        oldExpLable.setWidth("100%");
        oldExpLayout.addComponent(oldExpLable);
        oldExpLayout.setComponentAlignment(oldExpLable, Alignment.TOP_CENTER);
        expList = handler.getExperiments(null);
        List<String> strExpList = util.getStrExpList(expList, user.getEmail());
        select = new Select("Experiment ID", strExpList);
        select.setImmediate(true);
        selectListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object o = select.getValue();
                if (o != null) {
                    String str = select.getValue().toString();
                    String[] strArr = str.split("\t");
                    int id = (Integer.valueOf(strArr[0]));
                    ExperimentBean expDet = expList.get(id);
                    if (expDetails != null) {
                        expDetails.removeAllComponents();
                        if (expDet.getProteinsNumber() == 0) {
                            Label l = new Label("<h4 style='color:red'>1) Protein File is Missing</h4>");
                            l.setContentMode(Label.CONTENT_XHTML);
                            expDetails.addComponent(l);
                        } else {
                            Label l = new Label("<h4 style='color:blue'>1) Protein File is Uploaded</h4>");
                            l.setContentMode(Label.CONTENT_XHTML);
                            expDetails.addComponent(l);
                        }
                        if (expDet.getFractionsNumber() == 0) {
                            Label l = new Label("<h4 style='color:red'>2) Fraction File is Missing</h4>");
                            l.setContentMode(Label.CONTENT_XHTML);
                            expDetails.addComponent(l);
                        } else {
                            Label l = new Label("<h4 style='color:blue'>2) Fraction File Uploaded</h4>");
                            l.setContentMode(Label.CONTENT_XHTML);
                            expDetails.addComponent(l);
                        }
                        if (expDet.getPeptidesNumber() == 0) {
                            Label l = new Label("<h4 style='color:red'>3) Peptides File is Missing</h4>");
                            l.setContentMode(Label.CONTENT_XHTML);
                            expDetails.addComponent(l);
                        } else {
                            Label l = new Label("<h4 style='color:blue'>3) Peptides File Uploaded</h4>");
                            l.setContentMode(Label.CONTENT_XHTML);
                            expDetails.addComponent(l);

                        }
                    }
                } else {
                    expDetails.removeAllComponents();
                    Label labelDetails = new Label("<h4 style='color:red;'>Please Select Experiment To Show the Details.</h4>");
                    labelDetails.setContentMode(Label.CONTENT_XHTML);
                    expDetails.addComponent(labelDetails);

                }


            }
        };
        select.addListener(selectListener);
        select.setWidth("100%");
        selectLayout.addComponent(select);
        oldExpLayout.addComponent(selectLayout);
        // Create the Upload component. 
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("30px");
        oldExpLayout.addComponent(spacer);

        upload = new Upload(null, this);
        upload.setStyleName("small");
        //   upload.setImmediate(true);
        upload.setVisible(true);
        upload.setHeight("30px");
        upload.setWidth("100%");
        upload.setButtonCaption("UPLOAD ADD / EDIT EXPERIMENT");
        upload.addListener(this);

        oldExpLayout.addComponent(upload);

        Label label = new Label("<h4 style='color:blue;'>Please upload proteins file first</h4><h4 style='color:blue;'>Please upload proteins files in (.txt) format.</h4><h4 style='color:blue;'>Upload fraction range file after upload protein fraction file.</h4><h4 style='color:blue;'>Upload fraction range file in (.xlsx) format.</h4>");
        label.setContentMode(Label.CONTENT_XHTML);
        helpNote = help.getHelpNote(label);
        helpNote.setMargin(new MarginInfo(false, true, true, true));
        oldExpLayout.addComponent(pi);
        oldExpLayout.addComponent(helpNote);
        oldExpLayout.setComponentAlignment(helpNote, Alignment.BOTTOM_RIGHT);
        oldExpLayout.addComponent(expDetails);


        removeExpLayout = new VerticalLayout();
        removeExpLayout.setMargin(true);
        removeExpLayout.setSpacing(true);
        removeExpLayout.setWidth("100%");
        oldExpLayout.addComponent(removeExpLayout);
        oldExpLayout.setComponentAlignment(removeExpLayout, Alignment.MIDDLE_CENTER);

        Label removeExpLable = new Label("<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Remove Existing Experiment</strong></h4>");
        removeExpLable.setContentMode(Label.CONTENT_XHTML);
        removeExpLable.setHeight("45px");
        removeExpLable.setWidth("100%");
        removeExpLayout.addComponent(removeExpLable);
        removeExpLayout.setComponentAlignment(removeExpLable, Alignment.TOP_CENTER);




        selectRemoveExpLayout = new VerticalLayout();
        selectRemoveExpLayout.setWidth("100%");
        removeExpLayout.addComponent(selectRemoveExpLayout);
        removeExpLayout.setComponentAlignment(selectRemoveExpLayout, Alignment.MIDDLE_CENTER);

        selectRemoveExp = new Select("Experiment ID", strExpList);
        selectRemoveExp.setWidth("100%");
        selectRemoveExpLayout.addComponent(selectRemoveExp);
        selectRemoveExpLayout.setComponentAlignment(selectRemoveExp, Alignment.MIDDLE_LEFT);

        Button removeExpButton = new Button("Remove Experiment");
        removeExpButton.setStyleName(Reindeer.BUTTON_SMALL);
        removeExpLayout.addComponent(removeExpButton);
        removeExpLayout.setComponentAlignment(removeExpButton, Alignment.BOTTOM_RIGHT);

        removeExpButton.addListener(new Button.ClickListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (selectRemoveExp.getValue().toString() != null && !selectRemoveExp.getValue().toString().equalsIgnoreCase("")) {
                    String str = selectRemoveExp.getValue().toString();
                    String[] strArr = str.split("\t");
                    Integer expId = (Integer.valueOf(strArr[0]));
                    if (expId != null) {
                        boolean test = handler.getAuthenticator().removeExp(expId);
                        if (test) {
                            cleanOver();
                        } else {
                            Notification.show("Failed to Remove The Experiment! ", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                    }

                } else {
                    Notification.show("Select Experiment to Remove ! ", Notification.Type.ERROR_MESSAGE);

                }
            }
        });




    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null; // Output stream to write to
        file = new File(filename);
        try {
            if (mimeType.equalsIgnoreCase("text/plain") || mimeType.trim().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".trim()) || mimeType.equalsIgnoreCase("application/octet-stream")) {
                fos = new FileOutputStream(file);
            }
        } catch (final java.io.FileNotFoundException e) {
            Label l = new Label("<h4 style='color:red'>" + e.getMessage() + "</h4>");
            l.setContentMode(Label.CONTENT_XHTML);
            expDetails.addComponent(l);
            System.err.println(e.getLocalizedMessage());
            return null;
        } catch (final Exception e) {
            // Error while opening the file. Not reported here.
            System.err.println(e.getLocalizedMessage());
            return null;
        }

        return fos; // Return the output stream to write to
    }

//    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        pi.setVisible(false);
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        boolean validData = false;
        try {
            ExperimentBean newExp = this.validatExpForm();
            if (newExp != null) {
                validData = true;
            }
            if (validData) {
                // send the file to the reader to extract the information 
                boolean test = handler.handelExperimentFile(file, event.getMIMEType(), newExp);//Getting data from the uploaded file..here we assume that the experiment id is 1
                if (!test) {
                    Notification.show("Failed !  Please Check Your Uploaded File !", Notification.Type.TRAY_NOTIFICATION);  //file didn't store in db  
                } else {
                    // Display the uploaded file in the file panel.
                    Notification.show("Successful !", Notification.Type.TRAY_NOTIFICATION);
                    cleanOver();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

//    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        try {

            expDetails.removeAllComponents();
            Thread.sleep(1000);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    pi.setVisible(true);

                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

//    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        if (event.getMIMEType().equals("")) {
            expDetails.addComponent(new Label("Uploading " + event.getFilename() + " of type '" + event.getMIMEType() + "' failed."));
        } else {
            expDetails.addComponent(new Label("Sorry Only files with txt or xlsx type is allowed to upload, ...Upload failed."));
        }
        Notification.show("Failed :-( !");
    }

    private ExperimentBean validatExpForm() {
        ExperimentBean newExp;
        if (select.getValue() == null)//new experiment
        {

            String expName = (String) expNameField.getValue();
            String expSpecies = (String) speciesField.getValue();
            String expSampleType = (String) sampleTypeField.getValue();
            String expSampleProcessing = (String) sampleProcessingField.getValue();
            String expInstrumentType = (String) instrumentTypeField.getValue();
            String expFragMode = (String) fragModeField.getValue();
            String expUploadedByName = (String) UploadedByNameField.getValue();
            String expEmail = (String) emailField.getValue();
            String expPublicationLink = (String) publicationLinkField.getValue();
            String expDescription = (String) descriptionField.getValue();

            if ((expName == null) || (expDescription == null) || (expSpecies == null) || (expSampleType == null) || (expSampleProcessing == null) || (expInstrumentType == null) || (expFragMode == null) || (expUploadedByName == null) || (expEmail == null) || expName.equals("") || expDescription.equals("") || expSpecies.equals("") || expSampleType.equals("") || expSampleProcessing.equals("") || expInstrumentType.equals("") || expFragMode.equals("") || expUploadedByName.equals("") || expEmail.equals("")) {
                //file didn't store in data base  		
//                expNameField.commit();
//                speciesField.commit();
//                sampleTypeField.commit();
//                sampleProcessingField.commit();
//                instrumentTypeField.commit();
//                fragModeField.commit();
//                UploadedByNameField.commit();
//                emailField.commit();
//                publicationLinkField.commit();
//                descriptionField.commit();
                newExpForm.commit();
                newExpForm.focus();
                return null;

            } else {
                boolean checkName = false;

                for (ExperimentBean exp : expList.values()) {
                    if (exp.getName().equalsIgnoreCase(expName)) {
                        checkName = true;
                        break;
                    }
                }
                if (checkName) {
                    expNameField.setValue("This Name is Not  Available Please Choose Another Name ");
                    expNameField.commit();
                    newExpForm.focus();
                    return null;
                } else {

                    newExp = new ExperimentBean();
                    newExp.setName(expName);
                    newExp.setSpecies(expSpecies);
                    newExp.setSampleType(expSampleType);
                    newExp.setSampleProcessing(expSampleProcessing);
                    newExp.setInstrumentType(expInstrumentType);
                    newExp.setFragMode(expFragMode);
                    newExp.setUploadedByName(expUploadedByName);
                    newExp.setEmail(expEmail);
                    newExp.setPublicationLink(expPublicationLink);
                    newExp.setExpId(-1);
                    newExp.setDescription(expDescription);
                    return newExp;
                }

            }
        } else//update old experiment
        {
            String str = select.getValue().toString();
            String[] strArr = str.split("\t");
            int id = (Integer.valueOf(strArr[0]));
            ExperimentBean exp = expList.get(id);
            return exp;

        }


    }

    private void cleanOver() {
        expNameField.setValue("");
        speciesField.setValue("");
        sampleTypeField.setValue("");
        sampleProcessingField.setValue("");
        instrumentTypeField.setValue("");
        fragModeField.setValue("");
        UploadedByNameField.setValue("");
        emailField.setValue("");
        publicationLinkField.setValue("");
        descriptionField.setValue("");
        expDetails.removeAllComponents();

        if (select != null) {
            select.removeListener(selectListener);
        }
        selectLayout.removeAllComponents();

        List<String> strExpList = util.getStrExpList(expList, user.getEmail());
        select = new Select("Experiment ID", strExpList);
        select.setImmediate(true);
        select.addListener(selectListener);
        select.setWidth("100%");
        selectLayout.addComponent(select);

        selectRemoveExpLayout.removeAllComponents();
        selectRemoveExp = new Select("Experiment ID", strExpList);
        selectRemoveExp.setWidth("100%");
        selectRemoveExpLayout.addComponent(selectRemoveExp);
        selectRemoveExpLayout.setComponentAlignment(selectRemoveExp, Alignment.MIDDLE_CENTER);

    }
}