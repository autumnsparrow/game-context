/**
 * 
 */
package com.sky.game.context.spring.proxy;

import java.lang.reflect.Proxy;


/**
 * @author sparrow
 *
 */
public class RemoteServiceProxyFactory<T> {
	
	 private final Class<T> mapperInterface;

	
	 /**
	 * @param mapperInterface
	 */
	public RemoteServiceProxyFactory(Class<T> mapperInterface) {
		super();
		this.mapperInterface = mapperInterface;
	}

	@SuppressWarnings("unchecked")
	 protected T newInstance(RemoteServiceProxy<T> mapperProxy) {
	    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
	 }

	 public T newInstance() {
	    final RemoteServiceProxy<T> mapperProxy = new RemoteServiceProxy<T>( mapperInterface);
	    return newInstance(mapperProxy);
	 }

}
