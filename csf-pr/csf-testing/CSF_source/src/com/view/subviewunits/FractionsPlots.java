package com.view.subviewunits;
/*
import java.io.Serializable;
import java.util.Map;

import com.model.beans.ProteinBean;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.VerticalLayout;
import com.virtuallypreinstalled.hene.charts.BarChart;

public class FractionsPlots extends VerticalLayout implements   TabSheet.SelectedTabChangeListener, Button.ClickListener,Serializable{

	
	/**
	 * 
	 *
	private static final long serialVersionUID = 1L;
	private TabSheet t;
    private VerticalLayout l1;
    private VerticalLayout l2;
    private VerticalLayout l3;
    @SuppressWarnings("unused")
	private Tab t1, t2, t3;
    private Map<Integer, ProteinBean> protienFractionList;
    @SuppressWarnings("deprecation")
	public  FractionsPlots(Map<Integer, ProteinBean> protienFractionList) {
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
        BarChart bt = plot1(protienFractionList,"# Peptides");
        l1.removeAllComponents();
        l1.addComponent(bt);   
       
    }

	public void buttonClick(ClickEvent event) {
		  t.requestRepaint();
		
	}

	 public void selectedTabChange(SelectedTabChangeEvent event) {
	        String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
	        BarChart bt = plot1(protienFractionList,c);
	        if(c.equals("# Peptides"))
	        {
	        	l1.removeAllComponents();	        	
	        	l1.addComponent(bt);
	        }
	        else if(c.equals("# Spectra"))
	        {
	        	l2.removeAllComponents();	        	
	        	l2.addComponent(bt);
	        }
	        else
	        {
	        	l3.removeAllComponents();	        	
	        	l3.addComponent(bt);}
	    }
	 @SuppressWarnings("deprecation")
	private BarChart plot1(Map<Integer,ProteinBean> protienFractionList, String c)
		{
			BarChart barChart = new BarChart();
			barChart.setWidth(0.1f);
			barChart.setHeight(0.1f);
			IndexedContainer cont = new IndexedContainer();
			int highScore=0;
			if(c.equals("# Peptides") || c.equals("# Spectra"))
			{
				cont.addContainerProperty("value", Integer.class, null);
				cont.addContainerProperty("caption", String.class, null);
				Object itemId = null;
				Object mainItemId = null;	
				int x = 1;
				highScore=0;
				if(c.equals("# Peptides"))
				{
					barChart.setColors(new String[] { "#497482", "#ADD8E6", "#ADD8E6",
			                "#ADD8E6", "#ADD8E6", "#ADD8E6","#ADD8E6", "#ADD8E6",
			                "#ADD8E6", "#ADD8E6", "#ADD8E6" });
					
					mainItemId = cont.addItem();
					cont.getContainerProperty(mainItemId, "value").setValue(5);
					cont.getContainerProperty(mainItemId, "caption").setValue("1-10");
					
			
					for(ProteinBean pb:protienFractionList.values())
					{
						itemId = cont.addItem();
						cont.getContainerProperty(itemId, "value").setValue(pb.getNumberOfPeptidePerFraction());
						if(highScore < pb.getNumberOfPeptidePerFraction())
							highScore = pb.getNumberOfPeptidePerFraction();
						cont.getContainerProperty(itemId, "caption").setValue(x+"-"+(x+1));
						x++;
						
					}
				}
				else
				{
					barChart.setColors(new String[] { "#7BA6B4", "#7BA6B4", "#7BA6B4",
			                "#7BA6B4", "#7BA6B4", "#7BA6B4" });
			
					for(ProteinBean pb:protienFractionList.values())
					{
						itemId = cont.addItem();
						cont.getContainerProperty(itemId, "value").setValue(pb.getNumberOfSpectraPerFraction());
						if(highScore < pb.getNumberOfSpectraPerFraction())
							highScore = pb.getNumberOfSpectraPerFraction();
						cont.getContainerProperty(itemId, "caption").setValue(x);
						x++;
						
					}
				}
			}
			else{
				barChart.setColors(new String[] { "#497482", "#497482", "#497482",
		                "#497482", "#497482", "#497482" });
	
				cont.addContainerProperty("value", Double.class, null);
				cont.addContainerProperty("caption", Double.class, null);
				Object itemId = null;
				int x = 1;
				highScore=0;
				for(ProteinBean pb:protienFractionList.values())
				{
					itemId = cont.addItem();
					cont.getContainerProperty(itemId, "value").setValue(pb.getAveragePrecursorIntensityPerFraction());
					if(highScore < pb.getAveragePrecursorIntensityPerFraction())
						highScore =(int) pb.getAveragePrecursorIntensityPerFraction();
					cont.getContainerProperty(itemId, "caption").setValue(x);
					x++;
					
				}
				
			}
			barChart.setContainerDataSource(cont);
			barChart.setItemValuePropertyId("value");
			barChart.setItemCaptionPropertyId("caption");
			barChart.setGridMaxValue(highScore);
			barChart.setGridMarkLineCount(3);
		
			return barChart;
		}
		
		


}*/
