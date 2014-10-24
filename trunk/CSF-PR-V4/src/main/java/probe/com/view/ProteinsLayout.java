
package probe.com.view;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.components.DatasetDetailsComponent;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.GeneralUtil;
import probe.com.view.core.IconGenerator;

/**
 *
 * @author Yehia Farag
 */
public final class ProteinsLayout extends VerticalLayout implements Serializable, Property.ValueChangeListener {

    private final TreeMap<Integer, String> datasetNamesList;
    //dataset details layout visability
    private boolean visability = false;
    private Select selectDataset;
    private int proteinskey = -1;
    private DatasetDetailsComponent datasetDetailsLayout;
    private CustomExternalLink proteinLabel;
    private VerticalLayout peptideLayout;
    private final String defaultSelectString = "\t \t \t Please Select Dataset";
    private int starter = 1;
    private final MainHandler handler;
    private ProteinsTableLayout protTableLayout;
    private String accession;
    private String otherAccession;
    private Map<Integer, FractionBean> fractionsList = null;
    private PeptidesTableLayout peptideTableLayout;
    private VerticalLayout fractionLayout;
    private Map<String, ProteinBean> proteinsList;
    private final VerticalLayout typeILayout;
    private final GeneralUtil util = new GeneralUtil();
    private TreeMap<Integer, Integer> selectionIndexes;
    private int nextIndex;
    private IconGenerator iconGenerator = new IconGenerator();

    /**
     *
     * @param handler dataset handler
     */
    public ProteinsLayout(MainHandler handler) {
        this.setSizeFull();
        setMargin(true);
        this.handler = handler;
        this.datasetNamesList = handler.getDatasetNamesList();
        this.setWidth("100%");
        this.setHeight("100%");
        this.removeAllComponents();
        typeILayout = new VerticalLayout();
        typeILayout.setVisible(false);
        buildMainLayout();
    }

    /**
     * initialize main proteins tab layout initialize drop down select dataset
     * list and add main value change listener to it
     */
    private void buildMainLayout() {
        VerticalLayout vlo = new VerticalLayout();
        vlo.setStyleName(Reindeer.LAYOUT_WHITE);
        vlo.setHeight("70px");
        vlo.setWidth("100%");
        this.addComponent(vlo);
        if (handler.getDatasetList() == null || handler.getDatasetList().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            vlo.addComponent(noExpLable);
        } else {
            Label selectDatasetLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Select Dataset :</h4>");
            selectDatasetLabel.setContentMode(ContentMode.HTML);
            selectDatasetLabel.setHeight("35px");
            vlo.addComponent(selectDatasetLabel);
            vlo.setComponentAlignment(selectDatasetLabel, Alignment.TOP_LEFT);

            HorizontalLayout selectDatasetListLayout = new HorizontalLayout();
            selectDatasetListLayout.setWidth("100%");
            vlo.addComponent(selectDatasetListLayout);
            selectDataset = new Select();

            selectDataset.setStyleName(Runo.PANEL_LIGHT);
            selectDataset.addItem(defaultSelectString);
            selectDataset.setValue(defaultSelectString);
            selectDataset.setNullSelectionAllowed(false);
            selectDataset.setValue(defaultSelectString);
            selectDataset.focus();

            for (String str : datasetNamesList.values()) {
                selectDataset.addItem(str);
            }
            selectDataset.setImmediate(true);
            selectDataset.addValueChangeListener(this);
            selectDataset.setWidth("90%");
            selectDataset.setNullSelectionAllowed(false);
            selectDatasetListLayout.addComponent(selectDataset);
            selectDatasetListLayout.setComponentAlignment(selectDataset, Alignment.TOP_LEFT);

            Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.</p></div>");
            infoLable.setContentMode(ContentMode.HTML);
            infoLable.setWidth("450px");
            iconGenerator = new IconGenerator();
            HorizontalLayout infoIco = iconGenerator.getInfoNote(infoLable);
            selectDatasetListLayout.addComponent(infoIco);
            selectDatasetListLayout.setComponentAlignment(infoIco, Alignment.TOP_RIGHT);
            this.addComponent(typeILayout);
        }
    }

