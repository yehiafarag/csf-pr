/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.datasetsoverview.heatmap;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * this class give the control on the header label style and events
 *
 * @author Yehia Farag
 */
public class HeaderCell extends Label {
    private final int index;
    private final String cellStyleName;

    public HeaderCell(boolean rowHeader, String value, int index) {
        super("<b>" + value + "</b>");
        this.setWidth("150px");
        this.setHeight("50px");
        this.setContentMode(ContentMode.HTML);
        this.index = index;
        if (rowHeader) {
            this.cellStyleName = "hmrowlabel_unselected";
            this.setStyleName("hmrowlabel_unselected");
        } else {
            this.cellStyleName = "hmcolumnlabel_unselected";
            this.setStyleName("hmcolumnlabel_unselected");
        }

    }
    public void heighlightCellStyle(){
    this.setStyleName(cellStyleName+"_heighlightcell");
    
    }
    public void resetCellStyle(){
    this.setStyleName(cellStyleName);
    
    }
    public void selectCellStyle(){
    this.setStyleName(cellStyleName);
    
    }
    public void unSelectCellStyle(){
    this.setStyleName(cellStyleName);
    
    }

}
