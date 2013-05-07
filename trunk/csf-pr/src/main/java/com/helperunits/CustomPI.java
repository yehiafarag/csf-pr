package com.helperunits;

import java.io.Serializable;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Embedded;

public class CustomPI extends Embedded implements Serializable,Comparable<CustomPI> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	public CustomPI(String value,Resource res){
		super(value,res);
		this.value = value;
                this.setHeight("15px");
		
	}
	public String toString()
	{
		return value;
	}
	public int compareTo(CustomPI pi) {
		String valueToCompare = pi.toString();
		return this.value.compareTo(valueToCompare);
	}



}
