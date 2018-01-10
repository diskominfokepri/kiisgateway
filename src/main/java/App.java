package id.go.kepriprov.kiisgateway;

import id.go.kepriprov.kiisgateway.lib.Database;

public class App {

	
	public static void main(String[] args) {


		String serverName = "localhost";
		String portNumber = "3306";
		String dbName = "testdb";
		String Username = "root";
		String Password = "";
		
		Database db = new Database(serverName, portNumber, dbName, Username, Password);
	}

}
