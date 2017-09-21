/**
 * 
 * @Date:Nov 14, 2014 - 9:54:12 AM
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
 * 
 * method annotation -
 * handle the async -event in an identified object.
 * @author sparrow
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterEventHandler {
	
	String transcode() default "";
}
