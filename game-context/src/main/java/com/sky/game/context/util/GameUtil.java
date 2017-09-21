/**
 * 
 */
package com.sky.game.context.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.InitializationData;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.annotation.introspector.IIdentifiedObject;
import com.sky.game.context.event.DefaultGameSession;
import com.sky.game.context.event.GameEvent;
import com.sky.game.context.event.IGameSession;
import com.sky.game.context.game.AbstractGameLife;
import com.sky.game.context.game.CrontabGameLife;
import com.sky.game.context.game.GameWorldTimer;
import com.sky.game.context.ice.IceProxyManager;

/**
 * @author Administrator
 *
 */
public class GameUtil {
	private static final Log logger=LogFactory.getLog(GameUtil.class);

	/**
	 * 
	 */
	public GameUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isNull(String str){
		
		return str==null?true:("\"\"\n".equals(str)?true:false);
	}
	
	public static int s2i(String s){
		int i=0;
		if(!(s==null||"".equals(s))){
			i=Integer.parseInt(s);
		}
		return i;
	}
	
	private static final ConcurrentHashMap<Long, String> token_maps=new ConcurrentHashMap<Long, String>();
	private static  AtomicLong  token_id;
	private static final String TOKEN_FILE="/tmp/token.serial";
	public static void initTokenSerail(){
		
		try {
			File f=new File(TOKEN_FILE);
			
			if(f.exists()){
				String id;
				id = FileUtils.readFileToString(f);
				token_id=new AtomicLong(Long.parseLong(id));
			}else{
				token_id=new AtomicLong(1);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			token_id=new AtomicLong(1);
		}
		
		
	}
	
	public static Long getTokenId(){
		if(token_id.longValue()>Long.MAX_VALUE-10){
			token_id.set(1);
		}
		final Long id= token_id.getAndIncrement();
		GameContextGlobals.postTask(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					FileUtils.writeStringToFile(new File("/tmp/token.serial"), String.valueOf(id.longValue()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//logger.info("remove token:"+t);
			}
			
		});
		return id;
	}
	
	
	public static String getToken(String s,String key){
		String token=String.format("%s%d",s,getTokenId());
		String t=null;
		try {
			t= DES.encrypt(token, key);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
		
	}
	
	public static void removeToken(final String t){
		GameContextGlobals.postTask(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.info("remove token:"+t);
			}
			
		});
		
		Long k=Long.parseLong(t);
		token_maps.remove(k);
	}
	
