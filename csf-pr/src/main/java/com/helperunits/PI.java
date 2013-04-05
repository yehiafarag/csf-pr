package com.helperunits;

import java.io.Serializable;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Embedded;

public class PI extends Embedded implements Serializable,Comparable<PI> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	public PI(String value,Resource res){
		super(value,res);
		this.value = value;
		
	}
	public String toString()
	{
		return value;
	}
	public int compareTo(PI pi) {
		String valueToCompare = pi.toString();
		return this.value.compareTo(valueToCompare);
	}



}
