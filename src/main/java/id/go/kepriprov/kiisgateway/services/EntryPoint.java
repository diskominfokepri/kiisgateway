package id.go.kepriprov.kiisgateway.services;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import id.go.kepriprov.kiisgateway.lib.Authentication;
import id.go.kepriprov.kiisgateway.lib.Database;


@Path("/")
public class EntryPoint {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String entry() {
		return "KIISGateway v.0.1";
	}

	@GET
	@Path("/query")
	@Produces(MediaType.TEXT_PLAIN)

	// public String query(@HeaderParam("Username") String username,
	// @HeaderParam("Password") String password) throws Exception {
	public String query(@Context HttpHeaders httpHeaders) throws Exception {
		String username = "";
		String password = "";
		try {
			username = httpHeaders.getRequestHeader("Username").get(0);
			password = httpHeaders.getRequestHeader("Password").get(0);
			Authentication auth = new Authentication();
			Database db = new Database();
			if (auth.isUsernameAndPasswordEmpty(username, password)) {
				db.insertRecord("INSERT INTO tb_log VALUES (activity,user) VALUES ('Seseorang mencoba untuk login kedalam sistem dengan IP "+ username + "', '" + username + "')");
				return "usernamekosong";
			} else {
				if (auth.checkUsernameAndPassword(username, password)) {
					return "loginberhasil";
				} else {
					String sql = " INSERT INTO tb_log SET id=NULL,activity='User " + username+ " gagal melakukan login karena kesalahan username/password', user='"+ username + "',times=NOW()";
					db.insertRecord(sql);
					return "logingagal";
				}
				
			}

		} catch (Exception e) {
			return "requestgagal";
		}

				
	}

}