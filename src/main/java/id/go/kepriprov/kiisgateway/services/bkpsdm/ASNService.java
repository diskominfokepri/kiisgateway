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
import id.go.kepriprov.kiisgateway.lib.conf.Configuration;
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
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				ResultSet result = db.query("SELECT  biodata.pegawai_id, biodata.nip_baru, biodata.nama, skpd.name, biodata.jk, agama.agama, jenisjabatan.jabatan_id, filediri.file_foto \r\n" + 
						"FROM bkpsdm_silat_biodata_s AS biodata\r\n" + 
						"LEFT JOIN bkpsdm_silat_skpd_bezzeting_s AS skpd ON left(biodata.skpd_id,3) = skpd.id\r\n" + 
						"LEFT JOIN bkpsdm_silat_ref_agama_s AS agama ON biodata.agama_id = agama.agama_id\r\n" + 
						"LEFT JOIN bkpsdm_silat_jenis_jabatan_s AS jenisjabatan ON jenisjabatan.pegawai_id = biodata.pegawai_id\r\n"+
						"LEFT JOIN bkpsdm_silat_file_diri_s as filediri ON biodata.pegawai_id = filediri.pegawai_id where biodata.nip_baru='" + nip + "'");				
				if (result.next()) {
					JSONObject dataasn = new JSONObject();	
					String jabatan_id = result.getString("jabatan_id");
					String pegawai_id = result.getString("pegawai_id");
					String jk = "",agama="", jabatan = "",pangkat = "",golongan = "",filefoto = "";
					jk = result.getString("jk");
					agama = result.getString("agama");
					if (StringUtils.equals(result.getString("file_foto"), null) || StringUtils.equals(result.getString("file_foto"), "") || StringUtils.equals(result.getString("file_foto"), "null")) {
						filefoto = "http://silat.kepriprov.go.id/images/foto_kosong.jpg";
					}
					else {
						filefoto = "http://silat.kepriprov.go.id/files_scan/" + result.getString("file_foto");
					}
					//Get Jabatan Struktural
					if (StringUtils.equals(jabatan_id, "1")) {
						ResultSet result2 = db.query("SELECT bkpsdm_silat_rwyt_jab_struktural_s.pegawai_id,bkpsdm_silat_rwyt_jab_struktural_s.jab_struktural_id,bkpsdm_silat_ref_jabatan_struktural_s.jab_struktural FROM bkpsdm_silat_rwyt_jab_struktural_s" + 
								" LEFT JOIN bkpsdm_silat_ref_jabatan_struktural_s on bkpsdm_silat_rwyt_jab_struktural_s.jab_struktural_id = bkpsdm_silat_ref_jabatan_struktural_s.jab_struktural_id "
								+ "WHERE pegawai_id=" + pegawai_id + " order by tgl_sk desc limit 0,1");
						if (result2.next()){
							jabatan = result2.getString("jab_struktural");
						}
					}
					//Get Jabatan Fungsional
					if (StringUtils.equals(jabatan_id, "2")) {
						ResultSet result2 = db.query("SELECT bkpsdm_silat_rwyt_jab_fungsional_s.pegawai_id,bkpsdm_silat_rwyt_jab_fungsional_s.jft_id,bkpsdm_silat_ref_jft_s.name,bkpsdm_silat_ref_jft_s.tingkat,bkpsdm_silat_ref_jft_s.jenjang_jab\r\n" + 
								"FROM bkpsdm_silat_rwyt_jab_fungsional_s\r\n" + 
								"LEFT JOIN bkpsdm_silat_ref_jft_s ON bkpsdm_silat_rwyt_jab_fungsional_s.jft_id = bkpsdm_silat_ref_jft_s.id\r\n" + 
								"WHERE pegawai_id=" + pegawai_id + " ORDER BY tgl_sk DESC LIMIT 0,1");
						if (result2.next()){
							jabatan = result2.getString("name") + " " + result2.getString("jenjang_jab");
						}
					}
					//Get Jabatan Fungsional
					if (StringUtils.equals(jabatan_id, "3")) {
						ResultSet result2 = db.query("SELECT bkpsdm_silat_rwyt_jab_pelaksana_s.pegawai_id,bkpsdm_silat_rwyt_jab_pelaksana_s.jfu_id,bkpsdm_silat_ref_jfu_s.name\r\n" + 
								"FROM bkpsdm_silat_rwyt_jab_pelaksana_s\r\n" + 
								"LEFT JOIN bkpsdm_silat_ref_jfu_s ON bkpsdm_silat_rwyt_jab_pelaksana_s.jfu_id = bkpsdm_silat_ref_jfu_s.id\r\n" + 
								"WHERE pegawai_id=" + pegawai_id + " ORDER BY tgl_sk DESC LIMIT 0,1");
						if (result2.next()){
							jabatan = result2.getString("name");
						}
					}
					ResultSet result2 = db.query("SELECT bkpsdm_silat_rwyt_pangkat_s.pegawai_id,bkpsdm_silat_rwyt_pangkat_s.pangkat_id,bkpsdm_silat_ref_pangkat_s.gol_ruang,bkpsdm_silat_ref_pangkat_s.pangkat\r\n" + 
							"FROM bkpsdm_silat_rwyt_pangkat_s\r\n" + 
							"LEFT JOIN bkpsdm_silat_ref_pangkat_s ON bkpsdm_silat_rwyt_pangkat_s.pangkat_id = bkpsdm_silat_ref_pangkat_s.pangkat_id\r\n" + 
							"WHERE pegawai_id=" + pegawai_id + " ORDER BY tgl_sk DESC LIMIT 0,1");
					if (result2.next()){
						pangkat = result2.getString("pangkat");
						golongan = result2.getString("gol_ruang");
					}
					dataasn.put("pegawai_id", result.getString("pegawai_id"));
					dataasn.put("nama_pegawai", result.getString("nama"));
					dataasn.put("nama_skpd", result.getString("skpd.name"));
					dataasn.put("nip_baru", result.getString("nip_baru"));
					dataasn.put("jk", jk);
					dataasn.put("agama", agama);
					dataasn.put("file_foto", filefoto);
					dataasn.put("golongan", golongan);
					dataasn.put("pangkat", pangkat);
					dataasn.put("jabatan", jabatan);
					dataJSON.put("payload", dataasn);
					
					message="Data ASN dengan NIP ("+nip+") ditemukan.";
					activity="melakukan query terhadap tabel biodata dengan NIP "+nip+".OUTPUTNYA : "+dataJSON.toString();
					
				}else {
					message="Data ASN dengan NIP ("+nip+") tidak ditemukan.";
					activity="Data ASN dengan NIP ("+nip+") tidak ditemukan.";
					level = 2;
				}
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
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	@GET
	@Path("/biodata/seluruhasn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryBiodataSeluruhASN(@QueryParam ("offset") String offset,@QueryParam ("limit") String limit,@QueryParam ("keyword") String keyword,@QueryParam ("skpd_id") String skpd_id,@QueryParam ("fieldsort") String fieldsort,@QueryParam ("typesort") String typesort, @Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				//set default
				if (StringUtils.equals(typesort, "") || StringUtils.equals(typesort, null)) {
					typesort = "asc";
				}
				if (StringUtils.equals(fieldsort, "") || StringUtils.equals(fieldsort, null)) {
					fieldsort = "biodata.pegawai_id";
				}
				
				sql = "SELECT biodata.pegawai_id, skpd.name, biodata.nip_baru, biodata.nama, biodata.gelar_depan, biodata.gelar_belakang, agama.agama, biodata.jk, timestampdiff(YEAR,biodata.tgl_lahir, now()) AS umur \r\n" + 
						"FROM bkpsdm_silat_biodata_s AS biodata\r\n" + 
						"LEFT JOIN bkpsdm_silat_skpd_bezzeting_s AS skpd ON left(biodata.skpd_id,3) = skpd.id \r\n" + 
						"LEFT JOIN bkpsdm_silat_ref_agama_s AS agama ON biodata.agama_id = agama.agama_id\r\n WHERE (biodata.nama<>'') ";
				if (!StringUtils.equals(keyword, "") && !StringUtils.equals(keyword, null)) {
					sql = sql + "AND (biodata.nip_baru like '%" + keyword + "%' or biodata.nama like '%" + keyword + "%')";
				}
				if (!StringUtils.equals(skpd_id, "") && !StringUtils.equals(skpd_id, null)) {
					sql = sql + "AND (biodata.skpd_id='" + skpd_id + "')";
				}
				sql = sql + "ORDER BY " + fieldsort + " " + typesort ;
				if ((!StringUtils.equals(offset, "") && !StringUtils.equals(limit,"")) && (!StringUtils.equals(offset, null) && !StringUtils.equals(limit,null))) {
					sql = sql + " LIMIT "+offset+","+limit;
				}
				ResultSet result = db.query(sql);				
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("pegawai_id", result.getString("pegawai_id"));
					dataUntukArray.put("nama_skpd", result.getString("name"));
					dataUntukArray.put("nip_baru", result.getString("nip_baru"));
					dataUntukArray.put("nama", result.getString("nama"));
					dataUntukArray.put("gelar_depan", result.getString("gelar_depan"));
					dataUntukArray.put("gelar_belakang", result.getString("gelar_belakang"));
					dataUntukArray.put("agama", result.getString("agama"));
					dataUntukArray.put("jk", result.getString("jk"));
					dataUntukArray.put("umur", result.getString("umur"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			}
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/jumlah")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryJumlahASN(@QueryParam ("keyword") String keyword,@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				if (StringUtils.equals(keyword, "") || StringUtils.equals(keyword, null)) {
					sql = "SELECT COUNT(pegawai_id) AS jumlahasn FROM bkpsdm_silat_biodata_s";
					activity="melakukan query mengetahui jumlah asn";	
				}else {
					sql = "SELECT COUNT(pegawai_id) AS jumlahasn FROM bkpsdm_silat_biodata_s WHERE (nip_baru like '%" + keyword + "%' or nama like '%" + keyword + "%')";
					activity="melakukan query mengetahui jumlah asn dengan keyword " + keyword ;	
				}
				ResultSet result = db.query(sql);			
				if (result.next()) {
					JSONObject dataasn = new JSONObject();	
					dataasn.put("jumlah", result.getString("jumlahasn"));					
					dataJSON.put("payload", dataasn);
				}						
				message="jumlah asn";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/statistikopd")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryStatistikSKPD(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				sql = "SELECT skpd.name, count(biodata.pegawai_id) AS jumlahpegawai\r\n" + 
					  "FROM bkpsdm_silat_biodata_s AS biodata\r\n" + 
					  "LEFT JOIN bkpsdm_silat_skpd_bezzeting_s AS skpd ON left(biodata.skpd_id,3) = skpd.id WHERE biodata.nama <> '' GROUP BY skpd.name ORDER BY jumlahpegawai desc";
				activity="melakukan query untuk mengetahui total jumlah pegawai di setiap OPD";	
				ResultSet result = db.query(sql);	
		
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("nama_skpd", result.getString("name"));
					dataUntukArray.put("jumlah_pegawai", result.getString("jumlahpegawai"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				message="statistik pegawai per OPD";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/statistikagama")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryStatistikAgama(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				sql = "SELECT agama.agama, count(biodata.pegawai_id) AS jumlahpegawai\r\n" + 
					  "FROM bkpsdm_silat_ref_agama_s AS agama\r\n" + 
					  "LEFT JOIN bkpsdm_silat_biodata_s AS biodata ON biodata.agama_id = agama.agama_id GROUP BY agama.agama ORDER BY jumlahpegawai desc";
				activity="melakukan query untuk mengetahui total jumlah pegawai untuk setiap agama";	
				ResultSet result = db.query(sql);	
		
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("agama", result.getString("agama"));
					dataUntukArray.put("jumlah_pegawai", result.getString("jumlahpegawai"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				message="statistik pegawai per agama";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/statistikjk")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryStatistikJK(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				sql = "SELECT if(jk is null,'Kosong',jk) AS jk,count(pegawai_id) AS jumlahpegawai FROM bkpsdm_silat_biodata_s WHERE nama<>'' GROUP BY jk ORDER BY jumlahpegawai desc";
				activity="melakukan query untuk mengetahui total jumlah pegawai untuk setiap jenis kelamin";	
				ResultSet result = db.query(sql);	
		
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("jk", result.getString("jk"));
					dataUntukArray.put("jumlah_pegawai", result.getString("jumlahpegawai"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				message="statistik pegawai per jenis kelamin";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/statistikumur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryStatistikUmur(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				sql = "SELECT tblumur.keterangan, count(biodata.pegawai_id) AS jumlahpegawai  FROM bkpsdm_silat_biodata_s AS biodata\r\n" + 
					  "JOIN bkpsdm_silat_statistik_umur AS tblumur ON (timestampdiff(YEAR,tgl_lahir, now())<=tblumur.max and timestampdiff(YEAR,tgl_lahir, now())>=tblumur.min) GROUP BY tblumur.keterangan ORDER BY jumlahpegawai desc";
				activity="melakukan query untuk mengetahui total jumlah pegawai untuk setiap kategori umur";	
				ResultSet result = db.query(sql);	
		
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("keterangan", result.getString("keterangan"));
					dataUntukArray.put("jumlah_pegawai", result.getString("jumlahpegawai"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				message="statistik pegawai per kategori umur";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/statistikjabatan")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryStatistikJabatan(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				sql = "SELECT refjenisjabatan.jenis_jabatan, count(jenisjabatan.jabatan_id) AS jumlahpegawai\r\n" + 
						"FROM bkpsdm_silat_jenis_jabatan_s AS jenisjabatan\r\n" + 
						"LEFT JOIN bkpsdm_silat_ref_jenis_jabatan_s AS refjenisjabatan ON jenisjabatan.jabatan_id = refjenisjabatan.jabatan_id GROUP BY refjenisjabatan.jenis_jabatan ORDER BY jumlahpegawai DESC";
				activity="melakukan query untuk mengetahui total jumlah pegawai untuk setiap jabatan";	
				ResultSet result = db.query(sql);	
		
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("jenis_jabatan", result.getString("jenis_jabatan"));
					dataUntukArray.put("jumlah_pegawai", result.getString("jumlahpegawai"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				message="statistik jumlah pegawai per jabatan";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
	
	@GET
	@Path("/listopd")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryListSKPD(@Context HttpHeaders httpHeaders,@Context HttpServletRequest request){
		int level = 1;
		Response response=null;
		AuthenticationHTTP auth = new AuthenticationHTTP(httpHeaders,request);	
		JSONObject dataJSON = auth.isValid();
		JSONArray arrayJSON = new JSONArray();
		int connection = (Integer) dataJSON.get("connection");
		String activity = null,message=null,sql;
		if (connection > 0) {			
			try {
				Configuration config = new Configuration();
				MySQLDatabase db = new MySQLDatabase (config.getKIISTransDBHost (),config.getKIISTransDBPort (),config.getKIISTransDBName(),config.getKIISTransDBUser(),config.getKIISTransDBPassword());
				sql = "SELECT * FROM bkpsdm_silat_skpd_bezzeting_s WHERE length(id)=3";
				activity="melakukan query untuk mengetahui list OPD";	
				ResultSet result = db.query(sql);	
		
				while (result.next()) {
					JSONObject dataUntukArray = new JSONObject();
					dataUntukArray.put("id", result.getString("id"));
					dataUntukArray.put("name", result.getString("name"));
					arrayJSON.put(dataUntukArray);
				}	
				dataJSON.put("payload", arrayJSON);
				message="list opd";
				sql = " INSERT INTO tb_activity SET id=NULL,activity='" + activity + "', user='"+ auth.getUsername() + "',times=NOW(),ip_address='"+auth.getIPAddress()+"',user_agent='"+auth.getUseragent()+"'";
				MySQLDatabase db2 = new MySQLDatabase();
				db2.insertRecord(sql);
				db2.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				message="internal server error";		
				activity=e.getMessage();
			} 
		}
		dataJSON.put("message", message);
		consoleMessage(ASNService.class.getName(), "User "+auth.getUsername() +activity, level);
		response = Response.status(Status.OK).entity(dataJSON.toString()).build();
		return response;		
	}
}
