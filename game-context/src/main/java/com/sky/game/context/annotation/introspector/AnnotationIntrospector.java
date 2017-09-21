/**
 * 
 */
package com.sky.game.context.annotation.introspector;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.annotation.HandlerAsyncType;
import com.sky.game.context.annotation.HandlerException;
import com.sky.game.context.annotation.HandlerExtraType;
import com.sky.game.context.annotation.HandlerMethod;
import com.sky.game.context.annotation.HandlerNamespaceExtraType;
import com.sky.game.context.annotation.HandlerRequestType;
import com.sky.game.context.annotation.HandlerResponseType;
import com.sky.game.context.annotation.HandlerType;
import com.sky.game.context.annotation.RegisterEventHandler;
import com.sky.game.context.handler.ProtocolException;
import com.sky.game.context.util.G;


/**
 * @author sparrow
 *
 */

public class AnnotationIntrospector {
	
	private static final Log logger=LogFactory.getLog(AnnotationIntrospector.class);
	
	/**
	 * 
	 * @author sparrow
	 *
	 */
	public static class ProtocolNamespaceEntry{
		public String namespace;
		public Class<?>  extraClazz;
		
		
		public ProtocolNamespaceEntry(String namespace) {
			super();
			this.namespace = namespace;
		}
		@Override
		public String toString() {
			return "ProtocolNamespaceEntry [namespace=" + namespace
					+ ", extraClazz=" + extraClazz + "]";
		}
		
		
		
		
	};
	
	public static class ProtocolMapEntry{
		public String transcode;
		public String namespace;
		public Class<?>  requestClazz;
		
		public String responsecode;
		public Class<?>  responseClazz;
		public Class<?>  handlerClazz;
		public Class<?>  extraClazz;
		
		public boolean sync;
		public boolean enable;
		public boolean enableFilter;
		public Method method;
		
		public String serviceName;
		
