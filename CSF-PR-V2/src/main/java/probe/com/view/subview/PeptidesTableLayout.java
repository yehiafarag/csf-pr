package probe.com.view.subview;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
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
import probe.com.model.beans.PeptideBean;
import probe.com.view.subview.util.ShowLabel;
import probe.com.view.subview.util.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class PeptidesTableLayout extends VerticalLayout implements Serializable, com.vaadin.event.LayoutEvents.LayoutClickListener {

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private String PepSize = "160px";
    private TableResizeSet trs;
    private PeptideTable pepTable;
    private VerticalLayout mainLayout;
    private ShowLabel show;
    private boolean stat;
    private VerticalLayout pepTableLayout = new VerticalLayout();
    private VerticalLayout exportPepLayout = new VerticalLayout();
    private PopupView expBtnPepPepTable;
    private PeptideTable vt;

    public PeptideTable getPepTable() {
        return pepTable;
    }

    public PeptidesTableLayout(final int validPep, int totalPep, final String desc, final Map<Integer, PeptideBean> pepProtList, final String accession, final String expName) {
        //for  peptides information (table) view
        MarginInfo m = new MarginInfo(false, false, true, false);
        this.setMargin(m);
        this.setSpacing(false);
        this.setWidth("100%");



        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("45px");
        headerLayout.setSpacing(true);
        show = new ShowLabel(true);
        headerLayout.addComponent(show);
        headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
        stat = true;

        final Label pepLabel = new Label("<h4 style='font-family:verdana;color:black;'>Peptides (" + validPep + "/" + totalPep + ") " + desc + "</h4>");
        pepLabel.setContentMode(Label.CONTENT_XHTML);
        pepLabel.setHeight("45px");
        headerLayout.addComponent(pepLabel);
        headerLayout.setComponentAlignment(pepLabel, Alignment.TOP_RIGHT);

        this.addComponent(headerLayout);
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        this.addComponent(mainLayout);
        mainLayout.addComponent(pepTableLayout);
        mainLayout.setComponentAlignment(pepTableLayout, Alignment.MIDDLE_CENTER);


        pepTable = new PeptideTable(pepProtList, null);
        pepTableLayout.addComponent(pepTable);
        if (trs != null) {
            PepSize = trs.getCurrentSize();
        }
        pepTable.setHeight(PepSize);

        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        lowerLayout.setSpacing(false);
        Panel toolbar = new Panel(lowerLayout);     
        toolbar.setStyleName(Reindeer.PANEL_LIGHT);
        toolbar.setHeight("35px");   
        mainLayout.addComponent(toolbar);
        mainLayout.setComponentAlignment(toolbar, Alignment.TOP_CENTER);
        
        VerticalLayout lowerLeftLayout = new VerticalLayout();
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
        selectionType.addItem("\t\tShow Validated Peptides Only");
        selectionType.setHeight("15px");
        lowerRightLayout.addComponent(selectionType);
        lowerRightLayout.setComponentAlignment(selectionType, Alignment.BOTTOM_LEFT);

        final TableResizeSet trs1 = new TableResizeSet(pepTable, PepSize);//resize tables 
        lowerRightLayout.addComponent(trs1);
        lowerRightLayout.setComponentAlignment(trs1, Alignment.BOTTOM_LEFT);
        
        exportPepLayout.setWidth("170px");
        lowerRightLayout.addComponent(exportPepLayout);
        lowerRightLayout.setComponentAlignment(exportPepLayout, Alignment.BOTTOM_RIGHT);

        mainLayout.setSpacing(true);
       

        headerLayout.addListener(new com.vaadin.event.LayoutEvents.LayoutClickListener() {
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
        });
        selectionType.setImmediate(true);
        selectionType.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (selectionType.isSelected("\t\tShow Validated Peptides Only")) {

                    headerLayout.removeAllComponents();
                    headerLayout.addComponent(show);
                    headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

                    Label pepLabel = new Label("<h4 style='font-family:verdana;color:black;'>Peptides (" + validPep + ") " + desc + "</h4>");
                    pepLabel.setContentMode(Label.CONTENT_XHTML);
                    pepLabel.setHeight("45px");
                    headerLayout.addComponent(pepLabel);
                    headerLayout.setComponentAlignment(pepLabel, Alignment.TOP_RIGHT);


                    Map<Integer, PeptideBean> vPepProtList = getValidatedList(pepProtList);

                    pepTableLayout.removeAllComponents();
                    vt = new PeptideTable(vPepProtList, null);
                    pepTableLayout.addComponent(vt);
                    trs1.setTable(vt);
                    vt.setHeight(pepTable.getHeight() + "");



                } else {
                    headerLayout.removeAllComponents();
                    headerLayout.addComponent(show);
                    headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
                    headerLayout.addComponent(pepLabel);
                    headerLayout.setComponentAlignment(pepLabel, Alignment.TOP_RIGHT);

                    pepTableLayout.removeAllComponents();
                    pepTableLayout.addComponent(pepTable);
                    trs1.setTable(pepTable);
                    pepTable.setHeight(vt.getHeight() + "");

                }
            }

            private Map<Integer, PeptideBean> getValidatedList(Map<Integer, PeptideBean> pepProtList) {
                Map<Integer, PeptideBean> vPepList = new HashMap<Integer, PeptideBean>();
                for (int key : pepProtList.keySet()) {
                    PeptideBean pb = pepProtList.get(key);
                    if (pb.getValidated() == 1) {
                        vPepList.put(key, pb);
                    }

                }
                return vPepList;

            }
        });

    }

    public String getPepSize() {
        return PepSize;
    }

    public void setPepSize(String PepSize) {
        this.PepSize = PepSize;
    }

    public TableResizeSet getTrs() {
        return trs;
    }

    public void setTrs(TableResizeSet trs) {
        this.trs = trs;
    }

    public void setExpBtnPepTable(PopupView expBtnPepPepTable) {
        this.expBtnPepPepTable = expBtnPepPepTable;
        updateExportLayouts();

    }

    private void updateExportLayouts() {
        exportPepLayout.removeAllComponents();
        expBtnPepPepTable.setHideOnMouseOut(false);
        exportPepLayout.addComponent(expBtnPepPepTable);
        expBtnPepPepTable.setDescription("Export Peptides");
        exportPepLayout.setComponentAlignment(expBtnPepPepTable, Alignment.MIDDLE_LEFT);

    }
}
