/*
 * To change this template, choose Tools | Templates
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
import probe.com.model.beans.DatasetBean;
import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 */
public class ExperimentUploadPanel extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener, Serializable {

    private final MainHandler handler;
    private final User user;
    private final ProgressIndicator pi = new ProgressIndicator();
    private final VerticalLayout newExpLayout = new VerticalLayout();
    private final VerticalLayout oldExpLayout = new VerticalLayout();
    private final VerticalLayout uploaderLayout = new VerticalLayout();
    private TextField expNameField, speciesField, sampleTypeField, sampleProcessingField, instrumentTypeField, fragModeField, UploadedByNameField, emailField, publicationLinkField;
    private TextArea descriptionField;
    private final HorizontalLayout bodyLayout = new HorizontalLayout();
    private Map<Integer, DatasetBean> expList;
    private final GeneralUtil util = new GeneralUtil();
    private Select select;
    private final VerticalLayout expDetails = new VerticalLayout();
    private Upload upload;
    private final IconGenerator help = new IconGenerator();
    private HorizontalLayout helpNote;
    private File file;
    private final VerticalLayout selectLayout = new VerticalLayout();
    private Property.ValueChangeListener selectListener;
    private Form newExpForm;
    private Select selectRemoveExp;
    private VerticalLayout removeExpLayout;
    private VerticalLayout selectRemoveExpLayout;

