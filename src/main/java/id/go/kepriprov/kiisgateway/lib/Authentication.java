/**
 * class untuk melakukan authentication user
 * @author reza
 */
package id.go.kepriprov.kiisgateway.lib;

import java.sql.ResultSet;
public class Authentication {
	/**
	 * digunakan untuk mengecek username dan password
	 * @param username
	 * @param password
	 * @return boolean true or false
	 * @throws Exception
	 */
	public boolean checkUsernameAndPassword(String username, String password) throws Exception {
		boolean success = false;		
		
		String password2 = (String) Helper.createSHA1(password);
		password2 = (String) Helper.createMD5(password2);
		password2 = (String) Helper.createSHA1(password2);
		password2 = (String) Helper.createMD5(password2);

		Database db = new Database();
		ResultSet hasil = db.query("SELECT * FROM mst_user WHERE id_user = '" + username + "' and password = '" + password2 + "'");
		String user = "";
		String pass = "";
		if (hasil.next()) {
			user = hasil.getString("id_user");
			pass = hasil.getString("password");
		}
		if (user.equals(username) && pass.equals(password2)) {
			success = true;
		}
		db.closeConnection();
		return success;
	}
}
