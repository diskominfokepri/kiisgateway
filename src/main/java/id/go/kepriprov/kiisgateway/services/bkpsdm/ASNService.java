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

import id.go.kepriprov.kiisgateway.lib.auth.AuthenticationHTTP;
import id.go.kepriprov.kiisgateway.lib.data.HiveDatabase;
import id.go.kepriprov.kiisgateway.lib.data.MySQLDatabase;


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
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		int connection = (Integer) dataJSON.get("connection");
		if (connection > 0) {
			String activity = null;		
			HiveDatabase hive = new HiveDatabase();
			ResultSet result = hive.query("SELECT * FROM biodata WHERE nip_baru='"+nip+"'");
			String message;
			if (result.next()) {
				message="Data ASN dengan NIP ("+nip+" ditemukan.";
				dataJSON.put("pegawai_id", result.getString("pegawai_id"));
				dataJSON.put("skpd_id", result.getString("skpd_id"));
				dataJSON.put("nip_baru", result.getString("nip_baru"));
				dataJSON.put("nip_lama", result.getString("nip_lama"));
				dataJSON.put("nuptk", result.getString("nuptk"));
				dataJSON.put("status_kep_id", result.getString("status_kep_id"));
				dataJSON.put("kppn_id", result.getString("kppn_id"));
				dataJSON.put("gelar_depan", result.getString("gelar_depan"));
				dataJSON.put("gelar_belakang", result.getString("gelar_belakang"));
				dataJSON.put("tempat_lahir", result.getString("tempat_lahir"));
				dataJSON.put("nik", result.getString("nik"));
				dataJSON.put("jk", result.getString("jk"));
				dataJSON.put("agama_id", result.getString("agama_id"));
				dataJSON.put("status_kawin_id", result.getString("status_kawin_id"));
				dataJSON.put("alamat", result.getString("alamat"));
				dataJSON.put("domisili_id", result.getString("domisili_id"));
				dataJSON.put("alamat_domisili", result.getString("alamat_domisili"));
				dataJSON.put("kode_pos", result.getString("kode_pos"));
				dataJSON.put("kode_pos_domisili", result.getString("kode_pos_domisili"));
				dataJSON.put("no_hp", result.getString("no_hp"));
				dataJSON.put("email", result.getString("email"));
				dataJSON.put("aktif", result.getString("aktif"));
				dataJSON.put("tgl_input", result.getString("tgl_input"));
				
				activity="melakukan query terhadap tabel biodata dengan NIP "+nip+".OUTPUTNYA : "+dataJSON.toString();
				
			}else {
				message="Data ASN dengan NIP ("+nip+" tidak ditemukan.";
				activity="Data ASN dengan NIP ("+nip+" tidak ditemukan.";
			}
			String sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
			new MySQLDatabase().insertRecord(sql);
			dataJSON.put("message",message);			
		}						
		
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	@GET
	@Path("/biodata/seluruhasn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryBiodataSeluruhASN(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request) throws Exception{
		Response response=null;
		Authentication auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		if (connection > 0) {
			HiveDatabase hive = new HiveDatabase();
			ResultSet result = hive.query("select * from biodata order by pegawai_id");	
			while (result.next()) {
				JSONObject dataUntukArray = new JSONObject();
				dataUntukArray.put("pegawai_id", result.getString("pegawai_id"));
				dataUntukArray.put("skpd_id", result.getString("skpd_id"));
				dataUntukArray.put("nip_lama", result.getString("nip_lama"));
				dataUntukArray.put("nuptk", result.getString("nuptk"));
				dataUntukArray.put("status_kep_id", result.getString("status_kep_id"));
				dataUntukArray.put("kppn_id", result.getString("kppn_id"));
				dataUntukArray.put("nama", result.getString("nama"));
				dataUntukArray.put("gelar_depan", result.getString("gelar_depan"));
				dataUntukArray.put("gelar_belakang", result.getString("gelar_belakang"));
				dataUntukArray.put("tempat_lahir", result.getString("tempat_lahir"));
				dataUntukArray.put("nik", result.getString("nik"));
				dataUntukArray.put("jk", result.getString("jk"));
				dataUntukArray.put("agama_id", result.getString("agama_id"));
				dataUntukArray.put("status_kawin_id", result.getString("status_kawin_id"));
				dataUntukArray.put("alamat", result.getString("alamat"));
				dataUntukArray.put("domisili_id", result.getString("domisili_id"));
				dataUntukArray.put("alamat_domisili", result.getString("alamat_domisili"));
				dataUntukArray.put("kode_pos", result.getString("kode_pos"));
				dataUntukArray.put("kode_pos_domisili", result.getString("kode_pos_domisili"));
				dataUntukArray.put("no_hp", result.getString("no_hp"));
				dataUntukArray.put("email", result.getString("email"));
				dataUntukArray.put("aktif", result.getString("aktif"));
				arrayJSON.put(dataUntukArray);
			}		
		}
		response = Response.status(Status.OK).entity(arrayJSON.toString()).build();
		return response;		
	}
}
