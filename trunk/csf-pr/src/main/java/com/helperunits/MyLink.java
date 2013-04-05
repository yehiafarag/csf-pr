package com.helperunits;

import java.io.Serializable;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

public class MyLink extends Link implements Serializable, Comparable<MyLink>{

	private String link;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	/**
	 * @param args
	 */
	public MyLink(String link,ExternalResource  res )
	{
		super(link,res);
		this.link = link;
		
	}
	public String toString()
	{
		return link;
	}
	public int compareTo(MyLink myLink) {
		 
		String compareLink = ((MyLink) myLink).getLink(); 
 
		//ascending order
		return (this.link.compareTo(compareLink));
 
		//descending order
		//return compareQuantity - this.quantity;
 
	}	
	public String getLink()
	{
		return this.link;
	}

}
