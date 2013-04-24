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

import com.compomics.util.Util;
import com.compomics.util.experiment.MsExperiment;
import com.compomics.util.experiment.ProteomicAnalysis;
import com.compomics.util.experiment.biology.Sample;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.IdentificationMethod;
import com.compomics.util.experiment.identification.SearchParameters;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.identifications.Ms2Identification;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.io.ExperimentIO;
import com.compomics.util.experiment.massspectrometry.SpectrumFactory;
import com.compomics.util.gui.SampleSelection;
import com.compomics.util.preferences.AnnotationPreferences;
import com.compomics.util.preferences.IdFilter;
import com.compomics.util.preferences.PTMScoringPreferences;
import com.compomics.util.preferences.ProcessingPreferences;
//import com.pepshak.myparameters.PeptideShakerSettings;

import eu.isas.peptideshaker.PeptideShaker;
import eu.isas.peptideshaker.myparameters.PSSettings;
import eu.isas.peptideshaker.myparameters.PeptideShakerSettings;
import eu.isas.peptideshaker.preferences.DisplayPreferences;
import eu.isas.peptideshaker.preferences.FilterPreferences;
import eu.isas.peptideshaker.preferences.ProjectDetails;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.utils.DisplayFeaturesGenerator;
import eu.isas.peptideshaker.utils.IdentificationFeaturesGenerator;
import eu.isas.peptideshaker.utils.Metrics;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class PCFileReader implements Serializable{
	 /**
     * The spectrum factory.
     */
    private SpectrumFactory spectrumFactory = SpectrumFactory.getInstance(100);
    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance(100000);
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DisplayPreferences displayPreferences = new DisplayPreferences();
	
	/**
     * The identification to display.
     */
    private Identification identification;
	
	/**
     * The compomics experiment.
     */
    private MsExperiment experiment = null;
    /**
     * The investigated sample.
     */
    
    private Sample sample;
	
    
    /**
     * The identification filter used for this project.
     */
    private IdFilter idFilter = new IdFilter();
    /**
     * The replicate number.
     */
    
    private int replicateNumber;
    
    
    private IdentificationFeaturesGenerator identificationFeaturesGenerator;
	
    /**
     * The parameters of the search.
     */
    private SearchParameters searchParameters = new SearchParameters();
    
    
    public  PCFileReader(File cpsFile,File fastaFile,File specFile,int z) {

		this.currentPSFile = cpsFile;
    	workOn(currentPSFile,fastaFile,specFile);
    	
    }
    private void workOn(File cpsFile,File fastaFile,File specFile)
    {
    	System.out.println("wegot the file");
    	try {
			readFiles(cpsFile,fastaFile,specFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    File currentPSFile;

	private AnnotationPreferences annotationPreferences;

	private SpectrumCountingPreferences spectrumCountingPreferences;

	private PTMScoringPreferences ptmScoringPreferences;

	private ProjectDetails projectDetails;

	private ProcessingPreferences processingPreferences;

	private Metrics metrics;
	private FilterPreferences filterPreferences;
           
    /**
     * The location of the folder used for serialization of matches.
     */
    public final static String SERIALIZATION_DIRECTORY = "resources/matches";
    /**
     * The name of the serialized experiment
     */
    public final static String experimentObjectName = "experiment";
        
    
    @SuppressWarnings({ "unused" })
	private void readFiles(File cpsFile, File fastaFile, File specFile) throws FileNotFoundException
    {
    	 File experimentFile = new File(SERIALIZATION_DIRECTORY, experimentObjectName);
         File matchFolder = new File(SERIALIZATION_DIRECTORY);
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
             long fileLength = currentPSFile.length();
        	
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

                     while ((count = tarInput.read(data, 0, BUFFER)) != -1) {
                         bos.write(data, 0, count);
                     }

                     bos.close();
                     fos.close();
                 }
                 
                 
                 tarInput.close();
                 
                 fi.close();
                 bis.close();
                 fi.close();
                 
                 
                 MsExperiment tempExperiment = ExperimentIO.loadExperiment(experimentFile);
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

                     experimentSettings = new PeptideShakerSettings(tempSettings.getSearchParameters(), tempSettings.getAnnotationPreferences(), tempSettings.getSpectrumCountingPreferences(), tempSettings.getProjectDetails(), tempSettings.getFilterPreferences(),
                             tempSettings.getDisplayPreferences(),  tempSettings.getMetrics(), tempProcessingPreferences, tempSettings.getIdentificationFeaturesCache(),
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
                 }
                 
                 ArrayList<Sample> samples = new ArrayList(tempExperiment.getSamples().values());

                 if (samples.size() == 1) {
                     tempSample = samples.get(0);
                 } else {
                     String[] sampleNames = new String[samples.size()];
                     for (int cpt = 0; cpt < sampleNames.length; cpt++) {
                         sampleNames[cpt] = samples.get(cpt).getReference();
                     }
             
                 }
                 ArrayList<Integer> replicates = new ArrayList<Integer>(tempExperiment.getAnalysisSet(tempSample).getReplicateNumberList());

                 int tempReplicate =0;

                if (replicates.size() == 1) {
                     tempReplicate = replicates.get(0);
                 } else {
	                     String[] replicateNames = new String[replicates.size()];
	                     for (int cpt = 0; cpt < replicateNames.length; cpt++) {
	                         replicateNames[cpt] = samples.get(cpt).getReference();
	                     }
                     }                
                 setProject(tempExperiment, tempSample, tempReplicate);
                 
                  identificationFeaturesGenerator = new IdentificationFeaturesGenerator(identification, searchParameters, idFilter, metrics, spectrumCountingPreferences);
                 if (experimentSettings.getIdentificationFeaturesCache() != null) {
                     identificationFeaturesGenerator.setIdentificationFeaturesCache(experimentSettings.getIdentificationFeaturesCache());
                 }
                 
                 
                 
                 try {
                    // File providedFastaLocation = experimentSettings.getSearchParameters().getFastaFile();
                	 
                	 String fileName = fastaFile.getName();
                     SequenceFactory.getInstance().loadFastaFile(fastaFile);
                    
                         if (fastaFile != null) {
                             searchParameters.setFastaFile(fastaFile);
                             try {
                                 SequenceFactory.getInstance().loadFastaFile(experimentSettings.getSearchParameters().getFastaFile());
                             } catch (Exception e2) {
                                 e2.printStackTrace();                                
                                 return;
                             }
                         } else {
                             return;
                         }
                     
                 } catch (Exception e) {
                   
                     if (fastaFile != null) {
                         searchParameters.setFastaFile(fastaFile);
                         try {
                             SequenceFactory.getInstance().loadFastaFile(experimentSettings.getSearchParameters().getFastaFile());
                         } catch (Exception e2) {
                            
                             return;
                         }
                     } else {
                        
                         return;
                     }
                 }
                 
                 
                 
                 projectDetails.addSpectrumFile(specFile);
                 if (identification.getSpectrumIdentificationMap() == null) {
                     // 0.18 version, needs update of the spectrum mapping
                     identification.updateSpectrumMapping();
                 }
                 ArrayList<String> oldKeys = new ArrayList<String>(metrics.getProteinKeys());
                 ArrayList<String> newKeys = new ArrayList<String>();
                 for (String proteinKey : oldKeys) {
                     newKeys.add(proteinKey.replaceAll(" ", ProteinMatch.PROTEIN_KEY_SPLITTER));
                 }
                 metrics.setProteinKeys(newKeys);
                 // save new project
                 identificationFeaturesGenerator.setProteinKeys(metrics.getProteinKeys());
                 try {
                     File mgfFile = specFile;
                     spectrumFactory.addSpectra(mgfFile);
                 } catch (Exception e) {
                     
                     return;
                 }

                 for(String pro:identification.getProteinMap().keySet()){
               	  ArrayList<String>prot = identification.getProteinMap().get(pro);
               		   
               	  try {
       				System.out.println(" "+identification.getProteinMatch(pro));
       			} catch (IllegalArgumentException e) {
       				// TODO Auto-generated catch block
       				e.printStackTrace();
       			} catch (ClassNotFoundException e) {
       				// TODO Auto-generated catch block
       				e.printStackTrace();
       			} catch (SQLException e) {
       				// TODO Auto-generated catch block
       				e.printStackTrace();
       			} catch (IOException e) {
       				// TODO Auto-generated catch block
       				e.printStackTrace();
       			}
                  }
               
                 

			}
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        public void setProject(MsExperiment experiment, Sample sample, int replicateNumber) {
            this.experiment = experiment;
            this.sample = sample;
            this.replicateNumber = replicateNumber;
            ProteomicAnalysis proteomicAnalysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
           identification = proteomicAnalysis.getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        
      
        
        }
        
        public void setMetrics(Metrics metrics) {
            this.metrics = metrics;
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
         * Updates the search parameters.
         *
         * @param searchParameters the new search parameters
         */
        public void setSearchParameters(SearchParameters searchParameters) {
            this.searchParameters = searchParameters;
            PeptideShaker.loadModifications(searchParameters);
        }
        
        
        public void setProjectDetails(ProjectDetails projectDetails) {
            this.projectDetails = projectDetails;
        }

        /*
        public void importFiles(IdFilter idFilter, ArrayList<File> idFiles, ArrayList<File> spectrumFiles,
                SearchParameters searchParameters, AnnotationPreferences annotationPreferences,
                ProcessingPreferences processingPreferences,  boolean backgroundThread) {

            ProteomicAnalysis analysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
            analysis.addIdentificationResults(IdentificationMethod.MS2_IDENTIFICATION, new Ms2Identification(getIdentificationReference()));
            Identification identification = analysis.getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
            identification.setIsDB(true);

        //    FileImporter fileImporter = new FileImporter(analysis, idFilter, null);

           // fileImporter.importFiles(idFiles, spectrumFiles, searchParameters, annotationPreferences, processingPreferences, ptmScoringPreferences, spectrumCountingPreferences, projectDetails, backgroundThread);
        }
        */
        public String getIdentificationReference() {
            return Identification.getDefaultReference(experiment.getReference(), sample.getReference(), replicateNumber);
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
         * Sets the initial processing preferences.
         *
         * @param processingPreferences the initial processing preferences
         */
        public void setProcessingPreferences(ProcessingPreferences processingPreferences) {
            this.processingPreferences = processingPreferences;
        }
        
        public Identification getIdentification()
        {
        	return this.identification;
        }

        public IdentificationFeaturesGenerator getIdentificationFeaturesGenerator() {
            return identificationFeaturesGenerator;
        }
        public DisplayPreferences getDisplayPreferences() {
            return displayPreferences;
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
         * Sets the display preferences to use.
         *
         * @param displayPreferences the display preferences to use
         */
        public void setDisplayPreferences(DisplayPreferences displayPreferences) {
            this.displayPreferences = displayPreferences;
        }
        
        /**
         * Returns the display features generator.
         *
         * @return the display features generator
         */


}
