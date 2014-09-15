package Main;

public class Examples {
	
	public static void reverseString(String temp){
		char[] c = new char[temp.length()];
		char[] p = new char[temp.length()];
		c = temp.toCharArray();
		char swag;
		for(int i = 0;i<temp.length();i++){
			swag = c[i];
			p[i] = swag;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
