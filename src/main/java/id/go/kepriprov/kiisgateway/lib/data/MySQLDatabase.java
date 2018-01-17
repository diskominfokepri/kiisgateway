package id.go.kepriprov.kiisgateway.lib.data;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import id.go.kepriprov.kiisgateway.lib.conf.Configuration;

public class MySQLDatabase extends Database {
	public MySQLDatabase() throws SQLException {
		driverName = "com.mysql.cj.jdbc.Driver";
		Configuration config = new Configuration();
		url = "jdbc:mysql://" + config.getKIISmpDBHost() + ":" + config.getKIISmpDBPort() + "/" + config.getKIISmpDBName() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";	
		
		try {
			Class.forName(driverName);			
			connection = DriverManager.getConnection(url, config.getKIISmpDBUser(), config.getKIISmpDBPassword());
		} catch (ClassNotFoundException e) {			
			System.out.println("ClassNotFoundException : " + e.getMessage());			
		} catch (SQLException e) {
			Logger.getLogger(MySQLDatabase.class.getName()).error("Tidak bisa melakukan koneksi ke server : " + e.getMessage());
		}
	}	
	
}
