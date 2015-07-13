/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.components.GelFractionsLayout;
import probe.com.view.components.PeptidesTableLayout;
import probe.com.view.components.ProteinsTableLayout;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.DatasetInformationLayout;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationDatasetLayout extends VerticalLayout implements Serializable, Property.ValueChangeListener {

    private final MainHandler handler;
    private final int datasetId;
    private final VerticalLayout fractionsLayout, peptidesLayout;
    private ProteinsTableLayout protTableLayout;
    private PeptidesTableLayout peptideTableLayout;

    private  Map<String, IdentificationProteinBean> proteinsList;
    private TreeMap<Integer, Object> selectionIndexes;
    private int nextIndex;

    public IdentificationDatasetLayout(MainHandler handler, int datasetId) {
        this.setSizeFull();
        setMargin(true);
        this.handler = handler;
        this.setWidth("100%");
        this.setHeight("100%");
        this.datasetId = datasetId;
        fractionsLayout = new VerticalLayout();
        peptidesLayout = new VerticalLayout();
        peptidesLayout.setMargin(true);
        buildMainLayout();

    }

    /**
     * initialize main proteins tab layout initialize drop down select dataset
     * list and add main value change listener to it
     */
    private void buildMainLayout() {


        //init dataset Details

         DatasetInformationLayout datasetInfoLayout =new DatasetInformationLayout(handler, datasetId,null);
        HideOnClickLayout dsLayout = new HideOnClickLayout(handler.getDataset(datasetId).getName(), datasetInfoLayout,null);
        dsLayout.setMargin(new MarginInfo(false,false,false,false));
//        datasetInfoLayout.setVisible(true);
        this.addComponent(dsLayout);
        //get proteins List
        proteinsList = handler.retriveProteinsList(datasetId);

        protTableLayout = new ProteinsTableLayout(proteinsList, handler.getDataset(datasetId).getFractionsNumber(), handler.getDataset(datasetId).getNumberValidProt(), handler.getDataset(datasetId).getProteinsNumber());
        this.addComponent(protTableLayout);
        this.setComponentAlignment(protTableLayout, Alignment.TOP_LEFT);
        protTableLayout.getProteinTableComponent().addValueChangeListener(IdentificationDatasetLayout.this);
        protTableLayout.setListener(IdentificationDatasetLayout.this);

        fractionsLayout.setWidth("100%");
        this.addComponent(fractionsLayout);
        peptidesLayout.setWidth("100%");
        this.addComponent(peptidesLayout);
        selectionIndexes = handler.getSearchIndexesSet(protTableLayout.getProteinTableComponent().getTableSearchMap(), protTableLayout.getProteinTableComponent().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toUpperCase().trim());
        protTableLayout.getProteinTableComponent().setCurrentPageFirstItemId(protTableLayout.getProteinTableComponent().getFirstIndex());
        protTableLayout.getProteinTableComponent().select(protTableLayout.getProteinTableComponent().getFirstIndex());
        protTableLayout.getProteinTableComponent().commit();
        ActionButtonTextField searchButtonTextField = ActionButtonTextField.extend(protTableLayout.getSearchField());
        searchButtonTextField.getState().type = ActionButtonType.ACTION_SEARCH;
        searchButtonTextField.addClickListener(new ActionButtonTextField.ClickListener() {
            @Override
            public void buttonClick(ActionButtonTextField.ClickEvent clickEvent) {
                selectionIndexes = handler.getSearchIndexesSet(protTableLayout.getProteinTableComponent().getTableSearchMap(), protTableLayout.getProteinTableComponent().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toUpperCase().trim());
                if (!selectionIndexes.isEmpty()) {
                    if (selectionIndexes.size() > 1) {
                        protTableLayout.getNextSearch().setEnabled(true);
                        protTableLayout.getNextSearch().focus();
                    } else {
                        protTableLayout.getNextSearch().setEnabled(false);
                    }
                    protIndex = 1;
                    nextIndex = selectionIndexes.firstKey();
                    protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
                    protTableLayout.getProteinTableComponent().setCurrentPageFirstItemId(selectionIndexes.get(nextIndex));
                    protTableLayout.getProteinTableComponent().select(selectionIndexes.get(nextIndex));
                    protTableLayout.getProteinTableComponent().commit();

                } else {
                    Notification.show("Not Exist");
                    protIndex = 1;
                }

            }
        });

        protTableLayout.getSearchField().addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                protTableLayout.getNextSearch().setEnabled(false);
                protTableLayout.getProtCounter().setValue("");

                protIndex = 1;

            }
        });

        protTableLayout.getNextSearch().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nextIndex = selectionIndexes.higherKey(nextIndex);
                protTableLayout.getProteinTableComponent().setCurrentPageFirstItemId(selectionIndexes.get(nextIndex));
                protTableLayout.getProteinTableComponent().select(selectionIndexes.get(nextIndex));
                protTableLayout.getProteinTableComponent().commit();
                protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
                if (nextIndex == selectionIndexes.lastKey()) {
                    nextIndex = selectionIndexes.firstKey() - 1;
                    protIndex = 1;
                }
            }
        });

    }

    private int protIndex = 1;
    private int proteinskey;
    private CustomExternalLink proteinLabel;

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        if (protTableLayout.getProteinTableComponent().getValue() != null) {
            proteinskey = (Integer) protTableLayout.getProteinTableComponent().getValue();
            System.out.println("prot key " + proteinskey);
        } else {
            return;
        }

        fractionsLayout.removeAllComponents();
        peptidesLayout.removeAllComponents();
        if (proteinLabel != null) {
            proteinLabel.rePaintLable("black");
        }
        protTableLayout.setLastSelectedIndex(proteinskey);
        final Item item = protTableLayout.getProteinTableComponent().getItem(proteinskey);
        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
        proteinLabel.rePaintLable("white");

        final String desc = (String) item.getItemProperty("Description").getValue();
        final String accession = item.getItemProperty("Accession").getValue().toString();
        final String otherAccession = (String) item.getItemProperty("Other Protein(s)").getValue();

        CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", datasetId, handler.getDataset(datasetId).getName(), accession, otherAccession, null, 0, null, null, null, desc);
        CustomExportBtnLayout exportAllDatasetProteinsLayout = (new CustomExportBtnLayout(handler, "prots", datasetId, handler.getDataset(datasetId).getName(), accession, otherAccession, proteinsList, handler.getDataset(datasetId).getFractionsNumber(), null, null, null, desc));

        PopupView exportAllProteinPeptidePopup = new PopupView("Export Peptides from All Datasets for ( " + accession + " )", exportAllProteinPeptidesLayout);
        exportAllProteinPeptidePopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) from All Datasets");
        PopupView exportAllDatasetProteinPeptidesPopup = new PopupView("Export All Proteins from Selected Dataset", exportAllDatasetProteinsLayout);
        exportAllDatasetProteinPeptidesPopup.setDescription("Export All Proteins from ( " + handler.getDataset(datasetId).getName() + " ) Dataset");
        protTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidePopup, exportAllDatasetProteinPeptidesPopup);

        if (proteinskey >= 0) {
            Map<Integer, PeptideBean> peptideProteintList = handler.getPeptidesProtList(datasetId, accession, otherAccession);
            if (!peptideProteintList.isEmpty()) {
                int validPep = handler.getValidatedPepNumber(peptideProteintList);
                if (peptideTableLayout != null) {
                    peptidesLayout.removeComponent(peptideTableLayout);
                }
                peptideTableLayout = new PeptidesTableLayout(validPep, peptideProteintList.size(), desc, peptideProteintList, accession, handler.getDataset(datasetId).getName());

                peptideTableLayout.setHeight("" + protTableLayout.getHeight());
                peptidesLayout.setHeight("" + protTableLayout.getHeight());
                peptidesLayout.addComponent(peptideTableLayout);
                CustomExportBtnLayout exportPeptidesBtnLayout = new CustomExportBtnLayout(handler, "protPep", datasetId, handler.getDataset(datasetId).getName(), accession, otherAccession, null, 0, peptideProteintList, null, null, desc);
                PopupView ExportDatasetProtenPeptidesLayout = new PopupView("Export Peptides from Selected Dataset for ( " + accession + " )", exportPeptidesBtnLayout);
                ExportDatasetProtenPeptidesLayout.setDescription("Export Peptides from ( " + handler.getDataset(datasetId).getName() + " ) Dataset for ( " + accession + " )");
                peptideTableLayout.setExpBtnPepTable(ExportDatasetProtenPeptidesLayout);

            }
            List<StandardProteinBean> standerdProtList = handler.retrieveStandardProtPlotList(datasetId);

            int fractionsNumber = handler.getDataset(datasetId).getFractionsNumber();

            if (fractionsNumber == 0 || datasetId == 0 || standerdProtList == null || standerdProtList.isEmpty()) {
                fractionsLayout.removeAllComponents();
                if (protTableLayout.getProteinTableComponent() != null) {
                    protTableLayout.getProteinTableComponent().setHeight("267.5px");
                    protTableLayout.setProtTableHeight("267.5px");
                }
                if (peptideTableLayout.getPepTable() != null) {
                    peptideTableLayout.getPepTable().setHeight("267.5px");
                    peptideTableLayout.setPeptideTableHeight("267.5px");
                }
            } else {

                Map<Integer, IdentificationProteinBean> fractionsList = handler.getProtGelFractionsList(datasetId, accession, otherAccession);

                if (fractionsList != null && !fractionsList.isEmpty()) {
                    double mw = 0.0;
                    try {
                        mw = Double.valueOf(item.getItemProperty("MW").toString());
                    } catch (NumberFormatException e) {
                        String str = item.getItemProperty("MW").toString();
                        String[] strArr = str.split(",");
                        if (strArr.length > 1) {
                            str = strArr[0] + "." + strArr[1];
                        }
                        mw = Double.valueOf(str);
                    }
                    GelFractionsLayout gelFractionLayout = new GelFractionsLayout(accession, mw, fractionsList, standerdProtList, handler.getDataset(datasetId).getName());
                    gelFractionLayout.setMargin(new MarginInfo(false, false, false, true));
                    fractionsLayout.addComponent(gelFractionLayout);

                }

            }
        }
    }

}
