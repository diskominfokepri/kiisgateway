/**
 * class untuk melakukan authentication user
 * @author reza
 */
package id.go.kepriprov.kiisgateway.lib.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.Database;
public abstract class Authentication {
	static Logger log = Logger.getLogger(AuthenticationHTTP.class.getName());
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
	 * 
	 * @return data user map
	 * @throws Exception
	 */
	public HashMap<String,String> getDataUser(){
		HashMap<String,String> datauser = new HashMap<String, String>();
		String sql = "SELECT id_user,password FROM mst_user WHERE id_user = '" + this.username+"'";
		try {
			Database db = new Database();
			ResultSet hasil = db.query(sql);			
			if (hasil.next()) {
				datauser.put("id_user", hasil.getString("id_user"));
				datauser.put("password", hasil.getString("password"));			 
			}
		} catch (SQLException e) {			
			log.info("Query perintah SQL gagal dengan pesan : "+e.getMessage());
		} catch (NullPointerException e) {}
		return datauser;
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
	/**
	 * digunakan untuk mengecek apakah semuanya sudah valid. 
	 * setiap aktivitas disini akan dicatat
	 * @return Response JSON
	 */
	abstract public JSONObject isValid ();
}
