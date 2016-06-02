package tests;

import org.mindrot.jbcrypt.BCrypt;

import chatroom.Config;

public class GenerateHashedPass {
	public static void main(String args[]){
		String k = BCrypt.hashpw("1234", BCrypt.gensalt(Config.GENSALT_WORKLOAD));
		System.out.println(k);
		System.out.println(k.length());
	}
}
