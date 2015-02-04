/*
 */
package com.pepshaker;

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

import com.compomics.util.preferences.GenePreferences;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import com.compomics.util.experiment.annotation.gene.GeneFactory;

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
import com.compomics.util.experiment.ProteomicAnalysis;
import com.compomics.util.experiment.annotation.go.GOFactory;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.SpectrumAnnotator;
import com.compomics.util.experiment.identification.matches.IonMatch;
import com.compomics.util.experiment.massspectrometry.MSnSpectrum;
import com.compomics.util.experiment.massspectrometry.Spectrum;

import eu.isas.peptideshaker.PeptideShaker;
import eu.isas.peptideshaker.gui.tabpanels.PtmPanel;
import eu.isas.peptideshaker.myparameters.PSSettings;
import eu.isas.peptideshaker.myparameters.PeptideShakerSettings;
import eu.isas.peptideshaker.preferences.DisplayPreferences;
import eu.isas.peptideshaker.preferences.FilterPreferences;
import eu.isas.peptideshaker.preferences.ProjectDetails;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences.SpectralCountingMethod;
import eu.isas.peptideshaker.utils.IdentificationFeaturesGenerator;
import eu.isas.peptideshaker.utils.Metrics;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

public class PSFileImporter {

    private File currentPSFile;
    private EnzymeFactory enzymeFactory = EnzymeFactory.getInstance();
    private String resource;
    /**
     * The spectrum annotator.
     */
    private SpectrumAnnotator spectrumAnnotator = new SpectrumAnnotator();
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
    private Identification identification;
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
     * The actually identified modifications.
     */
    /**
     * Returns the modifications found in this project.
     *
     * @return the modifications found in this project
     */
    public ArrayList<String> getFoundModifications() {
        if (identifiedModifications == null) {
            identifiedModifications = new ArrayList<String>();
            for (String peptideKey : identification.getPeptideIdentification()) {

                boolean modified = false;

                for (String modificationName : Peptide.getModificationFamily(peptideKey)) {
                    if (!identifiedModifications.contains(modificationName)) {
                        identifiedModifications.add(modificationName);
                        modified = true;
                    }
                }
                if (!modified && !identifiedModifications.contains(PtmPanel.NO_MODIFICATION)) {
                    identifiedModifications.add(PtmPanel.NO_MODIFICATION);
                }
            }
        }
        return identifiedModifications;
    }
    private ArrayList<String> identifiedModifications = null;
    /**
     * Metrics picked-up while loading the files.
     */
    private Metrics metrics;
    /**
     * The display preferences.
     */
    private DisplayPreferences displayPreferences = new DisplayPreferences();
    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance(30000);
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
    private ProgressDialogX progressDialog;
    private javax.swing.JProgressBar jprog;

    public PSFileImporter(javax.swing.JProgressBar jprog) {
        this.progressDialog = new ProgressDialogX(false);
        this.jprog = jprog;
    }

