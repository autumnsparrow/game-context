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
 * 
 * class annotation - async method invoke extra-parameters ,in the namespace level.
 * @author sparrow
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HandlerNamespaceExtraType {
	String namespace() default "";
}
