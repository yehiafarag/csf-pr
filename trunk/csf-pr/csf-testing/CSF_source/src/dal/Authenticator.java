package dal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import com.model.beans.User;


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
	public boolean validateUsername(String username)
	{
		boolean test = db.validateUsername(username);
		return test;
	}
	
	public String authenticate( String username) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		
		String password =  db.authenticate(username);		
		return password;
	}
	
	
	
	public boolean regUser( String username,String password,boolean admin)
	{
		
		boolean test = db.regUser(username, password,admin);
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

	
	public User getUser(String username) {
		User user = db.getUser(username);
		return user;
	}

	public Map<Integer, String> getUsersList() {
		
		return db.getUsersList();
	}

	public boolean removeUser(String user) {
		return db.removeUser(user);
	}

	public boolean removeExp(int expId) {
		
		return db.removeExperiment(expId);
	}

}
