/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.visualizationunits;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.DatasetBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.subview.util.CustomExportBtnLayout;
import probe.com.view.subview.util.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class SearchResultsTableLayout extends VerticalLayout implements Serializable {

    private SearchResultsTable searcheResultsTable, currentTable, vt;
    private VerticalLayout searchResultsTableLayout = new VerticalLayout();
    private VerticalLayout exportAllPepLayout = new VerticalLayout();
    private PopupView expBtnProtAllPepTable;
    private VerticalLayout exportSearchTableLayout = new VerticalLayout();
    private PopupView expBtnSearchResultsTable;
    private Property.ValueChangeListener listener;
    private Label  searchResultstLabel= new Label();
    private int validatedNum;
    
    public SearchResultsTableLayout(MainHandler handler, final Map<Integer, DatasetBean> expList, final Map<Integer, ProteinBean> fullExpProtList, boolean validatedOnly) {

        this.setWidth("100%");
        this.setSpacing(true);
//        this.setMargin(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        searchResultstLabel.setContentMode(Label.CONTENT_XHTML);
        searchResultstLabel.setHeight("30px");
        this.addComponent(searchResultstLabel);
        this.addComponent(searchResultsTableLayout);
        this.setComponentAlignment(searchResultsTableLayout, Alignment.MIDDLE_CENTER);

        final Map<Integer, ProteinBean> vProteinsList = getValidatedList(fullExpProtList);
        searchResultstLabel.setValue("<h4 style='font-family:verdana;color:black;'> Search Results (" + vProteinsList.size() + ")</h4>");
        searcheResultsTable = new SearchResultsTable(expList, vProteinsList);
        searchResultsTableLayout.addComponent(searcheResultsTable);
        currentTable = searcheResultsTable;


       HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
//        Panel toolbar = new Panel(lowerLayout);
//        toolbar.setStyleName(Reindeer.PANEL_LIGHT);
//        toolbar.setHeight("35px");
        this.addComponent(lowerLayout);
        this.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);
        
        
        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
        lowerLeftLayout.setSpacing(true);
        lowerLayout.addComponent(lowerLeftLayout);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false));
        lowerLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);
      //  lowerLayout.setExpandRatio(lowerLeftLayout, 0.2f);
        
        
        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerRightLayout.setSpacing(true);
      //  lowerRightLayout.setWidth("800px");
        lowerLayout.addComponent(lowerRightLayout);
        lowerLayout.setComponentAlignment(lowerRightLayout, Alignment.BOTTOM_RIGHT);
        //lowerLayout.setExpandRatio(lowerRightLayout, 0.8f);
        


        final OptionGroup selectionType = new OptionGroup();
        selectionType.setMultiSelect(true);
        Object itemId = selectionType.addItem("\t\tShow Validated Proteins Only");
        selectionType.select("\t\tShow Validated Proteins Only");                
        selectionType.setReadOnly(validatedOnly);
        
        selectionType.setHeight("15px");
        lowerLeftLayout.addComponent(selectionType);
        lowerLeftLayout.setComponentAlignment(selectionType, Alignment.BOTTOM_LEFT);



        final TableResizeSet trs1 = new TableResizeSet(currentTable, currentTable.getHeight() + "");//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.BOTTOM_CENTER);


        exportAllPepLayout.setWidth("300px");
       // exportAllPepLayout.setMargin(new MarginInfo(false, false, false, false));
        lowerRightLayout.addComponent(exportAllPepLayout);
        lowerRightLayout.setComponentAlignment(exportAllPepLayout, Alignment.BOTTOM_RIGHT);
        exportAllPepLayout.setVisible(true);

        exportSearchTableLayout.setWidth("200px");
        lowerRightLayout.addComponent(exportSearchTableLayout);
        lowerRightLayout.setComponentAlignment(exportSearchTableLayout, Alignment.BOTTOM_RIGHT);

        CustomExportBtnLayout ce2 = new CustomExportBtnLayout(handler, "searchResult", 0, null, null, null, expList, null, 0, null, null, fullExpProtList);
        expBtnSearchResultsTable = new PopupView("Export CSF-PR Search Results", ce2);
        exportSearchTableLayout.removeAllComponents();
        expBtnSearchResultsTable.setHideOnMouseOut(false);
        exportSearchTableLayout.addComponent(expBtnSearchResultsTable);
        expBtnSearchResultsTable.setDescription("Export CSF-PR Search Results");
        exportSearchTableLayout.setComponentAlignment(expBtnSearchResultsTable, Alignment.MIDDLE_LEFT);

