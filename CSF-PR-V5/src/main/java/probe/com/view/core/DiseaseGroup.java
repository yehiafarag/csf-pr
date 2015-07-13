/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

/**
 *
 * @author Yehia Farag
 */
public class DiseaseGroup {

    private String patientsGroupI;
    private String patientsGroupII;
    private int quantDatasetIndex,originalDatasetIndex;

    private String patientsSubGroupI, patientsSubGroupII;

    private String patientsGroupILabel;
    private String patientsGroupIILabel;

    public String getPatientsGroupI() {
        return patientsGroupI;
    }

    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    public void setPatientsGroupI(String patientsGroupI) {
        this.patientsGroupI = patientsGroupI;
    }

    public String getPatientsGroupII() {
        return patientsGroupII;
    }

    public void setPatientsGroupII(String patientsGroupII) {
        this.patientsGroupII = patientsGroupII;
    }

    public String getPatientsSubGroupI() {
        return patientsSubGroupI;
    }

    public void setPatientsSubGroupI(String patientsSubGroupI) {
        this.patientsSubGroupI = patientsSubGroupI;
    }

    public String getPatientsSubGroupII() {
        return patientsSubGroupII;
    }

    public void setPatientsSubGroupII(String patientsSubGroupII) {
        this.patientsSubGroupII = patientsSubGroupII;
    }

    public String getPatientsGroupILabel() {
        return patientsGroupILabel;
    }

    public void setPatientsGroupILabel(String patientsGroupILabel) {
        this.patientsGroupILabel = patientsGroupILabel;
    }

    public String getPatientsGroupIILabel() {
        return patientsGroupIILabel;
    }

    public void setPatientsGroupIILabel(String patientsGroupIILabel) {
        this.patientsGroupIILabel = patientsGroupIILabel;
    }

    public boolean checkLabel(String label) {
        if (label.equalsIgnoreCase(patientsGroupILabel) || label.equalsIgnoreCase(patientsGroupIILabel)) {
            return true;
        }
        return false;

    }

    public String getValLabel(String key) {
        if (key.equalsIgnoreCase(patientsGroupILabel)) {
            return patientsGroupIILabel;
        } else if (key.equalsIgnoreCase(patientsGroupIILabel)) {
            return patientsGroupILabel;
        } else {
            return null;
        }

    }

    public int getOriginalDatasetIndex() {
        return originalDatasetIndex;
    }

    public void setOriginalDatasetIndex(int originalDatasetIndex) {
        this.originalDatasetIndex = originalDatasetIndex;
    }

}
