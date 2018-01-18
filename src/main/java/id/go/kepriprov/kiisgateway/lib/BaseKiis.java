package id.go.kepriprov.kiisgateway.lib;

import org.apache.log4j.Logger;

public class BaseKiis {
	static Logger log;
	
	/**
	 * 
	 * @param classname
	 * @param message
	 */
	public void consoleMessage (String classname,String message,int level) {
		log = Logger.getLogger(classname);
		switch (level) {
			case 1://INFO
				log.info(message);
			break;
			case 2://ERROR
				log.error(message);
			break;
			case 3://fatal
				log.fatal(message);
			break;
		}
	}
}
