/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 */
public class UpdateExperDetailsLayout extends VerticalLayout implements Serializable{
    private Select select;
    private Form existExpForm;
     private TextField expNameField, speciesField, sampleTypeField, sampleProcessingField, instrumentTypeField, fragModeField, UploadedByNameField, emailField, publicationLinkField;
    private TextArea descriptionField;
    private VerticalLayout selectLayout = new VerticalLayout();
    private  Map<Integer, ExperimentBean> expList;
    private  ExperimentBean expDet;
    private ExperimentHandler handler;
    private User user;
    private Property.ValueChangeListener listener;
   
    public UpdateExperDetailsLayout(final ExperimentHandler handler,User user)
    {
        this.setWidth("100%");
        this.addComponent(selectLayout);
        this.handler = handler;
        this.user = user;
        
        expList = handler.getExperiments(null);
        List<String> strExpList = new ArrayList<String>();
        for (ExperimentBean exp : expList.values()) {
            if (user.getEmail().equalsIgnoreCase("admin@csf.no") || exp.getEmail().equalsIgnoreCase(user.getEmail())) {
                String str = exp.getExpId() + "	" + exp.getName() + "	( " + exp.getUploadedByName() + " )";
                strExpList.add(str);
            }
        }
        select = new Select("Experiment ID", strExpList);
        select.setImmediate(true);
        selectLayout.addComponent(select);
        existExpForm = new Form();
        existExpForm.setCaption("Exist Experiments");
        
        this.addComponent(existExpForm);
        final Button updateBtn = new Button("UPDATE");
        updateBtn.setStyleName(Reindeer.BUTTON_SMALL);
        // Create the Form
       
        existExpForm.setInvalidCommitted(false); // no invalid values in data model
        // Determines which properties are shown, and in which order:	        
        expNameField = new TextField("Experiment Name:");
        expNameField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        expNameField.setRequired(true);
        expNameField.setRequiredError("EXPERIMENT NAME CAN NOT BE EMPTY!");
        expNameField.setWidth("350px");
        expNameField.setMaxLength(70);
        expNameField.setEnabled(false);

        speciesField = new TextField("Species:");
        speciesField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        speciesField.setRequired(true);
        speciesField.setRequiredError("EXPERIMENT SPECIES CAN NOT BE EMPTY!");
        speciesField.setWidth("350px");
        speciesField.setMaxLength(70);
        speciesField.setEnabled(false);

        sampleTypeField = new TextField("Sample Type:");
        sampleTypeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        sampleTypeField.setRequired(true);
        sampleTypeField.setRequiredError("EXPERIMENT SAMPLE TYPE CAN NOT BE EMPTY!");
        sampleTypeField.setWidth("350px");
        sampleTypeField.setMaxLength(70);
        sampleTypeField.setEnabled(false);

        sampleProcessingField = new TextField("Sample Processing:");
        sampleProcessingField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        sampleProcessingField.setRequired(true);
        sampleProcessingField.setRequiredError("EXPERIMENT SAMPLE PROCESSING CAN NOT BE EMPTY!");
        sampleProcessingField.setWidth("350px");
        sampleProcessingField.setMaxLength(70);
        sampleProcessingField.setEnabled(false);

        instrumentTypeField = new TextField("Instrument Type:");
        instrumentTypeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        instrumentTypeField.setRequired(true);
        instrumentTypeField.setEnabled(false);
        instrumentTypeField.setRequiredError("EXPERIMENT INSTURMENT TYPE CAN NOT BE EMPTY!");
        instrumentTypeField.setWidth("350px");
        instrumentTypeField.setMaxLength(70);

        fragModeField = new TextField("Frag Mode:");
        fragModeField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        fragModeField.setRequired(true);
        fragModeField.setRequiredError("EXPERIMENT FRAG MODE CAN NOT BE EMPTY!");
        fragModeField.setWidth("350px");
        fragModeField.setMaxLength(70);
        fragModeField.setEnabled(false);

        UploadedByNameField = new TextField("Uploaded By:");
        UploadedByNameField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        UploadedByNameField.setRequired(true);
        UploadedByNameField.setRequiredError("EXPERIMENT UPLOADED BY CAN NOT BE EMPTY!");
        UploadedByNameField.setValue(user.getUsername());
        UploadedByNameField.setEnabled(false);
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
        emailField.setEnabled(false);

        descriptionField = new TextArea("Description:");
        descriptionField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        descriptionField.setRequired(true);
        descriptionField.setRequiredError("EXPERIMENT Description CAN NOT BE EMPTY!");
        descriptionField.setWidth("350px");
        descriptionField.setMaxLength(950);
        descriptionField.setEnabled(false);

        publicationLinkField = new TextField("Publication Link:");
        publicationLinkField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        publicationLinkField.setWidth("350px");
        publicationLinkField.setMaxLength(300);
        publicationLinkField.setEnabled(false);

        existExpForm.addField(Integer.valueOf(1), expNameField);
        existExpForm.addField(Integer.valueOf(2), descriptionField);

        existExpForm.addField(Integer.valueOf(3), speciesField);
        existExpForm.addField(Integer.valueOf(4), sampleTypeField);
        existExpForm.addField(Integer.valueOf(5), sampleProcessingField);
        existExpForm.addField(Integer.valueOf(6), instrumentTypeField);
        existExpForm.addField(Integer.valueOf(7), fragModeField);
        existExpForm.addField(Integer.valueOf(8), UploadedByNameField);
        existExpForm.addField(Integer.valueOf(9), emailField);
        existExpForm.addField(Integer.valueOf(10), publicationLinkField);
        this.addComponent(updateBtn);
        updateBtn.setEnabled(false);
        listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object o = select.getValue();
                if (o != null) {
                    String str = select.getValue().toString();
                    String[] strArr = str.split("\t");
                    final int id = (Integer.valueOf(strArr[0]));
                    updateBtn.setEnabled(true);
                   expDet = expList.get(id);

                    expNameField.setValue(expDet.getName());
                    expNameField.setEnabled(true);

                    speciesField.setValue(expDet.getSpecies());
                    speciesField.setEnabled(true);

                    sampleTypeField.setValue(expDet.getSampleType());
                    sampleTypeField.setEnabled(true);

                    sampleProcessingField.setValue(expDet.getSampleProcessing());
                    sampleProcessingField.setEnabled(true);

                    instrumentTypeField.setValue(expDet.getInstrumentType());
                    instrumentTypeField.setEnabled(true);

                    fragModeField.setValue(expDet.getFragMode());
                    fragModeField.setEnabled(true);

                    emailField.setValue(expDet.getEmail());
                    // emailField.setEnabled(true);

                    descriptionField.setValue(expDet.getDescription());
                    descriptionField.setEnabled(true);

                    publicationLinkField.setValue(expDet.getPublicationLink());
                    publicationLinkField.setEnabled(true);

                    updateBtn.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            expDet = validateForm(expDet,id);
                            if (expDet != null) {
                                handler.updateExpData(expDet);
                            }
                            updateSelect();
                            clean();
                        }
                        private void clean()
                    {
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

                    
                    }
                    });
                    

                }

            }
        };
        select.addListener(listener);
    }
     private ExperimentBean validateForm(ExperimentBean newExp,int id) {

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
            existExpForm.commit();
            existExpForm.focus();
            return null;

        } else {
            boolean checkName = false;

            if (expName.equals(expName)) {
            } else {
                for (ExperimentBean exp : expList.values()) {
                    if (exp.getName().equalsIgnoreCase(expName)) {
                        checkName = true;
                        break;
                    }
                }
            }
            if (checkName) {
                expNameField.setValue("This Name is Not  Available Please Choose Another Name ");
                expNameField.commit();
                existExpForm.focus();
                return null;
            } else {
                newExp.setName(expName);
                newExp.setSpecies(expSpecies);
                newExp.setSampleType(expSampleType);
                newExp.setSampleProcessing(expSampleProcessing);
                newExp.setInstrumentType(expInstrumentType);
                newExp.setFragMode(expFragMode);
                newExp.setUploadedByName(expUploadedByName);
                newExp.setEmail(expEmail);
                newExp.setPublicationLink(expPublicationLink);
                newExp.setDescription(expDescription);
                newExp.setPeptidesNumber(getValidatedPeptideNumber(id));
                return newExp;
            }
        }
    }
     private void updateSelect()
     {
         select.removeListener(listener);
         selectLayout.removeAllComponents();
           expList = handler.getExperiments(null);
        List<String> strExpList = new ArrayList<String>();
        for (ExperimentBean exp : expList.values()) {
            if (user.getEmail().equalsIgnoreCase("admin@csf.no") || exp.getEmail().equalsIgnoreCase(user.getEmail())) {
                String str = exp.getExpId() + "	" + exp.getName() + "	( " + exp.getUploadedByName() + " )";
                strExpList.add(str);
            }
        }
        select = new Select("Experiment ID", strExpList);
        select.setImmediate(true);
        selectLayout.addComponent(select);
        select.addListener(listener);
     
     }
     
     private int getValidatedPeptideNumber(int expId)
     {
         return handler.getPeptidesList(expId, true).size();
     
     
     }

    
}
