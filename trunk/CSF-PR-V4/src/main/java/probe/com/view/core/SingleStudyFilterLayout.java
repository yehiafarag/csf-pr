/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import probe.com.model.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 */
public class SingleStudyFilterLayout extends VerticalLayout {

    private final Component[] publicationInfoLayoutComponents = new Component[27];

    public SingleStudyFilterLayout(QuantDatasetObject dataset, int datasetIndex, boolean[] uniqueAttr, boolean view) {

        init();
        this.setMargin(new MarginInfo(false, false, false, false));
        VerticalLayout miniLayout = new VerticalLayout();
        Label miniAttrLabel = new Label();
        if (!dataset.getUniqueValues().equalsIgnoreCase("")) {
            miniAttrLabel.setValue("[ " + dataset.getUniqueValues() + "]");
        }
        miniLayout.addComponent(miniAttrLabel);
        GridLayout studyInfo = new GridLayout();
        studyInfo.setColumns(3);
        studyInfo.setWidth("100%");

        SubTreeHideOnClick layout = new SubTreeHideOnClick("Dataset " + datasetIndex, studyInfo, miniLayout, view);
        this.addComponent(layout);
        this.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        for (int i = 0; i < uniqueAttr.length; i++) {
            
            boolean check = uniqueAttr[i];
          
            if (check) {
                if (!dataset.getProperty(i).toString().trim().equalsIgnoreCase("Not Available") && !dataset.getProperty(i).toString().trim().equalsIgnoreCase("0")&& !dataset.getProperty(i).toString().trim().equalsIgnoreCase("-1")) {
                    if (publicationInfoLayoutComponents[i] != null) {
                        studyInfo.addComponent(publicationInfoLayoutComponents[i]);
                        ((InformationField) publicationInfoLayoutComponents[i]).setValue(dataset.getProperty(i).toString(), null);
                    }
                }

            }

        }
        Button btn = new Button("Load Dataset");
        btn.setStyleName(Reindeer.BUTTON_SMALL);
        VerticalLayout btnLayout = new VerticalLayout();
        btnLayout.setMargin(true);
        btnLayout.setHeight("30px");
        btnLayout.addComponent(btn);

        int rowNum = studyInfo.getRows();
        if (studyInfo.getComponentCount() % 3 != 0) {
            studyInfo.addComponent(btnLayout, 2, rowNum - 1);
        } else {
            studyInfo.setRows(rowNum + 1);
            studyInfo.addComponent(btnLayout, 2, rowNum);
        }

        studyInfo.setComponentAlignment(btnLayout, Alignment.BOTTOM_LEFT);

    }
    InformationField diseaseGroup, year, author, rowData, filesNum;

    private void init() {

        author = new InformationField("Author");
        publicationInfoLayoutComponents[0] = author;
        year = new InformationField("Year");
        publicationInfoLayoutComponents[1] = year;
        
        
        InformationField identifiedProteinsNumber = new InformationField("# Identified Proteins");
        publicationInfoLayoutComponents[2] = identifiedProteinsNumber;
        InformationField quantifiedProteinsNumber = new InformationField("# Quantified Proteins");
       
        publicationInfoLayoutComponents[3] = quantifiedProteinsNumber;
        diseaseGroup = new InformationField("Disease Group");
        publicationInfoLayoutComponents[4] = diseaseGroup;
        
         rowData = new InformationField("Raw Data");
        publicationInfoLayoutComponents[5] = rowData;

        filesNum = new InformationField("#Files");
        publicationInfoLayoutComponents[6] = filesNum;

        InformationField typeOfStudy = new InformationField("Type of Study");
        publicationInfoLayoutComponents[7] = typeOfStudy;
        InformationField sampleType = new InformationField("Sample Type");
        publicationInfoLayoutComponents[8] = sampleType;

        InformationField sampleMatching = new InformationField("Sample Matching");
        publicationInfoLayoutComponents[9] = sampleMatching;

        InformationField shotgunTargeted = new InformationField("Shotgun/Targeted");
        publicationInfoLayoutComponents[10] = shotgunTargeted;

        InformationField technology = new InformationField("Technology");
        publicationInfoLayoutComponents[11] = technology;

        InformationField analyticalApproach = new InformationField("Analytical Approach");
        publicationInfoLayoutComponents[12] = analyticalApproach;

        InformationField enzyme = new InformationField("Enzyme");
        publicationInfoLayoutComponents[13] = enzyme;
        InformationField quantificationBasis = new InformationField("Quantification Basis");
        publicationInfoLayoutComponents[14] = quantificationBasis;

        InformationField quantBasisComment = new InformationField("Quantification BasisComment");
        publicationInfoLayoutComponents[15] = quantBasisComment;

        InformationField normalization_strategy = new InformationField("Normalization Strategy");
        publicationInfoLayoutComponents[16] = normalization_strategy;
        
         InformationField pumedID = new InformationField("PumedID");
        publicationInfoLayoutComponents[17] = pumedID;

        InformationField patientsGroup1 = new InformationField("Patients Gr.I");
        publicationInfoLayoutComponents[18] = patientsGroup1;

        InformationField patientsGroup1Number = new InformationField("#Patients Gr.I");
        publicationInfoLayoutComponents[19] = patientsGroup1Number;
        InformationField patientsGroup1Comm = new InformationField("Patients Gr.I Comments");
        publicationInfoLayoutComponents[20] = patientsGroup1Comm;
        InformationField patientsSubGroup1 = new InformationField("Patients Sub-Gr.I ");
        publicationInfoLayoutComponents[21] = patientsSubGroup1;

        InformationField patientsGroup2 = new InformationField("Patients Gr.II");
        publicationInfoLayoutComponents[22] = patientsGroup2;

        InformationField patientsGroup2Number = new InformationField("#Patients Gr.II");
        publicationInfoLayoutComponents[23] = patientsGroup2Number;
        InformationField patientsGroup2Comm = new InformationField("Patients Gr.II Comments");
        publicationInfoLayoutComponents[24] = patientsGroup2Comm;
        InformationField patientsSubGroup2 = new InformationField("Patients Sub-Gr.II ");
        publicationInfoLayoutComponents[25] = patientsSubGroup2;

        InformationField additionalComments = new InformationField("Additional Comments");
        publicationInfoLayoutComponents[26] = additionalComments;
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
      

       

    }

}
