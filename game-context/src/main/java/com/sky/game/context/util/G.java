/**
 * 
 */
package com.sky.game.context.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.annotation.introspector.IIdentifiedObject;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolMapEntry;
import com.sky.game.context.event.WebsocketEvent;
import com.sky.game.context.ice.IceProxyManager;
import com.sky.game.context.id.LLGlobalIdGenerator;
import com.sky.game.context.id.LLIdTypes;


/**
 * @author sparrow
 *
 */
public class G {
	
	
	public static final boolean DEBUG=true;
	
	private static final Log logger=LogFactory.getLog(G.class);
	
	
	public static <T> T evtAsObj(WebsocketEvent evt){
		T t=null;
		if(evt!=null&&evt.obj!=null){
			//Class<?> clz=Class.forName(t.getClass().getName());
			t=(T) evt.obj;
		}
		
		return t;
	}
	
	public static <T> T asObject(Object obj){
		T t=null;
		
		if(obj!=null){
			t=(T)obj;
		}
		return t;
	}
	
	public static Long getId(LLIdTypes types){
		return LLGlobalIdGenerator.g.getId(types.type);
	}

	
	public static <T> T o(Class<?> clz){
		return GameUtil.obtain(clz);
	}
	
	
	
	public static <T> T exp(Object o,String e){
		return BeanUtil.v(o, e);
	}

	
	
	/**
	 * {@link GameContextGlobals#registerEventHandler(String, IIdentifiedObject)}
	 * @param obj
	 * @param transcode
	 */
	public static void regeisterHandler(IIdentifiedObject obj,String ...strings ){
		
		StringBuffer buffer=new StringBuffer();
		buffer.append("registerEventHandler(");
		for(String s:strings){
			GameContextGlobals.registerEventHandler(s,obj);
			buffer.append(s).append(",");
			
		}
		buffer.append(obj.getClass().getSimpleName()+"@"+obj.getId()+")");
		//logger.info(buffer.toString());
	}
	
	/**
	 * 
	 * @param obj
	 * @param strings
	 */
	public static void removeHandler(IIdentifiedObject obj,String ...strings ){
		for(String s:strings){
			GameContextGlobals.unregisterEventHandler(s,obj);
		}
	}
	
	
	public static void clearAllHandlers(IIdentifiedObject obj){
		GameContextGlobals.removeObjectEventHandler(obj);
	}

	public static boolean isHandled(Object obj){
		ProtocolMapEntry entry = GameContextGlobals.getAnnotationIntrospector()
				.getProtocolMapEntryByRequestClass(obj);
		return entry!=null?true:false;
	}
	
	public static String transcode(Object obj,boolean request){
		String t=null;
		ProtocolMapEntry entry = GameContextGlobals.getAnnotationIntrospector()
				.getProtocolMapEntryByRequestClass(obj);
		if (entry != null) {
			t = request?entry.transcode:entry.responsecode;
		}
		return t;
	}
	
	public static IceProxyManager IPM(){
		return GameContextGlobals.getIceProxyManager();
	}
	
	public static String getNode(){
		String path=System.getenv("NODE_NAME");
		path=path.replace(".", "/");
		System.out.println("Node="+path);
		return path;
	}
	
	public static String getNamespace(String node){
		String namespace=null;
		if(node.contains("-")){
			namespace=node.split("-")[0];
		}else{
			namespace=node;
		}
		return namespace;
		
	}
	
	
	public static interface IExecution{
		
		public void execute();
		
	}
	
	
	public static void testExecute(IExecution e,int count){
		long begin=System.currentTimeMillis();
		for(int i=0;i<count;i++)
			e.execute();
		logger.info("<D> (duration="+(System.currentTimeMillis()-begin)+",loop="+count);
		
	}
	
	public static <T> T parse(String content,Class<?> clz){
		T t=GameContextGlobals.getJsonConvertor().convert(content, clz);
		return t;
	}
	
	public static void dump(String fileName,String content){
		try {
			File f=new File("/tmp/"+fileName);
			
			FileUtils.writeStringToFile(f, CronUtil.getFormatedDateNow()+"\r\n"+content);
			logger.info("write :"+f.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("write :"+fileName+ " - "+e.getMessage());
			File f=new File(fileName);
			
			try {
				FileUtils.writeStringToFile(f, CronUtil.getFormatedDateNow()+"\r\n"+content);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.info("write :"+f.getAbsolutePath());
			//logger.info(content);
		}
	}
	
}
