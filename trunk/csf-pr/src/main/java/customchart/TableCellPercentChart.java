package customchart;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class TableCellPercentChart extends VerticalLayout implements Serializable,Comparable<TableCellPercentChart>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double value;
	private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
	private DecimalFormat df;

	public TableCellPercentChart(Double value)
	{
		otherSymbols.setGroupingSeparator('.'); 
		df = new DecimalFormat("#.#",otherSymbols);
		
		this.value = value;
		/*if(value == 100.0){
			 PI hund = null;
			 Resource res  = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-ash3/559887_117839348399666_1520262262_n.jpg");
			 
			 hund  = new PI(df.format(value), res);		
			 hund.setCaption("");
			 hund.setAlternateText("100%");
			 hund.setSizeUndefined();
			 hund.setDescription(""+df.format(value)+"%");			 
			 this.addComponent(hund);
		        this.setComponentAlignment(hund, Alignment.TOP_CENTER);
		        this.setHeight("20px");
			
		}
		else*/{
				HorizontalLayout hlo = new HorizontalLayout();
				CustomBarChartComponent bar = new CustomBarChartComponent();
				if(value == null || value == 0){
					bar.addSerie("Serie_0", new double[] {0d, 0d, 0d, 0d,0d, 0d, 0d, 0d,0d, 0d});
					this.value = 0d;
				}
				else if(value > 0 && value <= 10)
					bar.addSerie("Serie_0", new double[] {0d, 10d, 0d, 0d,0d, 0d, 0d, 0d,0d, 0d});
				else if(value > 10 && value <= 20)
					bar.addSerie("Serie_0", new double[] {0d, 0d, 10d, 0d,0d, 0d, 0d, 0d,0d, 0d});
				else if(value > 20 && value <= 30)
					bar.addSerie("Serie_0", new double[] {0d, 0d, 0d, 10d,0d, 0d, 0d, 0d,0d, 0d});
				else if(value > 30 && value <= 40)
					bar.addSerie("Serie_0", new double[] {0d, 0d, 0d, 0d,10d, 0d, 0d, 0d,0d, 0d});
				else if(value > 40 && value <= 50)
					bar.addSerie("Serie_0", new double[] {0d, 0d, 0d, 0d,10d, 0d, 0d, 0d,0d, 0d});
				else if(value > 50 && value <= 60)
					bar.addSerie("Serie_0", new double[] {0d, 00d, 0d, 0d,0d, 10d, 0d, 0d,0d, 0d});
				
				else if(value > 60 && value <= 70)
					bar.addSerie("Serie_0", new double[] {0d, 0d, 0d, 0d,0d, 0d, 10d, 0d,0d, 0d});
				else if(value > 70 && value <= 80)
					bar.addSerie("Serie_0", new double[] {0d, 00d, 0d, 0d,0d, 0d, 0d, 10d,0d, 0d});
				else if(value > 80 && value <= 90)
					bar.addSerie("Serie_0", new double[] {0d, 00d, 0d, 0d,0d, 0d, 0d, 0d,10d, 0d});
				else if(value > 90 && value <= 100)
					bar.addSerie("Serie_0", new double[] {0d, 00d, 0d, 0d,0d, 0d, 0d, 0d,0d, 10d});
				bar.setMarginTop(1d);
				bar.setMarginBottom(2d);
				bar.setMarginRight(2d);
				
		        bar.setGroupNames(new String[]{"1","2","3","4","5","6","7","8","9"});
		        bar.setChartWidth(120d);
		        bar.setChartHeight(18d); 
		     	bar.setXAxisVisible(true);
		        bar.setYAxisLabelStep(5d);        
		        bar.setBarInset(0.1d);
		        bar.setGroupInset(5d);
		        bar.setTooltipEnabled(false);
		        hlo.addComponent(bar);
		        bar.setDescription(df.format(value)+"%");
		        hlo.setDescription(df.format(value)+"%");
		        this.addComponent(hlo);
		        //this.setComponentAlignment(hlo, Alignment.TOP_RIGHT);
		}
       // Label l = new Label(df.format(value));
       // hlo.addComponent(l);
       // l.setWidth("50px");
       // hlo.setComponentAlignment(bar, Alignment.MIDDLE_LEFT);
       // hlo.setComponentAlignment(l, Alignment.MIDDLE_RIGHT);
      //  hlo.setWidth("150px");
        //hlo.setExpandRatio(bar, 3.5f);
	    //hlo.setExpandRatio(l, 1.5f);
       
        //this.setDescription((df.format(value)+"%"));
	}
	public String toString()
	{
		
		return df.format(value);
	}
	public int compareTo(TableCellPercentChart o) {
		double valueToCompare = o.getValue();
		return this.value.compareTo(valueToCompare);
	}
	public Double getValue()
	{
		return this.value;
	}

	

}
