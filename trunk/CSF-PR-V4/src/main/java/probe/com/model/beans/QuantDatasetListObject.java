/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class QuantDatasetListObject {
    private QuantDatasetObject[] quantDatasetsList;
        private boolean[] activeHeaders;

    public QuantDatasetObject[] getQuantDatasetList() {
        return quantDatasetsList;
    }

    public void setQuantDatasetList(QuantDatasetObject[] quantDatasetsList) {
        this.quantDatasetsList = quantDatasetsList;
    }

    public boolean[] getActiveHeaders() {
        return activeHeaders;
    }

    public void setActiveHeaders(boolean[] activeHeaders) {
        this.activeHeaders = activeHeaders;
    }
}
