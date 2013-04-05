package com.view.subviewunits;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import MyUpdate.MyBarChartComponent;

import com.bibounde.vprotovis.AreaChartComponent;
import com.bibounde.vprotovis.LineChartComponent;

import com.bibounde.vprotovis.chart.bar.BarTooltipFormatter;
import com.bibounde.vprotovis.common.Point;
import com.model.FractionRangeUtilitie;
import com.model.beans.ProteinBean;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("unused")
public class FractionsPlots extends VerticalLayout implements  Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    private Map<Double, ProteinBean> protienFractionList;
    private double mw ;
    private ArrayList<String> ranges;
    private Button  close;
	int index = 0;

    private FractionRangeUtilitie fru = new FractionRangeUtilitie();
    
    @SuppressWarnings("deprecation")
	public  FractionsPlots(Map<Double, ProteinBean> protienFractionList, double mw,ArrayList<String> ranges) {
        this.mw = mw;
        this.ranges =ranges;
    	setSpacing(true);
    	this.setWidth("80%");      
        this.protienFractionList = protienFractionList;       
        VerticalLayout vlo = plotFull(protienFractionList,"# Peptides",ranges,mw);  
        vlo.setStyle(Runo.PANEL_LIGHT);
        vlo.setWidth("100%");
        this.setWidth("100%");      
        this.addComponent(vlo);
        
        
        
        
     
       
    }

	private VerticalLayout plotFull(Map<Double,ProteinBean> protienFractionList, String c,ArrayList<String>ranges,double mw)
		{
		final Map<String,String> rangesMap = 	fru.getRangeMap(ranges); 
		
		 int defaultPosition = this.getdefaultPos(ranges,mw); 
         VerticalLayout vlo = new VerticalLayout();
         vlo.setSizeUndefined();     
         
         
         MyBarChartComponent bar1 = new MyBarChartComponent(); 
         Label pepLable = new Label("<h5 style='font-family:verdana;color:#497482;'>"+"# Peptides"+"</h5>");
         pepLable.setContentMode(Label.CONTENT_XHTML);
         pepLable.setHeight("12px");  
         bar1.setMarginLeft(50.0d);
         bar1.setMarginRight(30.0d);
         bar1.setMarginBottom(15d);
         bar1.setXAxisVisible(true);
         bar1.setXAxisLabelVisible(true);
         bar1.setYAxisVisible(true);
         bar1.setYAxisLabelVisible(true);
         bar1.setYAxisGridVisible(true);
         bar1.setLegendAreaWidth(50.0d); 
         bar1.setLegendVisible(false);
         bar1.setBarInset(0.1d);
         bar1.setGroupInset(10d);
         int highScore1 = 0;	    	
         bar1.setColors(new String[]{ "#497482","#C0C0C0"});  				
         double[]fractionRanges1 = new double[protienFractionList.size()];
         double[]defaultRanges1 = new double[protienFractionList.size()];
         String[]rangeValues1 = new String[protienFractionList.size()] ;
         
         
         int x = 0;							
         for(ProteinBean pb:protienFractionList.values())
		 {
        	 
				fractionRanges1[x] = pb.getNumberOfPeptidePerFraction();
				if(defaultPosition == x)
					rangeValues1[x] = "["+(x+1)+"]";
				else
					rangeValues1[x] = ""+(x+1);
				if(highScore1 < pb.getNumberOfPeptidePerFraction()){
					highScore1 = pb.getNumberOfPeptidePerFraction();
				}
				
				x++;
		  }
          double step = plotYStepOpt(highScore1);
          bar1.setYAxisLabelStep(step);
          bar1.addSerie(null,fractionRanges1);//fractions values
          bar1.setGroupNames(rangeValues1);	
          bar1.setXAxisLabelVisible(true);
          bar1.setChartWidth(plotWidthOpt(rangeValues1.length));//1250.0d);
          bar1.setChartHeight(120.0d);
          
          
         
          
          
          
          //bar1.set
          
         BarTooltipFormatter tooltipFormatter = new BarTooltipFormatter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String getTooltipHTML(String serieName, double value,
					String groupName) {
				String tooltipName= "";
				String tempGN = "";
				String def = "";
				if(groupName.contains("["))
				{StringBuffer sb = new StringBuffer();
					for(int x = 1;x<groupName.length();x++)
					{
						if(groupName.charAt(x) != ']')
							sb.append(groupName.charAt(x));
						else{
							def ="   [Theoretical MW]";
							break;
						}
					}
					tempGN =sb.toString();
				}
				if(rangesMap.containsKey(groupName) || rangesMap.containsKey(tempGN))
				{
					tooltipName = rangesMap.get(groupName);
					if(tooltipName == null)
						tooltipName = rangesMap.get(tempGN);
					
					
				}
				String str = "Value : "+value+"   Range:"+tooltipName+""+def;
				
				return str;
			}
		};
          bar1.setTooltipFormatter(tooltipFormatter);
          MyBarChartComponent bar2 = new MyBarChartComponent();
          
          Label specLable = new Label("<h5 style='font-family:verdana;color:#497482;'>"+"# Spectra"+"</h5>");
          specLable.setContentMode(Label.CONTENT_XHTML);
          specLable.setHeight("12px");
                   
          bar2.setMarginLeft(50.0d);
          bar2.setMarginRight(30.0d);
          bar2.setMarginBottom(15d);
          bar2.setXAxisVisible(true);
          bar2.setXAxisLabelVisible(true);
          bar2.setYAxisVisible(true);
          bar2.setYAxisLabelVisible(true);
          bar2.setYAxisGridVisible(true);
          bar2.setLegendAreaWidth(50.0d);  
          bar2.setLegendVisible(false);
          bar2.setBarInset(0.1d);
          bar2.setGroupInset(10d);
          int highScore2 = 0;
          bar2.setColors(new String[]{"#497482","#C0C0C0"});
          highScore2=0;
		  double[]fractionRanges2 = new double[protienFractionList.size()];
		  double[]defaultRanges2 = new double[protienFractionList.size()];
		  String[]rangeValues2 = new String[protienFractionList.size()] ;
		  int x2 = 0;		
		  for(ProteinBean pb:protienFractionList.values())
		  {
			  fractionRanges2[x2] = pb.getNumberOfSpectraPerFraction();
			  if(defaultPosition == x2)
					rangeValues2[x2] =  "["+(x2+1)+"]";
				else
					rangeValues2[x2] = ""+(x2+1);
			  if(highScore2 < pb.getNumberOfSpectraPerFraction()){
					highScore2 = pb.getNumberOfSpectraPerFraction();
				}
				x2++;
			}
			double step2 = plotYStepOpt(highScore2);
			bar2.setYAxisLabelStep(step2);
			bar2.addSerie("Protein Fractions",fractionRanges2);//fractions values
			bar2.setGroupNames(rangeValues2);				
			bar2.setChartWidth(plotWidthOpt(rangeValues2.length));//1250.0d);
	        bar2.setChartHeight(120.0d);
	        
	        bar2.setTooltipFormatter(tooltipFormatter);
	          MyBarChartComponent bar3 = new MyBarChartComponent();
	          
	          Label intensityLable = new Label("<h5 style='font-family:verdana;color:#497482;'>"+"Avg Precursor Intensity"+"</h5>");
	          intensityLable.setContentMode(Label.CONTENT_XHTML);
	          intensityLable.setHeight("12px");
	          bar3.setMarginLeft(50.0d);
	          bar3.setMarginRight(30.0d);
	          bar3.setMarginBottom(15d);
	          bar3.setXAxisVisible(true);
	          bar3.setXAxisLabelVisible(true);
	          bar3.setYAxisVisible(true);
	          bar3.setYAxisLabelVisible(true);
	          bar3.setYAxisGridVisible(true);
	          bar3.setLegendAreaWidth(50.0d); 
	          bar3.setLegendVisible(false);
	          bar3.setBarInset(0.1d);
	          bar3.setGroupInset(10d);
			
			
			
				double highScore3=0d;
	    		bar3.setColors(new String[]{"#497482","#C0C0C0"});
				
				double[]fractionRanges3 = new double[protienFractionList.size()];
				double[]defaultRanges3 = new double[protienFractionList.size()];
				String[]rangeValues3 = new String[protienFractionList.size()] ;
					int x3 = 0;		
				for(ProteinBean pb:protienFractionList.values())
				{
					fractionRanges3[x3] = pb.getAveragePrecursorIntensityPerFraction();
					if(defaultPosition == x3)
						rangeValues3[x3] = "["+(x3+1)+"]";
					else
						rangeValues3[x3] = ""+(x3+1);
					if(highScore3 < pb.getAveragePrecursorIntensityPerFraction()){
						highScore3 = pb.getAveragePrecursorIntensityPerFraction();
					}
					x3++;
				}
				double step3 = plotYStepOpt(highScore3);
				bar3.setYAxisLabelStep(step3);
				
				bar3.addSerie("Protein Fractions",fractionRanges3);//fractions values
				bar3.setGroupNames(rangeValues3);			
				bar3.setChartWidth(plotWidthOpt(rangeValues3.length));//1250.0d);
		        bar3.setChartHeight(120.0d);
		        bar3.setTooltipFormatter(tooltipFormatter);
		        
		        
		        vlo.addComponent(pepLable);
		        vlo.addComponent(bar1);
		        vlo.addComponent(specLable);
		        vlo.addComponent(bar2);
		        vlo.addComponent(intensityLable);
		        vlo.addComponent(bar3);
		        
		        
		 	
			return vlo;
		}
	
	
	
	

	private double[] convertorToArray(ArrayList<Double> rangeValuesList) {
		double[] arr = new double[rangeValuesList.size()];
		for(int x=0;x<rangeValuesList.size();x++)
			arr[x]=rangeValuesList.get(x);
		return arr;
	}
	private String[] convertorToStringArray(ArrayList<String> rangeValuesList) {
		String[] arr = new String[rangeValuesList.size()];
		for(int x=0;x<rangeValuesList.size();x++)
			arr[x]=rangeValuesList.get(x);
		return arr;
	}

	private int getdefaultPos( ArrayList<String> ranges2, double mw2) {
		double minRange = 0d;
		 double maxRange  = 0d;
		for(int x=0;x<ranges2.size();x++){
			 minRange = Double.valueOf((ranges2.get(x).split("\t")[1]).split("-")[0]);
			
			 try{
			 maxRange = Double.valueOf(ranges2.get(x).split("\t")[1].split("-")[1].split(" ")[0]);
			
			 }catch(java.lang.NumberFormatException e){
				 if((ranges2.get(x).split("\t")[1].split("-")[1]).contains(">"))
				 {
					 maxRange = Double.valueOf(0);
					 if(mw2>=minRange)
						 return x;
				 }
			 }
			 
			 if(mw2>=minRange && mw2<maxRange)
				return  x;
		}
		return 0;
	}
	
	private double plotWidthOpt(int rangesNumber)
	{
		return Double.valueOf(23*rangesNumber);
		
	}
	
	
	private double plotYStepOpt(double highScore)
	{
		return  Double.valueOf((int)highScore/5);
	}
	private double plotXStepOpt(double maxRange)
	{
		return Double.valueOf((int) maxRange/30);
	}
	
	private double plotLengthOpt(double highScore,double step)
	{
		
		double stepsNumber = highScore/step;
		if (stepsNumber <= 5) 
			return 200.1d;
		else if (stepsNumber>5 && stepsNumber <= 10) 
			return 400.0d;
		else if (stepsNumber>10 && stepsNumber <= 20) 
			return 400.0d;
		else if (stepsNumber>20 && stepsNumber <= 30) 
			return 500.0d;
		else  
			return 700.0d;
		
	}
	

}