package com.view.subviewunits;
/*
 * this class is up-loader class
 * allow administrator to upload files to use as data fed for the database
 * */

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.handlers.Authenticator;
import com.handlers.ExperimentHandler;
import com.helperunits.Help;
import com.model.beans.ExperimentBean;
import com.model.beans.User;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.themes.Reindeer;

public class UploadUnit  extends CustomComponent implements Upload.Receiver,Upload.SucceededListener,Upload.FailedListener,Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Panel container;         // Root element for contained components.
	private	Panel root;         // Root element for contained components.
	private	File  file;         // File to write to.
	private  TextField expNameField;
	private TextArea descriptionField;
	private TextField speciesField, sampleTypeField, sampleProcessingField, instrumentTypeField, fragModeField,UploadedByNameField, emailField, publicationLinkField;
	private ExperimentHandler eh ;
	private Map<Integer,ExperimentBean> expList;
	private Upload upload;
	private Select select;
	
	
	private TabSheet mainTabs;
	private TabSheet subTabs;
	private  Panel expDetails;
	private ProgressIndicator pi = new ProgressIndicator();
	
	    
	    
	    
	private  Form newExpForm;
	private User user ;
	private Help help = new Help();
	private HorizontalLayout helpNote ;
	private Form removeExperimentLayout;
	private Form removeExperimentForm;
	private HorizontalLayout hslo;
	private Authenticator auth;

    
    
	public UploadUnit(String url,String dbName,String driver,String userName, String password, User user,TabSheet mainTabs,TabSheet subTabs) {
		this.user = user;
		this.mainTabs = mainTabs;
		this.subTabs = subTabs;
		eh = new ExperimentHandler( url,dbName,driver,userName, password);
		this.updateComponents(this.user);
		auth = new  Authenticator(url,dbName,driver,userName, password);
		pi.setValue(0f);
		pi.setCaption("Loading...");
		pi.setVisible(false);
		
	}
	// Callback method to begin receiving the upload.
	    public OutputStream receiveUpload(String filename, String MIMEType) 
	    {
	    	mainTabs.setReadOnly(true);
			subTabs.setReadOnly(true);
	    	FileOutputStream fos = null; // Output stream to write to
	        file = new File(filename);
	        
	        try {
	        	if(MIMEType.equalsIgnoreCase("text/plain") || MIMEType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	        	{
		            fos = new FileOutputStream(file);       
	        		
	        	}
	        	else
	        	{
	        		    		
	        	}
	            
	            
	            
	            
	           } catch (final java.io.FileNotFoundException e) {
	            // Error while opening the file. Not reported here.
	           // e.printStackTrace();
	            return null;
	        }catch (final Exception e) {
	            // Error while opening the file. Not reported here.
	           // e.printStackTrace();
	            return null;
	        }

	        return fos; // Return the output stream to write to
	    }
	    

	    // This is called if the upload is finished.
	    public void uploadSucceeded(Upload.SucceededEvent event) {
	        
	    	boolean validData = false;
            try 
            {
            	ExperimentBean newExp = null;
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
            			String expDescription = (String) descriptionField.getValue();
            			 
            			if((expName == null)||(expDescription == null)||(expSpecies == null)||(expSampleType == null)||(expSampleProcessing == null)||(expInstrumentType == null)||(expFragMode == null)||(expUploadedByName == null)||(expEmail == null)||expName.equals("")||expDescription.equals("")|| expSpecies.equals("")|| expSampleType.equals("")|| expSampleProcessing.equals("")|| expInstrumentType.equals("")|| expFragMode.equals("")|| expUploadedByName.equals("")|| expEmail.equals(""))
            			{	            	
            				//file didn't store in data base  		
            					 newExpForm.commit();            					
            				
            			}
            			else
            			{
            				boolean checkName = false;
            				
            				for(ExperimentBean exp:expList.values())
            				{
            					if(exp.getName().equalsIgnoreCase(expName)){
            						checkName  = true;
            						break;
            					}
            				}
            				if(checkName)
            				{
            					expNameField.setValue("This Name is Not  Available Please Choose Another Name ");
            					expNameField.commit();
            					newExpForm.focus();
            				}
            				else{
            				
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
			            		validData = true;
            				}
	            		
            			}
            	}
            	else//update old experiment
            	{
            		
            		String str = select.getValue().toString();
            		String[] strArr =str.split("\t");
            		int id = (Integer.valueOf(strArr[0]));
            		ExperimentBean exp = expList.get(id);
            		newExp = exp;            	 
            		validData =true;
            		
            	}
            	if(validData){
            	// send the file to the reader to extract the information
	            	boolean test = eh.handelExperimentFile(file,event.getMIMEType(),newExp);//Getting data from the uploaded file..here we assume that the experiment id is 1
	            	if(!test){
	            		getWindow().showNotification("Failed !  Please Check Your Uploaded File !");  //file didn't store in db  
	            	}else{		            	
		            	    // Display the uploaded file in the file panel.
		            	getWindow().showNotification("Successful !");
				        updateComponents(user);
	            	}
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
	    		root.addComponent(new Label("Sorry Only files with txt or xlsx type is allowed to upload, ...Upload failed."));
	    	getWindow().showNotification("Failed :-( !");
	   
	    }
	    @SuppressWarnings({ "deprecation", "serial" })
		private void updateComponents(User user)
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
	        expNameField.setWidth("350px");
	        expNameField.setMaxLength(70);
	        
	        speciesField = new TextField("Species:");
	        speciesField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        speciesField.setRequired(true);
	        speciesField.setRequiredError("EXPERIMENT SPECIES CAN NOT BE EMPTY!");	        
	        speciesField.setWidth("350px");
	        speciesField.setMaxLength(70);
	        
	        sampleTypeField = new TextField("Sample Type:");
	        sampleTypeField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        sampleTypeField.setRequired(true);
	        sampleTypeField.setRequiredError("EXPERIMENT SAMPLE TYPE CAN NOT BE EMPTY!");	        
	        sampleTypeField.setWidth("350px");
	        sampleTypeField.setMaxLength(70);
	        
	        sampleProcessingField = new TextField("Sample Processing:");
	        sampleProcessingField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        sampleProcessingField.setRequired(true);
	        sampleProcessingField.setRequiredError("EXPERIMENT SAMPLE PROCESSING CAN NOT BE EMPTY!");	        
	        sampleProcessingField.setWidth("350px");
	        sampleProcessingField.setMaxLength(70);
	        
	        
	        instrumentTypeField = new TextField("Instrument Type:");
	        instrumentTypeField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        instrumentTypeField.setRequired(true);
	        instrumentTypeField.setRequiredError("EXPERIMENT INSTURMENT TYPE CAN NOT BE EMPTY!");	        
	        instrumentTypeField.setWidth("350px");
	        instrumentTypeField.setMaxLength(70);
	        
	        fragModeField = new TextField("Frag Mode:");
	        fragModeField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        fragModeField.setRequired(true);
	        fragModeField.setRequiredError("EXPERIMENT FRAG MODE CAN NOT BE EMPTY!");	        
	        fragModeField.setWidth("350px");
	        fragModeField.setMaxLength(70);
	        
	        UploadedByNameField = new TextField("Uploaded By:");
	        UploadedByNameField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        UploadedByNameField.setRequired(true);
	        UploadedByNameField.setRequiredError("EXPERIMENT UPLOADED BY CAN NOT BE EMPTY!");	        
	        UploadedByNameField.setValue(user.getUsername());
	        UploadedByNameField.setEnabled(false);
	        UploadedByNameField.setWidth("350px");
	        UploadedByNameField.setMaxLength(70);
	        
	        emailField = new TextField("Email:");
	        emailField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        emailField.setRequired(true);	        
	        emailField.setValue(user.getEmail());
	        emailField.setEnabled(false);
	        emailField.setRequiredError("EXPERIMENT EMAIL CAN NOT BE EMPTY!");        
	        emailField.setWidth("350px");
	        emailField.setMaxLength(70);
	        
	        
	        descriptionField = new TextArea("Description:");
	        descriptionField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        descriptionField.setRequired(true);	  
	        descriptionField.setRequiredError("EXPERIMENT Description CAN NOT BE EMPTY!");        
	        descriptionField.setWidth("350px");
	        descriptionField.setMaxLength(950);
	        
	        publicationLinkField = new TextField("Publication Link:");
	        publicationLinkField.setStyle(Reindeer.TEXTFIELD_SMALL);
	        publicationLinkField.setWidth("350px");
	        publicationLinkField.setMaxLength(300);
	        
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
	    
	        
	        // Add form to layout
	        container.addComponent(newExpForm);
	        
	        
	        Panel p = new Panel();
	        Label l = new Label("<h4 style='color:blue'>Or Update Existing Experiments !</h4><h4 style='color:blue'>For New Experiment Please Leave Experiment ID Blank!</h4><h4 style='color:blue'><strong style='color:red'>* </strong> For New Experiment Please Remember to Upload Protein file first!</h4>");
            l.setContentMode(Label.CONTENT_XHTML);	        
	        p.addComponent(l);
	        container.addComponent(p);
	        
	        // Create the Form
	        Form existExpForm = new Form();
	        existExpForm.setCaption("Exist Experiments");	        
	        expList = eh.getExperiments(null);
	        List<String>strExpList = new ArrayList<String>();
	        for(ExperimentBean exp:expList.values())
	        {
	        	if(user.getEmail().equalsIgnoreCase("admin@csf.no")||exp.getEmail().equalsIgnoreCase(user.getEmail())){
		        	String str = exp.getExpId()+"	"+exp.getName()+"	( "+exp.getUploadedByName()+" )";
		        	strExpList.add(str);
	        	}
	        }
	        select = new Select("Experiment ID", strExpList);	
	        select.setImmediate(true);
	        select.addListener(new Property.ValueChangeListener(){

				public void valueChange(ValueChangeEvent event) {
					Object o = select.getValue();
					if(o != null){
						String str = select.getValue().toString();
	            		String[] strArr =str.split("\t");
	            		int id = (Integer.valueOf(strArr[0]));
	            		ExperimentBean expDet = expList.get(id);
	            		if(expDetails != null)
	            		{
	            			expDetails.removeAllComponents();
	            			
	            			
	            			
	            			if(expDet.getProteinsNumber() == 0){
	            				 Label l = new Label("<h4 style='color:red'>1) Protein File is Missing</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);	
	            				expDetails.addComponent(l);
	            			}
	            			else{
	            				Label l = new Label("<h4 style='color:blue'>1) Protein File is Uploaded</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
	            				expDetails.addComponent(l);
	            			}if(expDet.getFractionsNumber() == 0){
	            				Label l = new Label("<h4 style='color:red'>2) Fraction File is Missing</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
		            				expDetails.addComponent(l);
	            			}else{
	            				Label l = new Label("<h4 style='color:blue'>2) Fraction File Uploaded</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
		            				expDetails.addComponent(l);
	            			}if(expDet.getFractionRange() == 0){
	            				Label l = new Label("<h4 style='color:red'>3) Fraction Range File is Missing</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
		            				expDetails.addComponent(l);
	            			}else{
	            				Label l = new Label("<h4 style='color:blue'>3) Fraction Range File Uploaded</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
		            				expDetails.addComponent(l);
	            			}if(expDet.getPeptidesNumber() == 0){
	            				Label l = new Label("<h4 style='color:red'>4) Peptides File is Missing</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
		            				expDetails.addComponent(l);
	            			}else{
	            				Label l = new Label("<h4 style='color:blue'>4) Peptides File Uploaded</h4>");
	            		          l.setContentMode(Label.CONTENT_XHTML);
		            				expDetails.addComponent(l);
	            		
	            			}
	            		}
					}else{
	            			expDetails.removeAllComponents();
	            			  Label labelDetails = new Label("<h4 style='color:red;'>Please Select Experiment To Show the Details.</h4>");
	            		        labelDetails.setContentMode(Label.CONTENT_XHTML);
	            		        expDetails.addComponent(labelDetails);
	            		        
	            		}
            			
					
				}});
	        select.setWidth("60%");
            existExpForm.addField(Integer.valueOf(1),select); 
            
           
	        // Add form to layout
            VerticalLayout vlo = new VerticalLayout();   
            
            if(hslo != null)
            	vlo.removeComponent(hslo);
           hslo =  new  HorizontalLayout();
           hslo.setSizeFull();           
           hslo.addComponent(existExpForm);
            vlo.addComponent(hslo);
            if(removeExperimentLayout != null )
            	hslo.removeComponent(removeExperimentLayout);
            removeExperimentLayout = this.getRemoveForm(user.getEmail());
            hslo.addComponent(removeExperimentLayout);
            hslo.setComponentAlignment(removeExperimentForm, Alignment.MIDDLE_CENTER);
            vlo.addComponent(hslo);
	        container.addComponent(vlo);

	        // Create the Upload component. 
	      
	        upload =   new Upload(null, this);	        
	        upload.setStyleName("small");	        
	        upload.setVisible(true);
	        upload.setHeight("30px");
	        upload.setButtonCaption("ADD / EDIT EXPERIMENT !");
	        
	      
	        
	        
	        
	        
	        
	        
 //*****************************************************
	     upload.addListener(new Upload.StartedListener(){

			@SuppressWarnings("static-access")
			public void uploadStarted(StartedEvent event) {
				try {
					
					Thread.currentThread().sleep(1000);
					Thread t = new Thread(new Runnable() {
						
						public void run() {
							pi.setVisible(true);
							
						}
					});
					t.start();
					t.join();
				} catch (InterruptedException e) {
				}
				
				
				mainTabs.setReadOnly(true);
				subTabs.setReadOnly(true);
				
			}});
	       
	       
	        
	        
	       upload.addListener(new Upload.FinishedListener() {
	            public void uploadFinished(FinishedEvent event) {
	            	pi.setVisible(false);
	            	mainTabs.setReadOnly(false);
					subTabs.setReadOnly(false);
	                file = new File(event.getFilename());
	               
	            }
	        });
	        
	     
	        //***********************************
	        
	        upload.addListener((Upload.SucceededListener) this);
	        upload.addListener((Upload.FailedListener) this); 
	        if(helpNote != null)
	        	vlo.removeComponent(helpNote);
	        Label label = new Label("<h4 style='color:red;'>Please upload proteins file first</h4><h4 style='color:red;'>Please upload proteins files in (.txt) format.</h4><h4 style='color:red;'>Upload fraction range file after upload protein fraction file.</h4><h4 style='color:red;'>Upload fraction range file in (.xlsx) format.</h4>");
	        label.setContentMode(Label.CONTENT_XHTML);
	        helpNote = help.getHelpNote(label);
	        helpNote.setMargin(false,true,true,true);
	           
	        vlo.addComponent(upload);	 
	        vlo.addComponent(pi);
	        vlo.addComponent(helpNote);
	        vlo.setComponentAlignment(helpNote, Alignment.MIDDLE_RIGHT);
	        
	        expDetails = new Panel("Experiment Details");
	        Label labelDetails = new Label("<h4 style='color:red;'>Please Select Experiment To Show the Details.</h4>");
	        labelDetails.setContentMode(Label.CONTENT_XHTML);
	        expDetails.addComponent(labelDetails);
	        
	        vlo.addComponent(expDetails);
	        root.addComponent(vlo);
	        container.addComponent(root);
	    }
	    
		@SuppressWarnings("deprecation")
		private Form getRemoveForm(final String admin)
		{
			
				removeExperimentForm = new Form(); 
				removeExperimentForm.setCaption("Remove Existing Experiment");
				removeExperimentForm.setWidth("50%");
				expList = eh.getExperiments(null);
			    List<String>strExpList = new ArrayList<String>();
			    	  for(ExperimentBean exp:expList.values())
				        {
			    		 
			    		  if(user.getEmail().equalsIgnoreCase("admin@csf.no")||exp.getEmail().equalsIgnoreCase(user.getEmail())){
			   		       	String str = exp.getExpId()+"	"+exp.getName()+"	( "+exp.getUploadedByName()+" )";
				        	
				        	strExpList.add(str);
			    		  }
				        }
			    	
			    
			     final Select selectExp = new Select("Experiment ID", strExpList);	    
			    
			     selectExp.setWidth("100%");
			     
			     removeExperimentForm.addField(Integer.valueOf(1),selectExp);             	
			    Button removeExpButton = new Button("Remove Experiment");
			    removeExpButton.setStyle(Reindeer.BUTTON_SMALL);
			    removeExperimentForm.addField(Integer.valueOf(2), removeExpButton);
			    removeExpButton.addListener(new ClickListener() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						String str = selectExp.getValue().toString();
		        		String[] strArr =str.split("\t");
		        		Integer expId = (Integer.valueOf(strArr[0]));
						if(expId != null)
						{
							boolean test = auth.removeExp(expId);
							if(test)
							{
								updateComponents(user);
							}
							else
							{

								getWindow().showNotification("Failed to Remove The Experiment! ");
							}
						}
						else{
				        	
						}
						
					}
				} );
			  
			    return removeExperimentForm;
		}
	    
        
		 


}
