package id.go.kepriprov.kiisgateway.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import id.go.kepriprov.kiisgateway.lib.conf.Configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
	private Connection connection;

	private String driverName;

	private String url;
	private PreparedStatement PreparedStatement;
	private Statement stmt;

	public Database() {
		driverName = "com.mysql.cj.jdbc.Driver";
		Configuration config = new Configuration();
		url = "jdbc:mysql://" + config.getDBHost() + ":" + config.getDBPort() + "/" + config.getDBName();
		koneksi(config.getDBUser(), config.getDBPassword());
	}

	
	public boolean koneksi(String Username, String Password) {
		try {
			// Load the JDBC driver
			Class.forName(driverName);
			// Create a connection to the database
			connection = DriverManager.getConnection(url, Username, Password);
		} catch (ClassNotFoundException e) {
			// Could not find the database driver
			System.out.println("ClassNotFoundException : " + e.getMessage());
			return false;
		} catch (SQLException e) {
			// Could not connect to the database
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public void insertRecord(String sqlString) {
		try {
			String insertQueryStatement = sqlString;

			PreparedStatement = connection.prepareStatement(insertQueryStatement);

			// execute insert SQL statement
			PreparedStatement.executeUpdate();
			System.out.println("Data Telah Ditambahkan");
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRecord(String sqlString) {
		try {
			String updateQueryStatement = sqlString;

			PreparedStatement = connection.prepareStatement(updateQueryStatement);

			// execute insert SQL statement
			PreparedStatement.executeUpdate();
			System.out.println("Data Telah Diubah");
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteRecord(String sqlString) {
		try {
			String deleteQueryStatement = sqlString;

			PreparedStatement = connection.prepareStatement(deleteQueryStatement);

			// execute insert SQL statement
			PreparedStatement.executeUpdate();
			System.out.println("Data Telah Dihapus");
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet query(String sqlString) {
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlString);
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void closeConnection() throws Exception {
		connection.close();
	}
}
