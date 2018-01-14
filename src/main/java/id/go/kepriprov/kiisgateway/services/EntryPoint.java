package id.go.kepriprov.kiisgateway.services;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import di.go.kepriprov.kiisgateway.lib.auth.Authentication;
import di.go.kepriprov.kiisgateway.lib.auth.AuthenticationHTTP;
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
	public String query(@Context HttpHeaders httpHeaders) throws Exception {		
		try {
			
			Authentication auth = new AuthenticationHTTP(httpHeaders);
			Database db = new Database();
			if (auth.isUsernameAndPasswordEmpty()) {
				db.insertRecord("INSERT INTO tb_log VALUES (activity,user) VALUES ('Seseorang mencoba untuk login kedalam sistem dengan IP "+ auth.getUsername() + "', '" + auth.getUsername() + "')");
				return "usernamekosong";
			} else {
				if (auth.checkUsernameAndPassword()) {
					return "loginberhasil";
				} else {
					String sql = " INSERT INTO tb_log SET id=NULL,activity='User " + auth.getUsername() + " gagal melakukan login karena kesalahan username/password', user='"+ auth.getUsername() + "',times=NOW()";
					db.insertRecord(sql);
					return "logingagal";
				}
				
			}

		} catch (Exception e) {
			return "requestgagal";
		}

				
	}

}