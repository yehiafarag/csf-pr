package probe.com.view.datasetsoverview;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.handlers.MainHandler;
import probe.com.selectionmanager.FilterUtility;
import probe.com.view.datasetsoverview.heatmap.DatasetExploringHeatMapFilters;
import probe.com.view.components.ExploreDatasetsTableLayout;
import probe.com.view.components.PublicationExplorerTreeLayout;
import probe.com.view.datasetsoverview.popupinteractivefilter.PopupInteractiveFilterComponent;

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

    public DatasetsOverviewLayout(MainHandler handler) {

        filterUtility = new FilterUtility(handler);
        exploringFiltersManager = new DatasetExploringSelectionManagerRes(filterUtility.getQuantDatasetArr(), filterUtility.getActiveFilters());//,filterUtility.getFullFilterList()

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        
        //init level 1 heatmapFiltere    
        filtersLayout = new PopupInteractiveFilterComponent(exploringFiltersManager);
        HorizontalLayout comparisonLayout = new HorizontalLayout();
        comparisonLayout.setWidth("100%");
        this.addComponent(comparisonLayout);
        
        
        QuantProteinsComparisonTable compTable = new QuantProteinsComparisonTable(exploringFiltersManager, handler);       
        
        DatasetExploringHeatMapFilters heatmapFilter = new DatasetExploringHeatMapFilters(exploringFiltersManager,filtersLayout,compTable);
        comparisonLayout.addComponent(heatmapFilter);       
        heatmapFilter.setWidth("100%");
        
        
        
//        
//         DatasetExploringHM datasestExploringHM = new DatasetExploringHM(exploringFiltersManager);
//        this.addComponent(datasestExploringHM);

//      
        
        
        
//        
//        
//        PublicationExplorerTreeLayout publicationTreeLayout = new PublicationExplorerTreeLayout(handler.getQuantDatasetListObject().getQuantDatasetList(),exploringFiltersManager);
//        comparisonLayout.addComponent(publicationTreeLayout);
//        
        
//        
//        
//        
//
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
    
    
    
    

    

    
}
