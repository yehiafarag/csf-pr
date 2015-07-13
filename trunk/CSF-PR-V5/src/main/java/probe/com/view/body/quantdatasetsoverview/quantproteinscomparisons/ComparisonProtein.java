/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class ComparisonProtein extends HorizontalLayout implements Serializable, Comparable<ComparisonProtein> {

    private String uniProtAccess;
    private String protName;
    private final int uniqueId;

    private int trindCategory;

    public String getSequance() {
        return sequance;
    }

    public void setSequance(String sequance) {
        this.sequance = sequance;
    }
    private String sequance;
    private Set<QuantPeptide> quantPeptidesList;
    private final Map<String, List<Integer>> patientsNumToTrindMap = new HashMap<String, List<Integer>>();
    private final Map<String, List<Integer>> patientsNumToDSIDMap = new HashMap<String, List<Integer>>();

    public Map<String, List<Integer>> getPatientsNumToTrindMap() {
        return patientsNumToTrindMap;
    }

    public GroupsComparison getComparison() {
        return comparison;
    }

    private final GroupsComparison comparison;

    public String getProtName() {
        return protName;
    }

    public void setProtName(String protName) {
        this.protName = protName;
    }
    private Integer up = 0;
    private Integer down = 0;
    private int notReg;
    private int notProvided;
    private double penalty = 0.0;
    private String key;
    private Label upLabel;
    private Label downLabel;
    private final int total;
    private final DecimalFormat df;
    private Double trendValue = 0.0;
    private double cellValue;

    public ComparisonProtein(int total, GroupsComparison comparison, int uniqueId) {
        this.comparison = comparison;
        this.uniqueId = uniqueId;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.#", otherSymbols);
//        this.upReg=upReg;
        this.total = total;
        upLayout = new VerticalLayout();
        downLayout = new VerticalLayout();
        notRegLayout = new VerticalLayout();
        notProvidedLayout = new VerticalLayout();
        patientsNumToTrindMap.put("up", new ArrayList<Integer>());
        patientsNumToTrindMap.put("notReg", new ArrayList<Integer>());
        patientsNumToTrindMap.put("down", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("up", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("notReg", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("down", new ArrayList<Integer>());
        
        initLabelLayout();
    }

    final VerticalLayout upLayout, downLayout, notRegLayout, notProvidedLayout;

    private void initLabelLayout() {
        this.setWidth("100%");
        this.setHeight("20px");
        this.setSpacing(false);
        this.setMargin(false);

        downLabel = new Label();
        downLabel.setWidth("50px");
        downLabel.setHeight("20px");
        downLabel.setContentMode(ContentMode.HTML);
        this.addComponent(downLabel);
        this.setComponentAlignment(downLabel, Alignment.TOP_RIGHT);

        downLayout.setHeight("15px");
        downLayout.setStyleName("greenlayout");
        this.addComponent(downLayout);
        this.setComponentAlignment(downLayout, Alignment.MIDDLE_CENTER);

        //        notProvidedLayout.setWidth("100%");
        notProvidedLayout.setHeight("15px");
        notProvidedLayout.setStyleName("lightbluelayout");
        this.addComponent(notProvidedLayout);
        this.setComponentAlignment(notProvidedLayout, Alignment.MIDDLE_CENTER);
//
        notRegLayout.setHeight("15px");
        notRegLayout.setStyleName("empty");//"empty"
        this.addComponent(notRegLayout);
        this.setComponentAlignment(notRegLayout, Alignment.MIDDLE_CENTER);

        upLayout.setHeight("15px");
        upLayout.setStyleName("redlayout");
        this.addComponent(upLayout);
        this.setComponentAlignment(upLayout, Alignment.MIDDLE_CENTER);
        upLayout.setCaptionAsHtml(true);

        upLabel = new Label();
        upLabel.setContentMode(ContentMode.HTML);
        upLabel.setWidth("50px");
        upLabel.setHeight("20px");
        this.addComponent(upLabel);
        this.setComponentAlignment(upLabel, Alignment.TOP_LEFT);

    }

    public double getCellValue() {
        return cellValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUniProtAccess() {
        return uniProtAccess;
    }

    public void setUniProtAccess(String uniProtAccess) {
        this.uniProtAccess = uniProtAccess;
    }

    public int getUp() {
        return up;
    }

    public void addUp(int up, int patientsNumber, int dsID) {
        trendValue += (double) up;
        this.up += up;
        List<Integer> upList = this.patientsNumToTrindMap.get("up");
        upList.add(patientsNumber);
        this.patientsNumToTrindMap.put("up", upList);

        List<Integer> upDsList = this.patientsNumToDSIDMap.get("up");
        upDsList.add(dsID);
        this.patientsNumToDSIDMap.put("up", upDsList);

    }

    public int getDown() {
        return down;
    }

    public void addDown(int down, int patientsNumber, int dsID) {
        trendValue -= (double) down;
        this.down += down;
        List<Integer> downList = this.patientsNumToTrindMap.get("down");
        downList.add(patientsNumber);
        this.patientsNumToTrindMap.put("down", downList);

        List<Integer> downDsList = this.patientsNumToDSIDMap.get("down");
        downDsList.add(dsID);
        this.patientsNumToDSIDMap.put("down", downDsList);
    }

    public int getNotReg() {
        return notReg;
    }

    public void addNotReg(int notReg, int patientsNumber, int dsID) {
        penalty += 0.5;
        this.notReg += notReg;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("notReg");
        notRegList.add(patientsNumber);
        this.patientsNumToTrindMap.put("notReg", notRegList);

        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("notReg");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("notReg", notRegDsList);
    }

    public int getNotProvided() {
//        trendValue-=0.5;
        return notProvided;
    }

    public void addNotProvided(int notProvided, int patNumber, int dsID) {
//        trendValue-=0.5;
        penalty += 0.5;
        this.notProvided += notProvided;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("notReg");
        notRegList.add(patNumber);
        this.patientsNumToTrindMap.put("notReg", notRegList);

        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("notReg");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("notReg", notRegDsList);
    }

    @Override
    public int compareTo(ComparisonProtein t) {
        Double v1 = null;
        if (up.intValue() == down.intValue()) {
            v1 = trendValue;
        } else if (trendValue > 0) {
            double factor = penalty;
            v1 = trendValue - factor;
            v1 = Math.max(v1, 0) + ((double) (up - down) / 10.0);
        } else {
            double factor = penalty;
            v1 = trendValue + factor;
            v1 = Math.min(v1, 0) + ((double) (up - down) / 10.0);
        }
        Double v2 = null;
        if (t.up.intValue() == t.down.intValue()) {
            v2 = t.trendValue;
        } else if (t.trendValue > 0) {
            double factor = t.penalty;
            v2 = t.trendValue - factor;
            v2 = Math.max(v2, 0) + ((double) (t.up - t.down) / 10.0);
        } else {
            double factor = t.penalty;
            v2 = t.trendValue + factor;
            v2 = Math.min(v2, 0) + ((double) (t.up - t.down) / 10.0);
        }
        return (v1).compareTo(v2);

    }

    public void updateLabelLayout() {
        int counter = 0;
//        this.setExpandRatio(downLabel, ((float) 1.5 / total));
//        this.setExpandRatio(upLabel, ((float) 1.5 / total));
        int subTotal = this.total;
        downLabel.setValue("<p style='text-align: right;line-height:0.1'><strong> " + df.format(((double) down / (double) subTotal) * 100.0) + "% &#8595; </strong>&nbsp;</p>");
        upLabel.setValue("<p style='text-align: left;line-height:0.1'><strong>&nbsp;&#8593; " + df.format(((double) up / (double) subTotal) * 100.0) + "%</strong></p>");
//        if (((float) up / subTotal) <= 0.0) {
//            upLayout.setVisible(false);
//        } 
//        else {
//            counter += up;
//            this.setExpandRatio(upLayout, ((float) up / subTotal));
//        }
//        if (((float) notProvided / subTotal) <= 0.0) {
//            notProvidedLayout.setVisible(false);
//        } 
//        else {
//            counter += notProvided;
//            this.setExpandRatio(notProvidedLayout, ((float) notProvided / subTotal));
//        }
//        if (((float) down / subTotal) <= 0.0) {
//            downLayout.setVisible(false);
//        } 
//        else {
//            counter += down;
//            this.setExpandRatio(downLayout, ((float) down / subTotal));
//        }

        Double v1 = null;
        if (up.intValue() == down.intValue()) {
            v1 = trendValue;
        } else if (trendValue > 0) {
            double factor = penalty;
            v1 = trendValue - factor;
            v1 = Math.max(v1, 0) + ((double) (up - down) / 10.0);
        } else {
            double factor = penalty;
            v1 = trendValue + factor;
            v1 = Math.min(v1, 0) + ((double) (up - down) / 10.0);
        }
        if (v1 > 0) {
            cellValue = Math.min(v1, 1);
        } else if (v1 < 0) {
            cellValue = Math.max(v1, -1);
        }

        this.setExpandRatio(notRegLayout, ((float) (subTotal - counter) / subTotal));
        String overall;
        if (cellValue > 0) {
            overall = "Up Regulated (" + cellValue + ")";
            if (notProvided > 0 || notReg > 0 || down > 0) {
                trindCategory = 3;
            } else {
                trindCategory = 4;
            }
        } else if (cellValue == 0) {
            overall = "Not Regulated (" + cellValue + ")";
            trindCategory = 2;
        } else {
            overall = "Down Regulated (" + cellValue + ")";
            if (notProvided > 0 || notReg > 0 || up > 0) {
                trindCategory = 1;
            } else {
                trindCategory = 0;
            }
        }
        this.setDescription("Down Regulated: " + down + "  /  Not Regulated : " + notReg + " /  Up Regulated: " + up + " Overall Trend " + overall);

    }

    @Override
    public String toString() {
        if (cellValue > 0) {
            return "Up Regulated (" + cellValue + ")";
        } else if (cellValue == 0) {
            return "Not Regulated (" + cellValue + ")";
        } else {
            return "Down Regulated (" + cellValue + ")";
        }
    }

    public int getDSID(int x, int y) {
        if (x == 0) {
            return patientsNumToDSIDMap.get("up").get(y);
        } else if (x == 1) {
            return patientsNumToDSIDMap.get("notReg").get(y);
        } else if (x == 2) {
            return patientsNumToDSIDMap.get("down").get(y);
        } else {
            return -1;
        }

    }

    public Set<QuantPeptide> getQuantPeptidesList() {
        return quantPeptidesList;
    }

    public void setQuantPeptidesList(Set<QuantPeptide> quantPeptidesList) {
        this.quantPeptidesList = quantPeptidesList;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public int getTrindCategory() {
        return trindCategory;
    }

    public void updateWidth(int width) {
        this.setWidth(width + "px");
        float freeArea=width;
        int subTotal = this.total;
//        float labelRatio= 80.0f/(float)width;
//         this.setExpandRatio(downLabel, labelRatio);
//        this.setExpandRatio(upLabel, labelRatio);
        
        if (width > 200) {
            downLabel.setWidth("80px");
            upLabel.setWidth("80px");
            freeArea = width - 160;
            downLabel.setVisible(true);
            upLabel.setVisible(true);
        }
        
        
        else {
            downLabel.setVisible(false);
            upLabel.setVisible(false);

        }
            if (((float) up / subTotal) <= 0.0) {
                upLayout.setVisible(false);
            } else {
                float upWidth = ((float) up / (float) subTotal) * freeArea;
                upLayout.setWidth(upWidth + "px");
            }
            if (((float) notProvided / subTotal) <= 0.0) {
                notProvidedLayout.setVisible(false);
            } else {
                float notProvidedWidth = ((float) notProvided / (float) subTotal) * freeArea;
                notProvidedLayout.setWidth(notProvidedWidth + "px");

            }
            if (((float) down / subTotal) <= 0.0) {
                downLayout.setVisible(false);
            } else {
                float downWidth = ((float) down / (float) subTotal) * freeArea;
                downLayout.setWidth(downWidth + "px");
            }

    }

    
}
