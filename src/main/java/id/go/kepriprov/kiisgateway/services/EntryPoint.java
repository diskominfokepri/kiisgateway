package id.go.kepriprov.kiisgateway.services;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



import id.go.kepriprov.kiisgateway.lib.Authentication;


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

	public String query(@HeaderParam("Username") String username, @HeaderParam("Password") String password,
			@HeaderParam("REMOTE_ADDR") String ip) throws Exception {

		Authentication auth = new Authentication();
		
		
		if (auth.checkUsernameAndPassword(username, password)) {
			return "sukses";
		}else {
			return "gagal";
		}
		
	}
	
}