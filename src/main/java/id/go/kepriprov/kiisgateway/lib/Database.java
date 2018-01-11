package id.go.kepriprov.kiisgateway.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class Database {
	private Connection connection = null;

	private String driverName;

	private String url;
	private PreparedStatement PreparedStatement;

	public Database(String serverName, String portNumber, String dbName, String Username, String Password) {
		driverName = "com.mysql.cj.jdbc.Driver";

		url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;
		koneksi(Username, Password);
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
}
