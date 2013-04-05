package com.helperunits;

import java.io.Serializable;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("unchecked")
public class MyButton extends Button implements Serializable,Comparable<MyButton> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String expName;
	private Integer key;
	@SuppressWarnings("deprecation")
	public MyButton(String expName,int key,ClickListener listener)
	{
		super(expName);
		super.addListener(listener);
		this.expName = expName;
		this.setStyle(Reindeer.BUTTON_LINK);
		this.key = key;
	}
	public void addListener(ClickListener listener)
	{
		super.addListener(listener);
	}
	public String toString()
	{
		return expName;
	}
	public Integer getKey()
	{
		return key;
	}
	public String getExpName()
	{
		return this.expName;
	}
	public int compareTo(MyButton o) {
		String expNameToCompare = o.expName;
		return this.expName.compareTo(expNameToCompare);
	}
	
	

}
