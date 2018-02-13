package sda.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sda.web.views.PersonView;

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
	
	public static List<UserRole> seperateUserRoles(String roles){
		
		ArrayList<UserRole> result = new ArrayList<UserRole>();
		
		String[] rolesSplit = roles.split("~");
		
		for (String role : rolesSplit) {
			result.add(UserRole.valueOf(role));
		}
		
		return result;
	}
	
	public static String buildUserRoles(List<UserRole> roles){
		
		String result = "";
		for (int i = 0; i < roles.size(); i++) {
			
			if(i != roles.size() - 1)
				result += roles.get(i).name()+"~";
			else
				result += roles.get(i).name();
		}
		
		return result;
	}
	
	public static List<String> seperateUserIds(String ids)
	{
		List<String> result = new ArrayList<String>();
		
		String[] temp = ids.split("~");
		for (String id : temp)
			result.add(id);
	
		return result; 
	}
	
	public static String buildUserIds(ArrayList<PersonView> users)
	{
		String result = "";
		
		for (int i = 0; i < users.size(); i++) {
			
			if(i != users.size() - 1)
				result += users.get(i).getUuid()+"~";
			else
				result += users.get(i).getUuid();
		}
	
		return result; 
	}

}
