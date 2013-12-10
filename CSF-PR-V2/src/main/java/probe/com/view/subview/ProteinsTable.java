package probe.com.view.subview;

import com.vaadin.data.Item;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import probe.com.model.beans.ProteinBean;
import probe.com.view.subview.util.CustomEmbedded;
import probe.com.view.subview.util.CustomExternalLink;
import probe.com.view.subview.util.CustomPI;

public class ProteinsTable extends Table implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DecimalFormat df = null;
    private Map<String, Integer> tableSearchMap = new HashMap<String, Integer>();
    private Map<String, Integer> tableSearchMapIndex = new HashMap<String, Integer>();

    private int firstIndex;
    public ProteinsTable(Map<String, ProteinBean> proteinsList, int fractionNumber) {

        Map<String,Integer> rankMap = initRank(proteinsList);
        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.setHeight("160px");


        this.addContainerProperty("Index", Integer.class, null, "", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        String Protein_Inference = "Protein Inference";
        this.addContainerProperty(Protein_Inference, CustomPI.class, null, "PI", null, com.vaadin.ui.Table.ALIGN_CENTER);

        this.addContainerProperty("Accession", CustomExternalLink.class, null);

        this.addContainerProperty("Other Protein(s)", String.class, null);
        this.setColumnCollapsed("Other Protein(s)", true);

        this.addContainerProperty("Description", String.class, null);
        this.addContainerProperty("Chr", String.class, null, "CHROMOSOME", null, com.vaadin.ui.Table.ALIGN_RIGHT);

        this.addContainerProperty("Gene Name", String.class, null);
//        this.setColumnCollapsed("Gene Name", true);

        this.addContainerProperty("Sequence Coverage(%)", Double.class, null, "Coverage(%)", null, com.vaadin.ui.Table.ALIGN_CENTER);
        this.addContainerProperty("Non Enzymatic Peptides", CustomEmbedded.class, null, "Non Enzymatic Peptides", null, com.vaadin.ui.Table.ALIGN_CENTER);
        this.addContainerProperty("# Validated Peptides", Integer.class, null, "#Peptides", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("# Validated Spectra", Integer.class, null, "#Spectra", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("NSAF", Double.class, null, "NSAF", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("RANK", Integer.class, null, "RANK", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        this.addContainerProperty("MW", Double.class, null, "MW", null, com.vaadin.ui.Table.ALIGN_RIGHT);
        String Confidence = "Confidence";
        this.addContainerProperty(Confidence, Double.class, null, Confidence, null, com.vaadin.ui.Table.ALIGN_CENTER);
        if (fractionNumber > 0) {
            this.addContainerProperty("SpectrumFractionSpread lower range_kDa", Double.class, null, "Spectrum Lower Range", null, com.vaadin.ui.Table.ALIGN_RIGHT);
            this.addContainerProperty("SpectrumFractionSpread upper range kDa", Double.class, null, "Spectrum Upper Range", null, com.vaadin.ui.Table.ALIGN_RIGHT);
            this.addContainerProperty("PeptideFractionSpread lower range kDa", Double.class, null, "Peptide Lower Range", null, com.vaadin.ui.Table.ALIGN_RIGHT);
            this.addContainerProperty("PeptideFractionSpread upper range kDa", Double.class, null, "Peptide Upper Range", null, com.vaadin.ui.Table.ALIGN_RIGHT);
            this.setColumnCollapsed("SpectrumFractionSpread lower range_kDa", true);
            this.setColumnCollapsed("SpectrumFractionSpread upper range kDa", true);
            this.setColumnCollapsed("PeptideFractionSpread lower range kDa", true);
            this.setColumnCollapsed("PeptideFractionSpread upper range kDa", true);

        }
        this.addContainerProperty("Validated", CustomEmbedded.class, null, "Validated", null, com.vaadin.ui.Table.ALIGN_CENTER);

        /* Add a few items in the table. */

        int index = 1;

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        CustomExternalLink link = null;
        CustomEmbedded nonEnz = null;
        CustomEmbedded validated = null;
        Resource res = null;
        Double d1 = null;
        Double d2 = null;
        Double d3 = null;
        Double d4 = null;
        CustomPI pi = null;
        Resource res2 = null;
        Resource res3 = null;
        for (ProteinBean pb : proteinsList.values()) {

            link = new CustomExternalLink(pb.getAccession(), "http://www.uniprot.org/uniprot/" + pb.getAccession());
            link.setDescription("UniProt link for " + pb.getAccession());

            if (pb.isNonEnzymaticPeptides()) {
                res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
            } else {
                res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
            }


            if (pb.isValidated()) {
                res3 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
            } else {
                res3 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
            }



            nonEnz = new CustomEmbedded(pb.isNonEnzymaticPeptides(), res);
            nonEnz.setWidth("16px");
            nonEnz.setHeight("16px");
            nonEnz.setDescription("" + pb.isNonEnzymaticPeptides());

            validated = new CustomEmbedded(pb.isValidated(), res3);
            validated.setWidth("16px");
            validated.setHeight("16px");
            validated.setDescription("" + pb.isValidated());


            if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("SINGLE PROTEIN")) {
                res2 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-snc6/263426_116594491857485_1503571748_n.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("UNRELATED PROTEINS")) {
                res2 = new ExternalResource("http://sphotos-h.ak.fbcdn.net/hphotos-ak-prn1/549354_116594531857481_1813966302_n.jpg");
            } else if (pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS")) {
                res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("UNRELATED ISOFORMS") || pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)")) {
                res2 = new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
            } else if (pb.getProteinInferenceClass().trim().equalsIgnoreCase("Related Proteins")) {
                res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
                pi = new CustomPI(pb.getProteinInferenceClass(), res2);
                pi.setDescription(pb.getProteinInferenceClass());

            }

            pi = new CustomPI(pb.getProteinInferenceClass(), res2);
            pi.setDescription(pb.getProteinInferenceClass());
            try {
                d1 = Double.valueOf(pb.getSpectrumFractionSpread_lower_range_kDa());
            } catch (Exception nfx) {
                d1 = null;
            }
            try {
                d2 = Double.valueOf(pb.getSpectrumFractionSpread_upper_range_kDa());
            } catch (Exception nfx) {
                d2 = null;
            }

            try {
                d3 = Double.valueOf(pb.getPeptideFractionSpread_lower_range_kDa());
            } catch (Exception nfx) {
            }
            try {
                d4 = Double.valueOf(pb.getPeptideFractionSpread_upper_range_kDa());
            } catch (Exception nfx) {
            }


            int rank = rankMap.get(pb.getAccession()+","+pb.getOtherProteins());
            if (fractionNumber <= 0) {
                this.addItem(new Object[]{index, pi, link, pb.getOtherProteins(), pb.getDescription(), pb.getChromosomeNumber(), pb.getGeneName(), Double.valueOf(df.format(pb.getSequenceCoverage())), nonEnz, pb.getNumberValidatedPeptides(), pb.getNumberValidatedSpectra(), Double.valueOf(df.format(pb.getNsaf())),rank, Double.valueOf(df.format(pb.getMw_kDa())), Double.valueOf(df.format(pb.getConfidence())), validated}, new Integer(index));
            } else {
                this.addItem(new Object[]{index, pi, link, pb.getOtherProteins(), pb.getDescription(), pb.getChromosomeNumber(), pb.getGeneName(), Double.valueOf(df.format(pb.getSequenceCoverage())), nonEnz, pb.getNumberValidatedPeptides(), pb.getNumberValidatedSpectra(), Double.valueOf(df.format(pb.getNsaf())),rank, Double.valueOf(df.format(pb.getMw_kDa())), Double.valueOf(df.format(pb.getConfidence())), d1, d2, d3, d4, validated}, new Integer(index));
            }

            index++;

        }
        this.sort(new String[]{Confidence, "# Validated Peptides"}, new boolean[]{false, false});

        this.setSortAscending(false);
        for (Object propertyId : this.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals("Description")) {
                setColumnExpandRatio(propertyId, 4.0f);
            } else {
                setColumnExpandRatio(propertyId.toString(), 0.5f);
            }
        }
        setColumnWidth("Chr", 35);
        setColumnWidth("Index", 35);
        setColumnWidth("Validated", 35);
        setColumnWidth(Protein_Inference, 35);
        setColumnWidth(Confidence, 35);
        
        TreeMap<Integer, String> sortMap = new TreeMap<Integer, String>();
        int indexing = 1;
        for (Object id : this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            if(indexing == 1)
                firstIndex = (Integer)id;
            sortMap.put(indexing, item.getItemProperty("Accession").getValue().toString().toUpperCase().trim() + "," + item.getItemProperty("Other Protein(s)").getValue().toString().toUpperCase().trim() + "," + item.getItemProperty("Description").getValue().toString().toUpperCase().trim() + "," + (Integer) id);
            indexing++;
        }
        tableSearchMap.clear();
        tableSearchMapIndex.clear();
        for (int key = 1; key <= sortMap.size(); key++) {
            String str = sortMap.get(key);
            int itemIndex = Integer.valueOf(str.split(",")[str.split(",").length - 1]);
            tableSearchMap.put(str, itemIndex);
            tableSearchMapIndex.put(str, key);
        }

//        this.setItemDescriptionGenerator(new ItemDescriptionGenerator() {
//            private static final long serialVersionUID = 6268199275509867378L;
//
//            @Override
//            public String generateDescription(Component source, Object itemId, Object propertyId) {
//                if (propertyId == null) {
//                } else if (propertyId.equals("Accession")) {
//                    return "Accession";
//                } else if (propertyId.equals("SpectrumFractionSpread_lower_range_kDa")) {
//                    return "Spectrum Lower Range (kDa)";
//                } else if (propertyId.equals("SpectrumFractionSpread_upper_range_kDa")) {
//                    return "Spectrum Upper Range (kDa)";
//                } else if (propertyId.equals("PeptideFractionSpread_lower_range_kDa")) {
//                    return "Peptide lowerrange (kDa)";
//                } else if (propertyId.equals("PeptideFractionSpread_upper_range_kDa")) {
//                    return "Peptide Upper Range (kDa)";
//                } else if (propertyId.equals("Non Enzymatic_Peptides")) {
//                    return "Non Enzymatic_Peptides";
//                } else if (propertyId.equals("Protein Inference")) {
//                    return "Protein Inference";
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
//                } else if (propertyId.equals("NSAF")) {
//                    return "NSAF";
//                } else if (propertyId.equals("# Validated Spectra")) {
//                    return "# Validated Spectra";
//                } else if (propertyId.equals("MW")) {
//                    return "MW";
//                } else if (propertyId.equals("Confidence")) {
//                    return "Confidence";
//                } else if (propertyId.equals("Chr")) {
//                    return "Chromosome Number";
//                }
//
//                return null;
//            }
//        });
//
    }

    public Map<String, Integer> getTableSearchMap() {
        return tableSearchMap;
    }

    public Map<String, Integer> getTableSearchMapIndex() {
        return tableSearchMapIndex;
    }

    public void setTableSearchMapIndex(Map<String, Integer> tableSearchMapIndex) {
        this.tableSearchMapIndex = tableSearchMapIndex;
    }
    private  Map<String,Integer> initRank(Map<String, ProteinBean> proteinsList)
    {
        List<ProteinBean> protList = new ArrayList<ProteinBean>();
        Map<String,Integer> rankMap = new TreeMap<String, Integer>();
        protList.addAll(proteinsList.values());
        Collections.sort(protList);
        double currentNsaf = -1;
        int rank = 0;
        for(int index=(protList.size()-1);index >=0;index--)            
        {
            ProteinBean pb = protList.get(index);
            if(currentNsaf != pb.getNsaf())
                rank++;
           
            rankMap.put(pb.getAccession()+","+pb.getOtherProteins(),rank);
            currentNsaf = pb.getNsaf();       
           
        }
//////        for(String str :rankMap.keySet())
//////        {
//////            System.out.println("prot key is "+str+"  prot Rank "+rankMap.get(str));
//////        }
//////            System.out.println(proteinsList.keySet());
         
        return rankMap;
    
    }

    public int getFirstIndex() {
        return firstIndex;
    }
}