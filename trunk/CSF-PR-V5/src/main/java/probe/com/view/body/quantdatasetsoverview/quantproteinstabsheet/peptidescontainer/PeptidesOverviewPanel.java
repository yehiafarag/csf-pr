/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author yfa041
 */
public class PeptidesOverviewPanel extends VerticalLayout{
    private final Label authorLabel;
    private final AbsoluteLayout peptidesChartLayout;
    public PeptidesOverviewPanel(int width,int height){
        this.setWidth(width+"px");
        this.setHeight(height+"px");
        this.setStyleName(Reindeer.LAYOUT_BLUE);
        this.setMargin(true);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight("20px");
        topLayout.setWidth((width) + "px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.setMargin(false);
        this.addComponent(topLayout);
        
        authorLabel = new Label();
        topLayout.addComponent(authorLabel);
        topLayout.setComponentAlignment(authorLabel, Alignment.TOP_LEFT);
        authorLabel.setStyleName("filterShowLabel");
        
        peptidesChartLayout = new AbsoluteLayout();
        peptidesChartLayout.setWidth(width+"px");
        peptidesChartLayout.setHeight((height-20)+"px");
        
    }

    public AbsoluteLayout getPeptidesChartLayout() {
        return peptidesChartLayout;
    }
    public void updatePanel(){}
    

}
