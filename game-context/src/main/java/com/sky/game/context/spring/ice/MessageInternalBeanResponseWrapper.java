package com.sky.game.context.spring.ice;

import com.sky.game.context.spring.RemoteServiceException;

public class MessageInternalBeanResponseWrapper {
	
	String responseObject;
	RemoteServiceException exceptionObject;
	
	
	


	public String getResponseObject() {
		return responseObject;
	}



	public void setResponseObject(String responseObject) {
		this.responseObject = responseObject;
	}





	public RemoteServiceException getExceptionObject() {
		return exceptionObject;
	}



	public void setExceptionObject(RemoteServiceException exceptionObject) {
		this.exceptionObject = exceptionObject;
	}



	public static   MessageInternalBeanResponseWrapper getObject(){
		return new MessageInternalBeanResponseWrapper();
	}

}
