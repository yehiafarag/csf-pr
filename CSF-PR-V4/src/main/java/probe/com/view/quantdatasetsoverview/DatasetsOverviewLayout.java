package probe.com.view.quantdatasetsoverview;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.handlers.MainHandler;
import probe.com.selectionmanager.FilterUtility;
import probe.com.view.quantdatasetsoverview.heatmap.DatasetExploringHeatMapFilters;
import probe.com.view.components.ExploreDatasetsTableLayout;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.quantdatasetsoverview.popupinteractivefilter.PopupInteractiveFilterComponent;

/**
 * This is the studies layout include publication heatmapFiltere and publication table
 *
 * @author Yehia Farag
 */
public class DatasetsOverviewLayout extends VerticalLayout {

    private final PopupInteractiveFilterComponent filtersLayout;
//     private final DatasetsExplorerTreeLayout studiesExplorerTreeLayout;
    private final FilterUtility filterUtility;
    private final DatasetExploringSelectionManagerRes exploringFiltersManager;
    private final  ProteinsLayout proteinsLayout;

    public DatasetsOverviewLayout(MainHandler handler) {

        filterUtility = new FilterUtility(handler);
        exploringFiltersManager = new DatasetExploringSelectionManagerRes(filterUtility.getQuantDatasetArr(), filterUtility.getActiveFilters());//,filterUtility.getFullFilterList()

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();
        
        //init level 1 heatmapFiltere    
        filtersLayout = new PopupInteractiveFilterComponent(exploringFiltersManager);
//        HorizontalLayout comparisonLayout = new HorizontalLayout();
//        comparisonLayout.setWidth("100%");
//        this.addComponent(comparisonLayout);
        
        
        QuantProteinsComparisonTable compTable = new QuantProteinsComparisonTable(exploringFiltersManager, handler);        
        DatasetExploringHeatMapFilters heatmapFilter = new DatasetExploringHeatMapFilters(exploringFiltersManager,filtersLayout,compTable);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));
        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null);
        this.addComponent(comparisonLevelLayout);     
        comparisonLevelLayout.setVisability(true);
        
        proteinsLayout = new ProteinsLayout(exploringFiltersManager);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins", proteinsLayout, null){

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                super.layoutClick(event); //To change body of generated methods, choose Tools | Templates.
                proteinsLayout.redrawCharts();
            }        
        
        };
        this.addComponent(proteinsLevelLayout);
        proteinsLevelLayout.setVisability(true);
        
//         DatasetExploringHM datasestExploringHM = new DatasetExploringHM(exploringFiltersManager);
//        this.addComponent(datasestExploringHM);
        
//        PublicationExplorerTreeLayout publicationTreeLayout = new PublicationExplorerTreeLayout(handler.getQuantDatasetListObject().getQuantDatasetList(),exploringFiltersManager);
//        comparisonLayout.addComponent(publicationTreeLayout);

//        //init level 1 heatmapFiltere    
//        filtersLayout = filterUtility.initPopupFiltersLayout(exploringFiltersManager);
//
//        MiniFilterLayout minLayout = new MiniFilterLayout(exploringFiltersManager);
//        HideOnClickLayout mainExploringFiltersLayout = new HideOnClickLayout("Datasets Exploring", filtersLayout, minLayout);
//        this.addComponent(mainExploringFiltersLayout);
//        mainExploringFiltersLayout.setVisability(true);
//        //level 2 table layout
//
////level 3 study overall chart
//        studiesExplorerTreeLayout = new DatasetsExplorerTreeLayout(exploringFiltersManager);
//        //init level 2 heatmapFiltere
//        this.addComponent(studiesExplorerTreeLayout);
//
        ExploreDatasetsTableLayout studiesTable = new ExploreDatasetsTableLayout(exploringFiltersManager,filterUtility.getActiveHeaders());
        this.addComponent(studiesTable); 


    }
    
    public void redrawCharts(){
    proteinsLayout.redrawCharts();
    }
    
    
    
    

    

    
}