//        Label lab = new Label("Export Peptides from All Datasets For selected Protein");
//        lab.setDescription("Export CSF-PR Peptides for The Selected Protein from All Available Datasets");
//       exportAllPepLayout.addComponent(lab);
   
        
        selectionType.setImmediate(true);
        selectionType.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (!selectionType.isSelected("\t\tShow Validated Proteins Only")) {
                   // Map<Integer, ProteinBean> vProteinsList = getValidatedList(fullExpProtList);
                    searchResultstLabel.setValue("<h4 style='font-family:verdana;color:black;'> Search Results ("+ vProteinsList.size()+"/"+fullExpProtList.size()+")</h4>");
                    searchResultsTableLayout.removeAllComponents();
                    vt = new SearchResultsTable(expList, fullExpProtList);
                    searchResultsTableLayout.addComponent(vt);
                    trs1.setTable(vt);
                    vt.setHeight(getCurrentTable().getHeight() + "");
                    getCurrentTable().removeListener(getListener());
                    vt.addListener(getListener());
                    currentTable = vt;
                } else {
                    searchResultstLabel.setValue("<h4 style='font-family:verdana;color:black;'> Search Results ("+ vProteinsList.size()+")</h4>");
                   
                    searchResultsTableLayout.removeAllComponents();
                    searchResultsTableLayout.addComponent(searcheResultsTable);
                    trs1.setTable(searcheResultsTable);
                    currentTable = searcheResultsTable;
                    vt.removeListener(getListener());
                    searcheResultsTable.addListener(getListener());
                }
            }
            
        });
        if (validatedOnly) {
            selectionType.select(itemId);
            selectionType.setVisible(true);
            selectionType.setReadOnly(true);

        } else {
            selectionType.setVisible(true);
            selectionType.setReadOnly(false);
        }

    }

    public Property.ValueChangeListener getListener() {
        return listener;
    }

    public void setListener(Property.ValueChangeListener listener) {
        this.listener = listener;
    }

    public PopupView getExpBtnProtAllPepTable() {
        return expBtnProtAllPepTable;
    }

    public void setExpBtnProtAllPepTable(PopupView expBtnProtAllPepTable) {
        this.expBtnProtAllPepTable = expBtnProtAllPepTable;
        updateExportLayouts();

    }

    private void updateExportLayouts() {
        exportAllPepLayout.removeAllComponents();
        expBtnProtAllPepTable.setHideOnMouseOut(false);
        exportAllPepLayout.addComponent(expBtnProtAllPepTable);
        //expBtnProtAllPepTable.setDescription("Export all Protien's Peptides from all Data Sets");
        exportAllPepLayout.setComponentAlignment(expBtnProtAllPepTable, Alignment.MIDDLE_LEFT);
    }

    public SearchResultsTable getCurrentTable() {
        return currentTable;
    }
    
    private Map<Integer, ProteinBean> getValidatedList(Map<Integer, ProteinBean> proteinsList) {
                Map<Integer, ProteinBean> vProteinsList = new HashMap<Integer, ProteinBean>();
                for (int str : proteinsList.keySet()) {
                    ProteinBean pb = proteinsList.get(str);
                    if (pb.isValidated()) {
                        vProteinsList.put(str, pb);
                    }

                }
                return vProteinsList;

            }
}
