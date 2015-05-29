/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.QuantProtein;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.ShowLabel;
import probe.com.view.core.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class QuantSearchResultLayout extends VerticalLayout implements Serializable,com.vaadin.event.LayoutEvents.LayoutClickListener,Property.ValueChangeListener{

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
    if (stat) {
                    stat = false;
                    show.updateIcon(false);
                    mainLayout.setVisible(false);
                } else {
                    stat = true;
                    show.updateIcon(true);
                    mainLayout.setVisible(true);
                }    
    }
    
    private String searchTableHeight,peptideTableHeight = "260px";
    private TableResizeSet trs;
    private PeptideTable peptideTable;
    private VerticalLayout mainLayout;
    private ShowLabel show,showPeptideLayout;
    private boolean stat = true;
    private boolean peptideStat = true;
    private VerticalLayout searchTableLayout = new VerticalLayout();
     private PopupView expBtnSearchResultsTable,exportBtnPeptideTable;
    QuantDataSearchTable quantTable ;//= new  QuantDataSearchTable(null, null);
    private final Set<String> filtersList;
    private  Label peptideTableLabel;
    private final VerticalLayout exportSearchTableLayout = new VerticalLayout();
    private final VerticalLayout exportPeptideTableLayout = new VerticalLayout();
    private final HorizontalLayout tableFilterControllerLayout =new HorizontalLayout();
      private final HorizontalLayout peptideFilterControllerLayout =new HorizontalLayout();
    private  List<QuantProtein>  quantReptideProtList;
    public QuantSearchResultLayout( List<QuantProtein>  quantProtList,Set<String> filtersList){
        this.filtersList = filtersList;
        this.setSpacing(true);
          quantReptideProtList = new ArrayList<QuantProtein>();
        Map<String,QuantProtein> filteredProtPep = new HashMap<String, QuantProtein>();

        int keyindex = 0 ;
        for (QuantProtein qp : quantProtList) {
            if (qp.isPeptideProt()) {
                quantReptideProtList.add(qp);
                 filteredProtPep.put(qp.getqPeptideKey(), qp);
            }
            else
                filteredProtPep.put((keyindex++)+"", qp);

        }

        quantProtList.clear();
        quantProtList.addAll(filteredProtPep.values());

        
        
        quantTable = new QuantDataSearchTable(quantProtList,filtersList );
        quantTable.addValueChangeListener(QuantSearchResultLayout.this);
        MarginInfo m = new MarginInfo(false, false, true, false);
        this.setMargin(m);
        this.setSpacing(false);
        this.setWidth("100%");

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setHeight("20px");
        titleLayout.setSpacing(true);
        show = new ShowLabel();
        show.setHeight("20px");
        show.updateIcon(stat);
        titleLayout.addComponent(show);
        titleLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

        Label filterstLabel = new Label("Searching Results ( "+quantProtList.size()+" )");//"<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Searching Filters</strong></h4>");
        filterstLabel.setContentMode(ContentMode.HTML);
        
        filterstLabel.setStyleName("filterShowLabel");
        filterstLabel.setHeight("20px");
        titleLayout.addComponent(filterstLabel);
        titleLayout.setComponentAlignment(filterstLabel, Alignment.TOP_RIGHT);

        this.addComponent(titleLayout);
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("10px");
        spacer.setWidth("100%");
        this.addComponent(spacer);
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        this.addComponent(mainLayout);
         mainLayout.addComponent(searchTableLayout);
        mainLayout.setComponentAlignment(searchTableLayout, Alignment.MIDDLE_CENTER);
        mainLayout.addComponent(searchTableLayout);
        mainLayout.setComponentAlignment(searchTableLayout, Alignment.MIDDLE_CENTER);

        searchTableLayout.addComponent(quantTable);
        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        
        lowerLayout.setSpacing(false);
        mainLayout.addComponent(lowerLayout);
        mainLayout.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);

        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
       
        lowerLeftLayout.setSpacing(true);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false)); 
        lowerLayout.addComponent(lowerLeftLayout);
        lowerLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);
        
        final Button tableOptionBtn = new Button("Show Table Options");
        tableOptionBtn.setStyleName(Reindeer.BUTTON_SMALL);
        
        tableOptionBtn.setHeight("20px");
        lowerLeftLayout.addComponent(tableOptionBtn);
        lowerLeftLayout.setComponentAlignment(tableOptionBtn, Alignment.BOTTOM_LEFT);
        lowerLeftLayout.setMargin(new MarginInfo(false,false,false,true));
        tableOptionBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                quantTable.setInit(true);                
            tableFilterControllerLayout.setVisible(!tableFilterControllerLayout.isVisible());
            if(tableFilterControllerLayout.isVisible())
                tableOptionBtn.setCaption("Hide Table Options");
            else
                tableOptionBtn.setCaption("Show Table Options");
            }
            
        });

        final TableResizeSet trs1 = new TableResizeSet(quantTable, searchTableHeight);//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.TOP_LEFT);

        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerLayout.addComponent(lowerRightLayout);
        lowerLayout.setComponentAlignment(lowerRightLayout, Alignment.MIDDLE_RIGHT);
       

        exportSearchTableLayout.setWidth("300px");
        lowerRightLayout.addComponent(exportSearchTableLayout);
        lowerRightLayout.setComponentAlignment(exportSearchTableLayout, Alignment.BOTTOM_RIGHT);
        lowerRightLayout.setMargin(new MarginInfo(false,true,false,false));
        exportSearchTableLayout.setVisible(true);
        exportSearchTableLayout.removeAllComponents();
        
        CustomExportBtnLayout ce2 = new CustomExportBtnLayout(null, "searchResult", 0, null, null, null, null, 0, null, null, null, null);
        expBtnSearchResultsTable = new PopupView("Export CSF-PR Search Results", ce2);
        expBtnSearchResultsTable.setDescription("Export CSF-PR Search Results");
        expBtnSearchResultsTable.setHideOnMouseOut(false);
        exportSearchTableLayout.addComponent(expBtnSearchResultsTable);
       
        exportSearchTableLayout.setComponentAlignment(expBtnSearchResultsTable, Alignment.MIDDLE_RIGHT);
        mainLayout.setSpacing(true);       
        titleLayout.addLayoutClickListener(QuantSearchResultLayout.this);
        tableFilterControllerLayout.setVisible(false);
        tableFilterControllerLayout.setSpacing(false);
        tableFilterControllerLayout.addComponent(quantTable.getColumnController());
