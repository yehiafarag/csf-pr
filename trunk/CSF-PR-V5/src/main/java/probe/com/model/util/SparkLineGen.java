/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util;

import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author y-mok_000
 */
public class SparkLineGen {
    
    private double min;
    private double max;
    
    public SparkLineGen(double min,double max){
    this.max=max;
    this.min=min;
    
    
    }
    public VerticalLayout getSparkLine(double value){
    VerticalLayout sparkLine = new VerticalLayout();
    double width = value*100/max;
    sparkLine.setHeight("20px");
    sparkLine.setStyleName("bluesparkline");    
    sparkLine.setWidth(((int)width)+"px");
    
    
    return sparkLine;
    
    
    
    }
    
}