    /**
     * on selection of dataset from the drop down list initialize the proteins
     * table for the selected dataset initialize the dataset information layout
     * (dataset details)
     *
     * @param event value change event when user select dataset from drop down
     * list
     */
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        Object selectedDatasetKey = selectDataset.getValue();
        if (selectedDatasetKey != null && (!selectedDatasetKey.toString().equals(defaultSelectString))) {
            String datasetString = selectDataset.getValue().toString();
            //set the main dataset on logic layer
            handler.setMainDataset(datasetString);
            //layout for dataset type 1 Identification dataset
            if (handler.getMainDataset() != null && handler.getMainDataset().getDatasetType() == 1) {
                typeILayout.removeAllComponents();
                typeILayout.setVisible(true);
                starter = 1;
                if (datasetDetailsLayout != null) {
                    visability = datasetDetailsLayout.isVisability();
                    typeILayout.removeComponent(datasetDetailsLayout);
                }
                //init and add first component (dataset details) to proteins layout
                datasetDetailsLayout = this.initDatasetDetailsLayout(visability);//get experiment details view
                typeILayout.addComponent(datasetDetailsLayout);
                typeILayout.setComponentAlignment(datasetDetailsLayout, Alignment.TOP_LEFT);

                proteinsList = handler.retriveProteinsList(handler.getMainDataset().getDatasetId());
               
               if (protTableLayout != null) {
                    typeILayout.removeComponent(protTableLayout);
                }
                protTableLayout = new ProteinsTableLayout(proteinsList, handler.getMainDataset().getFractionsNumber(), handler.getMainDataset().getNumberValidProt(), handler.getMainDataset().getProteinsNumber());
                typeILayout.addComponent(protTableLayout);
                typeILayout.setComponentAlignment(protTableLayout, Alignment.TOP_LEFT);

                /**
                 * on selection proteins initialize peptides table and fractions
                 * layout
                 */
                ValueChangeListener listener = new Property.ValueChangeListener() {
                    /*
                     *change listener for protein selection
                     */

                    private static final long serialVersionUID = 1L;

                    @Override
                    @SuppressWarnings("SleepWhileHoldingLock")
                    public synchronized void valueChange(Property.ValueChangeEvent event) {
                        if (proteinLabel != null) {
                            proteinLabel.rePaintLable("black");
                        }
                        if (starter != 1) {
                            datasetDetailsLayout.hideDetails();
                        }
                        starter++;
                        String desc = "";
                        //fraction layout
                        if (fractionLayout != null) {
                            typeILayout.removeComponent(fractionLayout);
                        }
                        fractionLayout = new VerticalLayout();
                        typeILayout.addComponent(fractionLayout);
                        fractionLayout.setWidth("100%");
                        //peptide layout
                        if (peptideLayout != null) {
                            typeILayout.removeComponent(peptideLayout);
                        }
                        peptideLayout = new VerticalLayout();
                        typeILayout.addComponent(peptideLayout);
                        peptideLayout.setWidth("100%");
                        if (protTableLayout.getProteinTableComponent().getValue() != null) {
                            proteinskey = (Integer) protTableLayout.getProteinTableComponent().getValue();
                        }
                        final Item item = protTableLayout.getProteinTableComponent().getItem(proteinskey);
                        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                        proteinLabel.rePaintLable("white");
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException iexp) {
                            System.out.println(iexp.getLocalizedMessage());
                        }
                        desc = item.getItemProperty("Description").toString();
                        accession = item.getItemProperty("Accession").toString();
                        otherAccession = item.getItemProperty("Other Protein(s)").toString();

                        CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", handler.getMainDataset().getDatasetId(), handler.getMainDataset().getName(), accession, otherAccession, null, 0, null, null, null, desc);
                        CustomExportBtnLayout exportAllDatasetProteinsLayout = (new CustomExportBtnLayout(handler, "prots", handler.getMainDataset().getDatasetId(), handler.getMainDataset().getName(), accession, otherAccession, proteinsList, handler.getMainDataset().getFractionsNumber(), null, null, null, desc));

                        PopupView exportAllProteinPeptidePopup = new PopupView("Export Peptides from All Datasets for ( " + accession + " )", exportAllProteinPeptidesLayout);
                        exportAllProteinPeptidePopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) from All Datasets");
                        PopupView exportAllDatasetProteinPeptidesPopup = new PopupView("Export All Proteins from Selected Dataset", exportAllDatasetProteinsLayout);
                        exportAllDatasetProteinPeptidesPopup.setDescription("Export All Proteins from ( " + handler.getMainDataset().getName() + " ) Dataset");
                        protTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidePopup, exportAllDatasetProteinPeptidesPopup);
                        if (proteinskey >= 0) {
                            Map<Integer, PeptideBean> peptideProteintList = handler.getPeptidesProtList(handler.getMainDataset().getDatasetId(), accession, otherAccession);
                            if (handler.getMainDataset().getPeptideList() == null) {
                                handler.getMainDataset().setPeptideList(peptideProteintList);
                            } else {
                                handler.getMainDataset().getPeptideList().putAll(peptideProteintList);
                            }
                            if (!peptideProteintList.isEmpty()) {
                                int validPep = handler.getValidatedPepNumber(peptideProteintList);
                                if (peptideTableLayout != null) {
                                    peptideLayout.removeComponent(peptideTableLayout);
                                }
                                peptideTableLayout = new PeptidesTableLayout(validPep, peptideProteintList.size(), desc, peptideProteintList, accession, handler.getMainDataset().getName());
                                peptideLayout.setMargin(true);
                                peptideTableLayout.setHeight("" + protTableLayout.getHeight());
                                peptideLayout.setHeight("" + protTableLayout.getHeight());
                                peptideLayout.addComponent(peptideTableLayout);
                                CustomExportBtnLayout ce3 = new CustomExportBtnLayout(handler, "protPep", handler.getMainDataset().getDatasetId(), handler.getMainDataset().getName(), accession, otherAccession, null, 0, peptideProteintList, null, null, desc);
                                PopupView ExportDatasetProtenPeptidesLayout = new PopupView("Export Peptides from Selected Dataset for ( " + accession + " )", ce3);
                                ExportDatasetProtenPeptidesLayout.setDescription("Export Peptides from ( " + handler.getMainDataset().getName() + " ) Dataset for ( " + accession + " )");
                                peptideTableLayout.setExpBtnPepTable(ExportDatasetProtenPeptidesLayout);

                            }
                            fractionsList = handler.getFractionsList(handler.getMainDataset().getDatasetId());
                            handler.retrieveStandardProtPlotList();
                            if (handler.getMainDataset() == null || handler.getMainDataset().getStanderdPlotProt() == null || handler.getMainDataset().getStanderdPlotProt().isEmpty() || fractionsList == null || fractionsList.isEmpty()) {
                                if (protTableLayout.getProteinTableComponent() != null) {
                                    protTableLayout.getProteinTableComponent().setHeight("267.5px");
                                    protTableLayout.setProtTableHeight("267.5px");
                                }
                                if (peptideTableLayout.getPepTable() != null) {
                                    peptideTableLayout.getPepTable().setHeight("267.5px");
                                    peptideTableLayout.setPeptideTableHeight("267.5px");
                                }
                            } else {
                                if (handler.getMainDataset() != null && handler.getMainDataset().getProteinList() != null) {
                                    handler.getMainDataset().setFractionsList(fractionsList);
                                    handler.getDatasetList().put(handler.getMainDataset().getDatasetId(), handler.getMainDataset());
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
                                   
                                    Map<Integer, ProteinBean> proteinFractionAvgList = handler.getProteinFractionAvgList(accession + "," + otherAccession, fractionsList, handler.getMainDataset().getDatasetId());
                                    if (proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
                                        fractionLayout.removeAllComponents();
                                    } else {
                                       fractionLayout.addComponent(new FractionsLayout(
                                                accession, mw, proteinFractionAvgList, handler.getMainDataset().getStanderdPlotProt(), handler.getMainDataset().getName()));
                                    }
                                }
                            }
                        }
                    }
                };
                //add prot table listener
                protTableLayout.getProteinTableComponent().addValueChangeListener(listener);
                protTableLayout.setListener(listener);

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
        }
    }
    private int protIndex = 1;

    
    /**
     * initialize the selected dataset details component
     * @param visibility 
     */
    private DatasetDetailsComponent initDatasetDetailsLayout(boolean visibility) {
        return new DatasetDetailsComponent(visibility, handler);
    }
}
