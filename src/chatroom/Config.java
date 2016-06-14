package chatroom;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public static int DEFAULT_PORT = 1214;
	public static final int PACKET_BUFFER_SIZE = 1000;
	public static final int INPUTSTREAM_WAIT = 100;
	public static final int CONNECT_WAITOUT = 5000;
	public static final int LOG_REFRESH_TIME = 1000;
	public static final int INITIAL_MEMBERS_PROOM = 10;
	public static final int CLIENT_SIZE_ARRAY = 500;
	public static final int ROOM_SIZE_ARRAY = 500;
	public static final String DB_URL = "jdbc:mysql://localhost/chatroom";
	public static final String DB_USER = "dervel";
	public static final String DB_PASSWORD = "7106530";
	public static final int ROOM_MSG_ARRAYSIZE = 400;
	public static final int SERVER_LOG_SIZE = 100;
	public static final int GENSALT_WORKLOAD = 12;
	
	//Server
	public static String SERVER_NAME;
	public static boolean SERVER_AUTOSTART;
	public static int SERVER_PORT;
	
	public static boolean loadServerConfig() throws IOException{
		Properties prop = new Properties();
		String configFileName = "/config.properties";
		InputStream is = Config.class.getResourceAsStream(configFileName);
		
		if(is != null){
			prop.load(is);
			
			SERVER_NAME = prop.getProperty("server_name","Magic");
			SERVER_PORT = Integer.parseInt(prop.getProperty("server_port","1214"));
			SERVER_AUTOSTART = Boolean.parseBoolean(prop.getProperty("server_autostart","false"));
			return true;
			
		} else {
			throw new FileNotFoundException("property file "+configFileName+" not found in the class path.");
		}
	}
}
