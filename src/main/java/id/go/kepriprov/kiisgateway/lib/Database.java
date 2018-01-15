package id.go.kepriprov.kiisgateway.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import id.go.kepriprov.kiisgateway.lib.conf.Configuration;

public class Database {
	static Logger log = Logger.getLogger(Database.class.getName());
	
	private Connection connection;

	private String driverName;

	private String url;
	
	private PreparedStatement PreparedStatement;
	
	private Statement stmt;

	public Database() throws SQLException {
		driverName = "com.mysql.cj.jdbc.Driver";
		Configuration config = new Configuration();
		url = "jdbc:mysql://" + config.getDBHost() + ":" + config.getDBPort() + "/" + config.getDBName() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		koneksi(config.getDBUser(), config.getDBPassword());
	}	
	public void koneksi(String Username, String Password) {
		try {
			Class.forName(driverName);			
			connection = DriverManager.getConnection(url, Username, Password);
		} catch (ClassNotFoundException e) {			
			System.out.println("ClassNotFoundException : " + e.getMessage());			
		} catch (SQLException e) {			
			log.error("Tidak bisa melakukan koneksi ke server : " + e.getMessage());						
		}
	}

	public void insertRecord(String sqlString) throws SQLException  {		
		String insertQueryStatement = sqlString;
		PreparedStatement = connection.prepareStatement(insertQueryStatement);
		PreparedStatement.executeUpdate();
} 
	public void updateRecord(String sqlString) throws SQLException  {		
		String updateQueryStatement = sqlString;
		PreparedStatement = connection.prepareStatement(updateQueryStatement);		
		PreparedStatement.executeUpdate();			
	}

	public void deleteRecord(String sqlString) throws SQLException  {		
		String deleteQueryStatement = sqlString;
		PreparedStatement = connection.prepareStatement(deleteQueryStatement);
		PreparedStatement.executeUpdate();	
	}

	public ResultSet query (String sqlString) throws SQLException {
		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sqlString);	
		return rs;
	}	
	public void closeConnection() throws SQLException {
		connection.close();
	}
}
