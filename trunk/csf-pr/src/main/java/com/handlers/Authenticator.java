package com.handlers;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import com.model.beans.User;

public class Authenticator implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private com.model.Authenticator auth ;
	public Authenticator(String url,String dbName,String driver,String userName, String password)
	{
		auth = new com.model.Authenticator(url,dbName,driver,userName, password);
	}

	public User authenticate(String email, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		User user = auth.authenticate(email, password);
		return user;
	}

	public boolean reg(String username, String password, boolean admin,String email) {
		
		return auth.regUser(username, password, admin,email);
	}

	public Map<Integer, String> getUsersList() {
		return auth.getUsersList();
	}

	public boolean removeUser(String user) {
		
		return auth.removeUser(user);
	}

	public boolean changePassword(String name, String oldPass,
			String newPass) {
		return auth.changePassword( name, oldPass, newPass);
	}

	public boolean removeExp(Integer expId) {
		return auth.removeExp(expId); 
	}
	
	public boolean validEmail(String email)
	{
		return auth.validEmail(email);
	}

}
