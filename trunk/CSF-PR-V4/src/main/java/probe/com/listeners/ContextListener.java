package probe.com.listeners;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import probe.com.model.Authenticator;

import probe.com.dal.DataAccess;

public class ContextListener implements ServletContextListener,Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String URL,D_B_NAME ,DRIVER ,USER_NAME , PASSWORD ;
	private String adminName ,adminPassword,adminEmail;
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}
        @Override
	public void contextInitialized(ServletContextEvent event) {
		
		ServletContext scx = event.getServletContext();
		URL = scx.getInitParameter("url");
		D_B_NAME = scx.getInitParameter("dbName");
		DRIVER = scx.getInitParameter("driver");
		USER_NAME = scx.getInitParameter("userName"); 
		PASSWORD = scx.getInitParameter("password");
		adminName = scx.getInitParameter("adminName");
		adminEmail = scx.getInitParameter("adminEmail");
		adminPassword = scx.getInitParameter("adminPassword");		
		DataAccess da = new DataAccess(URL,D_B_NAME,DRIVER,USER_NAME,PASSWORD);
		da.createTable();
		Authenticator auth = new Authenticator(URL,D_B_NAME,DRIVER,USER_NAME,PASSWORD);
		if(adminName != null && adminPassword != null){
			adminName = adminName.toUpperCase();
			auth.regUser(adminName, adminPassword, true,adminEmail.toUpperCase());
			
		}
		
	}

}
