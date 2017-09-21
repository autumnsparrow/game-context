/**
 * 
 */
package com.sky.game.context.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.annotation.introspector.EventHandlerClass;
import com.sky.game.context.annotation.introspector.IIdentifiedObject;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolMapEntry;
import com.sky.game.context.domain.BaseRequest;
import com.sky.game.context.domain.MinaBeans;
import com.sky.game.context.event.DefaultGameSession;
import com.sky.game.context.event.IGameSession;

import com.sky.game.context.handler.ProtocolException;
import com.sky.game.context.route.RouterHeader;
import com.sky.game.context.util.GameUtil;

/**
 * @author Administrator
 *
 */
public class WebsocketEvent {

	public RouterHeader header;

	public Object obj;
	public String transcode;
	public String token;

	private static final Log logger = LogFactory.getLog(WebsocketEvent.class);

	/**
	 * 
	 */
	public WebsocketEvent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * invalid token
	 * 
	 */
	private void invalidToken() {

		// LG0010Response resp = GameUtil.obtain(LG0010Response.class);
		// ProtocolMapEntry entry =
		// GameContextGlobals.getAnnotationIntrospector()
		// .getProtocolMapEntryByResponse(resp);
		// if (entry != null) {
		// transcode = entry.transcode;
		// }
		// MinaBeans.sendMinaMessage(token, deviceId, transcode, resp);
	}
	
	private String  tokenByObject(Object obj){
		String t=null;
		if (obj instanceof BaseRequest) {
			t= ((BaseRequest) obj).getToken();
		}
		return t;
	}
	
	String protocolRetranscodeMapEntryByRequest(){
		String t=null;
		ProtocolMapEntry entry = GameContextGlobals.getAnnotationIntrospector()
				.getProtocolMapEntryByRequestClass(this.obj);
		if (entry != null) {
			t = entry.responsecode;
		}
		return t;
	}
	
	private String protocolMapEntryByRequest(Object obj){
		String t=null;
		ProtocolMapEntry entry = GameContextGlobals.getAnnotationIntrospector()
				.getProtocolMapEntryByRequestClass(obj);
		if (entry != null) {
			t = entry.transcode;
		}
		return t;
	}
	
	private String protocolMapEntryByReponse(Object obj){
		String t=null;
		ProtocolMapEntry entry = GameContextGlobals
				.getAnnotationIntrospector()
				.getProtocolMapEntryByResponse(obj);
		if (entry != null) {
			t = entry.responsecode;
		}
		return t;
		
	}


	/**
	 * 
	 * NOTICE: must login with token
	 * 
	 * @param obj
	 */
	public WebsocketEvent(RouterHeader routeHeader,Object obj) {
		super();

		
		this.header = routeHeader;
		
		this.obj = obj;
		this.token=tokenByObject(obj);	
		this.transcode=protocolMapEntryByRequest(obj);

	}

	
	
	
	

	public WebsocketEvent(RouterHeader header,Object obj,  boolean request) {
		super();
		
		
		
		this.header=header;
		this.token=header.getToken();
		
		this.obj=obj;
		try {
			
			if (request) {
				this.transcode=protocolMapEntryByRequest(this.obj);
			} else {
				this.transcode=protocolMapEntryByReponse(this.obj);
			}
		}	
		 catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	
	
	

	@Override
	public String toString() {
		return "MinaEvent [header=" + header + ", transcode=" + transcode
				+ ", obj=" + obj + ", token=" + token + "]";
	}

	

	//Response
	public static WebsocketEvent o(RouterHeader evt,Object obj, boolean request) {
		return new WebsocketEvent(evt,obj, request);
	}
	
	public static void send(RouterHeader header,Object obj, boolean request){
		
		header.m(MinaBeans.class.getSimpleName()+"_sendMessage");
		header.e("WebsocketRecievedTask_run");
		header.metrics();
		
		WebsocketEvent event=new WebsocketEvent(header,obj,request);
		event.sendMessage();
	}
	

	public static boolean filterMinaEvent(WebsocketEvent event,
			IIdentifiedObject identiedObject) {
		boolean ret = false;
		Object obj = event.obj;
		if (obj instanceof IIdentifiedObject) {
			IIdentifiedObject request = (IIdentifiedObject) obj;
			ret = (request.getId().longValue() == identiedObject.getId()
					.longValue());
		}

		return ret;

	}

	public Long getId() throws ProtocolException {
		Long id = null;

		if (obj instanceof IIdentifiedObject) {
			IIdentifiedObject request = (IIdentifiedObject) obj;
			id = request.getId();
		} else {
			/*
			throw new ProtocolException(-1, obj.getClass().getName()
					+ " must implements the IIdentifiedObject Interface");*/
			id=EventHandlerClass.DEFAULT_ID;
		}
		return id;

	}

	
	public void sendMessage() {

		boolean valid = true;

		if (valid) {
			
			MinaBeans.sendMinaMessage(token, header, transcode, obj);
		}
	}

}
