package id.go.kepriprov.kiisgateway.lib.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import id.go.kepriprov.kiisgateway.lib.Helper;

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
			Enumeration<?> enuKeys = properties.keys();			
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
	public String getKIISmpDBHost () {
		return config.get("kiismp_db_host");
	}
	public String getKIISmpDBPort () {
		return config.get("kiismp_db_port");
	}
	public String getKIISmpDBUser () {
		return config.get("kiismp_db_user");
	}
	public String getKIISmpDBPassword () {
		return config.get("kiismp_db_password");
	}
	public String getKIISmpDBName () {
		return config.get("kiismp_db_name");
	}
	
	public String getKIISHiveDBHost () {
		return config.get("kiishive_db_host");
	}
	public String getKIISHiveDBPort () {
		return config.get("kiishive_db_port");
	}	
	public String getKIISHiveDBUser () {
		return config.get("kiishive_db_user");
	}
	public String getKIISHiveDBPassword () {
		return config.get("kiishive_db_password");
	}
	public String getKIISHiveDBName () {
		return config.get("kiishive_db_name");
	}

}
