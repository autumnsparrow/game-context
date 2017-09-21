/**
 * 
 */
package com.sky.game.context.message.impl;

import java.lang.reflect.InvocationTargetException;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.SpringContext;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolMapEntry;
import com.sky.game.context.domain.BaseRequest;
import com.sky.game.context.domain.BaseResponse;
import com.sky.game.context.handler.ProtocolException;
import com.sky.game.context.json.HandlerObjectType;
import com.sky.game.context.json.IJsonConvertor;
import com.sky.game.context.message.IMessageHandler;
import com.sky.game.context.message.MessageBean;
import com.sky.game.context.message.MessageException;
import com.sky.game.context.route.RouterHeader;

/**
 * 
 * 
 * server side
 * 
 * @author sparrow
 *
 */
public class DefaultServantMessageHandler implements IMessageHandler {

	/**
	 * 
	 */
	public DefaultServantMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.message.IMessageHandler#send(com.sky.game.context.message.MessageBean)
	 */
	public MessageBean invoke(MessageBean message) throws MessageException {
		// TODO Auto-generated method stub
		MessageBean reponseBean=new MessageBean();
		String transcode=message.transcode;
		String content=message.content;
		String token=message.token;
		
		IJsonConvertor convertor=GameContextGlobals.getJsonConvertor();
		
		Object request=convertor.convert(transcode, content, HandlerObjectType.Request);
		
		if(request!=null&&(request instanceof BaseRequest)){
			BaseRequest baseRequest=(BaseRequest)request;
			baseRequest.setToken(token);
		}
		
		// find the handlers.
		AnnotationIntrospector introspector=GameContextGlobals.getAnnotationIntrospector();
		ProtocolMapEntry entry=introspector.getProtocolMapEntry(transcode);
		
		if(entry.enable&&entry.handlerClazz!=null){
			try {
				Object handler= SpringContext.getBean(entry.transcode);//entry.handlerClazz.newInstance();
				Object response= entry.responseClazz.newInstance();
				if(handler==null){
					throw new RuntimeException("trascode["+entry.transcode+"] don't exist!");
				}
				if(entry.method==null){
					throw new RuntimeException("transcode["+entry.transcode+"] don't have the @HandleMethod annotation!");
				}
				
				if(handler!=null){
					
					try {
						entry.method.invoke(handler,request, response);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						throw new RuntimeException("transcode["+entry.transcode+"] "
								+entry.method+request+response+" ,"+handler
								+entry.toString()+request.getClass().getName()+" - "+response.getClass().getName());
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						e.printStackTrace();
						
						if(e.getTargetException() instanceof ProtocolException){
							ProtocolException pEx=(ProtocolException)e.getTargetException();
							throw new MessageException(pEx.getState(), pEx.getMessage());
						}
					} catch (Exception ex){
						if(ex instanceof ProtocolException){
							ProtocolException pEx=(ProtocolException)ex;
							throw new MessageException(pEx.getState(), pEx.getMessage());
						}
						
					}
					
				}
				
				
				
				if(response!=null){
					
					reponseBean.content=convertor.format(response);
					reponseBean.transcode=entry.responsecode;
					if(response instanceof BaseResponse){
						BaseResponse baseResponse=(BaseResponse)response;
						reponseBean.token=baseResponse.getToken();
					}else{
						reponseBean.token=token;
					}
					
				}
				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(entry.enable&&entry.handlerClazz==null){
			//We should using the MinaEventHandler to deal with the async message..
			
		}else{
			
			throw new MessageException(-1, "transcode disable");
			
			
		}
		
		
		
		return reponseBean;
	}

	
	/**
	 * 
	 * 
	 * 
	 */
	public void onRecieve(MessageBean message, RouterHeader extra)
			throws MessageException {
		// TODO Auto-generated method stub
		
		
		String transcode=message.transcode;
		String content=message.content;
		String token=message.token;
		
		IJsonConvertor convertor=GameContextGlobals.getJsonConvertor();
		
		Object request=convertor.convert(transcode, content, HandlerObjectType.Request);
		
		// using the method extra or the namespace extra.
		if(request!=null&&(request instanceof BaseRequest)){
			BaseRequest baseRequest=(BaseRequest)request;
			baseRequest.setToken(token);
		}
		
		
		Object extraParam=extra;

		// find the handlers.
		AnnotationIntrospector introspector=GameContextGlobals.getAnnotationIntrospector();
		
		
		ProtocolMapEntry entry=introspector.getProtocolMapEntry(transcode);
		
		if(entry.enable&&entry.handlerClazz!=null){
			
			Object handler=null;
			try {
				if(entry.enableFilter){
					handler=SpringContext.getBean(entry.handlerClazz.getSimpleName());
				}else{
					handler = SpringContext.getBean(transcode);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
			try {
				//entry.handlerClazz.newInstance();
				
				if(handler!=null){
					
					try {
						if(entry.method==null)
							throw new ProtocolException(-1, handler.getClass().getName()+" method must be annotation with @HandlerMethod");
						entry.method.invoke(handler,request, extraParam);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						if(e.getTargetException() instanceof ProtocolException){
							ProtocolException pEx=(ProtocolException)e.getTargetException();
							throw new MessageException(pEx.getState(), pEx.getMessage());
						}
					} catch (Exception ex){
						if(ex instanceof ProtocolException){
							ProtocolException pEx=(ProtocolException)ex;
							throw new MessageException(pEx.getState(), pEx.getMessage());
						}
						
					}
					
				}
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}
				// when enable the filter.
				
			
			
		
		else{
			
			throw new MessageException(-1, "transcode disable");
			
			
		}
		
		
	}
  

	
}