//        Button hideFilterBtn = new Button("Hide Filters");
//        hideFilterBtn.setStyleName(Reindeer.BUTTON_SMALL);
//       
//        tableFilterControllerLayout.addComponent(hideFilterBtn);
//        tableFilterControllerLayout.setComponentAlignment(hideFilterBtn,Alignment.BOTTOM_RIGHT);
//        hideFilterBtn.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                hideColumnFilter();
//            }
//        });
        
        
        
        mainLayout.addComponent(tableFilterControllerLayout);
        
        
        
        
        ///init peptide layout
        
        peptideTileLayout = new HorizontalLayout();
        peptideTileLayout.setHeight("20px");
        peptideTileLayout.setSpacing(true);
        showPeptideLayout = new ShowLabel();
        showPeptideLayout.setHeight("20px");
        showPeptideLayout.updateIcon(peptideStat);
        peptideTileLayout.addComponent(showPeptideLayout);
        peptideTileLayout.setComponentAlignment(showPeptideLayout, Alignment.BOTTOM_LEFT);

        peptideTableLabel = new Label();//"<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Searching Filters</strong></h4>");
        peptideTableLabel.setContentMode(ContentMode.HTML);
        
        peptideTableLabel.setStyleName("filterShowLabel");
        peptideTableLabel.setHeight("20px");
        peptideTileLayout.addComponent(peptideTableLabel);
        peptideTileLayout.setComponentAlignment(peptideTableLabel, Alignment.TOP_RIGHT);

        mainLayout.addComponent(peptideTileLayout);
        VerticalLayout pepSpacer = new VerticalLayout();
        pepSpacer.setHeight("10px");
        pepSpacer.setWidth("100%");
        mainLayout.addComponent(pepSpacer);        
        mainLayout.addComponent(peptidesLayout);
          peptidesLayout.setMargin(m);
            peptidesLayout.setSpacing(true);
            peptidesLayout.setWidth("100%");
              peptideTileLayout.setVisible(false);
              peptidesLayout.setVisible(false);
              
              peptideTileLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
           if (peptideStat) {
                    peptideStat = false;
                    showPeptideLayout.updateIcon(false);
                    peptidesLayout.setVisible(false);
                } else {
                    peptideStat = true;
                    showPeptideLayout.updateIcon(true);
                    peptidesLayout.setVisible(true);
                }    
            }
        });
    }
    private VerticalLayout peptidesLayout = new VerticalLayout();
      HorizontalLayout peptideTileLayout;

    private int protKey;
    private String searchTableProtNameLable;//,rawDataLinkLabel,pumedIdLabel;

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        //externalLinkTableLabel
//        if(searchTableProtNameLable!= null)
//            searchTableProtNameLable.rePaintLable("black");
//         if(rawDataLinkLabel!= null)
//            rawDataLinkLabel.rePaintLable("black");
//         if(pumedIdLabel!= null)
//            pumedIdLabel.rePaintLable("black");
         peptideTileLayout.setVisible(false);
              peptidesLayout.setVisible(false);
