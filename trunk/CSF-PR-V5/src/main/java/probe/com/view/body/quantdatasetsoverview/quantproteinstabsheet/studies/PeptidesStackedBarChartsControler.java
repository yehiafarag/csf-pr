/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.model.beans.QuantPeptide;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.PeptideSequanceLocationOverview;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.ProteinInformationPopupComponent;

/**
 *
 * @author yfa041
 */
public class PeptidesStackedBarChartsControler {

    private final Map<String, PeptideSequanceLocationOverview> stackedBarChartCompDSIndexMap = new HashMap<String, PeptideSequanceLocationOverview>();

    private final ProteinInformationPopupComponent peptidesOverviewPopupPanel;

    public PeptidesStackedBarChartsControler(int width, ComparisonProtein cp) {

        int panelWidth = Page.getCurrent().getBrowserWindowWidth() - width-100;
        peptidesOverviewPopupPanel = new ProteinInformationPopupComponent(panelWidth);
        //init leftside components        
        Map<Integer, Set<QuantPeptide>> dsQuantPepMap = new HashMap<Integer, Set<QuantPeptide>>();
         for (QuantPeptide quantPep : cp.getQuantPeptidesList()) {             
            if (!dsQuantPepMap.containsKey(quantPep.getDsKey())) {
                Set<QuantPeptide> subList = new HashSet<QuantPeptide>();
                dsQuantPepMap.put(quantPep.getDsKey(), subList);
            }
            Set<QuantPeptide> subList = dsQuantPepMap.get(quantPep.getDsKey());
            subList.add(quantPep);
            dsQuantPepMap.put(quantPep.getDsKey(), subList);
        }
         
         
        int counter = 0;
        for (int dsID : dsQuantPepMap.keySet()) {            
            int subWidth = (int) ((double) panelWidth * 0.9);
            PeptideSequanceLocationOverview peptideStackedBarChart = new PeptideSequanceLocationOverview(cp.getSequance(), dsQuantPepMap.get(dsID), subWidth);
            stackedBarChartCompDSIndexMap.put(counter + "-" + dsID + "-", peptideStackedBarChart);
            counter++;
        }

    }

    public void updateSelectedProteinInformation(int dsIndex, QuantDatasetObject dataset,String protAcc) {
        for (String key : stackedBarChartCompDSIndexMap.keySet()) {
            if (key.contains("-" + dsIndex + "-")) {
                PeptideSequanceLocationOverview bar = stackedBarChartCompDSIndexMap.get(key);
               
                if (bar == null) {
                    Notification.show("No Information  available at this bar");
                } else {
                    String peptidesNumber = bar.getSignificantPeptidesNumber()+"/"+ bar.getTotalPeptidesNumber()+"";
                    peptidesOverviewPopupPanel.updateForm(dataset, bar,peptidesNumber,protAcc);
                }
                return;
            }

        }
        Notification.show("No Information  available");
    }

}
