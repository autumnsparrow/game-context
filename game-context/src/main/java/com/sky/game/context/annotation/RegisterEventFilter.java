/**
 * 
 * @Date:Nov 14, 2014 - 9:53:07 AM
 * @Author: sparrow
 * @Project :game-context 
 * 
 *
 */
package com.sky.game.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sparrow
 *
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterEventFilter {
	
	String transcode() default "";
	

}
