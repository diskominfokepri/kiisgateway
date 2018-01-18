package id.go.kepriprov.kiisgateway.lib.data;

import java.sql.DriverManager;
import java.sql.SQLException;

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
			consoleMessage(ClassNotFoundException.class.getName(), e.getMessage(), 2);
		} catch (SQLException e) {
			consoleMessage(MySQLDatabase.class.getName(), "Tidak bisa melakukan koneksi ke server : " + e.getMessage(), 2);
		}
	}	
	
}
