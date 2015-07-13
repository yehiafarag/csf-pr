package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.handlers.MainHandler;
import probe.com.selectionmanager.FilterUtility;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.components.ExploreDatasetsTableLayout;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters.StudiesPieChartFiltersContainerLayout;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;

/**
 * This is the studies layout include publication heatmapFiltere and publication table
 *
 * @author Yehia Farag
 */
public class QuantDatasetsOverviewLayout extends VerticalLayout {

    private final StudiesPieChartFiltersContainerLayout pieChartFiltersLayout;
//     private final DatasetsExplorerTreeLayout studiesExplorerTreeLayout;
    private final FilterUtility filterUtility;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final  QuantProteinsTabsheetContainerLayout proteinsLayout;
    private final   QuantProteinsComparisonsContainer  compTableLayout;

    public QuantDatasetsOverviewLayout(MainHandler handler) {

        filterUtility = new FilterUtility(handler);
        exploringFiltersManager = new DatasetExploringCentralSelectionManager(filterUtility.getQuantDatasetArr(), filterUtility.getActiveFilters());//,filterUtility.getFullFilterList()

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();
        // init piecharts filter
        pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(exploringFiltersManager);

        compTableLayout = new QuantProteinsComparisonsContainer(exploringFiltersManager, handler);
        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(exploringFiltersManager, pieChartFiltersLayout.getPieChartFiltersBtn(), compTableLayout);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));

        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null);
        this.addComponent(comparisonLevelLayout);     
        comparisonLevelLayout.setVisability(true);
        
        proteinsLayout = new QuantProteinsTabsheetContainerLayout(exploringFiltersManager);
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
//        pieChartFiltersLayout = filterUtility.initPopupFiltersLayout(exploringFiltersManager);
//
//        MiniFilterLayout minLayout = new MiniFilterLayout(exploringFiltersManager);
//        HideOnClickLayout mainExploringFiltersLayout = new HideOnClickLayout("Datasets Exploring", pieChartFiltersLayout, minLayout);
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
