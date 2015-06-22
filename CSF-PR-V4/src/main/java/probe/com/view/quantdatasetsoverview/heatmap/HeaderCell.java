/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview.heatmap;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * this class give the control on the header label style and events
 *
 * @author Yehia Farag
 */
public class HeaderCell extends VerticalLayout implements LayoutEvents.LayoutClickListener{
    private final int index;
    private final  String cellStyleName;
    private String selectStyle ="";
    private final Label valueLabel ;
    private boolean selected = false;
    public HeaderCell(boolean rowHeader, String value, int index) {
        valueLabel = new Label("<center><b>" + value + "</b></center>");
//        super("<b>" + value + "</b>");
        valueLabel.setWidth("150px");
        valueLabel.setHeight("40px");
        this.setWidth("150px");
        this.setHeight("50px");
        this.valueLabel.setContentMode(ContentMode.HTML);
        this.index = index;
        if (rowHeader) {
            this.cellStyleName = "hmrowlabel";
            this.setStyleName("hmrowlabel");
        } else {
            this.cellStyleName = "hmcolumnlabel";
            this.setStyleName("hmcolumnlabel");
        }
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.BOTTOM_CENTER);
        this.addLayoutClickListener(HeaderCell.this);
        

    }
    
    
    
    
    public void heighlightCellStyle(){
        
    this.setStyleName(cellStyleName+selectStyle+"_heighlightcell");
    
    }
    public void resetCellStyle(){
    this.setStyleName(cellStyleName+selectStyle);
    
    }
    public void selectCellStyle(){
      selectStyle ="_selected";
    this.setStyleName(cellStyleName+selectStyle);
    
    }
    public void unSelectCellStyle(){
        selectStyle="";
    this.setStyleName(cellStyleName);
    
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            selected = false;
            unSelectCellStyle();
        } else {
            selected = true;
            selectCellStyle();
        }
    }

}
