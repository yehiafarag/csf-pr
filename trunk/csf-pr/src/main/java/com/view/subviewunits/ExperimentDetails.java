package com.view.subviewunits;

import java.io.Serializable;

import com.model.beans.ExperimentBean;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ExperimentDetails extends VerticalLayout implements Serializable, com.vaadin.event.LayoutEvents.LayoutClickListener{

	private VerticalLayout vlo;
	private Label ExpLable;
	public ExperimentDetails(ExperimentBean exp,boolean visability)
	{
		ExpLable = new Label("<h4  style='font-family:verdana;color:#000000;'>Dataset  Information (Click To View!)</h4>");
		ExpLable.setContentMode(Label.CONTENT_XHTML);
		ExpLable.setHeight("20px");
		this.addListener(this);
		this.vlo = FormWithComplexLayout(exp);
		this.addComponent(ExpLable);	
		this.setWidth("100%");		
		this.addComponent(vlo);
		if(visability){
			this.showDetails();
		}
		else
		{
			this.hideDetails();
		}
	}
	
	@SuppressWarnings("deprecation")
	private VerticalLayout  FormWithComplexLayout(ExperimentBean exp) {
		 VerticalLayout vlo = new VerticalLayout();
		 vlo.setSpacing(true);
		 vlo.setSizeFull();
		HorizontalLayout hlo = new HorizontalLayout();
        VerticalLayout topSpacer = new VerticalLayout();
        topSpacer.setHeight("2px");
        topSpacer.setMargin(false, true, false, false);
        topSpacer.setStyle(Reindeer.LAYOUT_BLACK);
        vlo.addComponent(topSpacer);
        vlo.addComponent(hlo);
        VerticalLayout buttomSpacer = new VerticalLayout();
        buttomSpacer.setHeight("2px");
        buttomSpacer.setStyle(Reindeer.LAYOUT_BLACK);
        buttomSpacer.setMargin(true,true,false,false);
        vlo.addComponent(buttomSpacer);
        
        
        
        VerticalLayout l1 = new VerticalLayout();
       
		
        Label ExpLable1_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Dataset  Name:</strong><br/>"+exp.getName()+"</h5>");
		ExpLable1_1.setContentMode(Label.CONTENT_XHTML);
		ExpLable1_1.setHeight("25px");
		
		Label ExpLable1_2 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Species:</strong><br/>"+exp.getSpecies()+"</h5>");
		ExpLable1_2.setContentMode(Label.CONTENT_XHTML);
		ExpLable1_2.setHeight("25px");
		
		Label ExpLable1_3 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Sample Type:</strong><br/>"+exp.getSampleType()+"</h5>");
		ExpLable1_3.setContentMode(Label.CONTENT_XHTML);
		ExpLable1_3.setHeight("25px");
		
		
		Label ExpLable1_4 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Sample Processing:</strong><br/>"+exp.getSampleProcessing()+"</h5>");
		ExpLable1_4.setContentMode(Label.CONTENT_XHTML);
		ExpLable1_4.setHeight("25px");
		
		String  href = null;
		Label ExpLable1_5 = null;
        if(exp.getPublicationLink().equalsIgnoreCase("NOT AVAILABLE")||exp.getPublicationLink().equalsIgnoreCase(""))
        {
        	ExpLable1_5 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>No Publication Link Available </strong></h5>");
    		ExpLable1_5.setHeight("10px");
        }
        else
        {
        	href =exp.getPublicationLink().toLowerCase();
        	if(href.contains("http://")||href.contains("https://"))
        		;
        	else
        		href = "http://"+href;
        	ExpLable1_5 = new Label("<h5><a href='"+href+"'  target='_blank'>Publication Link</a></h5>");
        	ExpLable1_5.setHeight("25px");
      	
        }
         
        ExpLable1_5.setContentMode(Label.CONTENT_XHTML);
         l1.addComponent(ExpLable1_1);
      	if(exp.getDescription().length() <= 100){
			
			Label ExpLable2_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Description:</strong><br/>"+exp.getDescription()+"</h5>");
			ExpLable2_1.setContentMode(Label.CONTENT_XHTML);
			ExpLable2_1.setHeight("25px");
			l1.addComponent(ExpLable2_1);
			Panel p = new Panel();
			p.setWidth("80%");
			p.setHeight("60px");			
			p.setStyle(Reindeer.PANEL_LIGHT);        
	       
	        //l1.addComponent(p);
		}
      	else{
	        Label ExpLable2_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Description:</strong></h5>");
			ExpLable2_1.setContentMode(Label.CONTENT_XHTML);
			ExpLable2_1.setHeight("15px");
			
			Label ExpLable2_2 = new Label("<h5 style='font-family:verdana;color:gray;'>"+exp.getDescription()+"</h5>");
			ExpLable2_2.setContentMode(Label.CONTENT_XHTML);
			
			
			VerticalLayout lTemp = new VerticalLayout();
			//lTemp.addComponent(ExpLable2_1);
			lTemp.addComponent(ExpLable2_2);
			lTemp.setMargin(false);
			ExpLable2_2.setSizeFull();
			Panel p = new Panel();
			p.setContent(lTemp);
			p.setWidth("80%");
			p.setHeight("70px");
			p.setScrollable(true);
			p.setScrollTop(20);
			p.setScrollLeft(50);
			
			p.setStyle(Reindeer.PANEL_LIGHT);        
	       
	        l1.addComponent(ExpLable2_1);
	        l1.setComponentAlignment(ExpLable2_1, Alignment.BOTTOM_LEFT);
			l1.addComponent(p);
			l1.setComponentAlignment(p, Alignment.TOP_LEFT);
      	}
        
        
        
       
        

        
        VerticalLayout l2 = new VerticalLayout();
        
      
		
	
		Label ExpLable2_3 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Instrument Type:</strong><br/>"+exp.getInstrumentType()+"</h5>");
		ExpLable2_3.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_3.setHeight("25px");	
		Label ExpLable2_4 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Frag Mode:</strong><br/>"+exp.getFragMode()+"</h5>");
		ExpLable2_4.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_4.setHeight("25px");
		Label ExpLable2_5 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Uploaded By:</strong><br/>"+exp.getUploadedByName()+"</h5>");
		ExpLable2_5.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_5.setHeight("25px");
		
		Label ExpLable2_6 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Email:</strong><br/>"+exp.getEmail()+"</h5>");
		ExpLable2_6.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_6.setHeight("25px");
		
		
		Label ExpLable2_7 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Fractions</strong><br/>"+exp.getFractionsNumber()+"</h5>");
		ExpLable2_7.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_7.setHeight("25px");
		Label ExpLable2_8 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Proteins:</strong><br/>"+exp.getProteinsNumber()+"</h5>");
		ExpLable2_8.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_8.setHeight("25px");
		
		Label ExpLable2_9 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Peptides</strong><br/>"+exp.getPeptidesNumber()+"</h5>");
		ExpLable2_9.setContentMode(Label.CONTENT_XHTML);
		ExpLable2_9.setHeight("25px");
		
		
		
		l1.addComponent(ExpLable2_3);
		
		l2.addComponent(ExpLable1_2);
		l2.addComponent(ExpLable2_4);
		l2.addComponent(ExpLable1_4);
		
        l2.addComponent(ExpLable1_3);
        l2.addComponent(ExpLable2_9);
		
		
		VerticalLayout l3 = new VerticalLayout();
		
		
		l3.addComponent(ExpLable2_7);
		
		
		l3.addComponent(ExpLable2_8);
		l3.addComponent(ExpLable2_5);
		l3.addComponent(ExpLable2_6);
		l3.addComponent(ExpLable1_5);
		
		
        
        
        hlo.setWidth("100%");
        hlo.addComponent(l1);
        hlo.addComponent(l2);
        hlo.addComponent(l3);
        hlo.setExpandRatio(l1, 3);
        hlo.setExpandRatio(l2, 3);
        hlo.setExpandRatio(l3, 1);
        
        hlo.setComponentAlignment(l3, Alignment.TOP_RIGHT);
        return vlo;
    }

	public void layoutClick(LayoutClickEvent event) {
		
		if(vlo.isVisible()){
		//	ExpLable.setValue("<h4  style='font-family:verdana;color:#000000;'>Dataset  Information (Click To View!)</h4>");
		//	ExpLable.setContentMode(Label.CONTENT_XHTML);
		//	ExpLable.setHeight("20px");
		//	vlo.setVisible(false);
		}
		else{
			
			this.showDetails();
		}
		
	}
	private  void showDetails()
	{
		ExpLable.setValue("<h4  style='font-family:verdana;color:#000000;'>Dataset  Information</h4>");
		ExpLable.setContentMode(Label.CONTENT_XHTML);
		ExpLable.setHeight("20px");
		vlo.setVisible(true);
	}
	
	public void hideDetails()
	{
		ExpLable.setValue("<h4  style='font-family:verdana;color:#000000;'>Dataset  Information (Click To View!)</h4>");
		ExpLable.setContentMode(Label.CONTENT_XHTML);
		ExpLable.setHeight("20px");
		vlo.setVisible(false);
	}

	public boolean isVisability()
	{
		return vlo.isVisible();
	}
	public void setVisability(boolean test)
	{
		if(test)
			this.showDetails();
		else
			this.hideDetails();
	}
}
