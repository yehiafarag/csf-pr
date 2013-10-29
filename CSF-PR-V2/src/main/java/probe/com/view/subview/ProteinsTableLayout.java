/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.data.Property;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.subview.util.Help;
import probe.com.view.subview.util.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class ProteinsTableLayout extends VerticalLayout implements Serializable {
    
    private String protSize = "160px";
    private   Label protCounter;
    
    public String getProtSize() {
        return protSize;
    }
    
    public void setProtSize(String protSize) {
        this.protSize = protSize;
    }
    private ProteinsTable protTable, currentTable;
//    private String accession, otherAccession;
    private VerticalLayout exportAllPepLayout = new VerticalLayout();
    private VerticalLayout protTableLayout = new VerticalLayout();
    private VerticalLayout protLabelLayout = new VerticalLayout();
    private PopupView expBtnProtAllPepTable;
    private HorizontalLayout topLayout = new HorizontalLayout();
    private VerticalLayout exportProtLayout = new VerticalLayout();
    private PopupView expBtnProtPepTable;
    private Property.ValueChangeListener listener;
    private ProteinsTable vt;
    private Button nextSearch;
    
    public TextField getSearchField() {
        return searchField;
    }
    private TextField searchField;
    
    public ProteinsTableLayout(final Map<String, ProteinBean> proteinsList, final ExperimentBean exp, final Map<Integer, ExperimentBean> expList, final ExperimentHandler expHandler) {//, final String accession,final String otherAccession) {
        this.setWidth("100%");
        this.setSpacing(true);
        this.setMargin(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        
        this.addComponent(topLayout);
        topLayout.setWidth("100%");
        topLayout.setMargin(false);
        topLayout.setSpacing(false);
        topLayout.addComponent(protLabelLayout);
        topLayout.setComponentAlignment(protLabelLayout, Alignment.TOP_LEFT);
        
        topLayout.setExpandRatio(protLabelLayout, 0.15f);
        
//        Label infoLable = new Label("<center style='background-color:#E6E6FA;'><p  style='background-color:#E6E6FA;font-family:verdana;color:black;font-weight:bold;'>Select a protein to see all peptides identified for the protein and, if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed. </p></center>");
//        infoLable.setContentMode(Label.CONTENT_XHTML);
//        infoLable.setWidth("200px");
//        infoLable.setStyleName(Reindeer.LAYOUT_BLUE);
//        
//        Help help = new Help();
//        HorizontalLayout infoIco = help.getInfoNote(infoLable);
//        topLayout.addComponent(infoIco);
//        topLayout.setComponentAlignment(infoIco, Alignment.MIDDLE_RIGHT);
//        topLayout.setExpandRatio(infoIco, 0.35f);
        
        
        Label protLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Proteins (" + exp.getNumberValidProt() + "/" + exp.getProteinsNumber() + ")</h4>");
        protLabel.setContentMode(Label.CONTENT_XHTML);
        protLabel.setHeight("40px");
        protLabelLayout.addComponent(protLabel);
        
        
        searchField = new TextField(" ");
        searchField.setDescription("Search Proteins");
        
        topLayout.addComponent(searchField);
        topLayout.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(searchField, 0.47f);
        nextSearch = new Button();
        nextSearch.setDescription("Next Result");
        nextSearch.setStyleName(Reindeer.BUTTON_LINK);
        nextSearch.setIcon(new ThemeResource("img/next.gif"));
        nextSearch.setWidth("20px");
        nextSearch.setHeight("23px");        
        nextSearch.setEnabled(false);    
        
        topLayout.addComponent(nextSearch);
        topLayout.setExpandRatio(nextSearch, 0.03f);
        topLayout.setComponentAlignment(nextSearch, Alignment.BOTTOM_LEFT);
        
        protCounter = new Label("");
         protCounter.setWidth("150px");
        protCounter.setHeight("23px");     
         topLayout.addComponent(protCounter);
        topLayout.setExpandRatio(protCounter, 0.08f);
        topLayout.setComponentAlignment(protCounter, Alignment.BOTTOM_LEFT);
        
        searchField.setImmediate(true);
        searchField.setWidth("150px");
        searchField.setHeight("23px");
        
        
        this.addComponent(protTableLayout);
        this.setComponentAlignment(protTableLayout, Alignment.MIDDLE_CENTER);
        
        protTable = new ProteinsTable(proteinsList, exp.getFractionsNumber());
        protTableLayout.addComponent(protTable);
        currentTable = protTable;
        
        
        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        Panel toolbar = new Panel(lowerLayout);
        toolbar.setStyleName(Reindeer.PANEL_LIGHT);
        toolbar.setHeight("35px");
        this.addComponent(toolbar);
        this.setComponentAlignment(toolbar, Alignment.TOP_CENTER);
        
        
        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
        lowerLayout.addComponent(lowerLeftLayout);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, true));
        lowerLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);
        lowerLayout.setExpandRatio(lowerLeftLayout, 0.4f);
        
        
        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerRightLayout.setSpacing(true);
        lowerRightLayout.setWidth("600px");
        lowerLayout.addComponent(lowerRightLayout);
        lowerLayout.setComponentAlignment(lowerRightLayout, Alignment.BOTTOM_RIGHT);
        lowerLayout.setExpandRatio(lowerRightLayout, 0.6f);
        
        
        
        
        final OptionGroup selectionType = new OptionGroup();
        selectionType.setMultiSelect(true);
        selectionType.addItem("\t\tShow Validated Proteins Only");
        selectionType.setHeight("15px");
        lowerRightLayout.addComponent(selectionType);
        lowerRightLayout.setComponentAlignment(selectionType, Alignment.BOTTOM_LEFT);
        
        
        
        
        
        final TableResizeSet trs1 = new TableResizeSet(protTable, protSize);//resize tables 
        lowerRightLayout.addComponent(trs1);
        lowerRightLayout.setComponentAlignment(trs1, Alignment.BOTTOM_RIGHT);
        
        
        exportAllPepLayout.setWidth("200px");
        lowerRightLayout.addComponent(exportAllPepLayout);
        lowerRightLayout.setComponentAlignment(exportAllPepLayout, Alignment.BOTTOM_RIGHT);
        
        
        exportProtLayout.setWidth("100px");
        lowerRightLayout.addComponent(exportProtLayout);
        lowerRightLayout.setComponentAlignment(exportProtLayout, Alignment.BOTTOM_RIGHT);
        
        selectionType.setImmediate(true);
        selectionType.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (selectionType.isSelected("\t\tShow Validated Proteins Only")) {
                    
                    protLabelLayout.removeAllComponents();
                    Label protLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Proteins (" + exp.getNumberValidProt() + ")</h4>");
                    protLabel.setContentMode(Label.CONTENT_XHTML);
                    protLabel.setHeight("40px");
                    protLabelLayout.addComponent(protLabel);
                    Map<String, ProteinBean> vProteinsList = getValidatedList(proteinsList);
                    protTableLayout.removeAllComponents();
                    vt = new ProteinsTable(vProteinsList, exp.getFractionsNumber());
                    protTableLayout.addComponent(vt);
                    trs1.setTable(vt);
                    vt.setHeight(protTable.getHeight() + "");
                    protTable.removeListener(listener);
                    vt.addListener(listener);
                    currentTable = vt;
                    
                    
                } else {
                    protLabelLayout.removeAllComponents();
                    Label protLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Proteins (" + exp.getNumberValidProt() + "/" + exp.getProteinsNumber() + ")</h4>");
                    protLabel.setContentMode(Label.CONTENT_XHTML);
                    protLabel.setHeight("40px");
                    protLabelLayout.addComponent(protLabel);
                    protTableLayout.removeAllComponents();
                    protTableLayout.addComponent(protTable);
                    trs1.setTable(protTable);
                    currentTable = protTable;
                    vt.removeListener(listener);
                    protTable.addListener(listener);
                    
                    
                }
            }
            
            private Map<String, ProteinBean> getValidatedList(Map<String, ProteinBean> proteinsList) {
                Map<String, ProteinBean> vProteinsList = new HashMap<String, ProteinBean>();
                for (String str : proteinsList.keySet()) {
                    ProteinBean pb = proteinsList.get(str);
                    if (pb.isValidated()) {
                        vProteinsList.put(str, pb);
                    }
                    
                }
                return vProteinsList;
                
            }
        });
        
        
    }
    
    public ProteinsTable getProtTable() {
        return currentTable;
    }
    
    public PopupView getExpBtnProtAllPepTable() {
        return expBtnProtAllPepTable;
    }
    
    public void setExpBtnProtAllPepTable(PopupView expBtnProtAllPepTable, PopupView expBtnProtPepTable) {
        this.expBtnProtAllPepTable = expBtnProtAllPepTable;
        this.expBtnProtPepTable = expBtnProtPepTable;
        updateExportLayouts();
        
    }
    
    private void updateExportLayouts() {
        
        exportAllPepLayout.removeAllComponents();
        
        
        expBtnProtAllPepTable.setHideOnMouseOut(false);
        
        exportAllPepLayout.addComponent(expBtnProtAllPepTable);
        
        expBtnProtAllPepTable.setDescription("Export all Protien's Peptides from all Data Sets");
        exportAllPepLayout.setComponentAlignment(expBtnProtAllPepTable, Alignment.MIDDLE_LEFT);
        
        exportProtLayout.removeAllComponents();
        expBtnProtPepTable.setHideOnMouseOut(false);
        exportProtLayout.addComponent(expBtnProtPepTable);
        expBtnProtPepTable.setDescription("Export Protiens");
        exportProtLayout.setComponentAlignment(expBtnProtPepTable, Alignment.MIDDLE_LEFT);
        
    }
    
    public Property.ValueChangeListener getListener() {
        return listener;
    }
    
    public void setListener(Property.ValueChangeListener listener) {
        this.listener = listener;
    }
    
    public Button getNextSearch() {
        return nextSearch;
    }

    public Label getProtCounter() {
        return protCounter;
    }
}
