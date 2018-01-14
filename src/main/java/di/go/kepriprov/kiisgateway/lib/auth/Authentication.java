/**
 * class untuk melakukan authentication user
 * @author reza
 */
package di.go.kepriprov.kiisgateway.lib.auth;

import java.sql.ResultSet;

import id.go.kepriprov.kiisgateway.lib.Database;
import id.go.kepriprov.kiisgateway.lib.Helper;
public abstract class Authentication {
	/**
	 * username
	 */
	protected String username = "";
	/**
	 * userpassword
	 */
	protected String userpassword = "";
	
	/**
	 * mendapatkan username
	 * @return username 
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * fill new username	  
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * mendapatkan userpassword
	 * @return userpassword 
	 */
	public String getUserpassword() {
		return userpassword;
	}
	/**
	 * fill new userpassword	  
	 */
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	/**
	 * digunakan untuk mengecek username dan password
	 * @param username
	 * @param userpassword
	 * @return boolean true or false
	 * @throws Exception
	 */
	public boolean checkUsernameAndPassword() throws Exception {
		boolean success = false;		
		
		String password2 = (String) Helper.createSHA1(this.userpassword);
		password2 = (String) Helper.createMD5(password2);
		password2 = (String) Helper.createSHA1(password2);
		password2 = (String) Helper.createMD5(password2);
		Database db = new Database();
		ResultSet hasil = db.query("SELECT * FROM mst_user WHERE id_user = '" + this.username + "' and password = '" + password2 + "'");
		String user = "";
		String pass = "";
		//String opd = "";
		if (hasil.next()) {
			user = hasil.getString("id_user");
			pass = hasil.getString("password");
			//opd = 
		}
		if (user.equals(username) && pass.equals(password2)) {
			success = true;
			//simpan ke log
		}
		return success;
	}
	/**
	 * gidunakan untuk mengecek apakah username dan password kosong
	 * @return true of false
	 */
	public boolean isUsernameAndPasswordEmpty() {
		boolean success = false;		
		
		if (username.length() == 0 || userpassword.length() == 0 ) {
			success = true;
		}
		return success;
	}
}
