package probe.com.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import probe.com.model.beans.User;



public class Authenticator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private probe.com.dal.Authenticator auth;
	private Pattern pattern;
	private Matcher matcher;
 
	private String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"	+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public Authenticator(String url,String dbName,String driver,String userName, String password)
	{
		auth = new probe.com.dal.Authenticator(url,dbName,driver,userName, password);
	}
	
		public User authenticate(String email, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
			
			String hashedPassword  =  auth.authenticate(email);
			if (hashedPassword == null)
				return null;
			else if (hashedPassword.equals(hashPassword(password)))
			{
				User user = auth.getUser(email);
				return user;
			}
			else
				return null;
		}
		public boolean regUser( String username,String password,boolean admin,String email)
		{
			
			String hashedPassword = this.hashPassword(password);
			boolean test = auth.regUser(username, hashedPassword,admin,email);
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

	public boolean validEmail(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
		
	

}
