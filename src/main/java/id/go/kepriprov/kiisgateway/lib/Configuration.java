package id.go.kepriprov.kiisgateway.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {
	private String configFile =Helper.getAppLocation()+"server.properties";
	private HashMap<String,String> config;
	
	public Configuration () {		
		File f = new File (configFile);
		FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(f);			
			Properties properties = new Properties();			
			properties.load(fileInput);			
			fileInput.close();			
			Enumeration enuKeys = properties.keys();			
			config = new HashMap<String, String>();
			
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				config.put(key, value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	public int getJettyPort () {
		int port = Integer.parseInt(config.get("jetty_port"));
		return port;
	}
	public String getDBHost () {
		return config.get("db_host");
	}
	public String getDBPort () {
		return config.get("db_port");
	}
	public String getDBUser () {
		return config.get("db_user");
	}
	public String getDBPassword () {
		return config.get("db_password");
	}
	public String getDBName () {
		return config.get("db_name");
	}

}
