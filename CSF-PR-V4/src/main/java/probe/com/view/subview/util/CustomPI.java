package probe.com.view.subview.util;

import com.vaadin.server.Resource;
import java.io.Serializable;

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
                this.setWidth("20px");
		
	}
        @Override
	public String toString()
	{
		return value;
	}
        @Override
	public int compareTo(CustomPI pi) {
		String valueToCompare = pi.toString();
		return this.value.compareTo(valueToCompare);
	}



}
