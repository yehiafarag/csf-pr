package com.view.subviewunits;
/*
 * this class is uploader class
 * allow administrator to upload files to use as data fed for the database
 * */

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.handlers.ExperimentHandler;
import com.model.beans.ExperimentBean;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

public class UploadUnit  extends CustomComponent implements Upload.SucceededListener,Upload.FailedListener,Upload.Receiver,Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private Panel container;         // Root element for contained components.
		private	Panel root;         // Root element for contained components.
		private	Panel filePanel;   // Panel that contains the uploaded file.
		private	File  file;         // File to write to.
	    private  TextField expNameField;
	    private TextField speciesField, sampleTypeField, sampleProcessingField, instrumentTypeField, fragModeField,UploadedByNameField, emailField, publicationLinkField;
		private ExperimentHandler eh ;
	    private Map<Integer,ExperimentBean> expList;
	    private Upload upload;
	    private Select select;
	    private  Form newExpForm;
	   public UploadUnit(String url,String dbName,String driver,String userName, String password) {
			eh = new ExperimentHandler( url,dbName,driver,userName, password);
			this.updateComponents();
	    	
	    }

	    // Callback method to begin receiving the upload.
	    public OutputStream receiveUpload(String filename, String MIMEType) {
	        FileOutputStream fos = null; // Output stream to write to
	        file = new File(filename);
	        
	        try {
	        	if(MIMEType.equals("text/plain"))
	        	{
	        		// Open the file for writing.
		            fos = new FileOutputStream(file);       
	        		
	        	}
	        	else
	        	{
	        		getWindow().showNotification("Failed !  Please Check Your Uploaded File !");  //file didn't store in db  
	            		        		
	        	}
	            
	            
	            
	            
	           } catch (final java.io.FileNotFoundException e) {
	            // Error while opening the file. Not reported here.
	            e.printStackTrace();
	            return null;
	        }catch (final Exception e) {
	            // Error while opening the file. Not reported here.
	            e.printStackTrace();
	            return null;
	        }

	        return fos; // Return the output stream to write to
	    }
	    

	    // This is called if the upload is finished.
	    public void uploadSucceeded(Upload.SucceededEvent event) {
	        // Log the upload on screen.
	    	int expId=-1;
	    	String name = "";
	    	String species="";
	    	String sampleType="";
	    	String sampleProcessing="";
	    	String instrumentType="";
	    	String fragMode="";
	    	String uploadedByName="";
	    	String email ="";
	    	String publicationLink="";
	  		
	    	boolean validData = false;
            try 
            {
            	
            	if(select.getValue() == null)//new experiment
            		{
            			String expName = (String)expNameField.getValue();
            			String expSpecies= (String) speciesField.getValue(); 
            			String expSampleType= (String) sampleTypeField.getValue();
            			String expSampleProcessing= (String) sampleProcessingField.getValue();
            			String expInstrumentType= (String) instrumentTypeField.getValue();
            			String expFragMode= (String) fragModeField.getValue();
            			String expUploadedByName= (String) UploadedByNameField.getValue();
            			String expEmail= (String) emailField.getValue();
            			String expPublicationLink= (String) publicationLinkField.getValue();         			
            			
            			 
            			if((expName == null)||(expSpecies == null)||(expSampleType == null)||(expSampleProcessing == null)||(expInstrumentType == null)||(expFragMode == null)||(expUploadedByName == null)||(expEmail == null)||expName.equals("")|| expSpecies.equals("")|| expSampleType.equals("")|| expSampleProcessing.equals("")|| expInstrumentType.equals("")|| expFragMode.equals("")|| expUploadedByName.equals("")|| expEmail.equals(""))
            			{	            	
            				//file didn't store in data base  		
            					 newExpForm.commit();            					
            				
            			}
            			else
            			{
		            		name = expName;
		            		species = expSpecies;
		            		sampleType=expSampleType;
		            		sampleProcessing=expSampleProcessing;
		        	    	instrumentType=expInstrumentType;
		        	    	fragMode=expFragMode;
		        	    	uploadedByName=expUploadedByName;
		        	    	email =expEmail;
		        	    	publicationLink=expPublicationLink;
		            		validData = true;
	            		
            			}
            	}
            	else//update old experiment
            	{
            		String str = select.getValue().toString();
            		String[] strArr =str.split("\t");
            		int id = (Integer.valueOf(strArr[0]));
            		ExperimentBean exp = expList.get(id);
            		name = exp.getName();
            		expId=exp.getExpId();
            		species = exp.getSpecies();
            		sampleType=exp.getSampleType();
            		sampleProcessing=exp.getSampleProcessing();
        	    	instrumentType=exp.getInstrumentType();
        	    	fragMode=exp.getFragMode();
        	    	uploadedByName=exp.getUploadedByName();
        	    	email =exp.getEmail();
        	    	publicationLink=exp.getPublicationLink(); 
            		validData =true;
            		
            	}
            	if(validData){
            	// send the file to the reader to extract the information
	            	boolean test = eh.handelExperimentFile(file,event.getMIMEType(),expId,name,species,sampleType,sampleProcessing,instrumentType,fragMode,uploadedByName,email,publicationLink);//Getting data from the uploaded file..here we assume that the experiment id is 1
	            	
	            	if(!test){
	            		getWindow().showNotification("Failed !  Please Check Your Uploaded File !");  //file didn't store in db  
	            	}else{		            	
		            	root.addComponent(new Label("File " + event.getFilename()  + " of type '" + event.getMIMEType() + "' uploaded."));
			       	        // Display the uploaded file in the file panel.
		            	getWindow().showNotification("Successful !");
				        final FileResource fileResource =  new FileResource(file, getApplication());
				        filePanel.removeAllComponents();
				        filePanel.addComponent(new Embedded("", fileResource));				        
				        updateComponents();
	            	}
	            	//file.delete();
            	}
		     } catch (Exception e) {e.printStackTrace();
					}			
	        
	    }
	    

	    // This is called if the upload fails.
	    public void uploadFailed(Upload.FailedEvent event) {
	        // Log the failure on screen.
	    	if(event.getMIMEType().equals(""))	    		
	    		root.addComponent(new Label("Uploading "   + event.getFilename() + " of type '"  + event.getMIMEType() + "' failed."));
	    	else
	    		root.addComponent(new Label("Sorry Only files with txt type is allowed to upload, ...Upload failed."));
	    	getWindow().showNotification("Failed :-( !");
	   
	    }
	    @SuppressWarnings("deprecation")
		private void updateComponents()
	    {
	    	if(container != null)
	    		container.removeAllComponents();
	    	container = new Panel();
	        root = new Panel();	
	        root.setStyle(Reindeer.PANEL_LIGHT);
	        container.setHeight("100%");	
	        container.setStyle(Reindeer.PANEL_LIGHT);
	        setCompositionRoot(container);
	        
	     // Create the Form
	        newExpForm = new Form();
	        newExpForm.setCaption("New Experiment");
	        newExpForm.setWriteThrough(false); // we want explicit 'apply'
	        newExpForm.setInvalidCommitted(false); // no invalid values in data model
	        // Determines which properties are shown, and in which order:	        
	        expNameField = new TextField("Experiment Name:");
	        expNameField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        expNameField.setRequired(true);
	        expNameField.setRequiredError("EXPERIMENT NAME CAN NOT BE EMPTY!");
	       
	        
	        
	        speciesField = new TextField("Species:");
	        speciesField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        speciesField.setRequired(true);
	        speciesField.setRequiredError("EXPERIMENT SPECIES CAN NOT BE EMPTY!");	        
	        
	        
	        sampleTypeField = new TextField("Sample Type:");
	        sampleTypeField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        sampleTypeField.setRequired(true);
	        sampleTypeField.setRequiredError("EXPERIMENT SAMPLE TYPE CAN NOT BE EMPTY!");	        
	        
	        
	        sampleProcessingField = new TextField("Sample Processing:");
	        sampleProcessingField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        sampleProcessingField.setRequired(true);
	        sampleProcessingField.setRequiredError("EXPERIMENT SAMPLE PROCESSING CAN NOT BE EMPTY!");	        
	        
	        
	        instrumentTypeField = new TextField("Instrument Type:");
	        instrumentTypeField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        instrumentTypeField.setRequired(true);
	        instrumentTypeField.setRequiredError("EXPERIMENT INSTURMENT TYPE CAN NOT BE EMPTY!");	        
	        
	        
	        fragModeField = new TextField("Frag Mode:");
	        fragModeField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        fragModeField.setRequired(true);
	        fragModeField.setRequiredError("EXPERIMENT FRAG MODE CAN NOT BE EMPTY!");	        
	        
	        UploadedByNameField = new TextField("Uploaded By:");
	        UploadedByNameField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        UploadedByNameField.setRequired(true);
	        UploadedByNameField.setRequiredError("EXPERIMENT UPLOADED BY CAN NOT BE EMPTY!");	        
	        
	        emailField = new TextField("Email:");
	        emailField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        emailField.setRequired(true);
	        emailField.setRequiredError("EXPERIMENT EMAIL CAN NOT BE EMPTY!");	        
	        
	        publicationLinkField = new TextField("publication Link:");
	        publicationLinkField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        
	        
	        newExpForm.addField(Integer.valueOf(1), expNameField);
	        newExpForm.addField(Integer.valueOf(2), speciesField);
	        newExpForm.addField(Integer.valueOf(3), sampleTypeField);
	        newExpForm.addField(Integer.valueOf(4), sampleProcessingField);
	        newExpForm.addField(Integer.valueOf(5), instrumentTypeField);
	        newExpForm.addField(Integer.valueOf(6), fragModeField);
	        newExpForm.addField(Integer.valueOf(7), UploadedByNameField);
	        newExpForm.addField(Integer.valueOf(8), emailField);
	        newExpForm.addField(Integer.valueOf(9), publicationLinkField);
	        
	        
	        // Add form to layout
	        container.addComponent(newExpForm);
	        container.addComponent(new Label("Or Choose from !"));
	        
	        // Create the Form
	        Form existExpForm = new Form();
	        existExpForm.setCaption("Exist Experiments");	        
	        expList = eh.getExperiments(null);
	        List<String>strExpList = new ArrayList<String>();
	        for(ExperimentBean exp:expList.values())
	        {
	        	String str = exp.getExpId()+"	"+exp.getName()+"	"+exp.getUploadedByName();
	        	strExpList.add(str);
	        }
	        select = new Select("Experiment ID", strExpList);	
	        select.setWidth("40%");
            existExpForm.addField(Integer.valueOf(1),select); 
            
            Label l = new Label("<h4 style='color:blue'>For New Experiment Please Leave Experiment ID Blank!</h4>");
            l.setContentMode(Label.CONTENT_XHTML);	        
	        
	        // Add form to layout
            VerticalLayout vlo = new VerticalLayout();            
            vlo.addComponent(existExpForm);
            vlo.addComponent(l);
	        container.addComponent(vlo);

	        // Create the Upload component. 
	        
	        upload =   new Upload("Upload Proteins Files", this);
	        upload.setStyle(Reindeer.BUTTON_SMALL);	        
	        upload.setVisible(true);
	     
	        
	        // Use a custom button caption instead of plain "Upload".
	        upload.setButtonCaption("ADD / EDIT EXPERIMENT !");
	        
	        
	        // Listen for events regarding the success of upload.
	        
	        upload.addListener((Upload.SucceededListener) this);
	        upload.addListener((Upload.FailedListener) this);
	        vlo.addComponent(upload);	        
	        // Create a panel for displaying the uploaded file.
	        filePanel = new Panel("Uploaded file");
	        filePanel.setStyle(Reindeer.SPLITPANEL_SMALL);
	        filePanel.addComponent( new Label("No file uploaded yet"));
	        vlo.addComponent(filePanel);
	        root.addComponent(vlo);
	        container.addComponent(root);
	    }
	    
        
    


}
