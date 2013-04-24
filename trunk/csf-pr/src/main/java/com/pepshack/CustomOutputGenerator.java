/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack;

/**
 *
 * @author Yehia Mokhtar
 */
import com.compomics.util.experiment.biology.PTM;
import com.compomics.util.experiment.biology.PTMFactory;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.biology.Protein;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.massspectrometry.SpectrumFactory;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.preferences.ModificationProfile;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;


import com.model.beans.ProteinBean;
import static eu.isas.peptideshaker.export.OutputGenerator.getPeptideModificationLocations;
import static eu.isas.peptideshaker.export.OutputGenerator.getPeptideModificationsAsString;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.myparameters.PSPtmScores;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.scoring.PtmScoring;
import java.io.BufferedWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import no.uib.jsparklines.data.XYDataPoint;

/**
 * This class will generate the output as requested by the user.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class CustomOutputGenerator {

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
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * The spectrum factory.
     */
    private SpectrumFactory spectrumFactory = SpectrumFactory.getInstance();
    /**
     * The writer used to send the output to file.
     */
    private BufferedWriter writer;

    /**
     * Constructor.
     *
     * @param peptideShakerGUI
     * @throws SQLException
     */
    public CustomOutputGenerator(PSFileImporter importer) {
        this.importer = importer;
        identification = importer.getIdentification();
        progressDialog = new ProgressDialogX(false);
    }

    public Map<String, ProteinBean> getProteinsOutput(ArrayList<String> aProteinKeys) {
        Map<String, ProteinBean> proteinList = new HashMap<String, ProteinBean>();//use only in case of protein files
        // create final versions of all variables use inside the export thread
        final ArrayList<String> proteinKeys;  
        if (aProteinKeys == null) {
            proteinKeys = identification.getProteinIdentification();
        } else {
            proteinKeys = aProteinKeys;
        }

        ProteinBean pb = new ProteinBean();
        try {
            PSParameter proteinPSParameter = new PSParameter();
            PSParameter peptidePSParameter = new PSParameter();
            identification.loadProteinMatches(new ProgressDialogX(false));
            identification.loadProteinMatchParameters(proteinPSParameter, progressDialog);
            // store the maximal protein set of validated proteins
            ArrayList<String> maximalProteinSet = new ArrayList<String>();
            boolean createMaximalProteinSet = false;
            for (String proteinKey : proteinKeys) { // @TODO: replace by batch selection!!!
                proteinPSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, proteinPSParameter);
                ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey);
                pb.setAccession(proteinMatch.getMainMatch());
                if (createMaximalProteinSet && !maximalProteinSet.contains(proteinMatch.getMainMatch())) {
                    maximalProteinSet.add(proteinMatch.getMainMatch());//no use for now
                }
                // sort so that the protein accessions always come in the same order
                ArrayList<String> allProteins = proteinMatch.getTheoreticProteinsAccessions();
                Collections.sort(allProteins);
                StringBuilder completeProteinGroup = new StringBuilder();
                for (String otherProtein : allProteins) {
                    if (createMaximalProteinSet && !maximalProteinSet.contains(otherProtein)) {
                        maximalProteinSet.add(otherProtein);
                    }
                    if (completeProteinGroup.length() > 0) {
                        completeProteinGroup.append(", ");
                    }
                    completeProteinGroup.append(otherProtein);
                }
                pb.setOtherProteins(completeProteinGroup.toString());
                pb.setProteinInferenceClass(proteinPSParameter.getProteinInferenceClassAsString());
                try {
                    pb.setDescription(sequenceFactory.getHeader(proteinMatch.getMainMatch()).getDescription());
                } catch (Exception e) {
                    System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                }
                try {
                    pb.setSequenceCoverage(importer.getIdentificationFeaturesGenerator().getSequenceCoverage(proteinKey) * 100);
                    pb.setObservableCoverage(importer.getIdentificationFeaturesGenerator().getObservableCoverage(proteinKey) * 100);
                } catch (Exception e) {
                    System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                }
                ArrayList<String> peptideKeys = proteinMatch.getPeptideMatches();
                Protein currentProtein = sequenceFactory.getProtein(proteinMatch.getMainMatch());
                boolean allPeptidesEnzymatic = true;
                identification.loadPeptideMatches(peptideKeys, null);
                identification.loadPeptideMatchParameters(peptideKeys, peptidePSParameter, null);
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
                    String str1 = (importer.getIdentificationFeaturesGenerator().getPrimaryPTMSummary(proteinKey));
                    String[] strArr = str1.split("\\|");
                    if (strArr.length == 2) {
                        pb.setConfidentPtmSites(strArr[0]);
                        pb.setNumberConfident(strArr[1]);
                    } else {
                        pb.setConfidentPtmSites("");
                        pb.setNumberConfident("");

                    }
                    String str2 = (importer.getIdentificationFeaturesGenerator().getSecondaryPTMSummary(proteinKey) + SEPARATOR);
                    String[] strArr2 = str2.split("\\|");
                    if (strArr2.length == 2) {
                        pb.setOtherPtmSites(strArr2[0]);
                        pb.setNumberOfOther(strArr2[1]);
                    } else {
                        pb.setOtherPtmSites("");
                        pb.setNumberOfOther("");
                    }
                } catch (Exception e) {
                    System.out.println("error: " + e.getLocalizedMessage() + SEPARATOR);
                }
                try {
                    pb.setNumberValidatedPeptides(importer.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    pb.setNumberValidatedSpectra(importer.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    pb.setEmPai(importer.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey, SpectrumCountingPreferences.SpectralCountingMethod.EMPAI));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    pb.setNsaf(importer.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey,
                            SpectrumCountingPreferences.SpectralCountingMethod.NSAF));
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                proteinList.put(pb.getAccession(), pb);


            }
          

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errorrr");
        }
        return proteinList;

    }

 
    
    public Map<Integer, PeptideBean> getPeptidesOutput() {

        final ArrayList<String> peptideKeys;


        Map<Integer, PeptideBean> peptideList = new HashMap<Integer, PeptideBean>();

        peptideKeys = identification.getPeptideIdentification();
        try {

            PSParameter peptidePSParameter = new PSParameter();
            PSParameter secondaryPSParameter = new PSParameter();
            HashMap<String, HashMap<Integer, String[]>> surroundingAAs = new HashMap<String, HashMap<Integer, String[]>>();
            ProteinMatch proteinMatch = null;
            ModificationProfile ptmProfile = importer.getSearchParameters().getModificationProfile();

            identification.loadPeptideMatches(progressDialog);
            identification.loadPeptideMatchParameters(peptidePSParameter, progressDialog);

            int index = 0;
            for (String peptideKey : peptideKeys) { // @TODO: replace by batch selection!!!                            
                PeptideBean pb = new PeptideBean();
                boolean shared = false;
                PeptideMatch peptideMatch = identification.getPeptideMatch(peptideKey);
                peptidePSParameter = (PSParameter) identification.getPeptideMatchParameter(peptideKey, peptidePSParameter);

                Peptide peptide = peptideMatch.getTheoreticPeptide();
                ArrayList<String> possibleProteins = new ArrayList<String>();
                ArrayList<String> orderedProteinsKeys = new ArrayList<String>(); // @TODO: could be merged with one of the other maps perhaps?

                for (String parentProtein : peptide.getParentProteins()) {
                    ArrayList<String> parentProteins = identification.getProteinMap().get(parentProtein);
                    if (parentProteins != null) {
                        for (String proteinKey1 : parentProteins) {
                            if (!possibleProteins.contains(proteinKey1)) {
                                try {
                                    proteinMatch = identification.getProteinMatch(proteinKey1);
                                    if (proteinMatch.getPeptideMatches().contains(peptideKey)) {
                                        possibleProteins.add(proteinKey1);
                                    }
                                } catch (Exception e) {
                                    // protein deleted due to protein inference issue and not deleted from the map in versions earlier than 0.14.6
                                    System.out.println("Non-existing protein key in protein map: " + proteinKey1);
                                }
                            }
                        }
                    }
                }
                shared = possibleProteins.size() > 1;
                proteinMatch = identification.getProteinMatch(possibleProteins.get(0));




                String mainMatch, secondaryProteins = "", peptideProteins = "";
                String mainMatchDescription, secondaryProteinsDescriptions = "", peptideProteinDescriptions = "";
                ArrayList<String> accessions = new ArrayList<String>();

                mainMatch = proteinMatch.getMainMatch();
                mainMatchDescription = sequenceFactory.getHeader(mainMatch).getDescription();
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
                        secondaryProteinsDescriptions += sequenceFactory.getHeader(key).getDescription();
                        orderedProteinsKeys.add(key);
                    }
                }

                if (shared) {
                    mainMatch = "shared peptide";
                    mainMatchDescription = "shared peptide";
                }

                first = true;
                ArrayList<String> peptideAccessions = new ArrayList<String>(peptide.getParentProteins());
                Collections.sort(peptideAccessions);
                for (String key : peptideAccessions) {
                    if (!accessions.contains(key)) {
                        if (first) {
                            first = false;
                        } else {
                            peptideProteins += ", ";
                            peptideProteinDescriptions += "; ";
                        }
                        peptideProteins += key;
                        peptideProteinDescriptions += sequenceFactory.getHeader(key).getDescription();
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
                    first = true;
                    for (int start : starts) {
                        if (first) {
                            first = false;
                        } else {
                            subSequence += "|";
                        }
                        subSequence += surroundingAAs.get(proteinAccession).get(start)[1];
                    }
                    subSequence += ";";
                }

                subSequence = subSequence.substring(0, subSequence.length() - 1);
                pb.setAaAfter(subSequence);
                //writer.write(subSequence + SEPARATOR);

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
                            start += ", ";
                            end += ", ";
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


                if (peptidePSParameter.isValidated()) {
                    pb.setValidated(1);
                } else {
                    pb.setValidated(0);
                }
                if (peptideMatch.isDecoy()) {
                    pb.setDecoy(1);
                } else {
                    pb.setDecoy(0);
                }

                pb.setStarred(peptidePSParameter.isStarred());


                peptideList.put(index, pb);
                index++;

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return peptideList;
    }

    public ExperimentBean getFractionsOutput(ExperimentBean exp) {
        // @TODO: add the non enzymatic peptides detected information!!
        // create final versions of all variables use inside the export thread
        final ArrayList<String> proteinKeys;
        proteinKeys = identification.getProteinIdentification();
        Map<Integer, FractionBean> fractionsList = new HashMap<Integer, FractionBean>();
        try {
            ArrayList<String> fractionFileNames = new ArrayList<String>();

            for (String fileName : importer.getIdentification().getOrderedSpectrumFileNames()) {
                
                fractionFileNames.add(fileName);
            }

            exp.setFractionsNumber(fractionFileNames.size());
            PSParameter proteinPSParameter = new PSParameter();
            PSParameter peptidePSParameter = new PSParameter();
            int proteinCounter = 0;

            identification.loadProteinMatches(progressDialog);
            identification.loadProteinMatchParameters(proteinPSParameter, progressDialog);
            for (int z = 1; z <= fractionFileNames.size(); z++) {
                FractionBean fb = new FractionBean();
                Map<String, ProteinBean> temProteinList = new HashMap<String, ProteinBean>();
                fb.setProteinList(temProteinList);
                fractionsList.put((z), fb);
                System.out.println("fractionFileNames.size()  "+fractionFileNames.size());
            }


            for (String proteinKey : proteinKeys) {
                proteinPSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, proteinPSParameter);
                ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey);
                ProteinBean pb = null;
                if (exp.getProteinList() != null) {
                    pb = exp.getProteinList().get(proteinKey);
                }
                if (pb == null) {
                    pb = new ProteinBean();
                    pb.setAccession(proteinMatch.getMainMatch());

                    String str = "";
                    boolean first = true;
                    for (String otherProtein : proteinMatch.getTheoreticProteinsAccessions()) {
                        if (!otherProtein.equals(proteinMatch.getMainMatch())) {
                            if (first) {
                                first = false;
                            } else {
                                str = str + (", ");
                            }
                            str = str + otherProtein;
                        }
                    }
                    pb.setOtherProteins(str);
                    pb.setProteinInferenceClass(proteinPSParameter.getProteinInferenceClassAsString());
                    try {
                        pb.setDescription(sequenceFactory.getHeader(proteinMatch.getMainMatch()).getDescription());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Double proteinMW = sequenceFactory.computeMolecularWeight(proteinMatch.getMainMatch());
                    pb.setMw_kDa(proteinMW);
                    try {
                        pb.setNumberValidatedPeptides(importer.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey));
                    } catch (Exception e) {
                        String d = "" + Double.NaN;
                        pb.setNumberValidatedPeptides(Integer.valueOf(d));
                        e.printStackTrace();
                    }
                    try {
                        pb.setNumberValidatedSpectra(importer.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey));
//                                                    writer.write(importer.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey) + SEPARATOR);
                    } catch (Exception e) {
                        String d = "" + Double.NaN;
                        pb.setNumberValidatedSpectra(Integer.valueOf(d));
                    }
                    try {
                        pb.setSequenceCoverage(importer.getIdentificationFeaturesGenerator().getSequenceCoverage(proteinKey) * 100);
                        pb.setObservableCoverage(importer.getIdentificationFeaturesGenerator().getObservableCoverage(proteinKey) * 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pb.setStarred(proteinPSParameter.isStarred());

                    ArrayList<String> peptideKeys = proteinMatch.getPeptideMatches();
                    Protein currentProtein = sequenceFactory.getProtein(proteinMatch.getMainMatch());
                    boolean allPeptidesEnzymatic = true;

                    identification.loadPeptideMatches(peptideKeys, null);
                    identification.loadPeptideMatchParameters(peptideKeys, peptidePSParameter, null);

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
                }
                double maxMwRangePeptides = Double.MIN_VALUE;
                double minMwRangePeptides = Double.MAX_VALUE;

                for (String fraction : fractionFileNames) {
                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                            && proteinPSParameter.getFractionValidatedPeptides(fraction) != null
                            && proteinPSParameter.getFractionValidatedPeptides(fraction) > 0) {

                        HashMap<String, XYDataPoint> expectedMolecularWeightRanges =
                                importer.getSearchParameters().getFractionMolecularWeightRanges();

                        if (expectedMolecularWeightRanges != null && expectedMolecularWeightRanges.get(fraction) != null) {

                            double lower = expectedMolecularWeightRanges.get(fraction).getX();
                            double upper = expectedMolecularWeightRanges.get(fraction).getY();

                            if (lower < minMwRangePeptides) {
                                minMwRangePeptides = lower;
                            }
                            if (upper > maxMwRangePeptides) {
                                maxMwRangePeptides = upper;
                            }
                        }
                    }
                }

                if (maxMwRangePeptides != Double.MIN_VALUE && minMwRangePeptides != Double.MAX_VALUE) {
                    pb.setPeptideFractionSpread_upper_range_kDa("" + maxMwRangePeptides);
                    pb.setPeptideFractionSpread_lower_range_kDa("" + minMwRangePeptides);
                } else {
                    pb.setPeptideFractionSpread_upper_range_kDa("" + "N/A");
                    pb.setPeptideFractionSpread_lower_range_kDa("" + "N/A");

                }

                double maxMwRangeSpectra = Double.MIN_VALUE;
                double minMwRangeSpectra = Double.MAX_VALUE;

                for (String fraction : fractionFileNames) {
                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                            && proteinPSParameter.getFractionValidatedSpectra(fraction) != null
                            && proteinPSParameter.getFractionValidatedSpectra(fraction) > 0) {

                        HashMap<String, XYDataPoint> expectedMolecularWeightRanges =
                                importer.getSearchParameters().getFractionMolecularWeightRanges();

                        if (expectedMolecularWeightRanges != null && expectedMolecularWeightRanges.get(fraction) != null) {

                            double lower = expectedMolecularWeightRanges.get(fraction).getX();
                            double upper = expectedMolecularWeightRanges.get(fraction).getY();

                            if (lower < minMwRangeSpectra) {
                                minMwRangeSpectra = lower;
                            }
                            if (upper > maxMwRangeSpectra) {
                                maxMwRangeSpectra = upper;
                            }
                        }
                    }
                }

                if (maxMwRangeSpectra != Double.MIN_VALUE && minMwRangeSpectra != Double.MAX_VALUE) {
                    pb.setSpectrumFractionSpread_lower_range_kDa("" + minMwRangeSpectra);
                    pb.setSpectrumFractionSpread_upper_range_kDa("" + maxMwRangeSpectra);
                } else {
                    pb.setSpectrumFractionSpread_lower_range_kDa("" + "N/A");
                    pb.setSpectrumFractionSpread_upper_range_kDa("" + "N/A");
                }



                int index = 1;
                for (String fraction : fractionFileNames) {
                    ProteinBean tempPb = new ProteinBean(pb);
                    FractionBean fb = fractionsList.get(index);
                    Map<String, ProteinBean> temProteinList = fb.getProteinList();
                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                            && proteinPSParameter.getFractionValidatedPeptides(fraction) != null) {
                        tempPb.setNumberOfPeptidePerFraction(proteinPSParameter.getFractionValidatedPeptides(fraction));
        //System.out.println("  tempPb.setNumberOfPeptidePerFraction(  "+tempPb.getNumberOfPeptidePerFraction());
                          //writer.write(proteinPSParameter.getFractionValidatedPeptides(fraction) + SEPARATOR);
                    } else {
         //               System.out.println("  tempPb.setNumberOfPeptidePerFraction(  "+tempPb.getNumberOfPeptidePerFraction());
                        tempPb.setNumberOfPeptidePerFraction(0);
System.out.println("  tempPb.setNumberOfPeptidePerFraction(  "+tempPb.getNumberOfPeptidePerFraction());
                        
                        //  writer.write("0.0" + SEPARATOR);
                    }

                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                            && proteinPSParameter.getFractionValidatedSpectra(fraction) != null) {
                        tempPb.setNumberOfSpectraPerFraction(proteinPSParameter.getFractionValidatedSpectra(fraction));
                        // writer.write(proteinPSParameter.getFractionValidatedSpectra(fraction) + SEPARATOR);
                    } else {
                        tempPb.setNumberOfSpectraPerFraction(0);
                        //  writer.write("0.0" + SEPARATOR);
                    }

                    if (proteinPSParameter.getFractions() != null && proteinPSParameter.getFractions().contains(fraction)
                            && proteinPSParameter.getPrecursorIntensityAveragePerFraction(fraction) != null) {
                        tempPb.setAveragePrecursorIntensityPerFraction(proteinPSParameter.getPrecursorIntensityAveragePerFraction(fraction));
                        // writer.write(proteinPSParameter.getPrecursorIntensityAveragePerFraction(fraction) + SEPARATOR);
                    } else {
                        tempPb.setAveragePrecursorIntensityPerFraction(0.0);
                        //  writer.write("0.0" + SEPARATOR);
                    }
                    temProteinList.put(tempPb.getAccession(), tempPb);
                    fb.setProteinList(temProteinList);
                    fractionsList.put((index), fb);
                    index++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
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
                result.append(", ");
            }
            first2 = true;
            result.append(mod);
            result.append(" (");
            for (int aa : modMap.get(mod)) {
                if (first2) {
                    first2 = false;
                } else {
                    result.append(", ");
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
                result += ", ";
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
        } catch (Exception e) {
            e.printStackTrace();
            //ignore caching error
        }
        for (int i = 0; i < spectrumKeys.size(); i++) {
            try {
                int tempCharge = importer.getIdentification().getSpectrumMatch(spectrumKeys.get(i)).getBestAssumption().getIdentificationCharge().value;

                if (!charges.contains(tempCharge)) {
                    charges.add(tempCharge);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        // sort the charges
        Collections.sort(charges);

        // add the charges to the output
        for (int i = 0; i < charges.size(); i++) {
            if (i > 0) {
                results.append(", ");
            }

            results.append(charges.get(i));
        }

        return results.toString();
    }
}
