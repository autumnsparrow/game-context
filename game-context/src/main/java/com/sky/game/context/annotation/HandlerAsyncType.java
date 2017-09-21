/**
 * 
 */
package com.sky.game.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * class annotation - means async method object.
 * 
 * @author sparrow
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HandlerAsyncType {
	
	// the transcode
		String transcode() default "";

		boolean enable() default true;

		String namespace() default "";
		
		boolean enableFilter() default false;
		
		
		

}
