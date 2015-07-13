/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.jfreeutil;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.QuantPeptide;

/**
 *
 * @author yfa041
 */
public class StackedBarPeptideComponent extends VerticalLayout implements Comparable<StackedBarPeptideComponent> {

    private final Map<String, Object> param = new HashMap<String, Object>();
    private String defaultStyleShowAllMode;
    private String defaultStyleShowSignificatntMode;
    private boolean significant;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    private int level = 0;

    public String getDefaultStyleShowSignificatntMode() {
        return defaultStyleShowSignificatntMode;
    }

    public void setDefaultStyleShowSignificatntMode(String defaultStyleShowSignificatntMode) {
        this.defaultStyleShowSignificatntMode = defaultStyleShowSignificatntMode;
    }
    private boolean overlapped;

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
    private boolean merged;
    private final Set<QuantPeptide> quantpeptideSet = new LinkedHashSet<QuantPeptide>();

    public boolean isOverlapped() {
        return overlapped;
    }

    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    public String getDefaultStyleShowAllMode() {
        return defaultStyleShowAllMode;
    }

    public void setDefaultStyleShowAllMode(String defaultStyleShowAllMode) {
        this.defaultStyleShowAllMode = defaultStyleShowAllMode;
         this.setStyleName(defaultStyleShowAllMode);
    }

    public int getX0() {
        return x0;
    }

    public int getWidthArea() {
        return widthArea;
    }

    private final int x0;
    private final Integer widthArea;

    public StackedBarPeptideComponent(int x0, int widthArea) {
        this.setHeight("15px");
        this.setWidth((widthArea + 2) + "px");
        this.x0 = x0;
        this.widthArea = widthArea;
    }

    public void setParam(String key, Object value) {
        param.put(key, value);
    }

    public Object getParam(String key) {
        return param.get(key);
    }

    public void heighlight(boolean select) {
        if (select) {
            this.setStyleName("selectedbar");
        } else {
            this.setStyleName(defaultStyleShowAllMode);
        }
    }

    public void addQuantPeptide(QuantPeptide qp) {
        this.quantpeptideSet.add(qp);
    }

    public Set<QuantPeptide> getQuantpeptideSet() {
        return quantpeptideSet;
    }

    @Override
    public int compareTo(StackedBarPeptideComponent o) {
        return widthArea.compareTo(o.widthArea);
    }

    public boolean isSignificant() {
        return significant;
    }

    public void setSignificant(boolean significant) {
        this.significant = significant;
    }

}
