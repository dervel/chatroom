package chatroom;

public enum StatusReturnMessages {
	AUTH_SUCCESS(0,"Logged in Successfully."),
	AUTH_FAILED(1,"Wrong username or password."),
	REGISTER_DUPLICATE(2,"Username is already in use."),
	REGISTER_SUCCESS(3,"Registered successfully"),
	REGISTER_FAILED_PASS(4,"Wrong Password format, correct the password and try again");
	
	private final String message;
	private final int id;
	
	StatusReturnMessages(final int id,final String s){
		this.id = id;
		message = s;
	}
	
	public String toString(){
		return message;
	}
	public int getID(){
		return id;
	}
	
	public static String getMessage(int i){
		for (StatusReturnMessages message : StatusReturnMessages.values()) {
            if (message.getID() == i) {
                return message.toString();
            }
        }
        return null;
	}
}
