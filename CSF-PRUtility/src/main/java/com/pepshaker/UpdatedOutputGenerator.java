/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshaker;

import com.compomics.util.experiment.annotation.gene.GeneFactory;
import com.compomics.util.experiment.biology.PTM;
import com.compomics.util.experiment.biology.PTMFactory;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.biology.Protein;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.preferences.ModificationProfile;
import com.pepshaker.util.beans.ExperimentBean;
import com.pepshaker.util.beans.FractionBean;
import com.pepshaker.util.beans.PeptideBean;
import com.pepshaker.util.beans.ProteinBean;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.myparameters.PSPtmScores;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.scoring.PtmScoring;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;

/**
 *
 * @author Y.M
 */
public class UpdatedOutputGenerator {

    /**
     * The main fileImporter.
     */
    private PSFileImporter importer;
    /**
     * The progress dialog.
     */
    private ProgressDialogX progressDialog;
    /**
     * The corresponding identification.
     */
    private com.compomics.util.experiment.identification.Identification identification;
    /**
     * The separator (tab by default).
     */
    public static final String SEPARATOR = "\t";
    /**
     * The sequence factory.
     */
    private final SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * The spectrum factory.
     */
    // private SpectrumFactory spectrumFactory = SpectrumFactory.getInstance();
    /**
     * The gene factory.
     */
    private final GeneFactory geneFactory = GeneFactory.getInstance();
    private JLabel label;

    /**
     *
     * @param importer 
     * @param label 
     *
     */
    public UpdatedOutputGenerator(PSFileImporter importer, JLabel label) {
        this.importer = importer;
        this.identification = importer.getIdentification();
        this.progressDialog = new ProgressDialogX(false);
        this.label = label;

    }

    public UpdatedOutputGenerator() {
    }
    private int gcCounter=0;

