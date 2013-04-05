package com.view;


import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class WelcomePage extends Application implements Serializable, HttpServletRequestListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletResponse resp;
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
		
		initLayout();
	}
	@SuppressWarnings("deprecation")
	private void  initLayout() 
	{
		Embedded image1 = new Embedded("",  new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-snc7/375554_10151333504071187_494131087_n.jpg"));
		Embedded image2 = new Embedded("",  new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-prn1/32433_10151333504076187_400303310_n.jpg"));
		Embedded image3 = new Embedded("",  new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/537727_10151333504086187_168401241_n.jpg"));
		
		MainWindow mw = new MainWindow(url,dbName,driver,userName,  password,image1,image2,image3,resp);  		
		mw.setStyle(Reindeer.WINDOW_LIGHT);		
		setMainWindow(new Window("CSF PROTEOME RESOURCE (CSF PR)",mw ));	        
	       
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
	private  String url,dbName ,driver ,userName , password ;
	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {
		
		resp = response;
		
	}
	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	


}
