/**
 * 
 */
package com.sky.game.context.json.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sky.game.context.json.HandlerObjectType;
import com.sky.game.context.json.IJsonConvertor;

/**
 * @author sparrow
 *
 */
public class XmlJsonConvertor implements IJsonConvertor {

	
	private static XmlMapper objectMapper = new XmlMapper();
	
	/**
	 * 
	 */
	public XmlJsonConvertor() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#format(java.lang.Object)
	 */
	@Override
	public String format(Object obj) {
		// TODO Auto-generated method stub
		StringWriter writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, obj);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return writer.toString();
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#convert(java.lang.String, java.lang.String, com.sky.game.context.json.HandlerObjectType)
	 */
	@Override
	public <T> T convert(String transcode, String jsonStr,
			HandlerObjectType handlerObjectType) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#convertConfig(java.net.URL, java.lang.Class)
	 */
	@Override
	public <T> T convertConfig(URL url, Class<?> clazz) {
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

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#convert(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T convert(String jsonStr, Class<?> clazz) {
		// TODO Auto-generated method stub
		T t=null;
		try {
			t = (T) objectMapper.readValue(jsonStr,clazz);
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

	/* (non-Javadoc)
	 * @see com.sky.game.context.json.IJsonConvertor#convertConfig(java.io.InputStream, java.lang.Class)
	 */
	@Override
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
		return null;
	}

}
