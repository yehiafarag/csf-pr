/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.subview.util.CustomPI;
/*
 * @author Yehia Farag
 */

public class FractionPlotLayout extends VerticalLayout implements Serializable {

    private Map<String, List<StandardProteinBean>> standProtGroups;
    private double mw;
    private VerticalLayout leftSideLayout;
    private VerticalLayout rightSideLayout;
//    private PopupView popup;

    public Map<String, List<StandardProteinBean>> getStandProtGroups() {
        return this.standProtGroups;
    }

    public FractionPlotLayout(Map<Integer, ProteinBean> protienFractionList, double mw, List<StandardProteinBean> standProtList) {
        leftSideLayout = new VerticalLayout();
        rightSideLayout = new VerticalLayout();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
//        rightSideLayout.setWidth("40px");
        rightSideLayout.setSizeFull();
        rightSideLayout.setMargin(false);
        this.standProtGroups = initGroups(standProtList, mw);
        
//        final Table protStandTable = getStandardPlotTable(this.standProtGroups);
//        protStandTable.setWidth("320px");
//        protStandTable.setHeight("300px");

//        popup = new PopupView("Protein Standards", protStandTable);
//        //popup.setContent(content);
//        popup.setDescription("Click to View Protein Standards Table");
//        rightSideLayout.addComponent(popup);
//        rightSideLayout.setComponentAlignment(popup, Alignment.MIDDLE_LEFT);
        
        leftSideLayout.setWidth("100%");
        leftSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        this.mw = mw;
        setSpacing(true);
        this.setMargin(false);
        this.setWidth("100%");
        this.setStyleName(Reindeer.PANEL_LIGHT);
        leftSideLayout.addComponent(new PlotsLayout("#Peptides", protienFractionList, standProtGroups, mw));
        leftSideLayout.addComponent(new PlotsLayout("#Spectra", protienFractionList, standProtGroups, mw));
        leftSideLayout.addComponent(new PlotsLayout("Avg. Precursor Intensity", protienFractionList, standProtGroups, mw));
        this.addComponent(leftSideLayout);
        this.addComponent(rightSideLayout);
    }

    private Map<String, List<StandardProteinBean>> initGroups(List<StandardProteinBean> standProtList, double mw) {
        Map<String, List<StandardProteinBean>> colorMap = new HashMap<String, List<StandardProteinBean>>();
        List<StandardProteinBean> blueList = new ArrayList<StandardProteinBean>();
        List<StandardProteinBean> redList = new ArrayList<StandardProteinBean>();
        List<StandardProteinBean> lowerList = new ArrayList<StandardProteinBean>();
        List<StandardProteinBean> upperList = new ArrayList<StandardProteinBean>();
        for (StandardProteinBean spb : standProtList) {
            if (spb.getMW_kDa() > mw) {
                upperList.add(spb);
            } else {
                lowerList.add(spb);
            }
        }
        StandardProteinBean closeLowe = new StandardProteinBean();
        closeLowe.setMW_kDa(-10000);
        StandardProteinBean closeUpper = new StandardProteinBean();
        closeUpper.setMW_kDa((10000 * 1000));
        for (StandardProteinBean spb : lowerList) {
            if (closeLowe.getMW_kDa() <= spb.getMW_kDa()) {
                closeLowe = spb;
            }

        }
        for (StandardProteinBean spb : upperList) {
            if (closeUpper.getMW_kDa() >= spb.getMW_kDa()) {
                closeUpper = spb;
            }

        }
        for (StandardProteinBean spb : standProtList) {
            if ((spb.getMW_kDa() == closeLowe.getMW_kDa() && spb.getName().equalsIgnoreCase(closeLowe.getName())) || (spb.getMW_kDa() == closeUpper.getMW_kDa() && spb.getName().equalsIgnoreCase(closeUpper.getName()))) {
               spb.setTheoretical(true);
                redList.add(spb);
            } else {
                spb.setTheoretical(false);
                blueList.add(spb);
            }
        }
        colorMap.put("#CDE1FF", blueList);
        colorMap.put("#79AFFF", redList);
        return colorMap;

    }

//    private Table getStandardPlotTable(Map<String, List<StandardProteinBean>> standProtGroups) {
//        Table table = new Table();
//        table.setHeight("240px");
//        table.setWidth("100%");
//        table.setSelectable(false);
//        table.setColumnReorderingAllowed(true);
//        table.setColumnCollapsingAllowed(true);
//        table.setImmediate(true); // react at once when something is selected
//        table.addContainerProperty("Col", CustomPI.class, null, "", null, com.vaadin.ui.Table.ALIGN_CENTER);
//
//        table.addContainerProperty("Protein", String.class, null, "Protein", null, com.vaadin.ui.Table.ALIGN_CENTER);
//        table.addContainerProperty("MW", Double.class, null, "MW", null, com.vaadin.ui.Table.ALIGN_CENTER);
//        /* Add a few items in the table. */
//        int x = 0;
//        for (String key : standProtGroups.keySet()) {
//            CustomPI ce = null;
//            if (key.equalsIgnoreCase("#79AFFF")) {
//                List<StandardProteinBean> lsp = standProtGroups.get(key);
//                for (StandardProteinBean spb : lsp) {
//                    ce = new CustomPI("Selected Standard Plot", new ExternalResource("https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-frc3/q92/999992_175881092595491_1738137462_n.jpg"));
//                    table.addItem(new Object[]{ce, spb.getName(), spb.getMW_kDa()}, new Integer(x + 1));
//                    x++;
//                }
//            } else {
//                List<StandardProteinBean> lsp = standProtGroups.get(key);
//                for (StandardProteinBean spb : lsp) {
//                    ce = new CustomPI("Standard Plot", new ExternalResource("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-ash3/528295_137541356429465_212869913_n.jpg"));
//                    table.addItem(new Object[]{ce, spb.getName(), spb.getMW_kDa()}, new Integer(x + 1));
//                    x++;
//                }
//            }
//
//        }
//        for (Object propertyId : table.getSortableContainerPropertyIds()) {
//            if (propertyId.toString().equals("Protein")) {
//                table.setColumnExpandRatio(propertyId.toString(), 2.5f);
//            } else {
//                table.setColumnExpandRatio(propertyId.toString(), 1.0f);
//            }
//        }
//        table.setSortContainerPropertyId("MW");
//        table.setSortAscending(false);
//        return table;
//    }

//    public PopupView getPopup() {
//        return popup;
//    }
}