		public ProtocolMapEntry() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "ProtocolMapEntry - [\n transcode=" + transcode + ",\n namespace="
					+ namespace + ",\n requestClazz=" + requestClazz
					+ ",\n responsecode=" + responsecode + ",\n responseClazz="
					+ responseClazz + ",\n handlerClazz=" + handlerClazz
					+ ",\n extraClazz=" + extraClazz + ",\n sync=" + sync
					+ ",\n enable=" + enable + ",\n enableFilter=" + enableFilter
					+ ",\n method=" + method + "\n]\n";
		}
		private ProtocolMapEntry(String transcode) {
			super();
			this.transcode = transcode;
		}
		
		
	}

	/**
	 * 
	 */
	public AnnotationIntrospector() {
		// TODO Auto-generated constructor stub
	}
	
	private String beanPackages;
	
	private String beanLocations[];
	
	private String handlerPackages;
	private String handlerLocations[];

	@SuppressWarnings("unused")
	private AnnotationIntrospector(String beanPackages,String handlerBasePackages) {
		super();
		this.setBasePackages(beanPackages, handlerBasePackages);
	}
	
	
	
	public void setBasePackages(String beanPackages,String handlerBasePackages){
		this.beanPackages = beanPackages;
		if(this.beanPackages!=null&&this.beanPackages.indexOf(",")>0){
			beanLocations=this.beanPackages.split(",");
		}else{
			beanLocations=new String[]{this.beanPackages};
		}
		
		this.handlerPackages = handlerBasePackages;
		if(this.handlerPackages!=null&&this.handlerPackages.indexOf(",")>0){
			handlerLocations=this.handlerPackages.split(",");
		}else{
			handlerLocations=new String[]{this.handlerPackages};
		}
		logger.info("beanPackages:"+this.beanPackages+"\nhandlerPackages:"+this.handlerPackages);
	}
	
	private final ConcurrentHashMap<String,ProtocolMapEntry> protocolMap=new ConcurrentHashMap<String, ProtocolMapEntry>();
	private final ConcurrentHashMap<Integer,ProtocolException> protocolExceptionMap=new ConcurrentHashMap<Integer,ProtocolException>();
	private final ConcurrentHashMap<String,ProtocolNamespaceEntry> protocolNamespaceEntries=new ConcurrentHashMap<String, AnnotationIntrospector.ProtocolNamespaceEntry>();
	private final ConcurrentHashMap<String, EventHandlerClass> eventHandlerMap=new ConcurrentHashMap<String, EventHandlerClass>();
	
	private synchronized void scanHandlers() throws Exception{
		// filter the localstions.
		//logger.info("ScanHandlers .....");
		try {
			if(this.handlerLocations!=null){
				for(String location:this.handlerLocations){
					List<Class<?>> clazzz= ClassScanner.find(location);
					if(clazzz.size()>0)
						logger.info("ScanHandlers-"+location+" "+clazzz.size());
					for(Class<?> cls:clazzz){
						//Annotation []aa =cls.getAnnotations();
						//logger.info("parepared:"+cls.getName());
						registerHandlerType(cls);
						registerAsyncHandlerType(cls);
						
						// 
						registerEventHandler(cls);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("scanHandler fatal error:"+e.getMessage());
		}
		
	}
		
	/**
	 * 
	 * 
	 * @param method
	 * @throws Exception
	 */
	private void registerHandlerException(Method method) throws Exception{
		Annotation aa=method.getAnnotation(HandlerException.class);
		if(aa !=null){
			HandlerException handlerException=(HandlerException)aa;
			int states[]=handlerException.states();
			String messages[]=handlerException.messages();
			for(int i=0;i<states.length;i++){
			Integer k=Integer.valueOf(states[i]);
			if(!protocolExceptionMap.containsKey(k)){
				protocolExceptionMap.put(k, new ProtocolException(states[i], messages[i]));
			}else{
				throw new Exception("HandlerException state duplicated!"+k.intValue());
			}
		 }
		}
	}
	/**
	 * 
	 * 
	 * @param method
	 * @param entry
	 */
	private void registerMethod(Method method,ProtocolMapEntry entry){
		Annotation aa=method.getAnnotation(HandlerMethod.class);
		if(aa!=null){
				HandlerMethod m=(HandlerMethod)aa;
				if(m.enable()){
					entry.method=method;
				}
		}
		
	}
	
	
	/**
	 * 
	 * 
	 * @param cls
	 * @throws Exception
	 */
	private void registerHandlerType(Class<?> cls) throws Exception{
		Annotation a=cls.getAnnotation(HandlerType.class);
		if(a!=null){
			HandlerType handlerType=(HandlerType)a;
			String transcode=handlerType.transcode();
			if(!protocolMap.containsKey(transcode)){
				protocolMap.put(transcode, new ProtocolMapEntry(transcode));
			}
			ProtocolMapEntry entry=protocolMap.get(transcode);
			
			entry.handlerClazz=cls;
			entry.enable=handlerType.enable();
			entry.sync=true;
			entry.namespace=handlerType.namespace();
		
			
			// filled the exceptions.
			// also find the method annotations
			Method[]  methods=cls.getDeclaredMethods();
			for(Method method:methods){
				registerHandlerException(method);
				registerMethod(method, entry);
			}
			
		}
		
		
	}
	
	/**
	 * 
	 * @param cls
	 * @throws Exception
	 */
	private void registerAsyncHandlerType(Class<?> cls) throws Exception{
		Annotation a=cls.getAnnotation(HandlerAsyncType.class);
		if(a!=null){
			HandlerAsyncType handlerType=(HandlerAsyncType)a;
			String transcode=handlerType.transcode();
			if(!protocolMap.containsKey(transcode)){
				protocolMap.put(transcode, new ProtocolMapEntry(transcode));
			}
			ProtocolMapEntry entry=protocolMap.get(transcode);
			
			entry.handlerClazz=cls;
			entry.sync=false;
			entry.enable=handlerType.enable();
			entry.namespace=handlerType.namespace();
			entry.enableFilter=handlerType.enableFilter();
		
			
			// filled the exceptions.
			// also find the method annotations
			Method[]  methods=cls.getDeclaredMethods();
			for(Method method:methods){
				registerHandlerException(method);
				
				registerMethod(method, entry);
			}
			
		}
		
		
	}
	
	
	
	private void registerEventHandler(Class<?> cls) throws Exception{
		
		Method[]  methods=cls.getDeclaredMethods();
		for(Method method:methods){
			
			
			Annotation ah=method.getAnnotation(RegisterEventHandler.class);
			if(ah!=null){
				RegisterEventHandler m=(RegisterEventHandler)ah;
				String transcode=m.transcode();
				EventHandlerClass eventHandlerClass=eventHandlerMap.get(transcode);
				if(eventHandlerClass==null){
					eventHandlerClass=EventHandlerClass.obtain();
					eventHandlerMap.put(transcode, eventHandlerClass);
					
				}
				eventHandlerClass.handler=method;
				eventHandlerClass.eventHandlerClz=cls;
				eventHandlerClass.transcode=transcode;
				//eventHandlerClass.eventHandlerClz=cls;
					
			}
			
		}
		
		
	}
	/**
	 * 
	 * 
	 * @param clz
	 */
	private void registerRequestBean(Class<?> clz){
		Annotation a=clz.getAnnotation(HandlerRequestType.class);
		if(a!=null){
			HandlerRequestType handlerRequestType=(HandlerRequestType)a;
			String transcode=handlerRequestType.transcode();
			if(!protocolMap.containsKey(transcode)){
				protocolMap.put(transcode, new ProtocolMapEntry(transcode));
			}
			ProtocolMapEntry entry=protocolMap.get(transcode);
			entry.requestClazz=clz;
		}
	}
	
	
	private void registerNamespaceExtraBean(Class<?> clz){
		Annotation a=clz.getAnnotation(HandlerNamespaceExtraType.class);
		if(a!=null){
			HandlerNamespaceExtraType handlerRequestType=(HandlerNamespaceExtraType)a;
			String namespace=handlerRequestType.namespace();
			if(!protocolNamespaceEntries.containsKey(namespace)){
				protocolNamespaceEntries.put(namespace, new ProtocolNamespaceEntry(namespace));
			}
			
			ProtocolNamespaceEntry entry=protocolNamespaceEntries.get(namespace);
			entry.extraClazz=clz;
		
		}
	}
	
	/**
	 * 
	 * 
	 * @param clz
	 */
	private void registerExtraBean(Class<?> clz){
		Annotation a=clz.getAnnotation(HandlerExtraType.class);
		if(a!=null){
			HandlerExtraType handlerRequestType=(HandlerExtraType)a;
			String transcode=handlerRequestType.transcode();
			if(!protocolMap.containsKey(transcode)){
				protocolMap.put(transcode, new ProtocolMapEntry(transcode));
			}
			ProtocolMapEntry entry=protocolMap.get(transcode);
			entry.extraClazz=clz;
		}
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @param clz
	 */
	private void registerResponseBean(Class<?>  clz){
		Annotation aa=clz.getAnnotation(HandlerResponseType.class);
		if(aa!=null){
			HandlerResponseType handlerResponseType=(HandlerResponseType)aa;
			String transcode=handlerResponseType.transcode();
			if(!protocolMap.containsKey(transcode)){
				protocolMap.put(transcode, new ProtocolMapEntry(transcode));
			}
			ProtocolMapEntry entry=protocolMap.get(transcode);
			entry.responseClazz=clz;
			entry.responsecode=handlerResponseType.responsecode();
		}
	}
	
	private void scanBeans() throws Exception{
		// filter the localstions.
		logger.info("ScanBeans...");
		try {
			if(this.beanLocations!=null){
				for(String location:beanLocations){
					List<Class<?>> clazzz= ClassScanner.find(location);
					for(Class<?> cls:clazzz){
						//Annotation []aa =cls.getAnnotations();
						Class<?>[] cc=cls.getClasses();
						
						for(Class<?> clz:cc){
							
							registerRequestBean(clz);
							registerResponseBean(clz);
							// a class can be only handler or async handler.
							registerHandlerType(clz);
							registerExtraBean(clz);
							registerAsyncHandlerType(clz);
							registerNamespaceExtraBean(clz);
							
						}
					}
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
	
	
	
	
	 private void clearMaps(){
		this.protocolExceptionMap.clear();
		this.protocolMap.clear();
	 }
	 
	public void scan(){
		try {
			this.clearMaps();
			
			// the hanlder will overrite the handlers.
			scanBeans();
			scanHandlers();
			scanWildHandlers();
			
			
			this.dump();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void scanWildHandlers(){
		// if entry can't find,then find a match pattern
		logger.info("ScanWildHandlers");
		for(ProtocolMapEntry ee:this.protocolMap.values()){	
			
		if(ee.handlerClazz==null)
		for(ProtocolMapEntry e:this.protocolMap.values()){
			if(e.enableFilter){
				Pattern p=Pattern.compile(e.transcode);
				Matcher m=p.matcher(ee.transcode);
				if(m.find()){
					ee.handlerClazz=e.handlerClazz;
					ee.namespace=e.namespace;
					ee.enable=e.enable;
					ee.enableFilter=e.enableFilter;
					ee.method=e.method;
					
					break;
				}
						
						
			}
		}
				
		}
	}
	
	public Collection<EventHandlerClass> getEventHandlers(){
		return eventHandlerMap.values();
	}
	
	
	public synchronized EventHandlerClass getEventHandler(String transcode) throws Exception{
		EventHandlerClass handler= null;
		handler=eventHandlerMap.get(transcode);
		if(handler==null){
			throw new Exception(transcode+"- don't find any event handler");
		}
		return handler;
	}
	
	
	public Collection<ProtocolMapEntry> getProtocolMapEntries(){
		return this.protocolMap.values();
	}
	
	
	public ProtocolMapEntry getProtocolManEntryPattern(String transcode){
		ProtocolMapEntry ee=null;
		for(ProtocolMapEntry e:this.protocolMap.values()){
			if(e.enableFilter){
				Pattern p=Pattern.compile(e.transcode);
				Matcher m=p.matcher(transcode);
				if(m.find()){
					ee=e;
					break;
				}
						
						
			}
		}
		return ee;
	}
	
	
	
	public ProtocolMapEntry getProtocolMapEntry(String transcode){
		// completely match.
		// or pattern.
		
		ProtocolMapEntry entry= this.protocolMap.get(transcode);
		return entry;
	}
	
	public Collection<ProtocolException> getProtocolExceptions(){
		return this.protocolExceptionMap.values();
	}
	
	
	public ProtocolException getProtocolException(int state){
		Integer k=Integer.valueOf(state);
		ProtocolException ex=new ProtocolException(9999, "Unkown exception");
		if(protocolExceptionMap.containsKey(k))
			ex=protocolExceptionMap.get(k);
		return ex;
	}
	
	
	public ProtocolNamespaceEntry getProtocolNamespaceEntry(String namespace){
		ProtocolNamespaceEntry entry=null;
		if(protocolNamespaceEntries.containsKey(namespace)){
			entry=protocolNamespaceEntries.get(namespace);
		}
		return entry;
	}
	
	private void dump(){
		StringBuffer buffer=new StringBuffer();
		buffer.append("\n\n--------------------------------------------------------------------------\n\n");
		Collection<ProtocolMapEntry> entries=getProtocolMapEntries();
	
		for(ProtocolMapEntry entry:getProtocolMapEntries()){
			buffer.append("\n").append(entry.toString()).append("\n");
	      }
	    	  
	      for(ProtocolException ex:getProtocolExceptions()){
	    	  buffer.append("\n").append(ex.toString()).append("\n");
	      }
	      
	      for(ProtocolNamespaceEntry entry:protocolNamespaceEntries.values()){
	    	  buffer.append("\n").append(entry.toString()).append("\n");
	      }
	      
	      for(EventHandlerClass handler:eventHandlerMap.values()){
	    	  buffer.append("\n").append(handler.toString());
	      } 
	      
	     
	      buffer.append("\n\n--------------------------------------------------------------------------\n\n");
	      String name=GameContextGlobals.getConfig().getServiceName();
	     // logger.info(buffer.toString());
	      G.dump(name+"/annotation",buffer.toString());
	}
	
	public String getTranscodeByResponseTranscode(String responseTranscode){
		String transcode=null;
		for(ProtocolMapEntry entry:protocolMap.values()){
			if(entry.responsecode!=null&&entry.responsecode.equals(responseTranscode)){
				transcode=entry.transcode;
				break;
			}
		}
		return transcode;
		
	}
	
	public ProtocolMapEntry getProtocolMapEntryByResponse(Object obj){
		ProtocolMapEntry protocolMapEntry=null;
		for(ProtocolMapEntry entry:protocolMap.values()){
			
			if(entry.responseClazz!=null&&obj.getClass().getName().equals(entry.responseClazz.getName())){
				protocolMapEntry=entry;
				break;
			}
		}
		
		return protocolMapEntry;
	}
	
	public ProtocolMapEntry getProtocolMapEntryByRequestClass(Object obj){
		ProtocolMapEntry protocolMapEntry=null;
		for(ProtocolMapEntry entry:protocolMap.values()){
			if(entry.requestClazz!=null&&obj.getClass().getName().equals(entry.requestClazz.getName())){
				protocolMapEntry=entry;
				break;
			}
			
		}
		
		return protocolMapEntry;
	}



	

	public String[] getBeanLocations() {
		return beanLocations;
	}



	public void setBeanLocations(String[] beanLocations) {
		this.beanLocations = beanLocations;
	}



	public void setHandlerLocations(String[] handlerLocations) {
		this.handlerLocations = handlerLocations;
	}
}
