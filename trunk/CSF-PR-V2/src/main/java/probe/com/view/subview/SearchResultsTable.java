/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.data.Item;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Table;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.subview.util.CustomExternalLink;
import probe.com.view.subview.util.CustomInternalLink;
import probe.com.view.subview.util.CustomPI;

/**
 *
 * @author Yehia Farag
 */
public class SearchResultsTable extends Table implements Serializable {

    private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;

    public SearchResultsTable(Map<Integer, ExperimentBean> expList, Map<Integer, ProteinBean> fullExpProtList) {

        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.setHeight("150px");

        this.addContainerProperty("Index", Integer.class, null, "", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("Experiment", CustomInternalLink.class, null);
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
        this.addContainerProperty(Confidence, Double.class, null, Confidence, null, com.vaadin.ui.Table.ALIGN_CENTER);

        CustomExternalLink link = null;
        CustomInternalLink experimentLink = null;
        CustomPI pi = null;
        ExternalResource res2 = null;

        int index = 1;
        df = new DecimalFormat("#.##", otherSymbols);

        for (int key : fullExpProtList.keySet()) {
            ProteinBean pb = fullExpProtList.get(key);
            ExperimentBean exp = expList.get(pb.getExpId());
            if (pb.getProteinInferenceClass().equalsIgnoreCase("SINGLE PROTEIN")) {
                res2 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-snc6/263426_116594491857485_1503571748_n.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("UNRELATED PROTEINS")) {
                res2 = new ExternalResource("http://sphotos-h.ak.fbcdn.net/hphotos-ak-prn1/549354_116594531857481_1813966302_n.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS")) {
                res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("UNRELATED ISOFORMS") || pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)")) {
                res2 = new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("Related Proteins")) {
                res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
                pi = new CustomPI(pb.getProteinInferenceClass(), res2);
                pi.setDescription(pb.getProteinInferenceClass());

            }
            pi = new CustomPI(pb.getProteinInferenceClass(), res2);
            pi.setDescription(pb.getProteinInferenceClass());
            link = new CustomExternalLink(pb.getAccession(), "http://www.uniprot.org/uniprot/" + pb.getAccession());
            link.setDescription("UniProt link for " + pb.getAccession());

            experimentLink = new CustomInternalLink(exp.getName(), key);
            experimentLink.setDescription("VIEW " + exp.getName() + " DETAILS");
            this.addItem(new Object[]{index, experimentLink, link, exp.getSpecies(), exp.getSampleType(), exp.getSampleProcessing(), exp.getInstrumentType(), exp.getFragMode(), pi, pb.getOtherProteins(), pb.getDescription(), Double.valueOf(df.format(pb.getSequenceCoverage())), pb.getNumberValidatedPeptides(), pb.getNumberValidatedSpectra(), Double.valueOf(df.format(pb.getNsaf())), Double.valueOf(df.format(pb.getMw_kDa())), Double.valueOf(df.format(pb.getConfidence()))}, new Integer(index));
            index++;
        }

        for (Object propertyId : this.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals("Description")) {
                setColumnExpandRatio(propertyId, 4.0f);
            } else {
                setColumnExpandRatio(propertyId.toString(), 0.5f);
            }

        }
        setColumnWidth("Index", 35);
        this.setSortContainerPropertyId(Confidence);
        this.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;

        }
//        this.setItemDescriptionGenerator(new ItemDescriptionGenerator() {
//            private static final long serialVersionUID = 6268199275509867378L;
//
//            @Override
//            public String generateDescription(Component source, Object itemId, Object propertyId) {
//                if (propertyId == null) {
//                } else if (propertyId.equals("Experiment")) {
//                    return "Experiment";
//                } else if (propertyId.equals("Accession")) {
//                    return "Accession";
//                } else if (propertyId.equals("Sample Type")) {
//                    return "Sample Type";
//                } else if (propertyId.equals("Species")) {
//                    return "Species";
//                } else if (propertyId.equals("Sample Processing")) {
//                    return "Sample Processing";
//                } else if (propertyId.equals("Instrument Type")) {
//                    return "Instrument Type";
//                } else if (propertyId.equals("Other Protein(s)")) {
//                    return "Other Protein(s)";
//                } else if (propertyId.equals("Description")) {
//                    return "Description";
//                } else if (propertyId.equals("Sequence Coverage(%)")) {
//                    return "Sequence Coverage(%)";
//                } else if (propertyId.equals("# Validated Peptides")) {
//                    return "# Validated Peptides";
//                } else if (propertyId.equals("# Validated Spectra")) {
//                    return "# Validated Spectra";
//                } else if (propertyId.equals("MW")) {
//                    return "MW";
//                } else if (propertyId.equals("Confidence")) {
//                    return "Confidence";
//                } else if (propertyId.equals("NSAF")) {
//                    return "NSAF";
//                } else if (propertyId.equals("Protein Inference")) {
//                    return "Protein Inference";
//                } else if (propertyId.equals("Frag. Mode")) {
//                    return "Frag. Mode";
//                }
//                return null;
//            }
//        });
//
//
    }
}
