package id.go.kepriprov.kiisgateway.services.bkpsdm;


import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.auth.Authentication;
import id.go.kepriprov.kiisgateway.lib.auth.AuthenticationHTTP;
import id.go.kepriprov.kiisgateway.lib.data.HiveDatabase;


@Path("/asn")
public class ASNService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String entry() {
		return "BKPSDM Services";
	}

	@GET
	@Path("/biodata/seorangasn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryBiodataSeorangASN(@QueryParam ("nip") String nip,@Context HttpHeaders httpHeaders,@Context HttpServletRequest request) throws Exception{
		Response response=null;
		Authentication auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		int connection = (Integer) dataJSON.get("connection");
		if (connection > 0) {
			HiveDatabase hive = new HiveDatabase();
			ResultSet result = hive.query("SELECT * FROM biodata WHERE nip_baru='"+nip+"'");			
			if (result.next()) {
				dataJSON.put("nama", result.getString("nama"));
			}			
		}
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
}