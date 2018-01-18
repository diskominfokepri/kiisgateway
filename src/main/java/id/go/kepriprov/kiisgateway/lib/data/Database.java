package id.go.kepriprov.kiisgateway.lib.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import id.go.kepriprov.kiisgateway.lib.BaseKiis;

class Database extends BaseKiis{
	protected Connection connection;

	protected String driverName;

	protected String url;
	
	protected PreparedStatement PreparedStatement;
	
	protected Statement stmt;	
	
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