    /**
     *
     * @param handler main application handler
     * @param user authenticated user
     */
    public ExperimentUploadPanel(MainHandler handler, User user) {
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

    /**
     * initialize the upload unit layout
     */
    private void initLayouts() {
        Label newExpLable = new Label("<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>New Experiment:</strong></h4>");
        newExpLable.setContentMode(ContentMode.HTML);
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

        sampleTypeField = new TextField("Sample Type:");
        sampleTypeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        sampleTypeField.setRequired(true);
        sampleTypeField.setRequiredError("EXPERIMENT SAMPLE TYPE CAN NOT BE EMPTY!");
        sampleTypeField.setWidth("350px");
        sampleTypeField.setMaxLength(70);

        sampleProcessingField = new TextField("Sample Processing:");
        sampleProcessingField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        sampleProcessingField.setRequired(true);
        sampleProcessingField.setRequiredError("EXPERIMENT SAMPLE PROCESSING CAN NOT BE EMPTY!");
        sampleProcessingField.setWidth("350px");
        sampleProcessingField.setMaxLength(70);

        instrumentTypeField = new TextField("Instrument Type:");
        instrumentTypeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        instrumentTypeField.setRequired(true);
        instrumentTypeField.setRequiredError("EXPERIMENT INSTURMENT TYPE CAN NOT BE EMPTY!");
        instrumentTypeField.setWidth("350px");
        instrumentTypeField.setMaxLength(70);

        fragModeField = new TextField("Frag Mode:");
        fragModeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        fragModeField.setRequired(true);
        fragModeField.setRequiredError("EXPERIMENT FRAG MODE CAN NOT BE EMPTY!");
        fragModeField.setWidth("350px");
        fragModeField.setMaxLength(70);

        UploadedByNameField = new TextField("Uploaded By:");
        UploadedByNameField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        UploadedByNameField.setRequired(true);
        UploadedByNameField.setRequiredError("EXPERIMENT UPLOADED BY CAN NOT BE EMPTY!");
        UploadedByNameField.setValue(user.getUsername());
        UploadedByNameField.setEnabled(false);
        UploadedByNameField.setWidth("350px");
        UploadedByNameField.setMaxLength(70);

        emailField = new TextField("Email:");
        emailField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        emailField.setRequired(true);
        emailField.setValue(user.getEmail());
        emailField.setEnabled(false);
        emailField.setRequiredError("EXPERIMENT EMAIL CAN NOT BE EMPTY!");
        emailField.setWidth("350px");
        emailField.setMaxLength(70);

        descriptionField = new TextArea("Description:");
        descriptionField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        descriptionField.setRequired(true);
        descriptionField.setRequiredError("EXPERIMENT Description CAN NOT BE EMPTY!");
        descriptionField.setWidth("350px");
        descriptionField.setMaxLength(950);

        publicationLinkField = new TextField("Publication Link:");
        publicationLinkField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        publicationLinkField.setWidth("350px");
        publicationLinkField.setMaxLength(300);

        newExpForm.addField(1, expNameField);
        newExpForm.addField(2, descriptionField);

        newExpForm.addField(3, speciesField);
        newExpForm.addField(4, sampleTypeField);
        newExpForm.addField(5, sampleProcessingField);
        newExpForm.addField(6, instrumentTypeField);
        newExpForm.addField(7, fragModeField);
        newExpForm.addField(8, UploadedByNameField);
        newExpForm.addField(9, emailField);
        newExpForm.addField(10, publicationLinkField);

        /**
         * ***************************************************************************************************************
         */
        oldExpLayout.setMargin(true);
        oldExpLayout.setSpacing(true);
        Label oldExpLable = new Label("<h4 style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Or Update Existing Experiments !</strong></h4><h4>For New Experiment Please Leave Experiment ID Blank!</h4><h4 style='color:blue'><strong style='color:red'>* </strong> For New Experiment Please Remember to Upload Protein file first!</h4>");
        oldExpLable.setContentMode(ContentMode.HTML);
        oldExpLable.setHeight("45px");
        oldExpLable.setWidth("100%");
        oldExpLayout.addComponent(oldExpLable);
        oldExpLayout.setComponentAlignment(oldExpLable, Alignment.TOP_CENTER);
        expList = handler.getDatasetList();
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
                    DatasetBean expDet = expList.get(id);
                    if (expDetails != null) {
                        expDetails.removeAllComponents();
                        if (expDet.getProteinsNumber() == 0) {
                            Label l = new Label("<h4 style='color:red'>1) Protein File is Missing</h4>");
                            l.setContentMode(ContentMode.HTML);
                            expDetails.addComponent(l);
                        } else {
                            Label l = new Label("<h4 style='color:blue'>1) Protein File is Uploaded</h4>");
                            l.setContentMode(ContentMode.HTML);
                            expDetails.addComponent(l);
                        }
                        if (expDet.getFractionsNumber() == 0) {
                            Label l = new Label("<h4 style='color:red'>2) Fraction File is Missing</h4>");
                            l.setContentMode(ContentMode.HTML);
                            expDetails.addComponent(l);
                        } else {
                            Label l = new Label("<h4 style='color:blue'>2) Fraction File Uploaded</h4>");
                            l.setContentMode(ContentMode.HTML);
                            expDetails.addComponent(l);
                        }
                        if (expDet.getPeptidesNumber() == 0) {
                            Label l = new Label("<h4 style='color:red'>3) Peptides File is Missing</h4>");
                            l.setContentMode(ContentMode.HTML);
                            expDetails.addComponent(l);
                        } else {
                            Label l = new Label("<h4 style='color:blue'>3) Peptides File Uploaded</h4>");
                            l.setContentMode(ContentMode.HTML);
                            expDetails.addComponent(l);

                        }
                    }
                } else {
                    expDetails.removeAllComponents();
                    Label labelDetails = new Label("<h4 style='color:red;'>Please Select Experiment To Show the Details.</h4>");
                    labelDetails.setContentMode(ContentMode.HTML);
                    expDetails.addComponent(labelDetails);

                }

            }
        };
        select.addValueChangeListener(selectListener);
        select.setWidth("100%");
        selectLayout.addComponent(select);
        oldExpLayout.addComponent(selectLayout);
        // Create the Upload component. 
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("30px");
        oldExpLayout.addComponent(spacer);

        upload = new Upload(null, this);
        upload.setStyleName("small");
        upload.setVisible(true);
        upload.setHeight("30px");
        upload.setWidth("100%");
        upload.setButtonCaption("UPLOAD ADD / EDIT EXPERIMENT");
        upload.addSucceededListener(this);

        oldExpLayout.addComponent(upload);

        Label label = new Label("<h4 style='color:blue;'>Please upload proteins file first</h4><h4 style='color:blue;'>Please upload proteins files in (.txt) format.</h4><h4 style='color:blue;'>Upload fraction range file after upload protein fraction file.</h4><h4 style='color:blue;'>Upload fraction range file in (.xlsx) format.</h4>");
        label.setContentMode(ContentMode.HTML);
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
        removeExpLable.setContentMode(ContentMode.HTML);
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

        removeExpButton.addClickListener(new Button.ClickListener() {
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
                        boolean test = handler.getAuthenticatorHandler().removeExp(expId);
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
            l.setContentMode(ContentMode.HTML);
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

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        boolean validData = false;
        try {
            DatasetBean newExp = this.validatDatasetForm();
            if (newExp != null) {
                validData = true;
            }
            if (validData) {
                // send the file to the reader to extract the information 
                boolean test = handler.handelDatasetFile(file, event.getMIMEType(), newExp);//Getting data from the uploaded file..here we assume that the experiment id is 1
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

    /**
     * validate the dataset form before start uploading and processing the files
     */
    private DatasetBean validatDatasetForm() {
        DatasetBean newExp;
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

                newExpForm.commit();
                newExpForm.focus();
                return null;

            } else {
                boolean checkName = false;

                for (DatasetBean exp : expList.values()) {
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

                    newExp = new DatasetBean();
                    newExp.setName(expName);
                    newExp.setSpecies(expSpecies);
                    newExp.setSampleType(expSampleType);
                    newExp.setSampleProcessing(expSampleProcessing);
                    newExp.setInstrumentType(expInstrumentType);
                    newExp.setFragMode(expFragMode);
                    newExp.setUploadedByName(expUploadedByName);
                    newExp.setEmail(expEmail);
                    newExp.setPublicationLink(expPublicationLink);
                    newExp.setDatasetId(-1);
                    newExp.setDescription(expDescription);
                    return newExp;
                }

            }
        } else//update old experiment
        {
            String str = select.getValue().toString();
            String[] strArr = str.split("\t");
            int id = (Integer.valueOf(strArr[0]));
            DatasetBean exp = expList.get(id);
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
            select.removeValueChangeListener(selectListener);
        }
        selectLayout.removeAllComponents();

        List<String> strExpList = util.getStrExpList(expList, user.getEmail());
        select = new Select("Experiment ID", strExpList);
        select.setImmediate(true);
        select.addValueChangeListener(selectListener);
        select.setWidth("100%");
        selectLayout.addComponent(select);

        selectRemoveExpLayout.removeAllComponents();
        selectRemoveExp = new Select("Experiment ID", strExpList);
        selectRemoveExp.setWidth("100%");
        selectRemoveExpLayout.addComponent(selectRemoveExp);
        selectRemoveExpLayout.setComponentAlignment(selectRemoveExp, Alignment.MIDDLE_CENTER);

    }
}
