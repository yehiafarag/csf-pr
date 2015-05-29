/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import probe.com.view.core.SingleDatasetPublicationLayout;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.model.beans.PublicationRoot;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.core.InformationField;
import probe.com.view.core.ListGridCell;
import probe.com.view.core.PublicationRow;

/**
 * Publication view level is used for visualizing publication data on exploring dataset layout
 * @author YEhia Farag
 */
public class PublicationExplorerTreeLayout extends VerticalLayout implements CSFFilter {

    private final DatasetExploringSelectionManagerRes exploringFiltersManager;
    private final VerticalLayout mainbodyLayout = new VerticalLayout();
    private final HorizontalLayout treeContainerLayout = new HorizontalLayout();
    private final String[] headers = new String[27];
    private final HideOnClickLayout layout;
//    private Panel publicationTreePanel;
//    private Panel infoLayoutPanel;
    private VerticalLayout publicationLayout;

    public PublicationExplorerTreeLayout(QuantDatasetObject[] datasets,DatasetExploringSelectionManagerRes exploringFiltersManager) {

        headers[2] = "#ID:";
        headers[3] = "#Quant:";
        headers[19] = "#PGr1:";
        headers[23] = "#PGr2:";

        layout = new HideOnClickLayout("Publications Overview", mainbodyLayout, null);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.exploringFiltersManager = exploringFiltersManager;
        this.addComponent(layout);
        mainbodyLayout.setWidth("100%");
        mainbodyLayout.setMargin(false);
//        exploringFiltersManager.getFilteredDatasetsList()
        this.updateDatasetsNodes(exploringFiltersManager.getFilteredDatasetsList());
        mainbodyLayout.addComponent(treeContainerLayout);
        treeContainerLayout.setWidth("100%");
        initPublicationTreeLayout();
        updatePublicationTree();
        treeContainerLayout.addComponent(publicationLayout);
        treeContainerLayout.setComponentAlignment(publicationLayout, Alignment.MIDDLE_LEFT);
//        treeContainerLayout.addComponent(infoLayoutPanel);
        treeContainerLayout.setSpacing(true);
//        treeContainerLayout.setExpandRatio(publicationTreePanel, 0.6f);
//        treeContainerLayout.setExpandRatio(infoLayoutPanel, 0.45f);
//        treeContainerLayout.setComponentAlignment(infoLayoutPanel, Alignment.MIDDLE_RIGHT);
        
        exploringFiltersManager.registerFilter(PublicationExplorerTreeLayout.this);
        layout.setVisability(false);

    }

    private String lastSelected = null;
//

    private void initPublicationTreeLayout() {
//        publicationTreePanel = new Panel();
//        publicationTreePanel.setHeight("550px");
//        publicationTreePanel.setStyleName("grayborder");

        publicationLayout = new VerticalLayout();
//        publicationTreePanel.setContent(publicationLayout);
        publicationLayout.setMargin(new MarginInfo(true, false, true, true));
        publicationLayout.setSpacing(true);
        publicationLayout.setWidthUndefined();
//        publicationLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                if (lastSelected != null) {
//                    rowMap.get(lastSelected).layoutUnSelected();
//                }
//
//                lastSelected = ((ListGridCell) (event.getClickedComponent()).getParent()).getCellId();
//                rowMap.get(lastSelected).layoutSelected();
//            }
//        });
//        infoLayoutPanel = initStudyInformationLayout();

    }
    private PublicationRoot currentSelectedPublication;
    private final Map<String, PublicationRow> rowMap = new HashMap<String, PublicationRow>();