    @SuppressWarnings("static-access")
    public Map<String, ProteinBean> getProteinsOutput() {
        // create final versions of all variables use inside the export thread
        final ArrayList<String> proteinKeys;
        final boolean onlyValidated = false;
        final boolean includeHidden = false;
        final boolean onlyStarred = false;
        final boolean createMaximalProteinSet = false;
        final boolean otherAccessions = true;
        proteinKeys = identification.getProteinIdentification();
        final Map<String, ProteinBean> proteinList = new HashMap<String, ProteinBean>();//use only in case of protein files
        Thread t = new Thread("ExportProtThread") {
            @Override
            public void run() {
                try {
                    PSParameter proteinPSParameter = new PSParameter();
                    PSParameter peptidePSParameter = new PSParameter();
                    identification.loadProteinMatches(progressDialog);
                    identification.loadProteinMatchParameters(proteinPSParameter, progressDialog);
                    // store the maximal protein set of validated proteins
                    ArrayList<String> maximalProteinSet = new ArrayList<String>();
                    ProteinBean pb;
                    for (String proteinKey : proteinKeys) { // @TODO: replace by batch selection!!!
                        
                        pb = new ProteinBean();
                        proteinPSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, proteinPSParameter);
                        if (!ProteinMatch.isDecoy(proteinKey)) {//|| !onlyValidated) {

                            if ((onlyValidated && proteinPSParameter.isValidated()) || !onlyValidated) {
                                if ((!includeHidden && !proteinPSParameter.isHidden()) || includeHidden) {
                                    if ((onlyStarred && proteinPSParameter.isStarred()) || !onlyStarred) {
                                        
                                        
                                        ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey);
                                        pb.setAccession(proteinMatch.getMainMatch());

                                        if (createMaximalProteinSet && !maximalProteinSet.contains(proteinMatch.getMainMatch())) {
                                            maximalProteinSet.add(proteinMatch.getMainMatch());
                                        }
                                        if (createMaximalProteinSet || otherAccessions) {
                                            boolean first = true;
                                            // sort so that the protein accessions always come in the same order
                                            ArrayList<String> allProteins = proteinMatch.getTheoreticProteinsAccessions();
                                            Collections.sort(allProteins);
                                            StringBuilder completeProteinGroup = new StringBuilder();

                                            for (String otherProtein : allProteins) {
                                                if (otherProtein.equalsIgnoreCase(proteinMatch.getMainMatch())) {
                                                    continue;
                                                }
                                                if (createMaximalProteinSet && !maximalProteinSet.contains(otherProtein)) {
                                                    maximalProteinSet.add(otherProtein);
                                                }
                                                if (completeProteinGroup.length() > 0) {
                                                    completeProteinGroup.append(",");
                                                }
                                                completeProteinGroup.append(otherProtein);
                                            }

                                            if (otherAccessions) {
                                                pb.setOtherProteins(completeProteinGroup.toString());
                                            }
                                        }
                                        pb.setProteinInferenceClass(proteinPSParameter.getProteinInferenceClassAsString());
                                        try {
                                            pb.setDescription(sequenceFactory.getHeader(proteinMatch.getMainMatch()).getSimpleProteinDescription());
                                        } catch (IOException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (ClassNotFoundException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (InterruptedException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        }
                                        try {
                                            pb.setSequenceCoverage(importer.getIdentificationFeaturesGenerator().getSequenceCoverage(proteinKey) * 100);
                                            pb.setObservableCoverage(importer.getIdentificationFeaturesGenerator().getObservableCoverage(proteinKey) * 100);
                                        } catch (IOException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (ClassNotFoundException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        }
                                        // gene name and chromosome number
                                        catch (IllegalArgumentException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        }
                                        // gene name and chromosome number
                                        catch (InterruptedException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        }
                                        // gene name and chromosome number
                                        catch (SQLException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        }
                                        // gene name and chromosome number
                                        
                                        // gene name and chromosome number

                                        // gene name and chromosome number
                                        String tempGeneName = sequenceFactory.getHeader(proteinMatch.getMainMatch()).getGeneName();
                                        if (tempGeneName != null) {
                                            pb.setGeneName(tempGeneName);
                                        }
                                        String chromosomeNumber = geneFactory.getChromosomeForGeneName(tempGeneName);
                                        if (chromosomeNumber != null) {
                                            pb.setChromosomeNumber(chromosomeNumber);
                                        }
                                        ArrayList<String> peptideKeys = proteinMatch.getPeptideMatches();
                                        identification.loadPeptideMatches(peptideKeys, null);
                                        identification.loadPeptideMatchParameters(peptideKeys, peptidePSParameter, null);

                                        Protein currentProtein = sequenceFactory.getProtein(proteinMatch.getMainMatch());

                                        boolean allPeptidesEnzymatic = true;
                                        // see if we have non-tryptic peptides
                                        for (String peptideKey : peptideKeys) {
                                            String peptideSequence = identification.getPeptideMatch(peptideKey).getTheoreticPeptide().getSequence();
                                            peptidePSParameter = (PSParameter) identification.getPeptideMatchParameter(peptideKey, peptidePSParameter);
                                            if (peptidePSParameter.isValidated()) {
                                                boolean isEnzymatic = currentProtein.isEnzymaticPeptide(peptideSequence,
                                                        importer.getSearchParameters().getEnzyme());
                                                if (!isEnzymatic) {
                                                    allPeptidesEnzymatic = false;
                                                    break;
                                                }
                                            }
                                        }
                                        pb.setNonEnzymaticPeptides(!allPeptidesEnzymatic);
                                        try {

                                            String str1 = (importer.getIdentificationFeaturesGenerator().getPrimaryPTMSummary(proteinKey, SEPARATOR));

                                            String[] strArr = str1.split("\\|");
                                            if (strArr.length == 2) {
                                                pb.setConfidentPtmSites(strArr[0]);
                                                pb.setNumberConfident(strArr[1]);
                                            } else {
                                                pb.setConfidentPtmSites("");
                                                pb.setNumberConfident("");

                                            }
                                            String str2 = (importer.getIdentificationFeaturesGenerator().getSecondaryPTMSummary(proteinKey, SEPARATOR) + SEPARATOR);
                                            String[] strArr2 = str2.split("\\|");
                                            if (strArr2.length == 2) {
                                                pb.setOtherPtmSites(strArr2[0]);
                                                pb.setNumberOfOther(strArr2[1]);
                                            } else {
                                                pb.setOtherPtmSites("");
                                                pb.setNumberOfOther("");
                                            }
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (SQLException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (IOException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (ClassNotFoundException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        } catch (InterruptedException e) {
                                            System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                                        }
//                                        try {
//                                            pb.setNumberValidatedPeptides(importer.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey));
//                                        } catch (IllegalArgumentException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (SQLException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (IOException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (ClassNotFoundException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (InterruptedException e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                        try {
//                                            pb.setNumberValidatedSpectra(importer.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey));
//
//                                        } catch (IllegalArgumentException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (SQLException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (IOException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (ClassNotFoundException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (InterruptedException e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                        try {
//                                            pb.setEmPai(importer.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey, SpectrumCountingPreferences.SpectralCountingMethod.EMPAI));
//                                        } catch (IOException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (IllegalArgumentException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (SQLException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (ClassNotFoundException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (InterruptedException e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                        try {
//                                            pb.setNsaf(importer.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey,
//                                                    SpectrumCountingPreferences.SpectralCountingMethod.NSAF));
//                                        } catch (IOException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (IllegalArgumentException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (SQLException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (ClassNotFoundException e) {
//                                            System.out.println(e.getMessage());
//                                        } catch (InterruptedException e) {
//                                            System.out.println(e.getMessage());
//                                        }
                                        Double proteinMW = sequenceFactory.computeMolecularWeight(proteinMatch.getMainMatch());
                                        pb.setMw_kDa(proteinMW);
                                        pb.setScore(proteinPSParameter.getProteinScore());
                                        pb.setConfidence(proteinPSParameter.getProteinConfidence());
                                        if (proteinPSParameter.isValidated()) {
                                            pb.setValidated(true);
                                        } else {
                                            pb.setValidated(false);

                                        }
                                        pb.setStarred(proteinPSParameter.isStarred());
//                                        proteinList.put(pb.getAccession() + "," + pb.getOtherProteins(), pb);
                              
                                        proteinList.put(proteinKey, pb);
                                        label.setText("Proteins processing... " + ((proteinList.size() * 100) / (proteinKeys.size()*5)) + " %");
//
//                                       System.out.println(gcCounter++ +"prot old key : " + pb.getAccession() + "," + pb.getOtherProteins());
                                     if (gcCounter == 50) {
                                            System.gc();
                                            gcCounter=0;
                                        }
                                    }

                                }
                            }
                        }
                    }
                    Map<String, ProteinBean> updatedMap = new HashMap<String, ProteinBean>();
                  
                    for (String proteinKey : proteinList.keySet()) {
                        ProteinBean tpb = proteinList.get(proteinKey);

                        try {
                            tpb.setNumberValidatedPeptides(importer.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey));
                            updatedMap.put(proteinKey, tpb);

                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        } catch (ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        label.setText("Proteins processing... " + (((proteinKeys.size()+(updatedMap.size())) * 100) / (proteinKeys.size()*5)) + " %");
                        
                    }
                    proteinList.clear();
                    proteinList.putAll(updatedMap);
                    updatedMap.clear();

                    for (String proteinKey : proteinList.keySet()) {
                        ProteinBean tpb = proteinList.get(proteinKey);

                        try {
                            tpb.setNumberValidatedSpectra(importer.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey));
                            updatedMap.put(proteinKey, tpb);

                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        } catch (ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                         label.setText("Proteins processing... " + ((((2*proteinKeys.size())+(updatedMap.size())) * 100) / (proteinKeys.size()*5)) + " %");
////                         System.out.println("count prot is: " + gcCounter++);
                         
                                        if (gcCounter == 50) {
                                            System.gc();
                                            gcCounter=0;
                                        }
                    }
                    proteinList.clear();
                    proteinList.putAll(updatedMap);
                    updatedMap.clear();

                    for (String proteinKey : proteinList.keySet()) {
                        ProteinBean tpb = proteinList.get(proteinKey);

                        try {
                            tpb.setEmPai(importer.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey, SpectrumCountingPreferences.SpectralCountingMethod.EMPAI));
                            updatedMap.put(proteinKey, tpb);

                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        } catch (ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        label.setText("Proteins processing... " + ((((3*proteinKeys.size())+(updatedMap.size() ))* 100) / (proteinKeys.size()*5)) + " %");
//                        System.out.println("count prot is: " + gcCounter++);
                        
                                        if (gcCounter == 50) {
                                            System.gc();
                                            gcCounter=0;
                                        }
                    }
                    proteinList.clear();
                    proteinList.putAll(updatedMap);
                    updatedMap.clear();
                    for (String proteinKey : proteinList.keySet()) {
                        ProteinBean tpb = proteinList.get(proteinKey);

                        try {
                            tpb.setNsaf(importer.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey,
                                    SpectrumCountingPreferences.SpectralCountingMethod.NSAF));
                            updatedMap.put(proteinKey, tpb);

                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        } catch (ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        label.setText("Proteins processing... " + (((((4*proteinKeys.size())+updatedMap.size())) * 100) / (proteinKeys.size()*5)) + " %");
                        gcCounter++;
                                        if (gcCounter == 50) {
                                            System.gc();
                                            gcCounter=0;
                                        }
                    }
                    proteinList.clear();
                    proteinList.putAll(updatedMap);
                    updatedMap.clear();
                    
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        t.setPriority(Thread.MAX_PRIORITY);
//        t.start();
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(t);
        es.shutdown();
        try {
            boolean finshed = es.awaitTermination(10, TimeUnit.DAYS);
            System.gc();
            gcCounter=0;
//        t.start();
        } catch (InterruptedException ex) {
             System.out.println(ex.getMessage());
        }
//        int protIndexer = 0;
//        label.setText("Proteins processing... " + ((proteinList.size() * 100) / proteinKeys.size()) + " %");
//
//        while (t.isAlive()) {
//            if (protIndexer >= 12) {
//                label.setText("Proteins processing... " + ((proteinList.size() * 100) / proteinKeys.size()) + " %");
//
//                System.gc();
//                protIndexer = 0;
//            }
//            try {
//
//                Thread.currentThread().sleep(5000);
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//            protIndexer++;
//        }
        return proteinList;
    }

    /**
     * Sends the desired peptide output (based on the elements needed as
     * provided in arguments) to a user chosen file.
     * @return peptideList
     */
    public Map<Integer, PeptideBean> getPeptidesOutput() {
        // create final versions of all variables use inside the export thread
        final Map<Integer, PeptideBean> peptideList = new HashMap<Integer, PeptideBean>();
        final ArrayList<String> peptideKeys = identification.getPeptideIdentification();
        final boolean onlyValidated = false;
        final boolean includeHidden = true;
        final boolean onlyStarred = false;
        
        Thread t;
        t = new Thread("ExportPepThread") {
            @Override
            public void run() {
                try {
                    PSParameter peptidePSParameter = new PSParameter();
                    PSParameter secondaryPSParameter = new PSParameter();
                    HashMap<String, HashMap<Integer, String[]>> surroundingAAs = new HashMap<String, HashMap<Integer, String[]>>();
                    ProteinMatch proteinMatch = null;
                    ModificationProfile ptmProfile = importer.getSearchParameters().getModificationProfile();
                    identification.loadPeptideMatches(progressDialog);
                    identification.loadPeptideMatchParameters(peptidePSParameter, progressDialog);
                    PeptideBean pb = null;
                    int index = 0;
                    int indecator=0;
                    for (String peptideKey : peptideKeys) { // @TODO: replace by batch selection!!! 
                        boolean shared = false;
                        PeptideMatch peptideMatch = identification.getPeptideMatch(peptideKey);
                        
                        //for checking
                        if (peptideMatch.getTheoreticPeptide().isDecoy()) {
                                               continue;
                                            } 
                        
                        peptidePSParameter = (PSParameter) identification.getPeptideMatchParameter(peptideKey, peptidePSParameter);
                        if (!peptideMatch.getTheoreticPeptide().isDecoy() || !onlyValidated) {
                            if ((onlyValidated && peptidePSParameter.isValidated()) || !onlyValidated) {
                                if ((!includeHidden && !peptidePSParameter.isHidden()) || includeHidden) {
                                    if ((onlyStarred && peptidePSParameter.isStarred()) || !onlyStarred) {
                                        Peptide peptide = peptideMatch.getTheoreticPeptide();
                                        ArrayList<String> possibleProteins = new ArrayList<String>();
                                        ArrayList<String> orderedProteinsKeys = new ArrayList<String>(); // @TODO: could be merged with one of the other maps perhaps?                                              
                                        for (String parentProtein : peptide.getParentProteins()) {
                                            pb = new PeptideBean();
                                       /////////////////////     
                                            pb.setDecoy(0);
                                      //////////////////////////      
                                            ArrayList<String> parentProteins = identification.getProteinMap().get(parentProtein);
                                            if (parentProteins != null) {
                                                for (String proteinKey : parentProteins) {
                                                    if (!possibleProteins.contains(proteinKey)) {
                                                        try {
                                                            proteinMatch = identification.getProteinMatch(proteinKey);
                                                            if (proteinMatch.getPeptideMatches().contains(peptideKey)) {
                                                                possibleProteins.add(proteinKey);
                                                            }
                                                        } catch (IllegalArgumentException e) {
                                                            // protein deleted due to protein inference issue and not deleted from the map in versions earlier than 0.14.6
                                                            System.out.println("Non-existing protein key in protein map: " + proteinKey + "  error " + e.getMessage());

                                                        } catch (SQLException e) {
                                                            // protein deleted due to protein inference issue and not deleted from the map in versions earlier than 0.14.6
                                                            System.out.println("Non-existing protein key in protein map: " + proteinKey + "  error " + e.getMessage());
                                                        } catch (IOException e) {
                                                            // protein deleted due to protein inference issue and not deleted from the map in versions earlier than 0.14.6
                                                            System.out.println("Non-existing protein key in protein map: " + proteinKey + "  error " + e.getMessage());
                                                        } catch (ClassNotFoundException e) {
                                                            // protein deleted due to protein inference issue and not deleted from the map in versions earlier than 0.14.6
                                                            System.out.println("Non-existing protein key in protein map: " + proteinKey + "  error " + e.getMessage());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        shared = possibleProteins.size() > 1;
                                        proteinMatch = identification.getProteinMatch(possibleProteins.get(0));


                                        String mainMatch="", secondaryProteins = "", peptideProteins = "";
                                        String secondaryProteinsDescriptions = "", peptideProteinDescriptions = "";
                                        ArrayList<String> accessions = new ArrayList<String>();

                                        mainMatch = proteinMatch.getMainMatch();
                                        boolean first = true;
                                        if (!shared) {
                                            orderedProteinsKeys.add(mainMatch);
                                        }

                                        accessions.addAll(proteinMatch.getTheoreticProteinsAccessions());
                                        Collections.sort(accessions);
                                        for (String key : accessions) {
                                            if (!key.equals(mainMatch)) {
                                                if (first) {
                                                    first = false;
                                                } else {
                                                    secondaryProteins += ", ";
                                                    secondaryProteinsDescriptions += "; ";
                                                }
                                                secondaryProteins += key;
                                                secondaryProteinsDescriptions += sequenceFactory.getHeader(key).getSimpleProteinDescription();
                                                orderedProteinsKeys.add(key);
                                            }
                                        }

                                        if (shared) {
                                            mainMatch = "shared peptide";
                                        }

                                        first = true;
                                        ArrayList<String> peptideAccessions = new ArrayList<String>(peptide.getParentProteins());
                                        Collections.sort(peptideAccessions);
                                        for (String key : peptideAccessions) {
                                            if (shared || !accessions.contains(key)) {
                                                if (first) {
                                                    first = false;
                                                } else {
                                                    peptideProteins += ", ";
                                                    peptideProteinDescriptions += "; ";
                                                }
                                                peptideProteins += key;
                                                peptideProteinDescriptions += sequenceFactory.getHeader(key).getSimpleProteinDescription();
                                                orderedProteinsKeys.add(key);
                                            }
                                        }
                                        
                                        pb.setProtein(mainMatch);
                                        pb.setOtherProteins(secondaryProteins);
                                        pb.setPeptideProteins(peptideProteins);
                                        pb.setOtherProteinDescriptions(secondaryProteinsDescriptions);
                                        pb.setPeptideProteinsDescriptions(peptideProteinDescriptions);
                                        pb.setProteinInference(peptidePSParameter.getProteinInferenceClassAsString());


                                        for (String proteinAccession : orderedProteinsKeys) {
                                            surroundingAAs.put(proteinAccession,
                                                    sequenceFactory.getProtein(proteinAccession).getSurroundingAA(peptide.getSequence(),
                                                    importer.getDisplayPreferences().getnAASurroundingPeptides()));
                                        }
                                        String subSequence = "";
                                        for (String proteinAccession : orderedProteinsKeys) {
                                            ArrayList<Integer> starts = new ArrayList<Integer>(surroundingAAs.get(proteinAccession).keySet());
                                            Collections.sort(starts);
                                            first = true;
                                            for (int start : starts) {
                                                if (first) {
                                                    first = false;
                                                } else {
                                                    subSequence += "|";
                                                }
                                                subSequence += surroundingAAs.get(proteinAccession).get(start)[0];
                                            }

                                            subSequence += ";";
                                        }
                                        subSequence = subSequence.substring(0, subSequence.length() - 1);
                                        pb.setAaBefore(subSequence);
                                        pb.setSequence(peptide.getSequence());
                                        pb.setSequenceTagged(peptide.getTaggedModifiedSequence(importer.getSearchParameters().getModificationProfile(),
                                                false, false, true));
                                        subSequence = "";
                                        for (String proteinAccession : orderedProteinsKeys) {
                                            ArrayList<Integer> starts = new ArrayList<Integer>(surroundingAAs.get(proteinAccession).keySet());
                                            Collections.sort(starts);
                                            boolean first1 = true;
                                            for (int start : starts) {
                                                if (first1) {
                                                    first1 = false;
                                                } else {
                                                    subSequence += "|";
                                                }
                                                subSequence += surroundingAAs.get(proteinAccession).get(start)[1];
                                            }
                                            subSequence += ";";
                                        }

                                        subSequence = subSequence.substring(0, subSequence.length() - 1);
                                        pb.setAaAfter(subSequence);
                                        boolean isEnzymatic = sequenceFactory.getProtein(proteinMatch.getMainMatch()).isEnzymaticPeptide(peptide.getSequence(),
                                                importer.getSearchParameters().getEnzyme());
                                        pb.setEnzymatic(isEnzymatic);

                                        String start = "";
                                        String end = "";
                                        for (String proteinAccession : orderedProteinsKeys) {
                                            int endAA;
                                            String sequence1 = peptide.getSequence();
                                            ArrayList<Integer> starts = new ArrayList<Integer>(surroundingAAs.get(proteinAccession).keySet());
                                            Collections.sort(starts);
                                            first = true;
                                            for (int startAa : starts) {
                                                if (first) {
                                                    first = false;
                                                } else {
                                                    start += ",";
                                                    end += ",";
                                                }
                                                start += startAa;
                                                endAA = startAa + sequence1.length();
                                                end += endAA;
                                            }

                                            start += "; ";
                                            end += "; ";
                                        }
                                        start = start.substring(0, start.length() - 2);
                                        end = end.substring(0, end.length() - 2);
                                        pb.setPeptideStart(start);
                                        pb.setPeptideEnd(end);

                                        pb.setFixedModification(getPeptideModificationsAsString(peptide, false));
                                        pb.setVariableModification(getPeptideModificationsAsString(peptide, true));

                                        pb.setLocationConfidence(getPeptideModificationLocations(peptide, peptideMatch, ptmProfile));
                                        pb.setPrecursorCharges(getPeptidePrecursorChargesAsString(peptideMatch));

                                        int cpt = 0;
                                        identification.loadSpectrumMatchParameters(peptideMatch.getSpectrumMatches(), secondaryPSParameter, null);
                                        for (String spectrumKey : peptideMatch.getSpectrumMatches()) {
                                            secondaryPSParameter = (PSParameter) identification.getSpectrumMatchParameter(spectrumKey, secondaryPSParameter);
                                            if (secondaryPSParameter.isValidated()) {
                                                cpt++;
                                            }
                                        }
                                        pb.setNumberOfValidatedSpectra(cpt);
                                        pb.setScore(peptidePSParameter.getPeptideScore());
                                        pb.setConfidence(peptidePSParameter.getPeptideConfidence());

                                        if (!onlyValidated) {
                                            if (peptidePSParameter.isValidated()) {
                                                pb.setValidated(1);
                                            } else {
                                                pb.setValidated(0);
                                            }
//                                            if (peptideMatch.getTheoreticPeptide().isDecoy()) {
//                                                pb.setDecoy(1);
//                                            } else {
//                                                pb.setDecoy(0);
//                                            }
                                        }
//                                                if (includeHidden) {
//                                                    writer.write(peptidePSParameter.isHidden() + SEPARATOR);
//                                                }
                                        if (!onlyStarred) {
                                            pb.setStarred(peptidePSParameter.isStarred());
                                        }
                                        peptideList.put(index, pb);
                                        
                                        
                label.setText("Peptides processing... " + ((peptideList.size() * 100) / peptideKeys.size()) + " %");
                gcCounter++;
                                        if (gcCounter == 50) {
                                            System.gc();
                                            gcCounter=0;
                                        }
                
//                                        if(peptideList.size() == 500)
//                                            break;
                                        
                                        index++;
                                        

                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        
                                        
        t.setPriority(Thread.MAX_PRIORITY);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(t);
        es.shutdown();
        try {
            boolean finshed = es.awaitTermination(10, TimeUnit.DAYS);
            System.gc();
            gcCounter=0;
//        t.start();
        } catch (InterruptedException ex) {
             System.out.println(ex.getMessage());
        }

//        int pepIndex = 0;
//        label.setText("Peptides processing... " + ((peptideList.size() * 100) / peptideKeys.size()) + " %");
//
//        while (t.isAlive()) {
//            if (pepIndex== 12) {
//                label.setText("Peptides processing... " + ((peptideList.size() * 100) / peptideKeys.size()) + " %");
//                System.gc();
//                pepIndex = 0;
//            }
//            try {
//                
//                System.out.println(" thread to sleep "+Thread.currentThread().getName());
//                Thread.currentThread().sleep(10000);
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//            pepIndex++;
//        }
        return peptideList;

    }
    private int index;
    private int protIndex;

    @SuppressWarnings("static-access")
    public ExperimentBean getFractionsOutput(final ExperimentBean exp) {
        // @TODO: add the non enzymatic peptides detected information!!
        // create final versions of all variables use inside the export thread
//        final ArrayList<String> proteinKeys;
//        proteinKeys = identification.getProteinIdentification();
        final Map<Integer, FractionBean> fractionsList = new HashMap<Integer, FractionBean>();
        try {
            final ArrayList<String> fractionFileNames = new ArrayList<String>();

            for (String fileName : importer.getIdentification().getOrderedSpectrumFileNames()) {
                fractionFileNames.add(fileName);
//                System.out.println("fractions file names "+ fileName);
            }
            if (fractionFileNames.isEmpty() ){//|| fractionFileNames.size()==1) {
                exp.setFractionsList(fractionsList);
                return exp;
            }
            else{
                gcCounter=0;
            Thread t = new Thread("ExportFractThread") {
                @Override
                public void run() {
                    try {
                        exp.setFractionsNumber(fractionFileNames.size());
                        PSParameter proteinPSParameter = new PSParameter();
//                        PSParameter peptidePSParameter = new PSParameter();
                        for (int z = 1; z <= fractionFileNames.size(); z++) {
                            FractionBean fb = new FractionBean();
                            Map<String, ProteinBean> temProteinList = new HashMap<String, ProteinBean>();
                            fb.setProteinList(temProteinList);
                            fb.setFractionIndex(z);
                            fractionsList.put((z), fb);
                        }
                        protIndex = 0;
//                        for (String proteinKey : proteinKeys) {
                          for (String proteinKey : exp.getProteinList().keySet()) {
                            proteinPSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, proteinPSParameter);
                            ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey);


                            ArrayList<String> maximalProteinSet = new ArrayList<String>();
                            String accession = proteinMatch.getMainMatch();
                            String otherProteins = "";
                            maximalProteinSet.add(proteinMatch.getMainMatch());
//                                                                             
                            if (true) {
                                boolean first = true;
                                // sort so that the protein accessions always come in the same order
                                ArrayList<String> allProteins = proteinMatch.getTheoreticProteinsAccessions();
                                Collections.sort(allProteins);
                                StringBuilder completeProteinGroup = new StringBuilder();
//
                                for (String otherProtein : allProteins) {
                                    if (otherProtein.equalsIgnoreCase(proteinMatch.getMainMatch())) {
                                        continue;
                                    }
                                    if (true && !maximalProteinSet.contains(otherProtein)) {
                                        maximalProteinSet.add(otherProtein);
                                    }
                                    if (completeProteinGroup.length() > 0) {
                                        completeProteinGroup.append(",");
                                    }
                                    completeProteinGroup.append(otherProtein);
                                }
//
//                                            
                                otherProteins = completeProteinGroup.toString();
//                                          
                            }

                            ProteinBean pb = null;
                            if (exp.getProteinList() != null) {
//                                 pb = exp.getProteinList().get(accession+ "," + otherProteins);
//                            }
                                pb = exp.getProteinList().get(proteinKey);
                                if (pb == null) {
//                                    System.out.println("its null protiens");
                                    pb = new ProteinBean();
                                    pb.setAccession(accession);
                                    pb.setOtherProteins(otherProteins);//                              
                                    Double proteinMW = sequenceFactory.computeMolecularWeight(proteinMatch.getMainMatch());
                                    pb.setMw_kDa(proteinMW);
                                    try {
                                        pb.setNumberValidatedPeptides(importer.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey));
                                    } catch (IllegalArgumentException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedPeptides(Integer.valueOf(d));
                                        System.out.println(e.getLocalizedMessage());
//                                    e.printStackTrace();
                                    } catch (SQLException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedPeptides(Integer.valueOf(d));
                                        System.out.println(e.getLocalizedMessage());
//                                    e.printStackTrace();
                                    } catch (IOException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedPeptides(Integer.valueOf(d));
                                        System.out.println(e.getLocalizedMessage());
//                                    e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedPeptides(Integer.valueOf(d));
                                        System.out.println(e.getLocalizedMessage());
//                                    e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedPeptides(Integer.valueOf(d));
                                        System.out.println(e.getLocalizedMessage());
//                                    e.printStackTrace();
                                    }
                                    try {
                                        pb.setNumberValidatedSpectra(importer.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey));
                                    } catch (IllegalArgumentException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedSpectra(Integer.valueOf(d));
                                    } catch (SQLException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedSpectra(Integer.valueOf(d));
                                    }
//
                                    catch (IOException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedSpectra(Integer.valueOf(d));
                                    }
//
                                    catch (ClassNotFoundException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedSpectra(Integer.valueOf(d));
                                    }
//
                                    catch (InterruptedException e) {
                                        String d = "" + Double.NaN;
                                        pb.setNumberValidatedSpectra(Integer.valueOf(d));
                                    }
//
                                    
//                                
                                }
//                          
                                index = 1;
                                for (String fraction : fractionFileNames) {
                                    ProteinBean tempPb = new ProteinBean(pb);
                                    FractionBean fb = fractionsList.get(index);
                                    Map<String, ProteinBean> temProteinList = fb.getProteinList();
                                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                                            && proteinPSParameter.getFractionValidatedPeptides(fraction) != null) {
                                        tempPb.setNumberOfPeptidePerFraction(proteinPSParameter.getFractionValidatedPeptides(fraction));
                                    } else {

                                        tempPb.setNumberOfPeptidePerFraction(0);
                                    }

                                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                                            && proteinPSParameter.getFractionValidatedSpectra(fraction) != null) {
                                        tempPb.setNumberOfSpectraPerFraction(proteinPSParameter.getFractionValidatedSpectra(fraction));
                                    } else {
                                        tempPb.setNumberOfSpectraPerFraction(0);
                                    }

                                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                                            && proteinPSParameter.getPrecursorIntensityAveragePerFraction(fraction) != null) {
                                        tempPb.setAveragePrecursorIntensityPerFraction(proteinPSParameter.getPrecursorIntensityAveragePerFraction(fraction));
                                    } else {
                                        tempPb.setAveragePrecursorIntensityPerFraction(0.0);
                                    }
                                    temProteinList.put(proteinKey, tempPb);
                                    fb.setProteinList(temProteinList);
                                    fractionsList.put((index), fb);
                                    index++;
                                }
                            }
                            
                            label.setText("Fractions processing... " + ((protIndex * 100) /  exp.getProteinList().size() + " %"));
                            protIndex++;
                            gcCounter++;
                            if (gcCounter == 50) {
                                            System.gc();
                                            gcCounter=0;
                                        }
                  
                        }//remove it
                    } catch (IOException e) {
                        System.out.println(e.getLocalizedMessage());
//                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        System.out.println(e.getLocalizedMessage());
//                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getLocalizedMessage());
//                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        System.out.println(e.getLocalizedMessage());
//                        e.printStackTrace();
                    } catch (SQLException e) {
                        System.out.println(e.getLocalizedMessage());
//                        e.printStackTrace();
                    }
                }
            };          
        t.setPriority(Thread.MAX_PRIORITY);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(t);
        es.shutdown();
        try {
            boolean finshed = es.awaitTermination(10, TimeUnit.DAYS);
            System.gc();
            gcCounter=0;
//        t.start();
        } catch (InterruptedException ex) {
             System.out.println(ex.getMessage());
        }
//            
//            t.start();
//            label.setText("Fractions processing... " + ((protIndex * 100) /  exp.getProteinList().size() + " %"));
//            int fractIndex = 0;
//            while (t.isAlive()) {
//                if (fractIndex == 12) {
//                   label.setText("Fractions processing... " + ((protIndex * 100) /  exp.getProteinList().size() + " %"));
//                    System.gc();
//                    fractIndex = 0;
//                }
//                try {
//                    Thread.currentThread().sleep(5000);
//                } catch (InterruptedException e) {
//                    System.out.println(e.getMessage());
//                }
//                fractIndex++;
//            }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        exp.setFractionsList(fractionsList);
        return exp;
    }

    public static String getPeptideModificationsAsString(Peptide peptide, boolean variablePtms) {
        StringBuilder result = new StringBuilder();
        HashMap<String, ArrayList<Integer>> modMap = new HashMap<String, ArrayList<Integer>>();
        for (ModificationMatch modificationMatch : peptide.getModificationMatches()) {
            if ((variablePtms && modificationMatch.isVariable()) || (!variablePtms && !modificationMatch.isVariable())) {
                if (!modMap.containsKey(modificationMatch.getTheoreticPtm())) {
                    modMap.put(modificationMatch.getTheoreticPtm(), new ArrayList<Integer>());
                }
                modMap.get(modificationMatch.getTheoreticPtm()).add(modificationMatch.getModificationSite());
            }
        }
        boolean first = true, first2;
        ArrayList<String> mods = new ArrayList<String>(modMap.keySet());
        Collections.sort(mods);
        for (String mod : mods) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            first2 = true;
            result.append(mod);
            result.append(" (");
            for (int aa : modMap.get(mod)) {
                if (first2) {
                    first2 = false;
                } else {
                    result.append(",");
                }
                result.append(aa);
            }
            result.append(")");
        }
        return result.toString();
    }

    /**
     * Returns the peptide modification location confidence as a string.
     *
     * @param peptide the peptide
     * @param peptideMatch the peptide match
     * @param ptmProfile the PTM profile
     * @return the peptide modification location confidence as a string.
     */
    public static String getPeptideModificationLocations(Peptide peptide, PeptideMatch peptideMatch, ModificationProfile ptmProfile) {
        PTMFactory ptmFactory = PTMFactory.getInstance();
        String result = "";
        ArrayList<String> modList = new ArrayList<String>();
        for (ModificationMatch modificationMatch : peptide.getModificationMatches()) {
            if (modificationMatch.isVariable()) {
                PTM refPtm = ptmFactory.getPTM(modificationMatch.getTheoreticPtm());
                for (String equivalentPtm : ptmProfile.getSimilarNotFixedModifications(refPtm.getMass())) {
                    if (!modList.contains(equivalentPtm)) {
                        modList.add(equivalentPtm);
                    }
                }
            }
        }
        Collections.sort(modList);
        boolean first = true;

        for (String mod : modList) {
            if (first) {
                first = false;
            } else {
                result += ",";
            }
            PSPtmScores ptmScores = (PSPtmScores) peptideMatch.getUrParam(new PSPtmScores());
            result += mod + " (";
            if (ptmScores != null && ptmScores.getPtmScoring(mod) != null) {
                int ptmConfidence = ptmScores.getPtmScoring(mod).getPtmSiteConfidence();
                if (ptmConfidence == PtmScoring.NOT_FOUND) {
                    result += "Not Scored"; // Well this should not happen
                } else if (ptmConfidence == PtmScoring.RANDOM) {
                    result += "Random";
                } else if (ptmConfidence == PtmScoring.DOUBTFUL) {
                    result += "Doubtfull";
                } else if (ptmConfidence == PtmScoring.CONFIDENT) {
                    result += "Confident";
                } else if (ptmConfidence == PtmScoring.VERY_CONFIDENT) {
                    result += "Very Confident";
                }
            } else {
                result += "Not Scored";
            }
            result += ")";
        }

        return result;
    }

    /**
     * Returns the possible precursor charges for a given peptide match. The
     * charges are returned in increasing order with each charge only appearing
     * once.
     *
     * @param peptideMatch the peptide match
     * @return the possible precursor charges
     */
    private String getPeptidePrecursorChargesAsString(PeptideMatch peptideMatch) {
        StringBuilder results = new StringBuilder();
        ArrayList<String> spectrumKeys = peptideMatch.getSpectrumMatches();
        ArrayList<Integer> charges = new ArrayList<Integer>(5);
        // find all unique the charges
        try {
            identification.loadSpectrumMatches(spectrumKeys, null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //ignore caching error
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //ignore caching error
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            //ignore caching error
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            //ignore caching error
        }
        for (int i = 0; i < spectrumKeys.size(); i++) {
            try {
                int tempCharge = importer.getIdentification().getSpectrumMatch(spectrumKeys.get(i)).getBestAssumption().getIdentificationCharge().value;

                if (!charges.contains(tempCharge)) {
                    charges.add(tempCharge);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return "Error";
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return "Error";
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return "Error";
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
                return "Error";
            }
        }
        // sort the charges
        Collections.sort(charges);
        // add the charges to the output
        for (int i = 0; i < charges.size(); i++) {
            if (i > 0) {
                results.append(",");
            }
            results.append(charges.get(i));
        }
        return results.toString();
    }
}
