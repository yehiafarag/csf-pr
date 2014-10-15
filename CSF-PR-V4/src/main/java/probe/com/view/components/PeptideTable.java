package probe.com.view.components;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Table;
import probe.com.model.beans.PeptideBean;
import probe.com.view.core.CustomEmbedded;
import probe.com.view.core.CustomLabel;
import probe.com.view.core.CustomPI;

/**
 * @author Yehia Farag
     * initialize and create peptides table
     */
public class PeptideTable extends Table implements Serializable {

    private static final long serialVersionUID = 1L;
    private DecimalFormat df = null;

    public PeptideTable(Map<Integer, PeptideBean> peptideList, Set<String> pepSet, boolean isExporter, String mainProtDesc) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        this.setSelectable(false);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.setHeight("167px");
        this.addContainerProperty("Index", Integer.class, null, "", null, com.vaadin.ui.Table.ALIGN_RIGHT);

        String Protein_Inference = "Protein Inference";
        this.addContainerProperty(Protein_Inference, CustomPI.class, null, "PI", null, com.vaadin.ui.Table.ALIGN_CENTER);

        this.addContainerProperty("Peptide Protein(s)", String.class, null);
        this.setColumnCollapsed("Peptide Protein(s)", true);

        final String sequence = "Sequence";
        this.addContainerProperty(sequence, CustomLabel.class, null);
        this.addContainerProperty("AA Before", String.class, null);
        this.addContainerProperty("AA After", String.class, null);
        this.setColumnCollapsed("AA Before", true);
        this.setColumnCollapsed("AA After", true);
        this.addContainerProperty("Peptide Start", String.class, null, "Start", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("Peptide End", String.class, null, "End", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.setColumnCollapsed("Peptide Start", true);
        this.setColumnCollapsed("Peptide End", true);

        this.addContainerProperty("# Validated Spectra", Integer.class, null, "#Spectra", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("Other Protein(s)", String.class, null);
        this.addContainerProperty("Other Prot Descrip.", String.class, null);

        this.setColumnCollapsed("Other Protein(s)", true);

        this.addContainerProperty("Peptide Prot. Descrip.", String.class, null);
        this.setColumnCollapsed("Peptide Prot. Descrip.", true);
        this.addContainerProperty("Variable Modification", String.class, null);
        this.addContainerProperty("Location Confidence", String.class, null);
        this.setColumnCollapsed("Variable Modification", true);
        this.setColumnCollapsed("Location Confidence", true);

        this.addContainerProperty("Precursor Charge(s)", String.class, null, "Precursor Charge(s)", null, com.vaadin.ui.Table.ALIGN_RIGHT);

        this.addContainerProperty("Enzymatic", CustomEmbedded.class, null, "Enzymatic", null, com.vaadin.ui.Table.ALIGN_CENTER);

        this.addContainerProperty("Sequence Tagged", String.class, null, "Sequence Annotated", null, com.vaadin.ui.Table.ALIGN_LEFT);
        this.addContainerProperty("Deamidation & Glycopattern", CustomEmbedded.class, null, "Glycopeptide", null, com.vaadin.ui.Table.ALIGN_CENTER);
        this.addContainerProperty("Glycopattern Positions", String.class, null, "Glyco Position(s)", null, com.vaadin.ui.Table.ALIGN_RIGHT);

        String Confidence = "Confidence";
        this.addContainerProperty(Confidence, Double.class, null, Confidence, null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("Validated", CustomEmbedded.class, null, "Validated", null, com.vaadin.ui.Table.ALIGN_CENTER);

        CustomEmbedded enz = null;
        Resource res = null;
        CustomPI pi = null;
        Resource res2 = null;
        Resource res3 = null;
        CustomLabel seq = null;
        CustomEmbedded deamidationAndGlycopattern = null;

        CustomEmbedded validated = null;
        int index = 1;
        for (PeptideBean pb : peptideList.values()) {
            if (pb.isEnzymatic()) {
                res = new ThemeResource("img/true.jpg");//new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
            } else {
                res = new ThemeResource("img/false.jpg");//new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
            }

            enz = new CustomEmbedded(pb.isEnzymatic(), res);
            enz.setWidth("16px");
            enz.setHeight("16px");
            enz.setDescription("" + pb.isEnzymatic());

            if (pb.isDeamidationAndGlycopattern() == null) {
                res3 = new ThemeResource("img/false.jpg");//new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
                deamidationAndGlycopattern = new CustomEmbedded(false, res3);
                deamidationAndGlycopattern.setWidth("16px");
                deamidationAndGlycopattern.setHeight("16px");
                deamidationAndGlycopattern.setDescription("FALSE");
            } else if (pb.isDeamidationAndGlycopattern()) {
                res3 = new ThemeResource("img/true.jpg");//new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
                deamidationAndGlycopattern = new CustomEmbedded(pb.isDeamidationAndGlycopattern(), res3);
                deamidationAndGlycopattern.setWidth("16px");
                deamidationAndGlycopattern.setHeight("16px");
                deamidationAndGlycopattern.setDescription("" + pb.isDeamidationAndGlycopattern());
            } else {
                res3 = new ThemeResource("img/false.jpg");// new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");

                deamidationAndGlycopattern = new CustomEmbedded(pb.isDeamidationAndGlycopattern(), res3);
                deamidationAndGlycopattern.setWidth("16px");
                deamidationAndGlycopattern.setHeight("16px");
                deamidationAndGlycopattern.setDescription("" + pb.isDeamidationAndGlycopattern());

            }
            if (pb.getProteinInference() == null) {

            } else if (pb.getProteinInference().equalsIgnoreCase("SINGLE PROTEIN")) {
                res2 = new ThemeResource("img/green.jpg");
                pi = new CustomPI(pb.getProteinInference(), res2);
                pi.setDescription(pb.getProteinInference());
                //add peptides protein and desc value in case of single protein
                pb.setPeptideProteins(pb.getProtein());
                if (mainProtDesc != null && !mainProtDesc.equalsIgnoreCase("")) {
                    pb.setPeptideProteinsDescriptions(mainProtDesc);
                }

            } else if (pb.getProteinInference().trim().equalsIgnoreCase("UNRELATED PROTEINS")) {
                res2 = new ThemeResource("img/red.jpg");
                pi = new CustomPI(pb.getProteinInference(), res2);
                pi.setDescription(pb.getProteinInference());

            } else if (pb.getProteinInference().trim().equalsIgnoreCase("Related Proteins")) {
                res2 = new ThemeResource("img/yellow.jpg");
                pi = new CustomPI(pb.getProteinInference(), res2);
                pi.setDescription(pb.getProteinInference());
                if (!pb.getProtein().equalsIgnoreCase("SHARED PEPTIDE")) {
                    if(pb.getOtherProteins() != null && !pb.getOtherProteins().equalsIgnoreCase("")){
                    pb.setPeptideProteins(pb.getProtein() + "," + pb.getOtherProteins());
                    if (mainProtDesc != null && !mainProtDesc.equalsIgnoreCase("")) {
                        pb.setPeptideProteinsDescriptions(mainProtDesc + ";" + pb.getOtherProteinDescriptions());
                    }
                    }else {
                         pb.setPeptideProteins(pb.getProtein() + "," + pb.getPeptideProteins());
                    if (mainProtDesc != null && !mainProtDesc.equalsIgnoreCase("")) {
                        pb.setPeptideProteinsDescriptions(mainProtDesc + ";" + pb.getPeptideProteinsDescriptions());
                    }
                    
                    }
                }

            } else if (pb.getProteinInference().trim().equalsIgnoreCase("UNRELATED ISOFORMS") || pb.getProteinInference().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)")) {
                res2 = new ThemeResource("img/orange.jpg");//new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
                pi = new CustomPI(pb.getProteinInference(), res2);
                pi.setDescription(pb.getProteinInference());
            } else {

                res2 = new ThemeResource("img/red.jpg");//new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
                pi = new CustomPI(pb.getProteinInference(), res2);
                pi.setDescription(pb.getProteinInference());

            }
            boolean valid = false;

            if (pb.getValidated() == 1.0) {
                valid = true;
                res2 = new ThemeResource("img/true.jpg");//new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
            } else {
                res2 = new ThemeResource("img/false.jpg");//new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
            }

            validated = new CustomEmbedded(valid, res2);
            validated.setDescription(String.valueOf(pb.getValidated()));

            if (pepSet != null) {
                for (String str : pepSet) {
                    if (pb.getSequence().contains(str)) {
                        seq = new CustomLabel(pb.getSequence(), "red");
                        break;
                    }
                    seq = new CustomLabel(pb.getSequence(), "black");
                }
            } else {
                seq = new CustomLabel(pb.getSequence(), "black");
            }
            if (seq != null) {
                seq.setDescription("The Peptide Sequence: " + pb.getSequence());
            }

            this.addItem(new Object[]{index, pi, pb.getPeptideProteins(), seq, pb.getAaBefore(), pb.getAaAfter(), pb.getPeptideStart(), pb.getPeptideEnd(), pb.getNumberOfValidatedSpectra(),
                pb.getOtherProteins(), pb.getOtherProteinDescriptions(), pb.getPeptideProteinsDescriptions(),
                pb.getVariableModification(), pb.getLocationConfidence(), pb.getPrecursorCharges(), enz, pb.getSequenceTagged(), deamidationAndGlycopattern, pb.getGlycopatternPositions(), Double.valueOf(df.format(pb.getConfidence())), validated}, index);
            index++;
        }
        this.sort(new String[]{Confidence, "# Validated Spectra"}, new boolean[]{false, false});
        this.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

        setColumnWidth("Index", 33);
        setColumnWidth("Protein_Inference", 33);
        setColumnWidth("# Validated Spectra", 55);
        setColumnWidth("Other Prot Descrip.", 110);
        setColumnWidth("Precursor Charge(s)", 120);
        setColumnWidth("Enzymatic", 55);
        setColumnWidth("Deamidation & Glycopattern", 80);
        setColumnWidth("Glycopattern Positions", 85);
        setColumnWidth(Confidence, 65);
        setColumnWidth("Validated", 55);
        for (Object propertyId : this.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals(sequence)) {
                setColumnExpandRatio(propertyId, 0.4f);
            } else if (propertyId.toString().equals("Sequence Tagged")) {
                setColumnExpandRatio(propertyId.toString(), 0.6f);
            }
        }

    }
}
