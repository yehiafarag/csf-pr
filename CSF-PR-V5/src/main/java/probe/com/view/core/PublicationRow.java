package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.vaadin.marcus.MouseEvents;
import probe.com.model.beans.PublicationRoot;
import probe.com.model.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 */
public class PublicationRow {

    public void layoutSelected() {
        for (ListGridCell l : columns) {
            l.setStyleName("selectedpublicationoverviewlabel");
            l.setSelected(true);
            containerLayout.setVisible(true);
//            updateStudyInformationLayout(publication, true);

        }

    }

    public void layoutUnSelected() {
        for (ListGridCell l : columns) {
            l.setStyleName("publicationoverviewlabel");
            l.setSelected(false);
            containerLayout.setVisible(false);
        }
    }

    private final ListGridCell[] columns;

    public VerticalLayout getFullPublicationDetailsLayout() {
        return containerLayout;
    }

    public ListGridCell[] getColumns() {
        return columns;
    }

    private final PublicationRoot publication;

    public PublicationRow(PublicationRoot publication, int colNumber, int[] sorter, boolean[] masterColumn) {
      
//        this.parent = parent;
        columns = new ListGridCell[colNumber];
        this.publication = publication;
        mouseOverListener = new MouseEvents.MouseOverListener() {

            @Override
            public void mouseOver() {

//                updateStudyInformationLayout(publication, false);

            }
        };
//
        mouseOutListener = new MouseEvents.MouseOutListener() {

            @Override
            public void mouseOut() {
//                updateStudyInformationLayout(null, false);
            }
        };
        ListGridCell authorLabel = new ListGridCell("<b><span style='font-size: 13px;'>" + publication.getAuthor() + " (" + publication.getYear() + ")&nbsp;&nbsp;" + "</span></b> ", publication.getPumedID(), mouseOverListener, mouseOutListener, true);
        columns[0] = (authorLabel);
        ListGridCell studiesNumLabel = new ListGridCell("<b><span style='font-size: 13px;'>#Dataset:" + publication.getIncludedDatasets().size() + "&nbsp;&nbsp;</span></b>", publication.getPumedID(), mouseOverListener, mouseOutListener, false);
        columns[1] = (studiesNumLabel);
//        int counter = 2;
//        for (int z = 0; z < masterColumn.length; z++) {
//            int i = sorter[z];
//            if (masterColumn[i]) {
//                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 5 || i == 6 || i == 16 || i == 17 || i == 19 || i == 20 || i == 23 || i == 24 || i == 26) {
//                    continue;
//                }
//                boolean check = publication.getAttrCheck()[i];
//                if (!check) {
//                    Object prop = publication.getProperty(i);
//                    if (!prop.toString().equalsIgnoreCase("Not Available") && !prop.toString().equalsIgnoreCase("-1") && !prop.toString().trim().equalsIgnoreCase("")) {
//
//                    } else {
//                        prop = "   ";
//                    }
//                    ListGridCell l = new ListGridCell("<i><span>&nbsp;&nbsp;" + prop + "&nbsp;&nbsp;</span></i> ", publication.getPumedID(), mouseOverListener, mouseOutListener, false);
//                    columns[counter] = (l);
//                    counter++;
//                } else {
//                    ListGridCell l = new ListGridCell("<i><span >&nbsp;&nbsp;" + "   " + "&nbsp;&nbsp;</span></i> ", publication.getPumedID(), mouseOverListener, mouseOutListener, false);
//                    columns[counter] = (l);
//                    counter++;
//                }
//            }
//
//        }  
        initStudyInformationLayout();
//        updateStudyInformationLayout();
         containerLayout.setVisible(false);
        rowLayout = new GridLayout();
        
        rowLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (containerLayout.isVisible()) {
                    containerLayout.setVisible(false);
                } else {
                    containerLayout.setVisible(true);
                }
            }
        });

    }

    public GridLayout getRowLayout() {
        return rowLayout;
    }

    private final GridLayout rowLayout;
    private final MouseEvents.MouseOverListener mouseOverListener;
    private final MouseEvents.MouseOutListener mouseOutListener;
    private VerticalLayout containerLayout;
    private GridLayout commonInfoLayout;
    private VerticalLayout studyInfoLayout;
    private final Component[] publicationInfoLayoutComponents = new Component[27];

    private void updateStudyInformationLayout() {
//        System.out.println("at: "+publication.getYear() + "-" + publication.getPumedID());
//        PublicationRoot root = nodes.get(publication.getYear() + "-" + publication.getPumedID());
//        for (QuantDatasetObject dataset : root.getIncludedDatasets()) {
//            System.out.println("root " + dataset.getUniqueValues());
//
//        } 
//         for (QuantDatasetObject dataset : publication.getIncludedDatasets()) {
//               System.out.println("study  " + dataset.getUniqueValues());
//            }

//        if (selected) {
//            currentSelectedPublication = publication;
//        }
//        if (publication == null && currentSelectedPublication == null) {
//            containerLayout.setVisible(false);
//            return;
//
//        }
//        if (publication == null && currentSelectedPublication != null) {
//            publication = currentSelectedPublication;
//        }

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
    private Label authorLabel;
    private InformationField patientsGroup1, patientsGroup2, patientsSubGroup1, patientsSubGroup2, patientsGroup1Comm, patientsGroup2Comm, pumedId, filesNumber, identifiedProteinsNumber, quantifiedProteinsNumber, diseaseGroup, rawData, typeOfStudy, sampleType, sampleMatching, shotgunTargeted, technology, analyticalApproach, enzyme, quantificationBasis, quantBasisComment, patientsGroup1Number, patientsGroup2Number, normalization_strategy;

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

}
