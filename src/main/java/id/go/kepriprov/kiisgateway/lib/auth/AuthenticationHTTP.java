package id.go.kepriprov.kiisgateway.lib.auth;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.Helper;
import id.go.kepriprov.kiisgateway.lib.data.MySQLDatabase;

public class AuthenticationHTTP extends Authentication {
	private String ipaddress;
	private String user_agent;
	public AuthenticationHTTP (HttpHeaders httpHeaders, HttpServletRequest request) {
		try {
			username = httpHeaders.getRequestHeader("Username").get(0);
			userpassword = httpHeaders.getRequestHeader("Password").get(0);
			ipaddress = Helper.getIPAddressFrom(request, true);
			user_agent = request.getHeader("User-Agent");
		}catch (NullPointerException e) {
			consoleMessage(Authentication.class.getName(), "header username dan password belum terbentuk", 1);
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
				String sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ getUsername() + "',times=NOW(),ip_address='"+ipaddress+"',user_agent='"+user_agent+"'";
				new MySQLDatabase().insertRecord(sql);
			}else {
				throw new Exception ("User " + getUsername() + " gagal melakukan login karena kesalahan username/password (bila merasa sudah benar, hubungi admin.)");
			}
		}catch (Exception e) {
			dataJSON.put("message",e.getMessage());
			Logger.getLogger(AuthenticationHTTP.class.getName()).info(e.getMessage());
		}
		return dataJSON;		
	}
	public String getUseragent() {
		return user_agent;
	}
	public String getIPAddress() {
		return ipaddress;
	}
}
