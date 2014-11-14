/*
 */
package probe.com.view;

import probe.com.view.components.PeptidesTableLayout;
import probe.com.view.components.GelFractionsLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.QuantificationProteinsBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.components.SearchFiltersLayout;
import probe.com.view.components.IdentificationSearchResultsTableLayout;
import probe.com.view.components.QuantSearchResultLayout;
import probe.com.view.core.CustomErrorLabel;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 */
public class SearchLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    
    private final SearchFiltersLayout searchingUnit;
    
    private final VerticalLayout searchLayout = new VerticalLayout();
    private final VerticalLayout identificationSearchLayout = new VerticalLayout();    
    private final VerticalLayout quantificationSearchLayout = new VerticalLayout();
//    private final HorizontalLayout topRightLayout = new HorizontalLayout();
//    private final VerticalLayout topLeftLayout = new VerticalLayout();
//    private TextArea searchField;
  
    private final MainHandler handler;
    
    private Label  errorLabelI;// = new  Label();
    private CustomErrorLabel errorLabelII;
   

    private final VerticalLayout searchTableLayout = new VerticalLayout();
    private final VerticalLayout protSerarchLayout = new VerticalLayout();
    private final VerticalLayout peptidesLayout = new VerticalLayout();
    private final VerticalLayout fractionLayout = new VerticalLayout();
    private CustomExternalLink searchTableAccessionLable;
    private int key = -1;
    private String accession;
    private String otherAccession, desc;
    private PeptidesTableLayout peptideTableLayout;
    private Map<Integer, IdentificationProteinBean> fractionsList = null;
    

     private int fractionNumber = 0;
