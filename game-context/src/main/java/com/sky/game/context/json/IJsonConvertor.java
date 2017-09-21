/**
 * 
 */
package com.sky.game.context.json;

import java.io.InputStream;
import java.net.URL;

import com.fasterxml.jackson.databind.JavaType;

/**
 * @author sparrow
 *
 */
public interface IJsonConvertor {
	
	/**
	 * 
	 * 
	 * @param obj
	 * @return
	 */
	public String format(Object obj);
	
	/**
	 * 
	 * @param transcode
	 * @param jsonStr
	 * @return
	 */
	public <T> T convert(String transcode,String jsonStr,HandlerObjectType handlerObjectType); 
	
	
	public <T> T convertConfig(URL url,Class<?> clazz);
	
	public <T> T convert(String jsonStr,Class<?> clazz);
	
	public <T> T convertConfig(InputStream is,Class<?> clazz);
	
	
	public  <T> T convert(String value, final JavaType typeReference);
	

}
