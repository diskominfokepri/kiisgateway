package id.go.kepriprov.kiisgateway.services;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.auth.Authentication;
import id.go.kepriprov.kiisgateway.lib.auth.AuthenticationHTTP;


@Path("/")
public class EntryPoint {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String entry() {
		return "KIISGateway v.0.1";
	}

	@GET
	@Path("/query")
	@Produces(MediaType.APPLICATION_JSON)
	public Response query(@Context HttpHeaders httpHeaders) throws Exception{
		Response response;
		Authentication auth = new AuthenticationHTTP(httpHeaders);	
		JSONObject dataJSON = auth.isValid();
		int connection = (Integer) dataJSON.get("connection");
		if (connection > 0) {
			
		}
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
}