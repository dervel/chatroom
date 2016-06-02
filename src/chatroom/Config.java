package chatroom;

public class Config {
	public static final int DEFAULT_PORT = 1214;
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
}
