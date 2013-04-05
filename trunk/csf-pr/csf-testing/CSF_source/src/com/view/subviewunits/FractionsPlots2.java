package com.view.subviewunits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import com.bibounde.vprotovis.BarChartComponent;
import com.model.beans.ProteinBean;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;

public class FractionsPlots2 extends VerticalLayout implements   TabSheet.SelectedTabChangeListener, Button.ClickListener,Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabSheet t;
    private VerticalLayout l1;
    private VerticalLayout l2;
    private VerticalLayout l3;
    @SuppressWarnings("unused")
	private Tab t1, t2, t3;	
    private Map<Integer, ProteinBean> protienFractionList;
    private double mw ;
    private ArrayList<String> ranges;
    @SuppressWarnings("deprecation")
	public  FractionsPlots2(Map<Integer, ProteinBean> protienFractionList, ArrayList<String> ranges, double mw) {
        this.mw = mw;
        this.ranges = ranges;
    	setSpacing(true);
        setWidth("90%");
        this.setStyle(Reindeer.LAYOUT_WHITE);
        this.protienFractionList = protienFractionList;
        // Tab 1 content
        l1 = new VerticalLayout();
        l1.setMargin(true);
        // Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");
        // Tab 3 content        
        l3 = new VerticalLayout();
        l3.setMargin(true);

        t = new TabSheet();
        t.setStyle(Reindeer.TABSHEET_MINIMAL);
        t.setHeight("100%");
        t.setWidth("100%");
        t1 = t.addTab(l1, "# Peptides", null);
        t2 = t.addTab(l2, "# Spectra", null);
        t3 = t.addTab(l3, "Average Precursor Intensity", null);
        t.addListener(this); 
        addComponent(t);
        t.setSelectedTab(t1);
        VerticalLayout vlo = plot1(protienFractionList,"# Peptides",ranges,mw);
        l1.removeAllComponents();
        //VerticalLayout vlo = new VerticalLayout();
       // vlo.setSizeFull();
       // vlo.addComponent(bt);
        l1.addComponent(vlo);   
       
    }

	public void buttonClick(ClickEvent event) {
		  t.requestRepaint();
		
	}

	 public void selectedTabChange(SelectedTabChangeEvent event) {
	        String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
	        VerticalLayout vlo = plot1(protienFractionList,c,ranges,mw);
	        if(c.equals("# Peptides"))
	        {
	        	l1.removeAllComponents();	        	
	        	l1.addComponent(vlo);
	        }
	        else if(c.equals("# Spectra"))
	        {
	        	l2.removeAllComponents();	        	
	        	l2.addComponent(vlo);
	        }
	        else
	        {
	        	l3.removeAllComponents();	        	
	        	l3.addComponent(vlo);}
	    }

	private VerticalLayout plot1(Map<Integer,ProteinBean> protienFractionList, String c,ArrayList<String>ranges,double mw)
		{
		 
		 BarChartComponent bar = new BarChartComponent();         
         VerticalLayout vlo = new VerticalLayout();
         vlo.setWidth("1500px");
		 bar.setChartWidth(Double.valueOf(6)*140.9d);
         bar.setChartHeight(350.5d);
         
         bar.setWidth("800px");
         bar.setHeight("350px");

         bar.setMarginLeft(50d);
         bar.setMarginBottom(50d);

         bar.setXAxisVisible(true);
         bar.setXAxisLabelVisible(true);

         bar.setYAxisVisible(true);
         bar.setYAxisLabelVisible(true);
         bar.setYAxisGridVisible(true);
         bar.setColors(new String[]{"#497482","#C0C0C0"});
         

         bar.setLegendVisible(true);
         bar.setLegendAreaWidth(270.5d);
       

         this.addComponent(bar);
         int defaultPosition = this.getdefaultPos(ranges,mw);
	        
         	int highScore = 0;
         	int secHigh = 0;
	    	if(c.equals("# Peptides") || c.equals("# Spectra"))
			{
	    		
				if(c.equals("# Peptides"))
				{
					
					highScore=0;
		    		secHigh = 0;
					 bar.setColors(new String[]{ "#7BA6B4","#C0C0C0"});  				
					double[]fractionRanges = new double[protienFractionList.size()];
					double[]defaultRanges = new double[protienFractionList.size()];
					String[]rangeValues = new String[protienFractionList.size()] ;
					int x = 0;							
					for(ProteinBean pb:protienFractionList.values())
					{
						fractionRanges[x] = pb.getNumberOfPeptidePerFraction();
						rangeValues[x] = ranges.get(x).split("\t")[1];
						if(highScore < pb.getNumberOfPeptidePerFraction()){
							secHigh =highScore;
							highScore = pb.getNumberOfPeptidePerFraction();
						}
						
						x++;
					}
					if((highScore-secHigh)%(2) == 0)
						bar.setYAxisLabelStep(Double.valueOf((highScore-secHigh)/(6)));
					else
						bar.setYAxisLabelStep(Double.valueOf((highScore-secHigh)/(6)));
					
					bar.addSerie("Protein Fractions",fractionRanges);//fractions values
					bar.setGroupNames(rangeValues);					
					defaultRanges[defaultPosition] = highScore;					
					bar.addSerie("Default Range", defaultRanges);
				}
				else
				{
					
					bar.setColors(new String[]{"#497482","#C0C0C0"});
					highScore=0;
		    		secHigh = 0;
					double[]fractionRanges = new double[protienFractionList.size()];
					double[]defaultRanges = new double[protienFractionList.size()];
					String[]rangeValues = new String[protienFractionList.size()] ;
					int x = 0;		
					for(ProteinBean pb:protienFractionList.values())
					{
						fractionRanges[x] = pb.getNumberOfSpectraPerFraction();
						rangeValues[x] = ranges.get(x).split("\t")[1];
						if(highScore < pb.getNumberOfSpectraPerFraction()){
							secHigh =highScore;
							highScore = pb.getNumberOfSpectraPerFraction();
						}
						x++;
					}
					if((highScore-secHigh)%(2) == 0)
						bar.setYAxisLabelStep(Double.valueOf((highScore-secHigh)/(6)));
					else
						bar.setYAxisLabelStep(Double.valueOf((highScore-secHigh)/(6)));
							
					bar.addSerie("Protein Fractions",fractionRanges);//fractions values
					bar.setGroupNames(rangeValues);
					defaultRanges[defaultPosition] = highScore;					
					bar.addSerie("Default Range", defaultRanges);
				}
			}
			else{
				double highScore2=0d;
				double secHigh2 = 0d;
	    		bar.setColors(new String[]{"#000000","#C0C0C0"});
				
				double[]fractionRanges = new double[protienFractionList.size()];
				double[]defaultRanges = new double[protienFractionList.size()];
				String[]rangeValues = new String[protienFractionList.size()] ;
					int x = 0;		
				for(ProteinBean pb:protienFractionList.values())
				{
					fractionRanges[x] = pb.getAveragePrecursorIntensityPerFraction();
					rangeValues[x] = ranges.get(x).split("\t")[1];
					if(highScore2 < pb.getAveragePrecursorIntensityPerFraction()){
						secHigh2 =highScore2;
						highScore2 = pb.getAveragePrecursorIntensityPerFraction();
					}
					x++;
				}
				if((highScore2-secHigh2)%(2.0) == 0)
					bar.setYAxisLabelStep(Double.valueOf((highScore2-secHigh2)/(6.0)));
				else
					bar.setYAxisLabelStep(Double.valueOf((highScore2-secHigh2)/(6.0)));
				
				bar.addSerie("Protein Fractions",fractionRanges);//fractions values
				bar.setGroupNames(rangeValues);
				defaultRanges[defaultPosition] = highScore;					
				bar.addSerie("Default Range", defaultRanges);
			}
				 
		 	
	    	vlo.addComponent(bar);
			return vlo;
		}

	private int getdefaultPos(ArrayList<String> ranges2, double mw2) {
		double minRange = 0;
		 double maxRange  = 0;
		for(int x=0;x<ranges2.size();x++){
			 minRange = Double.valueOf(ranges2.get(x).split("\t")[1].split("-")[0]);
			 maxRange = Double.valueOf(ranges2.get(x).split("\t")[1].split("-")[1]);
			 if(mw2>=minRange && mw2<maxRange)
				return  x;
		}
		return 0;
	}


}

