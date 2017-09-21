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
 * class annotation - mean request parameter object.
 * @author sparrow
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HandlerRequestType {
	String transcode() default "";
}
