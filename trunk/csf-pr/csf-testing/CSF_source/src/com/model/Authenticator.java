package com.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import com.model.beans.User;



public class Authenticator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private dal.Authenticator auth;
	public Authenticator(String url,String dbName,String driver,String userName, String password)
	{
		auth = new dal.Authenticator(url,dbName,driver,userName, password);
	}
	
		public User authenticate( String username, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
			
			String hashedPassword  =  auth.authenticate(username);
			if (hashedPassword == null)
				return null;
			else if (hashedPassword.equals(hashPassword(password)))
			{
				User user = auth.getUser(username);
				return user;
			}
			else
				return null;
		}
		public boolean regUser( String username,String password,boolean admin)
		{
			
			String hashedPassword = this.hashPassword(password);
			boolean test = auth.regUser(username, hashedPassword,admin);
			return test;
			
		}
	
	public String hashPassword(String password)
	{
		
			String hashword = null;
			try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);

			} catch (NoSuchAlgorithmException nsae) {

			}
			return hashword;
	}
	public Map<Integer, String> getUsersList() {
		return auth.getUsersList();
	}
	public boolean removeUser(String user) {
		return auth.removeUser(user);
	}
	public boolean changePassword(String name, String oldPass, String newPass) {
	
		String oldHashedPassword = this.hashPassword(oldPass);
		String newHashedPassword = this.hashPassword(newPass);
		return auth.changePassword(oldHashedPassword, newHashedPassword, name,false);
	}
	public boolean removeExp(Integer expId) {
		return auth.removeExp(expId) ;
	}
	
		
	

}
