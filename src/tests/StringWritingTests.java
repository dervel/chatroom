package tests;

import java.nio.charset.StandardCharsets;

public class StringWritingTests {

	public static void main(String[] args) {
		String s = "abc";
		byte[] temp = s.getBytes(StandardCharsets.UTF_16);
		
		
		String s1 = new String(temp, StandardCharsets.UTF_16);
		
		System.out.println(s1.length());
	}

}
