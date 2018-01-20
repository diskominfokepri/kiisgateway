package id.go.kepriprov.kiisgateway.services.bkpsdm;


import java.sql.ResultSet;
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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.go.kepriprov.kiisgateway.lib.BaseKiis;
import id.go.kepriprov.kiisgateway.lib.auth.AuthenticationHTTP;
import id.go.kepriprov.kiisgateway.lib.data.HiveDatabase;
import id.go.kepriprov.kiisgateway.lib.data.MySQLDatabase;


@Path("/asn")
public class ASNService extends BaseKiis {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String entry() {

		return "BKPSDM Services";
	}

	@GET
	@Path("/biodata/seorangasn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryBiodataSeorangASN(@QueryParam ("nip") String nip,@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null;
		if (connection > 0) {				
			try {
				HiveDatabase hive = new HiveDatabase();
				ResultSet result = hive.query("SELECT * FROM bkpsdm_silat_biodata_s WHERE nip_baru='"+nip+"'");				
				if (result.next()) {
					JSONObject dataasn = new JSONObject();				
					dataasn.put("pegawai_id", result.getString("pegawai_id"));
					dataasn.put("skpd_id", result.getString("skpd_id"));
					dataasn.put("nip_baru", result.getString("nip_baru"));
					dataasn.put("nip_lama", result.getString("nip_lama"));
					dataasn.put("nuptk", result.getString("nuptk"));
					dataasn.put("status_kep_id", result.getString("status_kep_id"));
					dataasn.put("kppn_id", result.getString("kppn_id"));
					dataasn.put("gelar_depan", result.getString("gelar_depan"));
					dataasn.put("gelar_belakang", result.getString("gelar_belakang"));
					dataasn.put("tempat_lahir", result.getString("tempat_lahir"));
					dataasn.put("nik", result.getString("nik"));
					dataasn.put("jk", result.getString("jk"));
					dataasn.put("agama_id", result.getString("agama_id"));
					dataasn.put("status_kawin_id", result.getString("status_kawin_id"));
					dataasn.put("alamat", result.getString("alamat"));
					dataasn.put("domisili_id", result.getString("domisili_id"));
					dataasn.put("alamat_domisili", result.getString("alamat_domisili"));
					dataasn.put("kode_pos", result.getString("kode_pos"));
					dataasn.put("kode_pos_domisili", result.getString("kode_pos_domisili"));
					dataasn.put("no_hp", result.getString("no_hp"));
					dataasn.put("email", result.getString("email"));
					dataasn.put("aktif", result.getString("aktif"));
					dataasn.put("tgl_input", result.getString("tgl_input"));
					
					dataJSON.put("payload", dataasn);
					
					message="Data ASN dengan NIP ("+nip+") ditemukan.";
					activity="melakukan query terhadap tabel biodata dengan NIP "+nip+".OUTPUTNYA : "+dataJSON.toString();
					
				}else {
					message="Data ASN dengan NIP ("+nip+") tidak ditemukan.";
					activity="Data ASN dengan NIP ("+nip+") tidak ditemukan.";
					level = 2;
				}
				String sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				new MySQLDatabase().insertRecord(sql);
				dataJSON.put("message",message);					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}					
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() + " Melakukan query "+activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	@GET
	@Path("/biodata/seluruhasn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryBiodataSeluruhASN(@QueryParam ("offset") String offset,@QueryParam ("limit") String limit, @Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		if (connection > 0) {
			String activity = null,sql;		
			try {
				HiveDatabase hive = new HiveDatabase();
				if (StringUtils.equals(offset, "") || StringUtils.equals(limit,"") || StringUtils.equals(offset, null) || StringUtils.equals(limit,null)) {
					sql = "SELECT * FROM bkpsdm_silat_biodata_s ORDER BY pegawai_id";
				}else {
					sql = "SELECT * FROM bkpsdm_silat_biodata_s ORDER BY pegawai_id LIMIT "+offset+","+limit;
				}
				ResultSet result = hive.query(sql);				
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
				dataJSON.put("payload", arrayJSON);
				activity="melakukan query terhadap seluruh biodata ASN";			
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				new MySQLDatabase().insertRecord(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() + " Melakukan query "+activity, level);			
			}			
		}
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
}
