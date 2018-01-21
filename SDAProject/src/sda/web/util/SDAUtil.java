package sda.web.util;

import java.util.ArrayList;
import java.util.Random;

/* 	Util class to help 
 * 
 * */
public final class SDAUtil {

	private SDAUtil() {
		
    }
	
	public static String GenerateUuid(){
		
		Random rnd = new Random();
		return Integer.toString(100000000 + rnd.nextInt(900000000));
	}
	
	public static ArrayList<UserRole> seperateUserRoles(String roles){
		
		ArrayList<UserRole> result = new ArrayList<UserRole>();
		
		String[] rolesSplit = roles.split("~");
		
		for (String role : rolesSplit) {
			result.add(UserRole.valueOf(role));
		}
		
		return result;
	}
	
	/*
	public static String buildUserRoles(ArrayList<UserRole> roles){
		
		String result = "";
		for (UserRole userRole : roles) {
			result.
		}
		String[] rolesSplit = roles.split("~");
		
		for (String role : rolesSplit) {
			result.add(UserRole.valueOf(role));
		}
		
		return result;
	}
	*/
}
