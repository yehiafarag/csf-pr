package com.view;


import java.io.Serializable;

import javax.servlet.ServletContext;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Link;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class WelcomePage extends Application implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		 
		ApplicationContext ctx = getContext();
		WebApplicationContext webCtx = (WebApplicationContext) ctx;
		ServletContext scx = webCtx.getHttpSession().getServletContext();
		setUrl(scx.getInitParameter("url"));
		setDbName(scx.getInitParameter("dbName"));
		setDriver(scx.getInitParameter("driver"));
		setUserName(scx.getInitParameter("userName")); 
		setPassword(scx.getInitParameter("password"));                
		setDataFolderPath(scx.getRealPath("")+"/DataFolder");
		//this.setTheme(Runo.THEME_NAME);
		initLayout();
	}
	@SuppressWarnings("deprecation")
	private void  initLayout() 
	{
		
		ExternalResource ico_1 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-ash3/574636_108340259349575_2027925130_n.jpg");		
		Link image1 = new Link(null,     new ExternalResource("http://www.uib.no/rg/probe"));
		image1.setIcon(ico_1);
		image1.setTargetName("_blank");
		
		Link image2 = new Link(null,     new ExternalResource("http://www.uib.no/"));
		image2.setIcon( new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-prn1/533227_118477988335802_947238298_n.jpg"));
		image2.setTargetName("_blank");
		image2.setWidth("105px");
		
		Link image3 = new Link(null,     new ExternalResource("http://www.stiftkgj.no/"));
		image3.setIcon( new ExternalResource("http://sphotos-h.ak.fbcdn.net/hphotos-ak-snc6/188329_108340226016245_257713989_n.jpg"));
		image3.setTargetName("_blank");
		
		MainWindow mw = new MainWindow(url,dbName,driver,userName,  password,image1,image2,image3,dataFolderPath);  		
		//mw.setWidth("100%");
		//mw.setHeight("100%");
		mw.setStyle(Reindeer.WINDOW_LIGHT);		
		Window w = new Window("CSF Proteome Resource (CSF-PR)",mw );
		setMainWindow(w);
		
		//mw.setBodyHeight();
	
		
		
		
	       
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private  String url,dbName ,driver ,userName , password,dataFolderPath ;

    public String getDataFolderPath() {
        return dataFolderPath;
    }

    public void setDataFolderPath(String dataFolderPath) {
        this.dataFolderPath = dataFolderPath;
    }


	
	


}
