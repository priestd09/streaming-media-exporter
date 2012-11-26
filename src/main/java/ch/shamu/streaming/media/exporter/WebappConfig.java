package ch.shamu.streaming.media.exporter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class WebappConfig {

	private static Properties props;
	
	public static String get(String key){
	
		if (props == null)
		{
			Properties defaultConfig = new Properties();
			// load default conf
			InputStream is = WebappConfig.class.getClassLoader().getResourceAsStream("config.default.properties");
			try {
				defaultConfig.load(is);
			}
			catch(Exception e){
				System.out.println("Error reading default configuration");
				e.printStackTrace();
			}

			
			Properties config = new Properties();
			try {
				is = new FileInputStream("/var/streaming-media-exporter/conf.properties");
				config.load(is);
			} catch (Exception e) {
				System.out.println("Could not read webapp config in /var/streaming-media-exporter/conf.properties, using default config");
			}

			props = new Properties();
			props.putAll(defaultConfig);
			props.putAll(config);
			System.out.println("--------- Application config :");
			for(Object o : props.keySet()){
				System.out.println(o+":"+props.get(o));
			}
			System.out.println("--------- End of config :");
		}
		
		return (String)props.get(key);
		
	};
	
}
