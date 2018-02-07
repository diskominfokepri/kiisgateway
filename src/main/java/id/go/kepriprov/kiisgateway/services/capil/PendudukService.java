package id.go.kepriprov.kiisgateway.services.capil;

import java.sql.SQLException;

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

import org.json.JSONException;
import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.BaseKiis;
import id.go.kepriprov.kiisgateway.lib.auth.AuthenticationHTTP;
import id.go.kepriprov.kiisgateway.lib.conf.Configuration;
import id.go.kepriprov.kiisgateway.lib.data.MySQLDatabase;

public class PendudukService extends BaseKiis {
	@GET
	@Path("/capil/seorangwarga")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryBiodataSeorangASN(@QueryParam ("nik") String nik,@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null;
		if (connection > 0) {				
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				
					JSONObject dataasn = new JSONObject();
					dataasn.put("pegawai_id", nik);					
					dataJSON.put("payload", dataasn);					
					message="Data ASN dengan NIP ("+nik+") ditemukan.";
					activity="melakukan query terhadap tabel biodata dengan NIK "+nik+".OUTPUTNYA : "+dataJSON.toString();
					
				
				String sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
				dataJSON.put("message",message);	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}					
		consoleMessage(PendudukService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
}
