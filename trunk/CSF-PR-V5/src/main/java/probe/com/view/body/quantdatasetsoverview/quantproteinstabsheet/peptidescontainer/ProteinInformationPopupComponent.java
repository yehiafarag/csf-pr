/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer;

import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.PeptideSequanceLocationOverview;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.core.InformationField;

/**
 *
 * @author Yehia Farag
 */
public class ProteinInformationPopupComponent extends VerticalLayout {

    private final GridLayout mainBody;
//    private final HorizontalLayout topLayout;
    private final VerticalLayout peptidesInformationContainer;
    private final Window popupWindow;

    public ProteinInformationPopupComponent(int width) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width) + "px");
        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);
            }

        };
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((width + 40) + "px");
        popupWindow.setHeight((height) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.setPositionX(30);
        popupWindow.setPositionY(40);

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        this.mainBody = new GridLayout(4, 7);
        mainBody.setWidth((width - 10) + "px");
        mainBody.setMargin(true);
        mainBody.setStyleName("hideoverflowlayout");

        peptidesInformationContainer = new VerticalLayout();
        peptideProteinInfoHideShow = new HideOnClickLayout("Peptides Information", peptidesInformationContainer, null);
        peptidesInformationContainer.setWidth(width - 10 + "px");
        peptidesInformationContainer.setHeightUndefined();//300+"px");
        popupBody.addComponent(peptideProteinInfoHideShow);
        peptideProteinInfoHideShow.setVisability(true);

        HideOnClickLayout datasetInfoLayout = new HideOnClickLayout("Dataset Information", mainBody, null);
        datasetInfoLayout.setMargin(new MarginInfo(false, false, false, false));
        initQuantDatasetInformationLayout();
        popupBody.addComponent(datasetInfoLayout);
        datasetInfoLayout.setVisability(true);
    }
    private final HideOnClickLayout peptideProteinInfoHideShow;
    private InformationField pumedId, rawData, diseaseGroup, typeOfStudy, shotgunTargeted, enzyme, sampleType, technology, quantificationBasis, patientsGroup1Number, patientsGroup2Number, patientsGroup1, patientsGroup2, patientssubGroup1, patientsCommGroup1, patientssubGroup2, patientsCommGroup2, identifiedProteinsNumber, quantifiedProteinsNumber, sampleMatching, quantBasisComment, analyticalApproach, normalization_strategy;

    private void initQuantDatasetInformationLayout() {

        mainBody.setSpacing(true);
        mainBody.setMargin(false);

        pumedId = new InformationField("Pumed Id");
        mainBody.addComponent(pumedId, 0, 0);

        rawData = new InformationField("Raw Data");
        mainBody.addComponent(rawData, 1, 0);

        diseaseGroup = new InformationField("Disease Group");
        mainBody.addComponent(diseaseGroup, 2, 0);

        typeOfStudy = new InformationField("Type of Study");
        mainBody.addComponent(typeOfStudy, 3, 0);

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        mainBody.addComponent(shotgunTargeted, 0, 1);

        enzyme = new InformationField("Enzyme");
        mainBody.addComponent(enzyme, 1, 1);

        sampleType = new InformationField("Sample Type");
        mainBody.addComponent(sampleType, 2, 1);

        technology = new InformationField("Technology");
        mainBody.addComponent(technology, 3, 1);

        patientsGroup1 = new InformationField("Patients Gr.I");
        mainBody.addComponent(patientsGroup1, 0, 2);

        patientssubGroup1 = new InformationField("Patients Sub Gr.I");
        mainBody.addComponent(patientssubGroup1, 1, 2);

        patientsGroup1Number = new InformationField("#Patients Gr.I");
        mainBody.addComponent(patientsGroup1Number, 2, 2);

        patientsCommGroup1 = new InformationField("Patients Gr.I Comm.");
        mainBody.addComponent(patientsCommGroup1, 3, 2);

        patientsGroup2 = new InformationField("Patients Gr.II");
        mainBody.addComponent(patientsGroup2, 0, 3);

        patientssubGroup2 = new InformationField("Patients Sub Gr.II");
        mainBody.addComponent(patientssubGroup2, 1, 3);

        patientsGroup2Number = new InformationField("#Patients Gr.II");
        mainBody.addComponent(patientsGroup2Number, 2, 3);

        patientsCommGroup2 = new InformationField("Patients Gr.II Comm.");
        mainBody.addComponent(patientsCommGroup2, 3, 3);

        quantificationBasis = new InformationField("Quantification Basis");
        mainBody.addComponent(quantificationBasis, 0, 4);

        identifiedProteinsNumber = new InformationField("# Identified Proteins");
        mainBody.addComponent(identifiedProteinsNumber, 1, 4);

        quantifiedProteinsNumber = new InformationField("# Quantified Proteins");
        mainBody.addComponent(quantifiedProteinsNumber, 2, 4);

        sampleMatching = new InformationField("Sample Matching");
        mainBody.addComponent(sampleMatching, 3, 4);

        analyticalApproach = new InformationField("Analytical Approach");
        mainBody.addComponent(analyticalApproach, 0, 5);

        quantBasisComment = new InformationField("Quantification BasisComment");
        mainBody.addComponent(quantBasisComment, 1, 5);
//
        normalization_strategy = new InformationField("Normalization Strategy");
        mainBody.addComponent(normalization_strategy, 2, 5);

    }

    public void updateForm(QuantDatasetObject dataset, PeptideSequanceLocationOverview stackedBarChart, String peptideNumber, String protAcc) {

        popupWindow.setCaption("<a href='http://www.ncbi.nlm.nih.gov/pubmed/" + dataset.getPumedID() + "'target=\"_blank\">" + dataset.getAuthor() + " (" + dataset.getYear() + ")</a> - <a href='http://www.uniprot.org/uniprot/" + protAcc + "'target=\"_blank\"> (" + protAcc + ")</a>");

        pumedId.setValue(dataset.getPumedID(), "http://www.ncbi.nlm.nih.gov/pubmed/" + dataset.getPumedID());
        if (dataset.getRawDataUrl() == null || dataset.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
            rawData.setValue("Not Available", null);
        } else {
            rawData.setValue(dataset.getRawDataUrl(), dataset.getRawDataUrl());
        }
        diseaseGroup.setValue(dataset.getDiseaseGroups(), null);
        typeOfStudy.setValue(dataset.getTypeOfStudy(), null);
        shotgunTargeted.setValue(dataset.getShotgunTargeted(), null);
        enzyme.setValue(dataset.getEnzyme(), null);

        sampleType.setValue(dataset.getSampleType(), null);
        technology.setValue(dataset.getTechnology(), null);

        quantificationBasis.setValue(dataset.getQuantificationBasis(), null);
        patientsGroup1Number.setValue(dataset.getPatientsGroup1Number() + "", null);
        patientsGroup2Number.setValue(dataset.getPatientsGroup2Number() + "", null);
        patientsGroup1.setValue(dataset.getPatientsGroup1() + "", null);
        patientsGroup2.setValue(dataset.getPatientsGroup2() + "", null);

        patientssubGroup1.setValue(dataset.getPatientsSubGroup1(), null);
        patientsCommGroup1.setValue(dataset.getPatientsGroup1Comm(), null);

        patientssubGroup2.setValue(dataset.getPatientsSubGroup2(), null);
        patientsCommGroup2.setValue(dataset.getPatientsGroup2Comm(), null);

        identifiedProteinsNumber.setValue(dataset.getIdentifiedProteinsNumber() + "", null);
        quantifiedProteinsNumber.setValue(dataset.getQuantifiedProteinsNumber() + "", null);

        sampleMatching.setValue(dataset.getSampleMatching() + "", null);
        quantBasisComment.setValue(dataset.getQuantBasisComment() + "", null);
        analyticalApproach.setValue(dataset.getAnalyticalApproach() + "", null);
        normalization_strategy.setValue(dataset.getNormalizationStrategy(), null);

        peptidesInformationContainer.removeAllComponents();
        if (stackedBarChart != null) {
            peptidesInformationContainer.addComponent(stackedBarChart);
            peptidesInformationContainer.setComponentAlignment(stackedBarChart, Alignment.TOP_CENTER);
            if (stackedBarChart.isNoPeptide()) {
                peptideProteinInfoHideShow.updateFilterLabel("Protein Information");
            } else {
                peptideProteinInfoHideShow.updateFilterLabel("Peptides Information (" + peptideNumber + ")");
            }

        }
        popupWindow.setVisible(true);

    }

}
