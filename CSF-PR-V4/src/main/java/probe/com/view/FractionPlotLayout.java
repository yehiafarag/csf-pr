/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.components.PlotsLayout;
/*
 * @author Yehia Farag
 */

public class FractionPlotLayout extends VerticalLayout implements Serializable {

    private final Map<String, List<StandardProteinBean>> standProtGroups;
    private final VerticalLayout leftSideLayout;
    private final VerticalLayout rightSideLayout;

    public Map<String, List<StandardProteinBean>> getStandProtGroups() {
        return this.standProtGroups;
    }

    public FractionPlotLayout(Map<Integer, ProteinBean> protienFractionList, double mw, List<StandardProteinBean> standProtList) {
        leftSideLayout = new VerticalLayout();
        rightSideLayout = new VerticalLayout();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        rightSideLayout.setSizeFull();
        rightSideLayout.setMargin(false);
        this.standProtGroups = initGroups(standProtList, mw);        
        leftSideLayout.setWidth("100%");
        leftSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);

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

}