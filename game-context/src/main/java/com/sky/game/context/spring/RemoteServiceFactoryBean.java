package com.sky.game.context.spring;

import org.springframework.beans.factory.FactoryBean;

import com.sky.game.context.spring.proxy.RemoteServiceProxyFactory;

public class RemoteServiceFactoryBean<T> implements FactoryBean<T> {
	
	private Class<T> mapperInterface;

	RemoteServiceProxyFactory<T>  remoteServiceProxyFactory;
	private final Object lock=new Object();

	public RemoteServiceFactoryBean() {
		// TODO Auto-generated constructor stub
		//remoteServiceProxyFactory=new RemoteServiceProxyFactory(this.mapperInterface);
		}
	
	

	@Override
	public T getObject() throws Exception {
		// TODO Auto-generated method stub
		// create a proxy object 
		synchronized (lock) {
			if(remoteServiceProxyFactory==null){
				remoteServiceProxyFactory=new RemoteServiceProxyFactory<T>(mapperInterface);
			}
		}
		
		
		return remoteServiceProxyFactory.newInstance();
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return true;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

}
