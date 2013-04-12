package com.helperunits;

import java.io.Serializable;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Embedded;

public class CustomEmbedded extends Embedded implements Serializable,Comparable<CustomEmbedded> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean value;
	private String booleanValue;
	public CustomEmbedded(boolean value,Resource res) {
		super(String.valueOf(value),res);
		this.value = value;
		this.booleanValue= String.valueOf(value).toUpperCase();
	}
	
	public String toString()
	{
		return String.valueOf(value);
	}
	public String getBooleanValue()
	{
		return this.booleanValue;
	}

	public int compareTo(CustomEmbedded o) {
		String valueToCompare = o.getBooleanValue();
		return this.booleanValue.compareTo(valueToCompare);
	}
}
