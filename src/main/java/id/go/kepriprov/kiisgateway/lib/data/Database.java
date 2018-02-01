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
		/*try {
			String insertQueryStatement = sqlString;
			PreparedStatement = connection.prepareStatement(insertQueryStatement);
			PreparedStatement.executeUpdate();
		} finally {
			if (!connection.isClosed()) try { 
				connection.close();
			} catch (SQLException logOrIgnore) {
				
			}
		}*/
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

	public ResultSet query (String sqlString) {
		ResultSet rs = null;
		/*try {
			try {
				stmt = connection.createStatement();
				rs = stmt.executeQuery(sqlString);
			} finally {
				if (!connection.isClosed()) try { 
					connection.close();
				} catch (SQLException logOrIgnore) {
					
				}
			}		
		} catch (SQLException e) {
			consoleMessage(Database.class.getName(), "Tidak bisa mengeksekusi perintah sql \""+sqlString+"\" karena " + e.getMessage(), 2);
		}*/
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlString);
		} catch (SQLException e) {
			consoleMessage(Database.class.getName(), "Tidak bisa mengeksekusi perintah sql \""+sqlString+"\" karena " + e.getMessage(), 2);
		}
		return rs;
	}	
	
	public void closeConnection() throws SQLException {
		if (!connection.isClosed()) {
			try { 
				connection.close();
			} catch (SQLException logOrIgnore) {
				
			}
		}
			
	}
}
