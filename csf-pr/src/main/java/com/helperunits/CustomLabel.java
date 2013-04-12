package com.helperunits;

import java.io.Serializable;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;


public class CustomLabel extends VerticalLayout implements Serializable, Comparable<Object> {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String str ;
	public CustomLabel(String str ,String color)
	{
		Label l = new Label("<label style='font-family:verdana; color:"+color+";'>"+str+"</label>");		
		l.setContentMode(Label.CONTENT_XHTML);
		this.str = str;
		l.setHeight("17px");		
		this.setHeight("18px");
		this.setMargin(false,false,false,false);
		this.addComponent(l);
		this.setComponentAlignment(l, Alignment.TOP_LEFT);
		this.setDescription("The Peptide Sequence: "+str);
		
		
	}
	
	public int compareTo(Object myLabel) {
		 
		String compareString =  myLabel.toString(); 
 
		return (this.str.compareTo(compareString));
 
		
 
	}	
	public String toString()
	{
		return str;
	}

}