    public void importPeptideShakerFile(File aPsFile, final String resource) {

        this.resource = resource;
        this.currentPSFile = aPsFile;
        loadGeneMapping();
        loadEnzymes();
        resetPtmFactory();
        //setDefaultPreferences(); // @TODO: i tried re-adding this but then we get a null pointer, but the two below have to be added or the default neutral losses won't appear
        IonFactory.getInstance().addDefaultNeutralLoss(NeutralLoss.H2O);
        IonFactory.getInstance().addDefaultNeutralLoss(NeutralLoss.NH3);


        // exceptionHandler = new ExceptionHandler(this);

        new Thread("ProgressThread") {
            @Override
            public void run() {
                jprog.setValue(10);

            }
        }.start();

        Thread importThread = new Thread("ImportThread") {
            @Override
            public void run() {
                try {
                    // reset enzymes, ptms and preferences
                    loadEnzymes();
                    resetPtmFactory();
                    setDefaultPreferences();
                    try {
                        // close any open connection to an identification database
                        if (identification != null) {
                            identification.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

//	                                int progress = (int) (100 * tarInput.getBytesRead() / fileLength);
//	                               progressDialog.setValue(progress);
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
                        //missing gene refrences
                        GenePreferences genePreferences = getGenePreferences();
                        //     String selectedSpecies = genePreferences.getCurrentSpecies();
                        //    String speciesDatabase = genePreferences.getEnsemblDatabaseName(selectedSpecies);

                        experimentSettings = new PeptideShakerSettings(tempSettings.getSearchParameters(), tempSettings.getAnnotationPreferences(),
                                tempSettings.getSpectrumCountingPreferences(), tempSettings.getProjectDetails(), tempSettings.getFilterPreferences(),
                                tempSettings.getDisplayPreferences(),
                                tempSettings.getMetrics(), tempProcessingPreferences, tempSettings.getIdentificationFeaturesCache(),
                                tempPTMScoringPreferences, genePreferences, new IdFilter());
                        ;
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
                        SampleSelection sampleSelection = new SampleSelection(null, true, sampleNames, "sample");
                        sampleSelection.setVisible(false);
                        String choice = sampleSelection.getChoice();
                        for (Sample sampleTemp : samples) {
                            if (sampleTemp.getReference().equals(choice)) {
                                tempSample = sampleTemp;
                                break;
                            }
                        }
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

                    setProject(tempExperiment, tempSample, tempReplicate);



                    identificationFeaturesGenerator = new IdentificationFeaturesGenerator(identification, searchParameters, idFilter, metrics, spectrumCountingPreferences);
                    if (experimentSettings.getIdentificationFeaturesCache() != null) {
                        identificationFeaturesGenerator.setIdentificationFeaturesCache(experimentSettings.getIdentificationFeaturesCache());
                    }

//	                    mainProgressDialog.setTitle("Loading FASTA File. Please Wait...");

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


//	                 mainProgressDialog.setTitle("Locating Spectrum Files. Please Wait...");

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
                            clearData(true);
                            //clearPreferences();
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
                        updateAnnotationPreferencesFromSearchSettings();
                        annotationPreferences.useAutomaticAnnotation(true);
                    }

                    int cpt = 1, nfiles = identification.getSpectrumFiles().size();
                    for (String fileName : identification.getSpectrumFiles()) {

//                             mainProgressDialog.setTitle("Importing Spectrum Files. Please Wait...");
                        try {
                            File mgfFile = projectDetails.getSpectrumFile(fileName);
                            spectrumFactory.addSpectra(mgfFile, progressDialog);
                        } catch (Exception e) {
                            clearData(true);
                            e.printStackTrace();
                            return;
                        }
                    }
                    boolean compatibilityIssue = getSearchParameters().getIonSearched1() == null
                            || getSearchParameters().getIonSearched2() == null;

                    if (compatibilityIssue) {
                        JOptionPane.showMessageDialog(null,
                                "The annotation preferences for this project may have changed.\n\n"
                                + "Note that PeptideShaker has substancially improved, we strongly\n"
                                + "recommend reprocessing your identification files.",
                                "Annotation Preferences",
                                JOptionPane.INFORMATION_MESSAGE);
                        updateAnnotationPreferencesFromSearchSettings();
                    }


                    if (identification.getSpectrumIdentificationMap() == null) {
                        // 0.18 version, needs update of the spectrum mapping
                        identification.updateSpectrumMapping();
                    }
                } catch (OutOfMemoryError error) {
                    System.out.println("Ran out of memory! (runtime.maxMemory(): " + Runtime.getRuntime().maxMemory() + ")");
                    Runtime.getRuntime().gc();
                    JOptionPane.showMessageDialog(null,
                            "The task used up all the available memory and had to be stopped.\n"
                            + "Memory boundaries are set in ../resources/conf/JavaOptions.txt.",
                            "Out Of Memory Error",
                            JOptionPane.ERROR_MESSAGE);
                    error.printStackTrace();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        };
        importThread.setPriority(Thread.MAX_PRIORITY);
//        importThread.start();
//        while (importThread.isAlive()) {
//            try {
//                Thread.currentThread().sleep(100);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(importThread);
        es.shutdown();
        try {
            boolean finshed = es.awaitTermination(1, TimeUnit.DAYS);
            System.gc();
            return;
            
//        t.start();
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
            //Logger.getLogger(UpdatedOutputGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.gc();
//        return;


    }

    /**
     * Loads the enzymes from the enzyme file into the enzyme factory.
     */
    private void loadEnzymes() {
        try {
            enzymeFactory.importEnzymes(new File(resource, PeptideShaker.ENZYME_FILE));
        } catch (Exception e) {
            System.out.println("Not able to load the enzyme file." + "Wrong enzyme file.");
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
     * Imports the gene mapping.
     */
    private void loadGeneMapping() {
        try {
            geneFactory.initialize(new File(resource, GENE_MAPPING_PATH), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * The path to the gene mapping file.
     */
    private final String GENE_MAPPING_PATH = "/resources/conf/gene_ontology/gene_details_human";
    /**
     * The gene factory.
     */
    private GeneFactory geneFactory = GeneFactory.getInstance();

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
    public void setProject(MsExperiment experiment, Sample sample, int replicateNumber) {
        this.experiment = experiment;
        this.sample = sample;
        this.replicateNumber = replicateNumber;
        ProteomicAnalysis proteomic_Analysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
        identification = proteomic_Analysis.getIdentification(IdentificationMethod.MS2_IDENTIFICATION);



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

    public ArrayList<IonMatch> getIonsCurrentlyMatched() throws MzMLUnmarshallerException {
        return spectrumAnnotator.getCurrentAnnotation(annotationPreferences.getIonTypes(),
                annotationPreferences.getNeutralLosses(),
                annotationPreferences.getValidatedCharges());
    }

    public SpectrumAnnotator getSpectrumAnnorator() {
        return spectrumAnnotator;
    }

    public void clearData(boolean clearDatabaseFolder) {
        projectDetails = null;
        spectrumAnnotator = new SpectrumAnnotator();
        try {
            spectrumFactory.closeFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sequenceFactory.closeFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GOFactory.getInstance().closeFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            spectrumFactory.clearFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sequenceFactory.clearFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            GOFactory.getInstance().clearFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }



        identifiedModifications = null;

        if (clearDatabaseFolder) {
            clearDatabaseFolder();
        }

        resetFeatureGenerator();

        // set up the tabs/panels

        currentPSFile = null;
    }

    /**
     * Resets the feature generator.
     */
    public void resetFeatureGenerator() {
        identificationFeaturesGenerator = new IdentificationFeaturesGenerator(identification, searchParameters, idFilter, metrics, spectrumCountingPreferences);
    }

    /**
     * Clears the database folder.
     */
    private void clearDatabaseFolder() {

        boolean databaseClosed = true;

        // close the database connection
        if (identification != null) {

            try {
                identification.close();
                identification = null;
            } catch (SQLException e) {
                databaseClosed = false;
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to close the database.", "Database Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        // empty the matches folder
        if (databaseClosed) {
            File matchFolder = new File(resource, PeptideShaker.SERIALIZATION_DIRECTORY);

            if (matchFolder.exists()) {

                File[] tempFiles = matchFolder.listFiles();

                if (tempFiles != null) {
                    for (File currentFile : tempFiles) {
                        Util.deleteDir(currentFile);
                    }
                }

                if (matchFolder.listFiles() != null && matchFolder.listFiles().length > 0) {
                    JOptionPane.showMessageDialog(null, "Failed to empty the database folder:\n" + matchFolder.getPath() + ".",
                            "Database Cleanup Failed", JOptionPane.WARNING_MESSAGE);
                }

            }
        }
    }

    /**
     * Updates the ions used for fragment annotation.
     */
    public void updateAnnotationPreferencesFromSearchSettings() {
        annotationPreferences.setPreferencesFromSearchParamaers(searchParameters);
    }

    /**
     * Returns the gene preferences.
     *
     * @return the gene preferences
     */
    public GenePreferences getGenePreferences() {
        return genePreferences;
    }
    private GenePreferences genePreferences = new GenePreferences();

    /**
     * Returns the experiment.
     * @return the experiment
     */
    public MsExperiment getExperiment() {
        return experiment;
    }

    /*
     * Returns the sample.
     * @return the sample
     */
    public Sample getSample() {
        return sample;
    }
    /**
     * Returns the replicate number.
     *
     * @return the replicateNumber
     */
    public int getReplicateNumber() {
        return this.replicateNumber;
    }

    /**
     * Returns the desired spectrum.
     *
     * @param spectrumKey the key of the spectrum
     * @return the desired spectrum
     */
    public MSnSpectrum getSpectrum(String spectrumKey) {
        String spectrumFile = Spectrum.getSpectrumFile(spectrumKey);
        String spectrumTitle = Spectrum.getSpectrumTitle(spectrumKey);
        try {
            return (MSnSpectrum) spectrumFactory.getSpectrum(spectrumFile, spectrumTitle);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
