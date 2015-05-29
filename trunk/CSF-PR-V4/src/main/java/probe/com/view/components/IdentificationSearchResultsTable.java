/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Table;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.CustomPI;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationSearchResultsTable extends Table implements Serializable {

    private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;

    public IdentificationSearchResultsTable(Map<Integer, DatasetDetailsBean> datasetDetailsList, Map<Integer, IdentificationProteinBean> fullExpProtList) {

        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.setHeight("150px");

        this.addContainerProperty("Index", Integer.class, null, "", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("Experiment", String.class, null);
        this.addContainerProperty("Accession", CustomExternalLink.class, null);

        this.addContainerProperty("Species", String.class, null);
        this.addContainerProperty("Sample Type", String.class, null);

        this.addContainerProperty("Sample Processing", String.class, null);
        this.addContainerProperty("Instrument Type", String.class, null);
        this.addContainerProperty("Frag. Mode", String.class, null);

        String Protein_Inference = "Protein Inference";
        this.addContainerProperty(Protein_Inference, CustomPI.class, null, "PI", null, com.vaadin.ui.Table.ALIGN_CENTER);

        this.addContainerProperty("Other Protein(s)", String.class, null);
        this.addContainerProperty("Description", String.class, null);
        this.addContainerProperty("Sequence Coverage(%)", Double.class, null, "Coverage(%)", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("# Validated Peptides", Integer.class, null, "#Peptides", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("# Validated Spectra", Integer.class, null, "#Spectra", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("NSAF", Double.class, null, "NSAF", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("MW", Double.class, null, "MW", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        String Confidence = "Confidence";
        this.addContainerProperty(Confidence, Double.class, null, Confidence, null, com.vaadin.ui.Table.ALIGN_RIGHT);

        CustomExternalLink link = null;
        String dataset = null;
        CustomPI pi = null;
        Resource res2 = null;

        int index = 1;
        df = new DecimalFormat("#.##", otherSymbols);

        for (int key : fullExpProtList.keySet()) {
            IdentificationProteinBean pb = fullExpProtList.get(key);
            DatasetDetailsBean datasetDetails = datasetDetailsList.get(pb.getDatasetId());
            if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("SINGLE PROTEIN")) {
                res2 = new ThemeResource("img/green.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("UNRELATED PROTEINS")) {
                res2 = new ThemeResource("img/red.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS")) {
                res2 = new ThemeResource("img/yellow.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("UNRELATED ISOFORMS") || pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)")) {
                res2 = new ThemeResource("img/orange.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("Related Proteins")) {
                res2 = new ThemeResource("img/yellow.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("RELATED AND UNRELATED PROTEINS")) {
                res2 = new ThemeResource("img/red.jpg");
            }

            pi = new CustomPI(pb.getProteinInferenceClass(), res2);
            pi.setDescription(pb.getProteinInferenceClass());
            link = new CustomExternalLink(pb.getAccession(), "http://www.uniprot.org/uniprot/" + pb.getAccession());
            link.setDescription("UniProt link for " + pb.getAccession());

            dataset = datasetDetails.getName();
            this.addItem(new Object[]{index, dataset, link, datasetDetails.getSpecies(), datasetDetails.getSampleType(), datasetDetails.getSampleProcessing(), datasetDetails.getInstrumentType(), datasetDetails.getFragMode(), pi, pb.getOtherProteins(), pb.getDescription(), Double.valueOf(df.format(pb.getSequenceCoverage())), pb.getNumberValidatedPeptides(), pb.getNumberValidatedSpectra(), Double.valueOf(df.format(pb.getNsaf())), Double.valueOf(df.format(pb.getMw_kDa())), Double.valueOf(df.format(pb.getConfidence()))}, index);
            index++;
        }

        for (Object propertyId : this.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals("Description")) {
                setColumnExpandRatio(propertyId, 4.0f);
            } else {
                setColumnExpandRatio(propertyId.toString(), 0.5f);
            }

        }
        this.sort(new String[]{Confidence, "# Validated Peptides"}, new boolean[]{false, false});

        setColumnWidth("Experiment", 115);
        setColumnWidth("Index", 33);
        setColumnWidth("Accession", 60);
        setColumnWidth("Sequence Coverage(%)", 75);

        setColumnWidth("Species", 45);
        setColumnWidth("Sample Type", 73);
        setColumnWidth("Sample Processing", 90);
        setColumnWidth("Frag. Mode", 65);

        setColumnWidth("Instrument Type", 80);

        setColumnWidth(Protein_Inference, 33);
        setColumnWidth("# Validated Peptides", 58);
        setColumnWidth("# Validated Spectra", 55);
        setColumnWidth("NSAF", 35);
        setColumnWidth(Confidence, 65);
        setColumnWidth("MW", 55);
        for (Object propertyId : this.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals("Other Protein(s)")) {
                setColumnExpandRatio(propertyId, 1.0f);
            }
        }

        int indexing = 1;
        for (Object id : this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;

        }
    }
}
