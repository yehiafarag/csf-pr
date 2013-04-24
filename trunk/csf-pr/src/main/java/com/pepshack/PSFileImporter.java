/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack;

/**
 *
 * @author Yehia Mokhtar
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import com.compomics.util.db.ObjectsCache;
import com.compomics.util.experiment.MsExperiment;
import com.compomics.util.experiment.biology.Sample;
import com.compomics.util.experiment.biology.EnzymeFactory;
import com.compomics.util.experiment.biology.IonFactory;
import com.compomics.util.experiment.biology.NeutralLoss;
import com.compomics.util.experiment.biology.PTMFactory;

import com.compomics.util.experiment.identification.IdentificationMethod;
import com.compomics.util.experiment.identification.SearchParameters;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.io.ExperimentIO;
import com.compomics.util.experiment.massspectrometry.SpectrumFactory;
import com.compomics.util.gui.SampleSelection;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.preferences.AnnotationPreferences;
import com.compomics.util.preferences.IdFilter;
import com.compomics.util.preferences.PTMScoringPreferences;
import com.compomics.util.preferences.ProcessingPreferences;
import com.compomics.util.Util;

import eu.isas.peptideshaker.PeptideShaker;
import eu.isas.peptideshaker.myparameters.PSSettings;
import eu.isas.peptideshaker.myparameters.PeptideShakerSettings;
import eu.isas.peptideshaker.preferences.DisplayPreferences;
import eu.isas.peptideshaker.preferences.FilterPreferences;
import eu.isas.peptideshaker.preferences.ProjectDetails;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences.SpectralCountingMethod;
import eu.isas.peptideshaker.utils.IdentificationFeaturesGenerator;
import eu.isas.peptideshaker.utils.Metrics;

public class PSFileImporter {
	
	 private File currentPSFile;
	 private EnzymeFactory enzymeFactory = EnzymeFactory.getInstance();
	 private String resource;
	 
	 /**
	     * The project details.
	     */
	    private ProjectDetails projectDetails = null;
	    
	    /**
	     * The spectrum factory.
	     */
	    private SpectrumFactory spectrumFactory = SpectrumFactory.getInstance(100);
	    
	  /**
	     * The identification to display.
	     */
	    private com.compomics.util.experiment.identification.Identification identification;
	   
	    
	   /**
	     * The compomics PTM factory.
	     */
	    private PTMFactory ptmFactory = PTMFactory.getInstance();
	 
	    /**
	     * The parameters of the search.
	     */
	    private SearchParameters searchParameters = new SearchParameters();
	    /**
	     * The initial processing preferences
	     */
	    private ProcessingPreferences processingPreferences = new ProcessingPreferences();
	
	    /**
	     * The annotation preferences.
	     */
	    private AnnotationPreferences annotationPreferences = new AnnotationPreferences();
	    /**
	     * The spectrum counting preferences.
	     */
	    private SpectrumCountingPreferences spectrumCountingPreferences = new SpectrumCountingPreferences();
	    
	    /**
	     * The PTM scoring preferences
	     */
	    private PTMScoringPreferences ptmScoringPreferences = new PTMScoringPreferences();
	   
	    /**
	     * The identification filter used for this project.
	     */
	    private IdFilter idFilter = new IdFilter();
	    
	    /**
	     * Metrics picked-up while loading the files.
	     */
	    private Metrics metrics;
	    
	    /**
	     * The display preferences.
	     */
	    private DisplayPreferences displayPreferences = new DisplayPreferences();
	    /**
	     * The filter preferences.
	     */
	    private FilterPreferences filterPreferences = new FilterPreferences();
	    
	    /**
	     * The compomics experiment.
	     */
	    private MsExperiment experiment = null;
	    /**
	     * The investigated sample.
	     */
	    private Sample sample;
	    /**
	     * The replicate number.
	     */
	    private int replicateNumber;
	    
	    /**
	     * The class used to provide sexy features out of the identification.
	     */
	    private IdentificationFeaturesGenerator identificationFeaturesGenerator;
	    
	    /**
	     * The last folder opened by the user. Defaults to user.home.
	     */
	    private String lastSelectedFolder = "user.home";
	    
	    /**
	     * The object cache used for the identification database.
	     */
	    private ObjectsCache objectsCache;
		private com.compomics.util.experiment.ProteomicAnalysis proteomicAnalysis;
                
                
                
	
	 public void importPeptideShakerFile(File aPsFile,String resource) {

		 this.resource = resource;
	     this.currentPSFile = aPsFile;
	       // exceptionHandler = new ExceptionHandler(this);

	       ProgressDialogX progressDialog = new ProgressDialogX(false);
	        
	        try {
	                    // reset enzymes, ptms and preferences
	                    loadEnzymes();
	                    resetPtmFactory();	                    
	                   setDefaultPreferences();               

	                    // close any open connection to an identification database
	                    if (identification != null) {
	                        identification.close();
	                    }  
	                    File experimentFile = new File(PeptideShaker.SERIALIZATION_DIRECTORY, PeptideShaker.experimentObjectName);
	                    File matchFolder = new File(PeptideShaker.SERIALIZATION_DIRECTORY);             
	                    
	                 
	                    // empty the existing files in the matches folder
	                    if (matchFolder.exists()) {
	                        for (File file : matchFolder.listFiles()) {
	                            if (file.isDirectory()) {
	                                boolean deleted = Util.deleteDir(file);

	                                if (!deleted) {
	                                    System.out.println("Failed to delete folder: " + file.getPath());
	                                }
	                            } else {
	                                boolean deleted = file.delete();

	                                if (!deleted) {
	                                    System.out.println("Failed to delete file: " + file.getPath());
	                                }
	                            }
	                        }
	                    }  

	                    final int BUFFER = 2048;
	                    byte data[] = new byte[BUFFER];
	                    FileInputStream fi = new FileInputStream(currentPSFile);
	                    BufferedInputStream bis = new BufferedInputStream(fi, BUFFER);

	                    try {
	                        ArchiveInputStream tarInput = new ArchiveStreamFactory().createArchiveInputStream(bis);
  	                        ArchiveEntry archiveEntry;

	                        while ((archiveEntry = tarInput.getNextEntry()) != null) {
	                            File destinationFile = new File(archiveEntry.getName());
	                            File destinationFolder = destinationFile.getParentFile();
	                            boolean destFolderExists = true;

	                            if (!destinationFolder.exists()) {
	                                destFolderExists = destinationFolder.mkdirs();
	                            }

	                            if (destFolderExists) {
	                                FileOutputStream fos = new FileOutputStream(destinationFile);
	                                BufferedOutputStream bos = new BufferedOutputStream(fos);
	                                int count;

	                                while ((count = tarInput.read(data, 0, BUFFER)) != -1 && !progressDialog.isRunCanceled()) {
	                                    bos.write(data, 0, count);
	                                }

	                                bos.close();
	                                fos.close();
	                                
	                              //  int progress = (int) (100 * tarInput.getBytesRead() / fileLength);
	                               // progressDialog.setValue(progress);
	                            } else {
	                                System.out.println("Folder does not exist: \'" + destinationFolder.getAbsolutePath() + "\'. User preferences not saved.");
	                            }

	                            
	                        }

	                        tarInput.close();
	                    } catch (ArchiveException e) {
	                        //Most likely an old project
	                        experimentFile = currentPSFile;
	                        e.printStackTrace();
	                    }

	                    fi.close();
	                    bis.close();
	                    fi.close();
	                    
	                    com.compomics.util.experiment.MsExperiment tempExperiment = ExperimentIO.loadExperiment(experimentFile);
	                     Sample tempSample = null;
	                    PeptideShakerSettings experimentSettings = new PeptideShakerSettings();
	                    

	                     


	                    if (tempExperiment.getUrParam(experimentSettings) instanceof PSSettings) {

	                        // convert old settings files using utilities version 3.10.68 or older

	                        // convert the old ProcessingPreferences object
	                        PSSettings tempSettings = (PSSettings) tempExperiment.getUrParam(experimentSettings);
	                        ProcessingPreferences tempProcessingPreferences = new ProcessingPreferences();
	                        tempProcessingPreferences.setProteinFDR(tempSettings.getProcessingPreferences().getProteinFDR());
	                        tempProcessingPreferences.setPeptideFDR(tempSettings.getProcessingPreferences().getPeptideFDR());
	                        tempProcessingPreferences.setPsmFDR(tempSettings.getProcessingPreferences().getPsmFDR());

	                        // convert the old PTMScoringPreferences object
	                        PTMScoringPreferences tempPTMScoringPreferences = new PTMScoringPreferences();
	                        tempPTMScoringPreferences.setaScoreCalculation(tempSettings.getPTMScoringPreferences().aScoreCalculation());
	                        tempPTMScoringPreferences.setaScoreNeutralLosses(tempSettings.getPTMScoringPreferences().isaScoreNeutralLosses());
	                        tempPTMScoringPreferences.setFlrThreshold(tempSettings.getPTMScoringPreferences().getFlrThreshold());

	                        experimentSettings = new PeptideShakerSettings(tempSettings.getSearchParameters(), tempSettings.getAnnotationPreferences(),
	                                tempSettings.getSpectrumCountingPreferences(), tempSettings.getProjectDetails(), tempSettings.getFilterPreferences(),
	                                tempSettings.getDisplayPreferences(),
	                                tempSettings.getMetrics(), tempProcessingPreferences, tempSettings.getIdentificationFeaturesCache(),
	                                tempPTMScoringPreferences, new IdFilter());

	                    } else {
	                        experimentSettings = (PeptideShakerSettings) tempExperiment.getUrParam(experimentSettings);
	                    } 
	                    
	                    
	                    
	                   
	                    idFilter = experimentSettings.getIdFilter();
	                    setAnnotationPreferences(experimentSettings.getAnnotationPreferences());
	                    setSpectrumCountingPreferences(experimentSettings.getSpectrumCountingPreferences());
	                    setPtmScoringPreferences(experimentSettings.getPTMScoringPreferences());
	                    setProjectDetails(experimentSettings.getProjectDetails());
	                    setSearchParameters(experimentSettings.getSearchParameters());
	                    setProcessingPreferences(experimentSettings.getProcessingPreferences());
	                    setMetrics(experimentSettings.getMetrics());
	                    setDisplayPreferences(experimentSettings.getDisplayPreferences());

	                    if (experimentSettings.getFilterPreferences() != null) {
	                        setFilterPreferences(experimentSettings.getFilterPreferences());
	                    } else {
	                        setFilterPreferences(new FilterPreferences());
	                    }
	                    if (experimentSettings.getDisplayPreferences() != null) {
	                        setDisplayPreferences(experimentSettings.getDisplayPreferences());
	                        displayPreferences.compatibilityCheck(searchParameters.getModificationProfile());
	                    } else {
	                        setDisplayPreferences(new DisplayPreferences());
	                        displayPreferences.setDefaultSelection(searchParameters.getModificationProfile());
	                    }
	                    ArrayList<Sample> samples = new ArrayList(tempExperiment.getSamples().values());
	                    
	                    if (samples.size() == 1) {
	                        tempSample = samples.get(0);
	                    } else {
	                    	    tempSample = samples.get(0);
		                        String[] sampleNames = new String[samples.size()];
		                        for (int cpt = 0; cpt < sampleNames.length; cpt++) {
		                            sampleNames[cpt] = samples.get(cpt).getReference();
		                            System.out.println(sampleNames[cpt]);
		                        }
	                       /* SampleSelection sampleSelection = new SampleSelection(null, true, sampleNames, "sample");
	                        sampleSelection.setVisible(false);
	                        String choice = sampleSelection.getChoice();
	                        for (Sample sampleTemp : samples) {
	                            if (sampleTemp.getReference().equals(choice)) {
	                                tempSample = sampleTemp;
	                                break;
	                            }
	                        }*/
	                    }


	                   

	                    ArrayList<Integer> replicates = new ArrayList(tempExperiment.getAnalysisSet(tempSample).getReplicateNumberList());

	                    System.out.println(replicates);
	                    int tempReplicate;

	                    if (replicates.size() == 1) {
	                        tempReplicate = replicates.get(0);
	                    } else {
	                        String[] replicateNames = new String[replicates.size()];
	                        for (int cpt = 0; cpt < replicateNames.length; cpt++) {
	                            replicateNames[cpt] = samples.get(cpt).getReference();
	                        }
	                        SampleSelection sampleSelection = new SampleSelection(null, true, replicateNames, "replicate");
	                        sampleSelection.setVisible(false);
	                        Integer choice = new Integer(sampleSelection.getChoice());
	                        tempReplicate = 0;
	                    }                     
	                  
	                    setProject(tempExperiment, tempSample, tempReplicate,tempExperiment);

	                    

	                    identificationFeaturesGenerator = new IdentificationFeaturesGenerator(identification, searchParameters, idFilter, metrics, spectrumCountingPreferences);
	                    if (experimentSettings.getIdentificationFeaturesCache() != null) {
	                        identificationFeaturesGenerator.setIdentificationFeaturesCache(experimentSettings.getIdentificationFeaturesCache());
	                    }

	                    progressDialog.setTitle("Loading FASTA File. Please Wait...");

	                    try {
	                        File providedFastaLocation = experimentSettings.getSearchParameters().getFastaFile();
	                        String fileName = providedFastaLocation.getName();
	                        File projectFolder = currentPSFile.getParentFile();
	                        File dataFolder = new File(projectFolder, "data");

	                        // try to locate the FASTA file
	                        if (providedFastaLocation.exists()) {
	                            SequenceFactory.getInstance().loadFastaFile(providedFastaLocation);
	                        } else if (new File(projectFolder, fileName).exists()) {
	                            SequenceFactory.getInstance().loadFastaFile(new File(projectFolder, fileName));
	                            experimentSettings.getSearchParameters().setFastaFile(new File(projectFolder, fileName));
	                        } else if (new File(dataFolder, fileName).exists()) {
	                            SequenceFactory.getInstance().loadFastaFile(new File(dataFolder, fileName));
	                            experimentSettings.getSearchParameters().setFastaFile(new File(dataFolder, fileName));
	                        } else {
	                           //return error

	                           System.out.println("fastafile is missing");
	                        }
	                    } catch (Exception e) {
	                       e.printStackTrace();
	                       System.out.println("fastafile is missing");
	                    }
  
	                    
	                
	                   
	                   for (String spectrumFileName : identification.getSpectrumFiles()) {

	                        try {
	                            File providedSpectrumLocation = projectDetails.getSpectrumFile(spectrumFileName);
	                            // try to locate the spectrum file
	                            if (providedSpectrumLocation == null || !providedSpectrumLocation.exists()) {
	                                File projectFolder = currentPSFile.getParentFile();
	                                File fileInProjectFolder = new File(projectFolder, spectrumFileName);
	                                File dataFolder = new File(projectFolder, "data");
	                                File fileInDataFolder = new File(dataFolder, spectrumFileName);
	                                File fileInLastSelectedFolder = new File(getLastSelectedFolder(), spectrumFileName);
	                                if (fileInProjectFolder.exists()) {
	                                    projectDetails.addSpectrumFile(fileInProjectFolder);
	                                } else if (fileInDataFolder.exists()) {
	                                    projectDetails.addSpectrumFile(fileInDataFolder);
	                                } else if (fileInLastSelectedFolder.exists()) {
	                                    projectDetails.addSpectrumFile(fileInLastSelectedFolder);
	                                } else {

	                                	System.out.println("error no file");
	                                }
	                            }
	                        } catch (Exception e) {	                           
	                            e.printStackTrace();
	                            System.out.println("error no file");
	                            return;
	                        }
	                    }    
	                   
	                   
                   

	                    objectsCache = new ObjectsCache();
	                    objectsCache.setAutomatedMemoryManagement(true);

	                   
	                    if (identification.isDB()) {
	                        try {
	                            String dbFolder = new File(resource, PeptideShaker.SERIALIZATION_DIRECTORY).getAbsolutePath();
	                            identification.establishConnection(dbFolder, false, objectsCache);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    } else {   
	                    	annotationPreferences.useAutomaticAnnotation(true);
	                    }
	                  
	                    

	                    
	                   
	                    int cpt = 1, nfiles = identification.getSpectrumFiles().size();
	                    for (String fileName : identification.getSpectrumFiles()) {

	                        try {
	                            File mgfFile = projectDetails.getSpectrumFile(fileName);
	                            spectrumFactory.addSpectra(mgfFile, progressDialog);
	                        } catch (Exception e) {
	                           
	                            e.printStackTrace();
	                            return;
	                        }
	                    }
	                    
	                    

	                   

	                    if (identification.getSpectrumIdentificationMap() == null) {
	                        // 0.18 version, needs update of the spectrum mapping
	                        identification.updateSpectrumMapping();
	                    }
			  }catch(Exception exp){exp.printStackTrace();}
			          
	    }

	   
	
	 	/**
	     * Loads the enzymes from the enzyme file into the enzyme factory.
	     */
	    private void loadEnzymes() {
	        try {
	           
				enzymeFactory.importEnzymes(new File(resource, PeptideShaker.ENZYME_FILE));
	        } catch (Exception e) {
	            System.out.println( "Not able to load the enzyme file."+ "Wrong enzyme file.");
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Loads the modifications from the modification file.
	     */
	    public void resetPtmFactory() {

	        // reset ptm factory
	        ptmFactory.reloadFactory();
	        ptmFactory = PTMFactory.getInstance();

	        try {
	            ptmFactory.importModifications(new File(resource, PeptideShaker.MODIFICATIONS_FILE), false);
	        } catch (Exception e) {
	            e.printStackTrace();
	            }

	        try {
	            ptmFactory.importModifications(new File(resource, PeptideShaker.USER_MODIFICATIONS_FILE), true);
	        } catch (Exception e) {
	            e.printStackTrace();
	           
	        }
	    }
	    
	    
	    /**
	     * Set the default preferences.
	     */
	    private void setDefaultPreferences() {
	        searchParameters = new SearchParameters();
	        annotationPreferences.setAnnotationLevel(0.75);
	        annotationPreferences.useAutomaticAnnotation(true);
	        spectrumCountingPreferences.setSelectedMethod(SpectralCountingMethod.NSAF);
	        spectrumCountingPreferences.setValidatedHits(true);
	        IonFactory.getInstance().addDefaultNeutralLoss(NeutralLoss.NH3);
	        IonFactory.getInstance().addDefaultNeutralLoss(NeutralLoss.H2O);
	        processingPreferences = new ProcessingPreferences();
	        ptmScoringPreferences = new PTMScoringPreferences();
	    }
 

	    /**
	     * Updates the new annotation preferences.
	     *
	     * @param annotationPreferences the new annotation preferences
	     */
	    public void setAnnotationPreferences(AnnotationPreferences annotationPreferences) {
	        this.annotationPreferences = annotationPreferences;
	    }
	    
	    /**
	     * Sets new spectrum counting preferences.
	     *
	     * @param spectrumCountingPreferences new spectrum counting preferences
	     */
	    public void setSpectrumCountingPreferences(SpectrumCountingPreferences spectrumCountingPreferences) {
	        this.spectrumCountingPreferences = spectrumCountingPreferences;
	    }
	    
	    /**
	     * Sets the PTM scoring preferences
	     *
	     * @param ptmScoringPreferences the PTM scoring preferences
	     */
	    public void setPtmScoringPreferences(PTMScoringPreferences ptmScoringPreferences) {
	        this.ptmScoringPreferences = ptmScoringPreferences;
	    }
	    
	    /**
	     * Sets the project details.
	     *
	     * @param projectDetails the project details
	     */
	    public void setProjectDetails(ProjectDetails projectDetails) {
	        this.projectDetails = projectDetails;
	    }
	    
	    /**
	     * Updates the search parameters.
	     *
	     * @param searchParameters the new search parameters
	     */
	    public void setSearchParameters(SearchParameters searchParameters) {
	        this.searchParameters = searchParameters;
	        PeptideShaker.loadModifications(searchParameters);
	    }
	    /**
	     * Sets the initial processing preferences.
	     *
	     * @param processingPreferences the initial processing preferences
	     */
	    public void setProcessingPreferences(ProcessingPreferences processingPreferences) {
	        this.processingPreferences = processingPreferences;
	    }
	    /**
	     * Sets the metrics saved while loading the files.
	     *
	     * @param metrics the metrics saved while loading the files
	     */
	    public void setMetrics(Metrics metrics) {
	        this.metrics = metrics;
	    }
	    /**
	     * Sets the display preferences to use.
	     *
	     * @param displayPreferences the display preferences to use
	     */
	    public void setDisplayPreferences(DisplayPreferences displayPreferences) {
	        this.displayPreferences = displayPreferences;
	    }
	    
	    /**
	     * Sets the gui filter preferences to use. .\
	     *
	     * @param filterPreferences the gui filter preferences to use
	     */
	    public void setFilterPreferences(FilterPreferences filterPreferences) {
	        this.filterPreferences = filterPreferences;
	    }
	    
	    /**
	     * This method sets the information of the project when opened.
	     *
	     * @param experiment the experiment conducted
	     * @param sample The sample analyzed
	     * @param replicateNumber The replicate number
	     * @param tempExperiment1 
	     */
	    public void setProject(MsExperiment experiment, Sample sample, int replicateNumber, com.compomics.util.experiment.MsExperiment tempExperiment1) {
	        this.experiment = experiment;
	        this.sample = sample;
	        this.replicateNumber = replicateNumber;
	        proteomicAnalysis = tempExperiment1.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
	        identification =  proteomicAnalysis.getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
	   
	       
	    
	    }
	    
	    
	    /**
	     * Returns the last selected folder.
	     *
	     * @return the last selected folder
	     */
	    public String getLastSelectedFolder() {
	        return lastSelectedFolder;
	    }

	    /**
	     * Set the last selected folder.
	     *
	     * @param lastSelectedFolder the folder to set
	     */
	    public void setLastSelectedFolder(String lastSelectedFolder) {
	        this.lastSelectedFolder = lastSelectedFolder;
	    }
	    /**
	     * Returns the identification displayed.
	     *
	     * @return the identification displayed
	     */
	    public com.compomics.util.experiment.identification.Identification getIdentification() {
	        return identification;
	    }
	    /**
	     * Returns the identification features generator.
	     *
	     * @return the identification features generator
	     */
	    public IdentificationFeaturesGenerator getIdentificationFeaturesGenerator() {
	        return identificationFeaturesGenerator;
                
	    }

/**
     * Returns the search parameters.
     *
     * @return the search parameters
     */
    public SearchParameters getSearchParameters() {
        return searchParameters;
    }
     /**
     * Return the display preferences to use.
     *
     * @return the display preferences to use
     */
    public DisplayPreferences getDisplayPreferences() {
        return displayPreferences;
    }

  
   
		

}
