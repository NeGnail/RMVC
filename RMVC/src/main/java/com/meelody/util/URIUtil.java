package coolraw.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * URI工具
 * @author LiWei
 */

public class URIUtil {		
	
	public static boolean isMatch(String pathA,String pathB){
		String[] pathAPart=pathA.split("/");
		String[] pathBPart=pathB.split("/");
		if(pathAPart.length>pathBPart.length){
			return false;
		}
		for(int i=0;i<pathAPart.length;i++){
			if(!pathAPart[i].equals(pathBPart[i])){
				return false;
			}
		}
		return true;
	}
	
	public static String sub(String pathA,String pathB){
		return pathA.substring(pathB.length());
	}
	
	public static boolean isMatchForRest(String methodUri,
			String methodUriAndPar) {
		String[] pathAPart=methodUri.split("/");
		String[] pathBPart=methodUriAndPar.split("/");
		if(pathAPart.length!=pathBPart.length){
			return false;
		}
		for(int i=0;i<pathAPart.length;i++){
			if(!pathAPart[i].equals(pathBPart[i])){
				for(int j=i;j<pathAPart.length;j++){
					String bPart=pathBPart[j];
					if(!bPart.startsWith("{")||!bPart.endsWith("}")){
						return false;
					}
					
				}
			}
		}
		return true;
	}
	
	public static Map<String, String[]> getParametersForRest(String methodUri,
			String methodUriAndPar) {
		Map<String,String[]> map=new HashMap<String,String[]>();
		
		String[] pathAPart=methodUri.split("/");
		String[] pathBPart=methodUriAndPar.split("/");
		for(int i=0;i<pathAPart.length;i++){
			if(!pathAPart[i].equals(pathBPart[i])){
				for(int j=i;j<pathAPart.length;j++){
					String name=pathBPart[j].substring(1,pathBPart[j].length()-1);
					String[] value=new String[]{pathAPart[j]};
					map.put(name, value);
				}
			}
		}
		return map;
	}
	
	public static String getAccessUri(HttpServletRequest request) {
		return request.getRequestURI().substring(request.getContextPath().length());
	}
	

	public static void main(String[] args) {
		String a="/as/gs/gsd";
		String b="/as/gs";
		System.out.println(URIUtil.sub(a, b));
	
		
	}
}