//    final OptionGroup typeOfStudySelection = new OptionGroup();
//    private String typeofStudy, sampleType, patientGroupValueI, patientGroupValueII, patientSubGroupValueI, patientSubGroupValueII, technologyType, analyticalApproachI, analyticalMethod, shotgunTargetedQuant;

   
//    private final Button doneFilterBtn = new Button("Search");

    /**
     *
     * @param handler dataset handler
     */
    public SearchLayout(MainHandler handler) {
        this.handler = handler;
        
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingUnit = new SearchFiltersLayout(handler);
        this.addComponent(searchingUnit);
        
        this.setSpacing(true);
        //errorLabelI error in search keyword 
        initErrorLabels();
        this.addComponent(searchLayout);
       
        searchLayout.setSpacing(true);
        searchLayout.setMargin(new MarginInfo(true, false, true, false));
//        filtersController.setVisible(true);
//        filtersController.setSpacing(true);
        identificationSearchLayout.addComponent(searchTableLayout);
        searchTableLayout.setWidth("100%");
        identificationSearchLayout.addComponent(protSerarchLayout);

        protSerarchLayout.addComponent(fractionLayout);
        protSerarchLayout.addComponent(peptidesLayout);

        fractionLayout.setWidth("100%");
        peptidesLayout.setWidth("100%");

        searchLayout.setVisible(false);
        searchLayout.addComponent(identificationSearchLayout);
        searchLayout.addComponent(quantificationSearchLayout);
        searchingUnit.getMainSearchButton().addClickListener(this);

    }

 
   
   
    private void initErrorLabels() {
        errorLabelI = new Label("<h4 Style='color:red;'>Please Enter Valid Key Word </h4>");
        errorLabelI.setContentMode(ContentMode.HTML);
        errorLabelI.setHeight("30px");
        this.addComponent(errorLabelI);
        this.setComponentAlignment(errorLabelI, Alignment.TOP_LEFT);
        errorLabelI.setVisible(false);
        errorLabelII = new CustomErrorLabel();
        this.addComponent(errorLabelII);
        this.setComponentAlignment(errorLabelII, Alignment.TOP_LEFT);
        errorLabelII.setVisible(false);

    }

    
    
    
    

    /**
     * on click validate search inputs and start searching process
     *
     * @param event user search click buttons
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        
        searchLayout.setVisible(false);
        searchTableLayout.removeAllComponents();
        peptidesLayout.removeAllComponents();
        fractionLayout.removeAllComponents();
        errorLabelI.setVisible(false);
        errorLabelII.setVisible(false);

       //will we allow empty search??
        //        if (protSearchObject == null || protSearchObject.toString().equals("") || protSearchObject.toString().length() < 4 ) {
        //            errorLabelI.setVisible(true);
        //            searchField.focus();
        //        } 
//        else {
          boolean validQuery = searchingUnit.getFiltersController().isValidQuery();
            if (!validQuery) {
                errorLabelI.setVisible(true);
                searchingUnit.getKeywordFilter().getSearchField().focus();
            } else {
                Map<Integer, IdentificationProteinBean> searchIdentificationProtList = null;
                Map<Integer, QuantificationProteinsBean> searchQuantificationProtList = null;

                String notFound = "";
                //, "Quantification Data", "Both"
                if (searchingUnit.getFiltersController().getQuery().getSearchDataType().equalsIgnoreCase("Identification Data")) {            
          
                searchingUnit.getFiltersController().getQuery().setSearchKeyWords(searchingUnit.getKeywordFilter().getSearchField().getValue());
                String defaultText = searchingUnit.getFiltersController().getQuery().getSearchKeyWords();
                
                defaultText = defaultText.replace(",", "\n").replace(" ", "").trim();
                searchingUnit.getKeywordFilter().getSearchField().setValue(defaultText);
                searchIdentificationProtList = handler.searchIdentificationProtein(searchingUnit.getFiltersController().getQuery());
                notFound = handler.filterSearchingKeywords(searchIdentificationProtList, searchingUnit.getFiltersController().getQuery().getSearchKeyWords(), searchingUnit.getFiltersController().getQuery().getSearchBy());
                System.out.println("not found is " + notFound + "   size " + searchIdentificationProtList.size());
                
            }
            
        else {// start quant searching
            
            searchQuantificationProtList = handler.searchQuantificationProtein(searchingUnit.getFiltersController().getQuery());
            
        }

        //searching process  end here
        searchLayout.setVisible(true);
        if (!notFound.equals("")) {
            notFind(notFound);
        }
        boolean test = true, test2 = true;
        if (searchIdentificationProtList == null || searchIdentificationProtList.isEmpty()) {
            identificationSearchLayout.setVisible(false);
            test = false;
        }
        if (searchQuantificationProtList == null || searchQuantificationProtList.isEmpty()) {
            quantificationSearchLayout.setVisible(false);
            test2 = false;
        }        
        
        if (test || test2) {
            searchLayout.setVisible(true);
            searchingUnit.hideLayout();
            if (searchingUnit.getFiltersController().getQuery().getSearchDataType().equalsIgnoreCase("Identification Data")) {
                final IdentificationSearchResultsTableLayout searcheResultsTableLayout = new IdentificationSearchResultsTableLayout(handler, handler.getDatasetDetailsList(), searchIdentificationProtList, searchingUnit.getFiltersController().getQuery().isValidatedProteins());
                searchTableLayout.addComponent(searcheResultsTableLayout);
                Property.ValueChangeListener listener = new Property.ValueChangeListener() {
                    /*
                     *the main listener for search table
                     */
                    private static final long serialVersionUID = 1L;

                    /**
                     * on select search table value initialize the peptides
                     * table and fractions plots if exist * process
                     *
                     * @param event value change on search table selection
                     */
                    @Override
                    public synchronized void valueChange(Property.ValueChangeEvent event) {
                        
                        if (searchTableAccessionLable != null) {
                            searchTableAccessionLable.rePaintLable("black");
                        }
                        
                        if (searcheResultsTableLayout.getSearchTable().getValue() != null) {
                            key = (Integer) searcheResultsTableLayout.getSearchTable().getValue();
                        }
                        else
                            return;
                        final Item item = searcheResultsTableLayout.getSearchTable().getItem(key);
                        searchTableAccessionLable = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                        searchTableAccessionLable.rePaintLable("white");
      
                        peptidesLayout.removeAllComponents();
                        fractionLayout.removeAllComponents();
                        
                        String datasetName = item.getItemProperty("Experiment").toString();
                        accession = item.getItemProperty("Accession").toString();
                        otherAccession = item.getItemProperty("Other Protein(s)").toString();
                        desc = item.getItemProperty("Description").toString();
                        
                        handler.setMainDatasetId(datasetName);
                        
                        fractionNumber = handler.getDataset(handler.getMainDatasetId()).getFractionsNumber();
                        if (handler.getMainDatasetId() != 0 && handler.getDataset(handler.getMainDatasetId()).getDatasetType() == 1) {
                          CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", handler.getMainDatasetId(), datasetName, accession, otherAccession, null, 0, null, null, null, desc);
                            PopupView exportAllProteinPeptidesPopup = new PopupView("Export Peptides from All Datasets for (" + accession + " )", exportAllProteinPeptidesLayout);
                            exportAllProteinPeptidesPopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) for All Available Datasets");
                            searcheResultsTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidesPopup);// new PopupView("Export Proteins", (new CustomExportBtnLayout(handler, "prots",datasetId, datasetName, accession, otherAccession, datasetList, proteinsList, dataset.getFractionsNumber(), null,null))));
                            if (key >= 0) {
                                
                                Map<Integer, PeptideBean> pepProtList = handler.getPeptidesProtList(handler.getMainDatasetId(), accession, otherAccession);
                    
                                if (!pepProtList.isEmpty()) {
                                    int validPep = handler.getValidatedPepNumber(pepProtList);
                                    if (peptideTableLayout != null) {
                                        peptidesLayout.removeComponent(peptideTableLayout);
                                    }
                                    peptideTableLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, handler.getDataset(handler.getMainDatasetId()).getName());
                                    peptidesLayout.setMargin(false);
                                    peptidesLayout.addComponent(peptideTableLayout);
                                    
                                    CustomExportBtnLayout exportAllProteinsPeptidesLayout = new CustomExportBtnLayout(handler, "protPep", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, null, 0, pepProtList, null, null, desc);
                                    PopupView exportAllProteinsPeptidesPopup = new PopupView("Export Peptides from Selected Dataset for (" + accession + " )", exportAllProteinsPeptidesLayout);
                                    
                                    exportAllProteinsPeptidesPopup.setDescription("Export Peptides from ( " + handler.getDataset(handler.getMainDatasetId()).getName() + " ) Dataset for ( " + accession + " )");
                                    peptideTableLayout.setExpBtnPepTable(exportAllProteinsPeptidesPopup);
                                    
                                }
                              List<StandardProteinBean> standerdProtList = handler.retrieveStandardProtPlotList(handler.getMainDatasetId());//                          

                                if (fractionNumber == 0 || handler.getMainDatasetId() == 0 || standerdProtList == null || standerdProtList.isEmpty()) {
                                    fractionLayout.removeAllComponents();
                                    if (searcheResultsTableLayout.getSearchTable() != null) {
                                        searcheResultsTableLayout.getSearchTable().setHeight("267.5px");
                                    }
                                    if (peptideTableLayout.getPepTable() != null) {
                                        peptideTableLayout.getPepTable().setHeight("267.5px");
                                        peptideTableLayout.setPeptideTableHeight("267.5px");
                                    }
                                } else {
                                    fractionsList = handler.getProtGelFractionsList(handler.getMainDatasetId(), accession, otherAccession);
                                    
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

                                        fractionLayout.addComponent(new GelFractionsLayout(accession, mw, fractionsList, standerdProtList, handler.getDataset(handler.getMainDatasetId()).getName()));

                                    }
                                }
                            }
                        }
                    }
                    
                };
                searcheResultsTableLayout.setListener(listener);
                searcheResultsTableLayout.getSearchTable().addValueChangeListener(listener);
                identificationSearchLayout.setVisible(true);
            } else {
                quantificationSearchLayout.removeAllComponents();
//                Panel quantPanel = new Panel();
//                quantPanel.setHeight("400px");
                QuantSearchResultLayout quantResultLayout = new QuantSearchResultLayout(searchQuantificationProtList,null);
//                quantPanel.setContent(quantResultLayout);
                quantificationSearchLayout.addComponent(quantResultLayout);
                quantificationSearchLayout.setVisible(true);
                
                
                
            }
        }
        }
    }

    /**
     * no searching results found
     *
     * @param notFound
     */
    private void notFind(String notFound) {
        errorLabelII.updateErrot(notFound);
        errorLabelII.setVisible(true);

    }

}
