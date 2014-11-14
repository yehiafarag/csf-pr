
package probe.com.view;

import probe.com.view.components.ProteinsTableLayout;
import probe.com.view.components.PeptidesTableLayout;
import probe.com.view.components.GelFractionsLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.components.DatasetDetailsComponent;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.IconGenerator;

/**
 *
 * @author Yehia Farag
 */
public final class ProjectsLayout extends VerticalLayout implements Serializable, Property.ValueChangeListener {

//    private final TreeMap<Integer, String> datasetNamesList;
    //dataset details layout visability
    private boolean visability = false;
    private ComboBox selectDataset;
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
    private Map<Integer, IdentificationProteinBean> fractionsList = null;
    private PeptidesTableLayout peptideTableLayout;
    private VerticalLayout fractionLayout;
    private Map<String, IdentificationProteinBean> proteinsList;
    private final VerticalLayout typeILayout;
//    private final GeneralUtil util = new GeneralUtil();
    private TreeMap<Integer, Object> selectionIndexes;
    private int nextIndex;
    private IconGenerator iconGenerator = new IconGenerator();
    private HorizontalLayout selectDatasetLayout;
    private VerticalLayout identificationSelectLayout,quantificationSelectLayout;
    private Property.ValueChangeListener protTableListener;
    private ActionButtonTextField.ClickListener searchTableListener;
    private Button.ClickListener searchBtnLabelListener;
    private int fractionNumber=0;
    /**
     *
     * @param handler dataset handler
     */
    public ProjectsLayout(MainHandler handler) {
        this.setSizeFull();
        setMargin(true);
        this.handler = handler;
//        this.datasetNamesList = handler.getDatasetNamesList();
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
        selectDatasetLayout = new HorizontalLayout();
        selectDatasetLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        selectDatasetLayout.setHeight("70px");
        selectDatasetLayout.setWidth("100%");
        this.addComponent(selectDatasetLayout);
        
        
        
        
        /* start quntification layout */
        VerticalLayout selectDatatypeLayout = new VerticalLayout();
        selectDatatypeLayout.setHeight("70px");
        selectDatatypeLayout.setWidth("100%");
        Label selectDatasetTypeLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Select  Data Type :</h4>");
        selectDatasetTypeLabel.setContentMode(ContentMode.HTML);
        selectDatasetTypeLabel.setHeight("35px");
        selectDatatypeLayout.addComponent(selectDatasetTypeLabel);
        final OptionGroup datatypeSelect = new OptionGroup();
        datatypeSelect.addItems(Arrays.asList(new String[]{"Identification", "Quantification"}));

        datatypeSelect.setNullSelectionAllowed(false); // user can not 'unselect'
        datatypeSelect.setMultiSelect(false);
        
        Property.ValueChangeListener datatypeListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
               if(datatypeSelect.getValue().toString().equalsIgnoreCase("Identification")){
                   loadIdentificationLayout();
               }
               else{
                   
                   loadQuantificationLayout();
               }
            }
        };
        datatypeSelect.addValueChangeListener(datatypeListener);
         selectDatatypeLayout.addComponent(datatypeSelect);
        selectDatasetLayout.addComponent(selectDatatypeLayout);
        identificationSelectLayout = new VerticalLayout();
        quantificationSelectLayout= new VerticalLayout();
        identificationSelectLayout.setVisible(false);
        quantificationSelectLayout.setVisible(false);
        identificationSelectLayout.setHeight("70px");
        identificationSelectLayout.setWidth("100%");
        selectDatasetLayout.addComponent(identificationSelectLayout);
        selectDatasetLayout.addComponent(quantificationSelectLayout);
        selectDatasetLayout.setExpandRatio(selectDatatypeLayout, 1f);
        selectDatasetLayout.setExpandRatio(identificationSelectLayout, 5f);
        selectDatasetLayout.setExpandRatio(quantificationSelectLayout, 5f);
        
        datatypeSelect.select("Identification"); // select this by default
        datatypeSelect.setImmediate(true); // send the change to the server at once
        datatypeSelect.commit();
        

        /*          end         */
        
       
    }
    private void loadIdentificationLayout(){
        quantificationSelectLayout.setVisible(false);
        typeILayout.setVisible(true);
        
        identificationSelectLayout.removeAllComponents();
        identificationSelectLayout.setVisible(true);
         if (handler.getDatasetList() == null || handler.getDatasetList().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            identificationSelectLayout.addComponent(noExpLable);
        } else {
            Label selectDatasetLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Select Dataset :</h4>");
            selectDatasetLabel.setContentMode(ContentMode.HTML);
            selectDatasetLabel.setHeight("35px");
            identificationSelectLayout.addComponent(selectDatasetLabel);
            identificationSelectLayout.setComponentAlignment(selectDatasetLabel, Alignment.TOP_LEFT);

            HorizontalLayout selectDatasetListLayout = new HorizontalLayout();
            selectDatasetListLayout.setWidth("100%");
            identificationSelectLayout.addComponent(selectDatasetListLayout);
            selectDataset = new ComboBox();

            selectDataset.setStyleName(Runo.PANEL_LIGHT);
            selectDataset.addItem(defaultSelectString);
            selectDataset.setValue(defaultSelectString);
            selectDataset.setNullSelectionAllowed(false);
            selectDataset.setValue(defaultSelectString);
            selectDataset.focus();

            for (String str : handler.getDatasetNamesList().values()) {
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
    private void loadQuantificationLayout(){
        identificationSelectLayout.setVisible(false);
        typeILayout.setVisible(false);
        
    
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
            handler.setMainDatasetId(datasetString);
            fractionNumber = handler.getDataset(handler.getMainDatasetId()).getFractionsNumber();
            //layout for dataset type 1 Identification dataset
            if (handler.getMainDatasetId() != 0 &&  handler.getDataset(handler.getMainDatasetId()).getDatasetType() == 1) {
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

                proteinsList = handler.retriveProteinsList(handler.getMainDatasetId());
               
               if (protTableLayout != null) {
//                    protTableLayout.getProteinTableComponent().removeValueChangeListener(protTableListener);
                    typeILayout.removeComponent(protTableLayout);
                    }
                protTableLayout = new ProteinsTableLayout(proteinsList, handler.getDataset(handler.getMainDatasetId()).getFractionsNumber(), handler.getDataset(handler.getMainDatasetId()).getNumberValidProt(),  handler.getDataset(handler.getMainDatasetId()).getProteinsNumber());
                typeILayout.addComponent(protTableLayout);
                typeILayout.setComponentAlignment(protTableLayout, Alignment.TOP_LEFT);

                if (protTableListener == null){
                protTableListener = new Property.ValueChangeListener() {
                    /*
                     *change listener for protein selection
                     */

                    private static final long serialVersionUID = 1L;

                    @Override
                    @SuppressWarnings("SleepWhileHoldingLock")
                    public synchronized void valueChange(Property.ValueChangeEvent event) {
                        
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
                        if (proteinLabel != null) {
                            proteinLabel.rePaintLable("black");
                        }
                        if (protTableLayout.getProteinTableComponent().getValue() != null) {
                            proteinskey = (Integer) protTableLayout.getProteinTableComponent().getValue();
                            System.out.println("prot key "+proteinskey);
                        }
                        else
                            return;
                      
                        protTableLayout.setLastSelectedIndex(proteinskey);
                        final Item item = protTableLayout.getProteinTableComponent().getItem(proteinskey);
                        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                        proteinLabel.rePaintLable("white");
//                        try {
//                            Thread.sleep(10);
//                        } catch (InterruptedException iexp) {
//                            System.out.println(iexp.getLocalizedMessage());
//                        }
                        desc = (String)item.getItemProperty("Description").getValue();
                        accession = item.getItemProperty("Accession").getValue().toString();
                        otherAccession =(String)item.getItemProperty("Other Protein(s)").getValue();

                        CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, null, 0, null, null, null, desc);
                        CustomExportBtnLayout exportAllDatasetProteinsLayout = (new CustomExportBtnLayout(handler, "prots", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, proteinsList,handler.getDataset(handler.getMainDatasetId()).getFractionsNumber(), null, null, null, desc));

                        PopupView exportAllProteinPeptidePopup = new PopupView("Export Peptides from All Datasets for ( " + accession + " )", exportAllProteinPeptidesLayout);
                        exportAllProteinPeptidePopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) from All Datasets");
                        PopupView exportAllDatasetProteinPeptidesPopup = new PopupView("Export All Proteins from Selected Dataset", exportAllDatasetProteinsLayout);
                        exportAllDatasetProteinPeptidesPopup.setDescription("Export All Proteins from ( " + handler.getDataset(handler.getMainDatasetId()).getName() + " ) Dataset");
                        protTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidePopup, exportAllDatasetProteinPeptidesPopup);
                        if (proteinskey >= 0) {
                           
                            
                            
                            //testing 
                           
                            Map<Integer, PeptideBean> peptideProteintList = handler.getPeptidesProtList(handler.getMainDatasetId(), accession, otherAccession);
//                            if (handler.getMainDataset().getPeptideList() == null) {
//                                handler.getMainDataset().setPeptideList(peptideProteintList);
//                            } else {
//                                handler.getMainDataset().getPeptideList().putAll(peptideProteintList);
//                            }
                           
                            
                            
                            if (!peptideProteintList.isEmpty()) {
                                int validPep = handler.getValidatedPepNumber(peptideProteintList);
                                if (peptideTableLayout != null) {
                                    peptideLayout.removeComponent(peptideTableLayout);
                                }
                                peptideTableLayout = new PeptidesTableLayout(validPep, peptideProteintList.size(), desc, peptideProteintList, accession, handler.getDataset(handler.getMainDatasetId()).getName());
                                peptideLayout.setMargin(true);
                                peptideTableLayout.setHeight("" + protTableLayout.getHeight());
                                peptideLayout.setHeight("" + protTableLayout.getHeight());
                                peptideLayout.addComponent(peptideTableLayout);
                                CustomExportBtnLayout ce3 = new CustomExportBtnLayout(handler, "protPep", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, null, 0, peptideProteintList, null, null, desc);
                                PopupView ExportDatasetProtenPeptidesLayout = new PopupView("Export Peptides from Selected Dataset for ( " + accession + " )", ce3);
                                ExportDatasetProtenPeptidesLayout.setDescription("Export Peptides from ( " + handler.getDataset(handler.getMainDatasetId()).getName() + " ) Dataset for ( " + accession + " )");
                                peptideTableLayout.setExpBtnPepTable(ExportDatasetProtenPeptidesLayout);

                            }  
                            List<StandardProteinBean>  standerdProtList = handler.retrieveStandardProtPlotList(handler.getMainDatasetId());
                            
                            if(fractionNumber == 0 || handler.getMainDatasetId() == 0 || standerdProtList == null || standerdProtList.isEmpty() ){
                                fractionLayout.removeAllComponents();
                                if (protTableLayout.getProteinTableComponent() != null) {
                                    protTableLayout.getProteinTableComponent().setHeight("267.5px");
                                    protTableLayout.setProtTableHeight("267.5px");
                                }
                                if (peptideTableLayout.getPepTable() != null) {
                                    peptideTableLayout.getPepTable().setHeight("267.5px");
                                    peptideTableLayout.setPeptideTableHeight("267.5px");
                                }
                            }else{
                           
                            fractionsList = handler.getProtGelFractionsList(handler.getMainDatasetId(),accession, otherAccession);
                             
                                 
                                if (fractionsList != null && !fractionsList.isEmpty()) {
                             
//                                if (handler.getMainDatasetId() != 0 ){//&& handler.getMainDataset().getProteinList() != null) {
//                                    handler.getMainDataset().setFractionsList(fractionsList);
//                                    handler.getDatasetList().put(handler.getMainDataset().getDatasetId(), handler.getMainDataset());
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

//                                    Map<Integer, IdentificationProteinBean> proteinFractionAvgList = handler.getProteinFractionAvgList(accession + "," + otherAccession, fractionsList, handler.getMainDatasetId());
//                                    if (fractionsList==null || fractionsList.isEmpty()){//(proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
//                                        fractionLayout.removeAllComponents();
//                                    } else {
                                        GelFractionsLayout gelFractionLayout = new GelFractionsLayout(accession, mw,fractionsList, standerdProtList, handler.getDataset(handler.getMainDatasetId()).getName());
                                        gelFractionLayout.setMargin(new MarginInfo(false, false, false, true));
                                        fractionLayout.addComponent(gelFractionLayout);
//                                    }
//                                }
                            }
                            
                            }
                        }
                    }
                };
                }
                //add prot table listener
                protTableLayout.getProteinTableComponent().addValueChangeListener(protTableListener);
                protTableLayout.setListener(protTableListener);

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
    
//    private void initProteinsTableListeners()
//    {
//        if(protTableListener != null)
//            return;
//        this.protTableListener = new Property.ValueChangeListener() {
//                    /*
//                     *change listener for protein selection
//                     */
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
////                    @SuppressWarnings("SleepWhileHoldingLock")
//                    public synchronized void valueChange(Property.ValueChangeEvent event) {
//                        if (proteinLabel != null) {
//                            proteinLabel.rePaintLable("black");
//                        }
//                        if (starter != 1) {
//                            datasetDetailsLayout.hideDetails();
//                        }
//                        starter++;
//                        String desc = "";
//                        //fraction layout
//                        if (fractionLayout != null) {
//                            typeILayout.removeComponent(fractionLayout);
//                        }
//                        fractionLayout = new VerticalLayout();
//                        typeILayout.addComponent(fractionLayout);
//                        fractionLayout.setWidth("100%");
//                        //peptide layout
//                        if (peptideLayout != null) {
//                            typeILayout.removeComponent(peptideLayout);
//                        }
//                        peptideLayout = new VerticalLayout();
//                        typeILayout.addComponent(peptideLayout);
//                        peptideLayout.setWidth("100%");
//                        if (protTableLayout.getProteinTableComponent().getValue() != null) {
//                            proteinskey = (Integer) protTableLayout.getProteinTableComponent().getValue();
//                        }
//                        final Item item = protTableLayout.getProteinTableComponent().getItem(proteinskey);
//                        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
//                        proteinLabel.rePaintLable("white");
////                        try {
////                            Thread.sleep(10);
////                        } catch (InterruptedException iexp) {
////                            System.out.println(iexp.getLocalizedMessage());
////                        }
//                        desc = item.getItemProperty("Description").toString();
//                        accession = item.getItemProperty("Accession").toString();
//                        otherAccession = item.getItemProperty("Other Protein(s)").toString();
//
//                        CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, null, 0, null, null, null, desc);
//                        CustomExportBtnLayout exportAllDatasetProteinsLayout = (new CustomExportBtnLayout(handler, "prots", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, proteinsList,handler.getDataset(handler.getMainDatasetId()).getFractionsNumber(), null, null, null, desc));
//
//                        PopupView exportAllProteinPeptidePopup = new PopupView("Export Peptides from All Datasets for ( " + accession + " )", exportAllProteinPeptidesLayout);
//                        exportAllProteinPeptidePopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) from All Datasets");
//                        PopupView exportAllDatasetProteinPeptidesPopup = new PopupView("Export All Proteins from Selected Dataset", exportAllDatasetProteinsLayout);
//                        exportAllDatasetProteinPeptidesPopup.setDescription("Export All Proteins from ( " + handler.getDataset(handler.getMainDatasetId()).getName() + " ) Dataset");
//                        protTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidePopup, exportAllDatasetProteinPeptidesPopup);
//                        if (proteinskey >= 0) {
//                           
//                            
//                            
//                            //testing 
//                            if(true){
//                            Map<Integer, PeptideBean> peptideProteintList = handler.getPeptidesProtList(handler.getMainDatasetId(), accession, otherAccession);
////                            if (handler.getMainDataset().getPeptideList() == null) {
////                                handler.getMainDataset().setPeptideList(peptideProteintList);
////                            } else {
////                                handler.getMainDataset().getPeptideList().putAll(peptideProteintList);
////                            }
//                           
//                            
//                            
//                            if (!peptideProteintList.isEmpty()) {
//                                int validPep = handler.getValidatedPepNumber(peptideProteintList);
//                                if (peptideTableLayout != null) {
//                                    peptideLayout.removeComponent(peptideTableLayout);
//                                }
//                                peptideTableLayout = new PeptidesTableLayout(validPep, peptideProteintList.size(), desc, peptideProteintList, accession, handler.getDataset(handler.getMainDatasetId()).getName());
//                                peptideLayout.setMargin(true);
//                                peptideTableLayout.setHeight("" + protTableLayout.getHeight());
//                                peptideLayout.setHeight("" + protTableLayout.getHeight());
//                                peptideLayout.addComponent(peptideTableLayout);
//                                CustomExportBtnLayout ce3 = new CustomExportBtnLayout(handler, "protPep", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, null, 0, peptideProteintList, null, null, desc);
//                                PopupView ExportDatasetProtenPeptidesLayout = new PopupView("Export Peptides from Selected Dataset for ( " + accession + " )", ce3);
//                                ExportDatasetProtenPeptidesLayout.setDescription("Export Peptides from ( " + handler.getDataset(handler.getMainDatasetId()).getName() + " ) Dataset for ( " + accession + " )");
//                                peptideTableLayout.setExpBtnPepTable(ExportDatasetProtenPeptidesLayout);
//
//                            } 
//                            
//                           
//                            fractionsList = handler.getProtGelFractionsList(handler.getMainDatasetId(),accession, otherAccession);
//                             
//                                 
//                                 List<StandardProteinBean>  standerdProtList = handler.retrieveStandardProtPlotList(handler.getMainDatasetId());
//                            if (handler.getMainDatasetId() == 0 || standerdProtList == null || standerdProtList.isEmpty() || fractionsList == null || fractionsList.isEmpty()) {
//                                 fractionLayout.removeAllComponents();
//                                if (protTableLayout.getProteinTableComponent() != null) {
//                                    protTableLayout.getProteinTableComponent().setHeight("267.5px");
//                                    protTableLayout.setProtTableHeight("267.5px");
//                                }
//                                if (peptideTableLayout.getPepTable() != null) {
//                                    peptideTableLayout.getPepTable().setHeight("267.5px");
//                                    peptideTableLayout.setPeptideTableHeight("267.5px");
//                                }
//                            } else {
////                                if (handler.getMainDatasetId() != 0 ){//&& handler.getMainDataset().getProteinList() != null) {
////                                    handler.getMainDataset().setFractionsList(fractionsList);
////                                    handler.getDatasetList().put(handler.getMainDataset().getDatasetId(), handler.getMainDataset());
//                                    double mw = 0.0;
//                                    try {
//                                        mw = Double.valueOf(item.getItemProperty("MW").toString());
//                                    } catch (NumberFormatException e) {
//                                        String str = item.getItemProperty("MW").toString();
//                                        String[] strArr = str.split(",");
//                                        if (strArr.length > 1) {
//                                            str = strArr[0] + "." + strArr[1];
//                                        }
//                                        mw = Double.valueOf(str);
//                                    }
//
////                                    Map<Integer, IdentificationProteinBean> proteinFractionAvgList = handler.getProteinFractionAvgList(accession + "," + otherAccession, fractionsList, handler.getMainDatasetId());
////                                    if (fractionsList==null || fractionsList.isEmpty()){//(proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
////                                        fractionLayout.removeAllComponents();
////                                    } else {
//                                        fractionLayout.addComponent(new GelFractionsLayout(accession, mw,fractionsList, standerdProtList, handler.getDataset(handler.getMainDatasetId()).getName()));
////                                    }
////                                }
//                            }
//                            
//                        }//end if testing
//                        }
//                    }
//                };
//                
//                selectionIndexes = handler.getSearchIndexesSet(protTableLayout.getProteinTableComponent().getTableSearchMap(), protTableLayout.getProteinTableComponent().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toUpperCase().trim());
//                protTableLayout.getProteinTableComponent().setCurrentPageFirstItemIndex(protTableLayout.getProteinTableComponent().getFirstIndex());
//                protTableLayout.getProteinTableComponent().select(protTableLayout.getProteinTableComponent().getFirstIndex());
//                protTableLayout.getProteinTableComponent().commit();
//                final ActionButtonTextField searchButtonTextField = ActionButtonTextField.extend(protTableLayout.getSearchField());
//                searchButtonTextField.getState().type = ActionButtonType.ACTION_SEARCH;
//                if(searchTableListener == null ){
//                searchTableListener =new ActionButtonTextField.ClickListener() {
//                    @Override
//                    public void buttonClick(ActionButtonTextField.ClickEvent clickEvent) {
//                        selectionIndexes = handler.getSearchIndexesSet(protTableLayout.getProteinTableComponent().getTableSearchMap(), protTableLayout.getProteinTableComponent().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toUpperCase().trim());
//                        System.err.println("clicked -- "+selectionIndexes);
//                        if (!selectionIndexes.isEmpty()) {
//                            if (selectionIndexes.size() > 1) {
//                                protTableLayout.getNextSearch().setEnabled(true);
//                                protTableLayout.getNextSearch().focus();
//                            } else {
//                                protTableLayout.getNextSearch().setEnabled(false);
//                            }
//                            protIndex = 1;
//                            nextIndex = selectionIndexes.firstKey();
//                            protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
//                            System.out.println(" selected now "+selectionIndexes.get(nextIndex));
//                            protTableLayout.getProteinTableComponent().setCurrentPageFirstItemIndex(selectionIndexes.get(nextIndex));
//                            protTableLayout.getProteinTableComponent().select(selectionIndexes.get(nextIndex));
//                            protTableLayout.getProteinTableComponent().commit();
////                            protTableLayout.getProteinTableComponent().refreshRowCache();
//
//                        } else {
//                            Notification.show("Not Exist");
//                            protIndex = 1;
//                        }
//
//                    }
//                };
//                }
//        searchButtonTextField.addClickListener(searchTableListener);
//                protTableLayout.getSearchField().addFocusListener(new FieldEvents.FocusListener() {
//                    @Override
//                    public void focus(FieldEvents.FocusEvent event) {
//                        protTableLayout.getNextSearch().setEnabled(false);
//                        protTableLayout.getProtCounter().setValue("");
//
//                        protIndex = 1;
//
//                    }
//                });
//                if(searchBtnLabelListener == null){
//
//                searchBtnLabelListener = new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(Button.ClickEvent event) {
//                        nextIndex = selectionIndexes.higherKey(nextIndex);
//                        protTableLayout.getProteinTableComponent().setCurrentPageFirstItemIndex(selectionIndexes.get(nextIndex));
//                        protTableLayout.getProteinTableComponent().select(selectionIndexes.get(nextIndex));
//                        protTableLayout.getProteinTableComponent().commit();
//                        protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
//                        if (nextIndex == selectionIndexes.lastKey()) {
//                            nextIndex = selectionIndexes.firstKey() - 1;
//                            protIndex = 1;
//                        }
//                    }
//                };
//                }
//                protTableLayout.getNextSearch().addClickListener(searchBtnLabelListener);
//    
//    }


}
