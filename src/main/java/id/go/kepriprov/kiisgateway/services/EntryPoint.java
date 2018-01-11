package id.go.kepriprov.kiisgateway.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class EntryPoint {
	@GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String entry () {
        return "dari sinilah awal semuanya !!!";
    }
}
