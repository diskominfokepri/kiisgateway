package id.go.kepriprov.kiisgateway.lib.data;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import id.go.kepriprov.kiisgateway.lib.conf.Configuration;

public class HiveDatabase extends Database {
	public HiveDatabase() throws SQLException {
		driverName = "org.apache.hive.jdbc.HiveDriver";
		Configuration config = new Configuration();
		url = "jdbc:hive2://" + config.getKIISHiveDBHost() + ":" + config.getKIISHiveDBPort() + "/" + config.getKIISHiveDBName()+";transportMode=http;httpPath=cliservice";	
		
		try {
			Class.forName(driverName);			
			connection = DriverManager.getConnection(url, config.getKIISHiveDBUser(), config.getKIISHiveDBPassword());
		} catch (ClassNotFoundException e) {			
			System.out.println("ClassNotFoundException : " + e.getMessage());			
		} catch (SQLException e) {
			Logger.getLogger(HiveDatabase.class.getName()).error("Tidak bisa melakukan koneksi ke server : " + e.getMessage());
		}
	}	
	
}
