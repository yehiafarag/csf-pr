package com.view.subviewunits;

import java.io.Serializable;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

public class TableResizeSet extends HorizontalLayout implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table table;
	private final String SMALL_SIZE  =  "160px";
	private final String MEDIUM_SIZE =  "267.5px";
	private final String LARGE_SIZE  =  "360px";
	
	private String currentSize;
	public TableResizeSet(Table table,String currentSize)
	{
		this.currentSize = currentSize;
		this.setSpacing(false);
		this.table = table;
		this.setWidth("160px");
		this.setHeight("21px");
		
		Button b1 = init("http://sphotos-a.ak.fbcdn.net/hphotos-ak-ash4/483750_123345324515735_2017651328_n.jpg",SMALL_SIZE);
		Button b2 = init("http://sphotos-d.ak.fbcdn.net/hphotos-ak-ash4/381963_123344494515818_1837730074_n.jpg",MEDIUM_SIZE);
		Button b3 = init("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/418955_123344261182508_609071937_n.jpg",LARGE_SIZE);
		
		b1.setDescription("Small Table Size");  
		b2.setDescription("Medium Table Size");  
		b3.setDescription("Large Table Size");  
		this.addComponent(b1);
		this.addComponent(b2);
		this.addComponent(b3);
		this.setComponentAlignment(b1, Alignment.BOTTOM_RIGHT);
		this.setComponentAlignment(b2, Alignment.BOTTOM_RIGHT);
		this.setComponentAlignment(b3, Alignment.BOTTOM_RIGHT);
		this.setMargin(false);
		this.setSpacing(false);
		
	}
	
	
	private Button init(String link,final String size)
	{
		 Button b = new Button();
	     b.setStyleName(BaseTheme.BUTTON_LINK);
	     b.setIcon(new ExternalResource(link));
	   b.addListener(new ClickListener() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				table.setHeight(size);
				currentSize = size;
			}
		});
	     return b;
	}

	public String getCurrentSize()
	{
		return this.currentSize;
	}
}
