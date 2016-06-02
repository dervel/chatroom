package tests;

public class BufferReadingTests {
	public static void main(String args[]){
		
		int actual_value = 65535;
		
		int a = (actual_value & 0x000000FF);
		int b = (actual_value & 0x0000FF00) >> 8;
		
		int final_value = b <<8;
		final_value += a;
		
		System.out.println(final_value);
		System.out.println(Character.SIZE);
	}
}