    private void updatePublicationTree() {
        publicationLayout.removeAllComponents();
        publicationsLabelSorter = new int[27];
        for (String key : nodes.keySet()) {
            PublicationRoot root = nodes.get(key);
            for (int i = 0; i < root.getAttrCheck().length; i++) {
                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 5 || i == 6 || i == 16 || i == 17 || i == 19 || i == 20 || i == 23 || i == 24 || i == 26) {
                    continue;
                }
                if (!root.getAttrCheck()[i]) {
                    Object prop = root.getProperty(i);
                    if (!prop.toString().equalsIgnoreCase("Not Available") && !prop.toString().equalsIgnoreCase("-1000000000") && !prop.toString().equalsIgnoreCase("-1") && !prop.toString().trim().equalsIgnoreCase("0")) {
                        publicationsLabelSorter[i] = publicationsLabelSorter[i] + 1;
                    }
                }
            }
        }
        masterColumn = new boolean[27];
        int index = 0;
        colNumber = 0;
        for (int i : publicationsLabelSorter) {
            if (i > 0) {
                colNumber++;
                masterColumn[index] = true;
            }
            index++;
        }
        colNumber = colNumber + 2;
        int[] sortedArr = this.sortIndex(publicationsLabelSorter);

        Alignment[] ali = new Alignment[]{Alignment.MIDDLE_LEFT,Alignment.MIDDLE_RIGHT};
        rowMap.clear();
        for (String key : nodes.keySet()) {
           
            PublicationRow row = new PublicationRow(nodes.get(key), colNumber, sortedArr, masterColumn);
            GridLayout rowLayout =row.getRowLayout() ;
            rowLayout.setColumns(2);
//            rowLayout.setWidth("500px");
            rowMap.put(nodes.get(key).getPumedID(), row);
            for (int f = 0; f < 2; f++) {
                ListGridCell l = row.getColumns()[f];
                rowLayout.addComponent(l);
                rowLayout.setComponentAlignment(l, Alignment.MIDDLE_LEFT);

            }
            publicationLayout.addComponent(rowLayout);
            publicationLayout.addComponent(row.getFullPublicationDetailsLayout());
            publicationLayout.setComponentAlignment(rowLayout, Alignment.MIDDLE_LEFT);

        }

    }

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("filter")) {
            updateDatasetsNodes(exploringFiltersManager.getFilteredDatasetsList());
            this.updatePublicationTree();
//            containerLayout.setVisible(false);
            this.currentSelectedPublication = null;
        }
    }

    @Override
    public String getFilterId() {
        return "Publication Tree Layout";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private final TreeMap<String, PublicationRoot> nodes = new TreeMap<String, PublicationRoot>(Collections.reverseOrder());
    private String[] nodeKeys;
    private int[] publicationsLabelSorter;
    private int colNumber;
    private boolean[] masterColumn;

    private void updateDatasetsNodes(QuantDatasetObject[] updatedStudiesList) {
        nodes.clear();
        for (QuantDatasetObject pb : updatedStudiesList) {
            if (!nodes.containsKey(pb.getPumedID())) {
                PublicationRoot root = getRootNode(pb.getPumedID(), updatedStudiesList);
                nodes.put(root.getYear() + "-" + pb.getPumedID(), root);

            }

        }

        nodeKeys = new String[nodes.size()];
        int i = 0;
        for (String key : nodes.keySet()) {

            nodeKeys[i++] = key;

        }

        layout.updateFilterLabel("Publications (" + nodes.size() + ") Datasets (" + updatedStudiesList.length + ")");

    }

    private PublicationRoot getRootNode(String publicationId, QuantDatasetObject[] updatedStudiesList) {
        PublicationRoot root = new PublicationRoot();
        root.setPublicationId(publicationId);
        Set<QuantDatasetObject> publicationDatasets = new HashSet<QuantDatasetObject>();
        for (QuantDatasetObject pb : updatedStudiesList) {
            if (pb.getPumedID().equalsIgnoreCase(publicationId)) {
                publicationDatasets.add(pb);
            }

        }
        root.setIncludedStudies(publicationDatasets);
        root = filterStudyRoot(root);

        return root;
    }

    private PublicationRoot filterStudyRoot(PublicationRoot root) {
        boolean[] attrCheck = new boolean[27];
        Object[][] attrValues = new Object[root.getIncludedDatasets().size()][attrCheck.length];
        QuantDatasetObject representDs = null;
        int x = 0;
        for (QuantDatasetObject dataset : root.getIncludedDatasets()) {
            if (representDs == null) {
                representDs = dataset;
            }
            attrValues[x] = dataset.getValues();
            x++;
        }

        for (int y = 0; y < attrCheck.length; y++) {
            for (int i = 0; i < attrValues.length; i++) {
                if (i == attrValues.length - 1) {
                    break;
                }

                if (!attrValues[i][y].toString().equalsIgnoreCase(attrValues[i + 1][y].toString())) {
                    attrCheck[y] = true;
                    break;

                }
            }
        }
        String comm = " ";
        for (int i = 0; i < attrCheck.length; i++) {
            if (i == 2 || i == 3 || i == 5 || i == 6 || i == 16 || i == 17 || i == 19 || i == 23) {
                continue;
            }
            if (!attrCheck[i] && representDs != null) {
                Object prop = representDs.getProperty(i);
                if (!prop.toString().equalsIgnoreCase("Not Available") && !prop.toString().equalsIgnoreCase("-1") && !prop.toString().trim().equalsIgnoreCase("")) {
                    comm += prop + " , ";
                }

            }
        }
        if (comm.length() >= 2) {
            comm = comm.substring(0, (comm.length() - 2));
        }

        root.setCommonAttr(comm);
        root.setAttrCheck(attrCheck);
        root.setLabelsNumber(comm.split(",").length);
        Set<QuantDatasetObject> datasets = new HashSet<QuantDatasetObject>();
        for (QuantDatasetObject ds : root.getIncludedDatasets()) {
            datasets.add(filterChildDs(ds, attrCheck));
        }
        root.setIncludedStudies(datasets);

        //set common values 
//        QuantDatasetObject dataset = root.getIncludedDatasets().iterator().next();
        for (int i = 0; i < root.getAttrCheck().length; i++) {
            if (!root.getAttrCheck()[i]) {
                root.setProperty(i, representDs.getProperty(i));

            }

        }

        return root;

    }

    private QuantDatasetObject filterChildDs(QuantDatasetObject child, boolean[] attrCheck) {

        String unique = "";
        for (int i = 0; i < attrCheck.length; i++) {

            if (attrCheck[i]) {

                Object prop = child.getProperty(i);
                if (!prop.toString().equalsIgnoreCase("Not Available")) {
                    if (prop instanceof Integer) {
                        prop = headers[i] + prop;
                    }
                    if (prop.toString().contains("-1")) {
                        prop = prop.toString().replace("-1", "N/A");
                    }
                    unique += prop + " , ";
                }

            }
        }
        if (unique.length() >= 2) {
            unique = unique.substring(0, (unique.length() - 2));
        }

        child.setUniqueValues(unique);

        return child;
    }

    private Label authorLabel;
    private InformationField patientsGroup1, patientsGroup2, patientsSubGroup1, patientsSubGroup2, patientsGroup1Comm, patientsGroup2Comm, pumedId, filesNumber, identifiedProteinsNumber, quantifiedProteinsNumber, diseaseGroup, rawData, typeOfStudy, sampleType, sampleMatching, shotgunTargeted, technology, analyticalApproach, enzyme, quantificationBasis, quantBasisComment, patientsGroup1Number, patientsGroup2Number, normalization_strategy;

    private VerticalLayout containerLayout;
    private GridLayout commonInfoLayout;
    private VerticalLayout studyInfoLayout;
    private final Component[] publicationInfoLayoutComponents = new Component[27];

    private Panel initStudyInformationLayout() {

        Panel containerPanel = new Panel();
        containerPanel.setHeight("550px");
        containerPanel.setStyleName("grayborder");
        containerLayout = new VerticalLayout();

        containerLayout.setSpacing(true);
        containerLayout.setMargin(false);

        containerPanel.setContent(containerLayout);
        //title author

        authorLabel = new Label(" ");
        authorLabel.setStyleName("titleLabel");
        authorLabel.setWidth(("test title".length() * 10) + "px");
        containerLayout.addComponent(authorLabel);
        containerLayout.setComponentAlignment(authorLabel, Alignment.MIDDLE_CENTER);
        publicationInfoLayoutComponents[0] = authorLabel;

        commonInfoLayout = new GridLayout();
        commonInfoLayout.setWidth("100%");
        commonInfoLayout.setColumns(3);
        containerLayout.addComponent(commonInfoLayout);
        containerLayout.setComponentAlignment(commonInfoLayout, Alignment.MIDDLE_CENTER);
        rawData = new InformationField("Raw Data");
        commonInfoLayout.addComponent(rawData);
        publicationInfoLayoutComponents[5] = rawData;
        filesNumber = new InformationField("# Files");
        commonInfoLayout.addComponent(filesNumber);
        publicationInfoLayoutComponents[6] = filesNumber;

        pumedId = new InformationField("Pumed Id");
        commonInfoLayout.addComponent(pumedId);
        publicationInfoLayoutComponents[19] = pumedId;

        //
        studyInfoLayout = new VerticalLayout();
        studyInfoLayout.setWidth("100%");
        studyInfoLayout.setSpacing(true);
        containerLayout.addComponent(studyInfoLayout);
        containerLayout.setComponentAlignment(studyInfoLayout, Alignment.MIDDLE_CENTER);
        //
        identifiedProteinsNumber = new InformationField("# Identified Proteins");
        publicationInfoLayoutComponents[2] = identifiedProteinsNumber;
        quantifiedProteinsNumber = new InformationField("# Quantified Proteins");
        publicationInfoLayoutComponents[3] = quantifiedProteinsNumber;
        diseaseGroup = new InformationField("Disease Group");
        publicationInfoLayoutComponents[4] = diseaseGroup;

        typeOfStudy = new InformationField("Type of Study");
        publicationInfoLayoutComponents[7] = typeOfStudy;
        sampleMatching = new InformationField("Sample Matching");
        sampleType = new InformationField("Sample Type");
        publicationInfoLayoutComponents[8] = sampleType;

        publicationInfoLayoutComponents[9] = sampleMatching;
        shotgunTargeted = new InformationField("Shotgun/Targeted");
        publicationInfoLayoutComponents[10] = shotgunTargeted;
        technology = new InformationField("Technology");
        publicationInfoLayoutComponents[11] = technology;
        enzyme = new InformationField("Enzyme");

        analyticalApproach = new InformationField("Analytical Approach");
        publicationInfoLayoutComponents[12] = analyticalApproach;

        publicationInfoLayoutComponents[13] = enzyme;
        quantificationBasis = new InformationField("Quantification Basis");
        publicationInfoLayoutComponents[14] = quantificationBasis;

        quantBasisComment = new InformationField("Quantification BasisComment");
        publicationInfoLayoutComponents[15] = quantBasisComment;

        normalization_strategy = new InformationField("Normalization Strategy");
        publicationInfoLayoutComponents[16] = normalization_strategy;

        patientsGroup1 = new InformationField("Patients Gr.I");
        publicationInfoLayoutComponents[18] = patientsGroup1;

        patientsGroup1Number = new InformationField("#Patients Gr.I");
        publicationInfoLayoutComponents[19] = patientsGroup1Number;
        patientsGroup1Comm = new InformationField("Patients Gr.I Comments");
        publicationInfoLayoutComponents[20] = patientsGroup1Comm;

        patientsSubGroup1 = new InformationField("Patients Sub-Gr.I");
        publicationInfoLayoutComponents[21] = patientsSubGroup1;

        patientsGroup2 = new InformationField("Patients Gr.II");
        publicationInfoLayoutComponents[22] = patientsGroup2;

        patientsGroup2Number = new InformationField("#Patients Gr.II");
        publicationInfoLayoutComponents[23] = patientsGroup2Number;
        patientsGroup2Comm = new InformationField("Patients Gr.II Comments");
        publicationInfoLayoutComponents[24] = patientsGroup2Comm;
        patientsSubGroup2 = new InformationField("Patients Sub-Gr.II");
        publicationInfoLayoutComponents[25] = patientsSubGroup2;

        InformationField additionalComments = new InformationField("Additional Comments");
        publicationInfoLayoutComponents[26] = additionalComments;

        containerLayout.setVisible(false);
        return containerPanel;

    }

    private void updateStudyInformationLayout(PublicationRoot publication, boolean selected) {
//        System.out.println("at: "+publication.getYear() + "-" + publication.getPumedID());
//        PublicationRoot root = nodes.get(publication.getYear() + "-" + publication.getPumedID());
//        for (QuantDatasetObject dataset : root.getIncludedDatasets()) {
//            System.out.println("root " + dataset.getUniqueValues());
//
//        } 
//         for (QuantDatasetObject dataset : publication.getIncludedDatasets()) {
//               System.out.println("study  " + dataset.getUniqueValues());
//            }

        if (selected) {
            currentSelectedPublication = publication;
        }
        if (publication == null && currentSelectedPublication == null) {
            containerLayout.setVisible(false);
            return;

        }
        if (publication == null && currentSelectedPublication != null) {
            publication = currentSelectedPublication;
        }
        commonInfoLayout.removeAllComponents();
        studyInfoLayout.removeAllComponents();

        authorLabel.setValue("" + publication.getAuthor() + " (" + publication.getYear() + ")");
        authorLabel.setWidth((authorLabel.getValue().length() * 10) + "px");

        pumedId.setValue(publication.getPublicationId(), "http://www.ncbi.nlm.nih.gov/pubmed/" + publication.getPublicationId());
        filesNumber.setValue(publication.getFilesNumber() + "", null);
//        year.setValue(publication.getYear() + "", null);
        if (publication.getRawDataUrl() == null || publication.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
            rawData.setValue("Not Available", null);
        } else {
            rawData.setValue(publication.getRawDataUrl(), publication.getRawDataUrl());
        }
        commonInfoLayout.addComponent(pumedId);
        commonInfoLayout.addComponent(filesNumber);
        commonInfoLayout.addComponent(rawData);

        for (int i = 0; i < publication.getAttrCheck().length; i++) {
            if (i == 0 || i == 1 || i == 5 || i == 6 || i == 17) {
                continue;
            }
            boolean check = publication.getAttrCheck()[i];
            if (!check) {
                String prop = publication.getProperty(i).toString().trim();
                if (publicationInfoLayoutComponents[i] instanceof InformationField && !prop.equalsIgnoreCase("Not Available") && !prop.trim().equalsIgnoreCase("0") && !prop.trim().equalsIgnoreCase("")) {
                    prop = prop.replace("-1", "N/A");
                    commonInfoLayout.addComponent(publicationInfoLayoutComponents[i]);
                    ((InformationField) publicationInfoLayoutComponents[i]).setValue(prop, null);
                }

            }

        }
        if (publication.getIncludedDatasets().size() <= 1) {

            Button btn = new Button("Load Study");

            btn.setStyleName(Reindeer.BUTTON_SMALL);
            int rowNum = commonInfoLayout.getRows();
            if (commonInfoLayout.getComponentCount() % 3 != 0) {
                commonInfoLayout.addComponent(btn, 2, rowNum - 1);
            } else {
                commonInfoLayout.setRows(rowNum + 1);
                commonInfoLayout.addComponent(btn, 2, rowNum);
            }

            commonInfoLayout.setComponentAlignment(btn, Alignment.MIDDLE_LEFT);

        } else {
            int x = 1;
            for (QuantDatasetObject dataset : publication.getIncludedDatasets()) {

                studyInfoLayout.addComponent(new SingleDatasetPublicationLayout(dataset, x, publication.getAttrCheck()));
                x++;

            }
        }
        containerLayout.setVisible(true);
    }

    private int[] sortIndex(int[] labelIndexes) {
        int[] soreted = new int[labelIndexes.length];
        for (int x = 0; x < soreted.length; x++) {
            soreted[x] = getHighestValue(labelIndexes);
            labelIndexes[soreted[x]] = -100;
        }
        return soreted;

    }

    private int getHighestValue(int[] arr) {
        int heigh = -1000;
        int index = 0;
        for (int x = 0; x < arr.length; x++) {
            if (heigh < arr[x]) {
                heigh = arr[x];
                index = x;

            }

        }
        return index;

    }

}