	public static void putToken(final Long t,final String token){
		GameContextGlobals.postTask(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.info("put token:("+t+","+token);
			}
			
		});
		
		token_maps.put(t, token);
	}
	
	
	
	public  static String decode(String s) {
		//
		String a=null;
		if(!GameUtil.isNull(s)){
			Long k=Long.parseLong(s);
			a=token_maps.get(k);
		}
	    return a;
	}
	public static String encode(String s) {
		Long k=getTokenId();
		putToken(k, s);
	    return String.valueOf(k.longValue());
	}
	
	public static InitializationData getIceConfig(String iceConfig){
		InitializationData config=new InitializationData();
		config.properties=Ice.Util.createProperties();
		
		InputStream is=IceProxyManager.class.getResourceAsStream(iceConfig);
		java.util.Properties pros=new java.util.Properties();
		try {
			pros.load(is);
			
			for(Object k:pros.keySet()){
				
				Object v=pros.get(k);
				if(v!=null){
					String key=(String)k;
					String value=(String)v;
					config.properties.setProperty(key, value);
					logger.info("IceProperties:"+String.format("[key=%s,value=%s]", key,value));
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return config;
	}

	
	
	public static <T> List<T> getList(){
		List<T> t=new LinkedList<T>();
		return t;
	}
	
	
	public static <K,V> Map<K,V> getMap(){
		Map<K,V> map=new LinkedHashMap<K, V>();
		return map;
	}
	
	
	public static <T> List<T> asList(T[]  t){
		List<T> values=new LinkedList<T>();
		for(int i=0;i<t.length;i++){
			values.add(t[i]);
		}
		return values;
		
	}
	
	
	
	
	public static <T extends Object> T obtain(Class<?> clz){
		T t=null;
		try {
			//Class<?> clz=Class.forName(t.getClass().getName());
			t=(T) clz.newInstance();
		}catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
		
	}
	
	public static <V >  Map<Long,V> getListAsMap(List<V> obj){
		Map<Long,V> maps=getMap();
		for(V o:obj){
			if(o instanceof IIdentifiedObject){
				IIdentifiedObject iido=(IIdentifiedObject)o;
				maps.put(iido.getId(), o);
			}
		}
		return maps;
	}
	
	public static <K,V> List<V> getMapAsList(Map<K,V> obj){
		List<V> lists=getList();
		for(V v:obj.values()){
			lists.add(v);
		}
		return lists;
	}
	
	
	
	
	public static void clearGameLife(String uri){
		GameWorldTimer.getGameWorldTimer().destoryGameLife(uri);
	}
	
	
	
	public static CrontabGameLife gameLife(final String uri,String crontab,final Object o,final String method,final Object ...args){
		clearGameLife(uri);
		CrontabGameLife life=new CrontabGameLife(crontab){

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return uri;
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				//logger.info("timeout trigger - ("+uri+") "+this.getFormatedTime());
				try {
					BeanUtil.invoke(o, method, args);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.refresh();
			}
			
		}.start();
		
		return life;
	}
	
	/**
	 * gamelife.
	 * @param uri
	 * @param lives
	 * @param o
	 * @param method
	 * @param args
	 * @return
	 */
	public static AbstractGameLife gameLife(final String uri,long lives,final Object o,final String method,final Object ...args){
		
		clearGameLife(uri);
		AbstractGameLife life=new AbstractGameLife(lives) {
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return uri;
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				try {
					BeanUtil.invoke(o, method, args);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	super.refresh();
				
			}
			
		}.start();
		
		//life.set
		return life;
		
		
	}
	
	/**
	 * format the id.
	 * @param id
	 * @return
	 */
	public static String formatId(Long id,Object clz){
		
		return id==null?String.format("%08d", 0):String.format("[%08d-%20s]",id.longValue(),clz==null?"":clz.getClass().getSimpleName());
	}
	
	public static String formatUri(String uri,Object clz){
		return String.format("[%20s-%20s]", uri==null?"":uri,clz==null?"":clz.getClass().getSimpleName());
	}
	
	
	public static <T> T evtAsObj(GameEvent evt){
		T t=null;
		if(evt!=null&&evt.obj!=null){
			//Class<?> clz=Class.forName(t.getClass().getName());
			t=(T) evt.obj;
		}
		
		return t;
	}
	
	
	 public static String getStackTrace(Throwable aThrowable) {
		    final Writer result = new StringWriter();
		    final PrintWriter printWriter = new PrintWriter(result);
		    aThrowable.printStackTrace(printWriter);
		    return result.toString();
	}
	 
	 
	public static final IGameSession DEFAULT_GAMESESSION=new DefaultGameSession();
	
	
	
	private static final Random r=new Random();
	
	public static int getRandomPokerAiLevel(){
		int s=r.nextInt();
		int i=s%5;
		
		return i<0?i*-1:i;
	}
	
	public static void main(String args[]){
		for(int i=0;i<1000;i++)
		System.out.println(getRandom(5));
	}
	
	
	public static InputStream getInputStream(String s) throws Exception{
		InputStream is=null;
		if(s.startsWith("http://")){
			try {
				URL u=new URL(s);
				is=u.openStream();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			is=GameUtil.class.getResourceAsStream(s);
		}
		
		if(is==null){
			throw new Exception(" can't get the resource by url:"+s);
		}
		
		return is;
	}
	
	
	public static int getRandom(int seed){
		return (int)(Math.random()*seed);
	}
	
	
	public static Long c2s(Long id){
		return Long.valueOf(id.longValue()-19920606);
	}
	
	public static Long s2c(Long id){
		return Long.valueOf(id.longValue()+19920606);
	}
	
	public static List<Long> c2s(List<Long> ids){
		List<Long> cids=getList();
		if(ids!=null){
			for(Long l:ids){
				cids.add(c2s(l));
			}
		}
		return cids;
	}
	
	public static List<Long> s2c(List<Long> ids){
		List<Long> sids=getList();
		if(ids!=null){
			for(Long l:ids){
				sids.add(s2c(l));
			}
		}
		return sids;
	}



}
