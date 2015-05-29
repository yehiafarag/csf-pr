/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author Yehia Farag
 */
public class ComparisonProtein extends HorizontalLayout implements Serializable, Comparable<ComparisonProtein>{
    private String uniProtAccess;
    private String protName;

    public String getComparisonName() {
        return comparisonName;
    }

    public void setComparisonName(String comparisonName) {
        this.comparisonName = comparisonName;
    }
    private String comparisonName;

    public String getProtName() {
        return protName;
    }

    public void setProtName(String protName) {
        this.protName = protName;
    }
    private Integer up = 0;
    private Integer down=0;
    private int notReg;
    private int notProvided;
    private double penalty =0.0;
    private String key;
    private Label upLabel;
    private Label downLabel;
//    private final boolean upReg;
    private final  int total;
     private final DecimalFormat df ;
     private Double trendValue = 0.0;
     private double cellValue;
    public ComparisonProtein(int total) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.#", otherSymbols);
//        this.upReg=upReg;
        this.total=total+3;
         upLayout = new VerticalLayout();
         downLayout = new VerticalLayout();
         notRegLayout = new VerticalLayout();
         notProvidedLayout = new VerticalLayout();
        initLabelLayout();
    }
    
     final VerticalLayout upLayout,downLayout,notRegLayout,notProvidedLayout ;
       
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
        this.setComponentAlignment(downLabel, Alignment.TOP_LEFT);


            
        downLayout.setHeight("20px");
        downLayout.setStyleName("greenlayout");
        this.addComponent(downLayout);
        
        
        //        notProvidedLayout.setWidth("100%");
        notProvidedLayout.setHeight("20px");
        notProvidedLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        this.addComponent(notProvidedLayout);
//
//        downLayout.setWidth("100%");
         notRegLayout.setHeight("20px");
        notRegLayout.setStyleName("empty");//"empty"
        this.addComponent(notRegLayout);
        
        
        
        
        
        
        
        
        
        
       
//        
//        upLayout.setWidth("100%");
        upLayout.setHeight("20px");
        upLayout.setStyleName("redlayout");
        this.addComponent(upLayout);
        upLayout.setCaptionAsHtml(true);
        


         upLabel = new Label();
        upLabel.setContentMode(ContentMode.HTML);
        upLabel.setWidth("50px");
        upLabel.setHeight("20px");
        this.addComponent(upLabel);
         this.setComponentAlignment(upLabel, Alignment.TOP_RIGHT);
        
       
    

//        notRegLayout.setWidth("100%");
     
        
        
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
    
    public void addUp(int up) {
        trendValue+=(double)up;
        this.up += up;
    }
    
    public int getDown() {
        return down;
    }
    
    public void addDown(int down) {
        trendValue-=(double)down;
        this.down += down;
    }
    
    public int getNotReg() {
        return notReg;
    }
    
    public void addNotReg(int notReg) {
         penalty+=0.5;
        this.notReg += notReg;
    }
    
    public int getNotProvided() {
//        trendValue-=0.5;
        return notProvided;
    }
    
    public void addNotProvided(int notProvided) {
//        trendValue-=0.5;
        penalty+=0.5;
        this.notProvided += notProvided;
    }
    
    @Override
    public int compareTo(ComparisonProtein t) {
        Double v1 = null;
        if(up.intValue() == down.intValue()){        
            v1 = trendValue;
        }
        else if (trendValue > 0) {
            double factor = penalty;            
           v1  = trendValue - factor;
           v1 = Math.max(v1, 0) +((double)(up - down)/10.0);
        }
        else {
              double factor = penalty;          
           v1  = trendValue + factor ;
           v1 = Math.min(v1, 0) +((double)(up - down)/10.0);
        }
        Double v2 = null;
       if(t.up.intValue() == t.down.intValue()){        
            v2 = t.trendValue;
        }
        else if (t.trendValue > 0) {
            double factor = t.penalty;            
           v2  = t.trendValue - factor ;
           v2 = Math.max(v2, 0)+ ((double)(t.up - t.down)/10.0);
        }
        else {
              double factor = t.penalty;          
           v2  = t.trendValue + factor;
            v2 = Math.min(v2, 0) +((double)(t.up - t.down)/10.0);
        }
        return (v1).compareTo(v2);

    }
    
    public void updateLabelLayout() {
        int counter= 0;
        
//        int total = 6;//up + down + notProvided + notReg;
//        upLayout.setWidth(((up / total) * 100) + "%");
       
        this.setExpandRatio(upLayout, ((float)1.5 / total));
        this.setExpandRatio(upLayout, ((float)1.5 / total));
        int total = this.total-3; 
        downLabel.setValue("<p style='text-align: right;line-height:0.1'><strong> "+df.format(((double)down /(double) total) * 100.0) + "% &#8595; </strong>&nbsp;</p>");
        
        upLabel.setValue("<p style='text-align: right;line-height:0.1'><strong>&#8593; "+df.format(((double)up /(double) total) * 100.0) + "%</strong></p>");
       if (((float)up / total) <= 0.0) {
            upLayout.setVisible(false);
        } else {
            counter +=up;
            this.setExpandRatio(upLayout, ((float)up / total));
        }

//        notProvidedLayout.setWidth(((notProvided / total) * 100) + "%");
        if (((float)notProvided / total) <= 0.0) {
            notProvidedLayout.setVisible(false);
        } else {
            counter +=notProvided;
            this.setExpandRatio(notProvidedLayout, ((float)notProvided / total));
        }

//        downLayout.setWidth(((down / total) * 100) + "%");
        if (((float)down / total) <= 0.0) {
            downLayout.setVisible(false);
        } else {
            counter +=down;
            this.setExpandRatio(downLayout, ((float)down / total));
        }

//        notRegLayout.setWidth(((notReg / total) * 100) + "%");
//        if (((float)notReg / total) <= 0.0) {
////            notRegLayout.setVisible(false);
//        } else {
//            counter +=notReg;
//            
//        }
        Double v1 = null;
         if(up.intValue() == down.intValue()){        
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
        cellValue = v1;
//        v1 = Math.abs(v1);
        this.setExpandRatio(notRegLayout, ((float) (total - counter) / total));
        this.setDescription("down : " + down + " ========  not provided : " + notProvided + " ========  not regulated : " + notReg + " ========  up : " + up + " ========  trend value " + cellValue);

    }

    @Override
    public String toString() {
        return "down : " + down + " ========  not provided : " + notProvided + " ========  not regulated : " + notReg + " ========  up : " + up + " ========  trend value " + cellValue;
    }

}
