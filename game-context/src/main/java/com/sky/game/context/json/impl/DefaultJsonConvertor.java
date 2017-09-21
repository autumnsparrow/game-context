/**
 * 
 */
package com.sky.game.context.json.impl;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.json.HandlerObjectType;
import com.sky.game.context.json.IJsonConvertor;
import com.sky.game.context.util.GameUtil;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolMapEntry;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolNamespaceEntry;
/**
 * @author sparrow
 *
 */
public class DefaultJsonConvertor implements IJsonConvertor {
	
	private static final Log logger=LogFactory.getLog(DefaultJsonConvertor.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 
	 */
	public DefaultJsonConvertor() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#format(java.lang.Object)
	 */
	public String format(Object obj) {
		// TODO Auto-generated method stub
		StringWriter writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, obj);
		} catch (Exception e) {
		}
		return writer.toString();
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#convert(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(String transcode, String jsonStr,HandlerObjectType  extraType) {
		// TODO Auto-generated method stub
		AnnotationIntrospector annotationIntrospector=GameContextGlobals.getAnnotationIntrospector();
		
		ProtocolMapEntry entry=annotationIntrospector.getProtocolMapEntry(transcode);
		
		T t=null;
		Class<?> clz=null;
		try {
			// first using the transcode extra.
			
			if(extraType.compareTo(HandlerObjectType.Extra)==0){
				
				String namespace=entry.namespace;
				ProtocolNamespaceEntry namespaceEntry= annotationIntrospector.getProtocolNamespaceEntry(namespace);
				clz=namespaceEntry.extraClazz;
				
				
				// if the transcode extra is empty ,then we use the  namespace extra class.
				//
				if(entry.extraClazz!=null){
					clz=entry.extraClazz; 
				}
				
			}else if(extraType.compareTo(HandlerObjectType.Request)==0){
				clz=entry.requestClazz;
			}else if(extraType.compareTo(HandlerObjectType.Response)==0){
				clz=entry.responseClazz;
			}
			
			if(GameUtil.isNull(jsonStr)){
				t=(T)clz.newInstance();
			}else{
				t = (T) objectMapper.readValue(jsonStr,clz);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> T convertConfig(URL url,Class<?> clazz) {
		// TODO Auto-generated method stub
		T t=null;
		try {
			
			t = (T) objectMapper.readValue(url,clazz);
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
		
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(String jsonStr, Class<?> clazz) {
		// TODO Auto-generated method stub
		T t=null;
		try {
			
			t = (T) objectMapper.readValue(jsonStr,clazz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("json:"+jsonStr+"  - "+" clazz :"+clazz.getName()+e.getMessage());
			e.printStackTrace();
		}
		
		return t;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#convertConfig(java.io.InputStream, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T convertConfig(InputStream is, Class<?> clazz) {
		// TODO Auto-generated method stub
		
		T t=null;
		try {
			t = (T) objectMapper.readValue(is,clazz);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		return t;
	}

	@Override
	public <T> T convert(String value, JavaType typeReference) {
		// TODO Auto-generated method stub
		T t=null;
		try {
			
			t = objectMapper.readValue(value,typeReference);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("json:"+value+"  - "+" clazz : - "+e.getMessage());
			e.printStackTrace();
		}
		
		return t;
	}
}
