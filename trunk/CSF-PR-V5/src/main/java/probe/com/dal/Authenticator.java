package probe.com.dal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import probe.com.model.beans.User;


public class Authenticator implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataBase db;
	
	public Authenticator(String url,String dbName,String driver,String userName, String password)
	{
		 db = new DataBase(url,dbName,driver,userName, password);
	}

	/*
	 * this method check if the username is valid 
	 * 
	 *  */
	public boolean validateUsername(String email)
	{
		boolean test = db.validateUsername(email);
		return test;
	}
	
	public String authenticate( String email) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		
		String password =  db.authenticate(email);		
		return password;
	}
	
	
	
	public boolean regUser( String username,String password,boolean admin,String email)
	{
		
		boolean test = db.storeNewUser(username, password,admin,email);
		return test;
	}
	
	public boolean changePassword(String oldpassword, String newpassword,String username,boolean admin) {
		//get user password
		String password = db.authenticate(username);
		//compare with old
		if(oldpassword.equals(password))
		{
			return db.updateUserPassword(username,newpassword);
		}
		return false;
		
	}

	
	public User getUser( String email) {
		User user = db.getUser(email);
		return user;
	}

	public Map<Integer, String> getUsersList() {
		
		return db.getUsersList();
	}

	public boolean removeUser(String user) {
		return db.removeUser(user);
	}

	public boolean removeExp(int expId) {
		
		return db.removeDataset(expId);
	}

}
