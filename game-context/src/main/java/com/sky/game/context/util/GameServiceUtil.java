package com.sky.game.context.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;






import java.util.Random;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class GameServiceUtil {

	public GameServiceUtil() {
		// TODO Auto-generated constructor stub
	}
	
	private final static XmlMapper xmlMapper=new XmlMapper();
	
	public static LinkedList<String> getResourceAsLinkedList(String url) throws JsonParseException, JsonMappingException, IOException{
		InputStream is=GameServiceUtil.class.getResourceAsStream(url);
		LinkedList<String> list=xmlMapper.readValue(is, LinkedList.class);
		return list;
	}
	
	private static LinkedList<String> sex=null;
	private static LinkedList<String> men=null;
	private static LinkedList<String> women=null;
	
	private static String[] sexArray;
	private static String[] menArray;
	private static String[] womenArray;
	
	private static final String NAME_ROOT="/META-INF/random/";
	
	static{
		try {
			sex=getResourceAsLinkedList(NAME_ROOT+"sex.xml");
			men=getResourceAsLinkedList(NAME_ROOT+"men.xml");
			women=getResourceAsLinkedList(NAME_ROOT+"women.xml");
			
			sexArray=sex.toArray(new String[]{});
			menArray=men.toArray(new String[]{});
			womenArray=women.toArray(new String[]{});
			
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static enum SexTypes{
		Undefined(0,""),
		Men(1,"男"),
		Women(2,"女");
		
		public int value;
		public String message;
		
		
		private SexTypes(int value, String message) {
			this.value = value;
			this.message = message;
		}

		public boolean eq(SexTypes types){
			return this.value==types.value;
		}
		public String toString(){
			return message;
		}
		
		private static SexTypes[]  types=new SexTypes[]{Undefined,Men,Women};
		public static final int MEN=1;
		public static final int WOMEN=2;
		public static SexTypes getSexTypes(int i) throws Exception{
			if(i>types.length){
				throw new Exception("SexType not exist");
			}
			return types[i];
		}
	}
	
	public static  String getChinaName(SexTypes types) {
		 Random random = new Random();
    	String name = null;
    	if(types.eq(SexTypes.Men)){
           name = sexArray[random.nextInt(sexArray.length)]+menArray[random.nextInt(menArray.length)];
    	}else if(types.eq(SexTypes.Women)){
    		name = sexArray[random.nextInt(sexArray.length)]+womenArray[random.nextInt(womenArray.length)];
    	}
        return name;
    }
	
	public static void main(String args[]){
		for(int i=0;i<5;i++){
		String name=getChinaName(SexTypes.Men);
		
		/*System.out.println(" \n\n "+SexTypes.Men);
		System.out.println(name);
		Random r=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=9500;i<=10000;i++){
			int t=(1+r.nextInt(1));
			try {
				SexTypes type=SexTypes.getSexTypes(t);
				name=getChinaName(type);
				String sql=String.format("update user_extra set property_value='%s' where user_account_id in (select id from user_account where name='Robot%d') and property_id=%d;",name,i,26);
				sb.append(sql).append("\r\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		System.out.println(name);
		}
	}
	
	
	
	
	

}
