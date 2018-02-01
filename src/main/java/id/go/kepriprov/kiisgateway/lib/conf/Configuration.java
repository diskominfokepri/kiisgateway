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
	/**
	 * Web server Jetty Port
	 * @return int port
	 */
	public int getJettyPort () {
		int port = Integer.parseInt(config.get("jetty_port"));
		return port;
	}
	
	/**
	 * KIIS Monitoring and Management db host
	 * @return String db host
	 */
	public String getKIISmpDBHost () {
		return config.get("kiismp_db_host");
	}
	/**
	 * KIIS Monitoring and Management db port
	 * @return String db port
	 */
	public String getKIISmpDBPort () {
		return config.get("kiismp_db_port");
	}
	/**
	 * KIIS Monitoring and Management db user
	 * @return String db user
	 */
	public String getKIISmpDBUser () {
		return config.get("kiismp_db_user");
	}
	/**
	 * KIIS Monitoring and Management db password
	 * @return String db password
	 */
	public String getKIISmpDBPassword () {
		return config.get("kiismp_db_password");
	}
	/**
	 * KIIS Monitoring and Management db name
	 * @return String db name
	 */
	public String getKIISmpDBName () {
		return config.get("kiismp_db_name");
	}	
	
	/**
	 * KIIS Transactional db host
	 * @return String db host
	 */
	public String getKIISTransDBHost () {
		return config.get("kiistrans_db_host");
	}
	/**
	 * KIIS Transactional db port
	 * @return String db port
	 */
	public String getKIISTransDBPort () {
		return config.get("kiistrans_db_port");
	}
	/**
	 * KIIS Transactional db user
	 * @return String db user
	 */
	public String getKIISTransDBUser () {
		return config.get("kiistrans_db_user");
	}
	/**
	 * KIIS Transactional db password
	 * @return String db password
	 */
	public String getKIISTransDBPassword () {
		return config.get("kiistrans_db_password");
	}
	/**
	 * KIIS Transactional db name
	 * @return String db name
	 */
	public String getKIISTransDBName () {
		return config.get("kiistrans_db_name");
	}	
	
	/**
	 * KIIS Pipeline kafka host
	 * @return String kafka host
	 */
	public String getKIISKafkaHost () {
		return config.get("kiiskafka_host");
	}
	/**
	 * KIIS Pipeline kafka port
	 * @return String kafka port
	 */
	public String getKIISKafkaPort () {
		return config.get("kiiskafka_port");
	}	

}