//         
        if (quantTable.getValue() != null) {
            protKey = (Integer) quantTable.getValue();
            System.out.println("prot key " + protKey);
        } else {
            return;
        }
        final Item item = quantTable.getItem(protKey);
       //        searchTableProtNameLable.rePaintLable("white");
//         rawDataLinkLabel = (CustomExternalLink) item.getItemProperty("Raw Data Link").getValue();
//        rawDataLinkLabel.rePaintLable("white");
//        
//        pumedIdLabel = (CustomExternalLink) item.getItemProperty("PumedID").getValue();
//        pumedIdLabel.rePaintLable("white");

        String key = item.getItemProperty("key").getValue().toString();
        List<QuantProtein> quantPep = new ArrayList<QuantProtein>();
        for (QuantProtein qp : quantReptideProtList) {
            if (qp.getqPeptideKey().equalsIgnoreCase(key)) {
                quantPep.add(qp);
            }

        }
        if (quantPep.size() > 0) { 
            peptideFilterControllerLayout.removeAllComponents();
            peptidesLayout.removeAllComponents();
             peptideTileLayout.setVisible(true);
              peptidesLayout.setVisible(true);
           
            searchTableProtNameLable =  item.getItemProperty("Uniprot Protein Name").getValue().toString();
            peptideTableLabel.setValue(searchTableProtNameLable+" Peptides ( " + quantPep.size() + " )");//"<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Searching Filters</strong></h4>");
           

            final QuantPeptideTable quantPeptideTable = new QuantPeptideTable(quantPep, filtersList);
//            quantPeptideTable.addValueChangeListener(QuantSearchResultLayout.this);
//            MarginInfo m = new MarginInfo(false, false, true, false);            
            showPeptideLayout.updateIcon(peptideStat);
         
        peptidesLayout.addComponent(quantPeptideTable);
        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        
        lowerLayout.setSpacing(false);
        peptidesLayout.addComponent(lowerLayout);
        peptidesLayout.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);

        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
       
        lowerLeftLayout.setSpacing(true);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false)); 
        lowerLayout.addComponent(lowerLeftLayout);
        lowerLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);
        
        final Button tableOptionBtn = new Button("Show Table Options");
        tableOptionBtn.setStyleName(Reindeer.BUTTON_SMALL);
        
        tableOptionBtn.setHeight("20px");
        lowerLeftLayout.addComponent(tableOptionBtn);
        lowerLeftLayout.setComponentAlignment(tableOptionBtn, Alignment.BOTTOM_LEFT);
        lowerLeftLayout.setMargin(new MarginInfo(false,false,false,true));
        tableOptionBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                quantPeptideTable.setInit(true);                
            peptideFilterControllerLayout.setVisible(!peptideFilterControllerLayout.isVisible());
            if(peptideFilterControllerLayout.isVisible())
                tableOptionBtn.setCaption("Hide Table Options");
            else
                tableOptionBtn.setCaption("Show Table Options");
            }
            
        });

        final TableResizeSet trs1 = new TableResizeSet(quantPeptideTable, peptideTableHeight);//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.TOP_LEFT);

        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerLayout.addComponent(lowerRightLayout);
        lowerLayout.setComponentAlignment(lowerRightLayout, Alignment.MIDDLE_RIGHT);
       

        exportPeptideTableLayout.setWidth("300px");
        lowerRightLayout.addComponent(exportPeptideTableLayout);
        lowerRightLayout.setComponentAlignment(exportPeptideTableLayout, Alignment.BOTTOM_RIGHT);
        lowerRightLayout.setMargin(new MarginInfo(false,true,false,false));
        exportPeptideTableLayout.setVisible(true);
        exportPeptideTableLayout.removeAllComponents();
        
        CustomExportBtnLayout ce2 = new CustomExportBtnLayout(null, "peptides", 0, null, null, null, null, 0, null, null, null, null);
        exportBtnPeptideTable = new PopupView("Export Peptides", ce2);
        exportBtnPeptideTable.setDescription("Export CSF-PR Peptides Table");
        exportBtnPeptideTable.setHideOnMouseOut(false);
        exportPeptideTableLayout.addComponent(exportBtnPeptideTable);
       
        exportPeptideTableLayout.setComponentAlignment(exportBtnPeptideTable, Alignment.MIDDLE_RIGHT);
            
      
        peptideFilterControllerLayout.setVisible(false);
        peptideFilterControllerLayout.setSpacing(false);
        peptideFilterControllerLayout.addComponent(quantPeptideTable.getColumnController());
       
       peptidesLayout.addComponent(peptideFilterControllerLayout);
       
       
       
       }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

    }
//    private void showColumnFilter(){
//    tableFilterControllerLayout.setVisible(true);
//    
//    
//    }
//    private void hideColumnFilter(){
//    tableFilterControllerLayout.setVisible(false);
//    
//    
//    }


}
