/**
 * 
 */
package com.sky.game.context.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * framework basic:
 * 
 * 
 * local method invoke:
 * 
 *  public class ObjectClass {
 *  	
 *    // sync method invoke
 *    public ResponseObject  methodName(RequestObject request) ExceptionObject;
 *  
 *  }
 *  
 *  or 
 *  public class ObjectAsyncClass{
 *  
 *   	public void  methodAsyncName(RequestObject,Extra extra) ExceptionObject;
 *   }
 *   
 *   
 *  
 *   
 * 
 * 
 * class annotation - mean method object  
 * 
 * @author sparrow
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HandlerType {
	// the transcode
	String transcode() default "";

	boolean enable() default true;

	String namespace() default "";



}
