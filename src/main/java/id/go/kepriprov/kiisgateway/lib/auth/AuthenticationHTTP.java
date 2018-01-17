package id.go.kepriprov.kiisgateway.lib.auth;

import java.util.HashMap;

import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.Database;
import id.go.kepriprov.kiisgateway.lib.Helper;

public class AuthenticationHTTP extends Authentication {
	static Logger log = Logger.getLogger(AuthenticationHTTP.class.getName());
	public AuthenticationHTTP (HttpHeaders httpHeaders) {
		try {
			username = httpHeaders.getRequestHeader("Username").get(0);
			userpassword = httpHeaders.getRequestHeader("Password").get(0);
		}catch (NullPointerException e) {			
			log.info("header username dan password kosong");
		}
		
	}

	@Override
	public JSONObject isValid() {
		String activity;		
		JSONObject dataJSON = new JSONObject();
		dataJSON.put("connection", -1);					
		try {
			if (isUsernameAndPasswordEmpty()) {
				throw new Exception ("Username atau password di header HTTP kosong");
			}				
			HashMap<String, String> datauser = getDataUser();				
			if (datauser.size() > 0) {
				String password2 = (String) Helper.createSHA1(this.userpassword);
				password2 = (String) Helper.createMD5(password2);
				password2 = (String) Helper.createSHA1(password2);
				password2 = (String) Helper.createMD5(password2);
				
				if (datauser.get("apiuser").equals(username) && datauser.get("apipassword").equals(password2)) {
					activity ="User " + getUsername() + " berhasil melakukan login";
					dataJSON.put("connection", 1);	
					dataJSON.put("message", activity);
				}else {
					activity ="User " + getUsername() + " gagal melakukan login karena kesalahan username/password";					
					dataJSON.put("message", activity);
				}				
				String sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ getUsername() + "',times=NOW()";
				new Database().insertRecord(sql);
			}else {
				throw new Exception ("User " + getUsername() + " gagal melakukan login karena kesalahan username/password");
			}
		}catch (Exception e) {
			dataJSON.put("message",e.getMessage());
			log.info(e.getMessage());
		}
		return dataJSON;		
	}
}
