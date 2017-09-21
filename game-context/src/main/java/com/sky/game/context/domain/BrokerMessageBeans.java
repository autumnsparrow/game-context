/**
 * 
 */
package com.sky.game.context.domain;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.Message;
import com.sky.game.context.MessageException;
import com.sky.game.context.annotation.HandlerAsyncType;
import com.sky.game.context.annotation.HandlerNamespaceExtraType;
import com.sky.game.context.annotation.HandlerRequestType;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolMapEntry;
import com.sky.game.context.event.LocalServiceException;
import com.sky.game.context.message.IceProxyMessageInvoker;
import com.sky.game.context.message.MessageBean;
import com.sky.game.context.message.ProxyMessageInvoker;
import com.sky.game.context.route.RouterHeader;
import com.sky.game.context.util.G;

/**
 * @author sparrow
 *
 */
public class BrokerMessageBeans {
	
	@HandlerRequestType(transcode="BM0001")
	public static class Request{
		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		String namespace;
		String content;
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Request(String content) {
			super();
			this.content = content;
			
		}
		
		
		public Request(String namespace,MessageBean message){
			super();
			String m = message.transcode + "&"+message.token+"&" + message.content;
			this.content=m;
			this.namespace=namespace;
		}
		
		
		@Override
		public String toString() {
			return  content;
		}
		
		
		public Request() {
			super();
			// TODO Auto-generated constructor stub
		}

		public static Request broadcast(String namespace,String transcode,Object request,String token){
			MessageBean message=new MessageBean();
			message.transcode=transcode;
			message.content=GameContextGlobals.getJsonConvertor().format(request);
			message.token=token;//GameContextGlobals.getToken();
			return new Request(namespace,message);
		}
		
	}
	
	
	@HandlerNamespaceExtraType(namespace="BrokerMessage")
	public static class Extra extends RouterHeader{
		
		
	}
	
	@HandlerAsyncType(namespace="BrokerMessage",transcode="BM0001",enable=true,enableFilter=false)
	public static class Handler{
		
	}
	
	
	// add
	// don't allow the 
	
	public static void broadcast(Object obj) throws LocalServiceException{
		// fetch the object namespace.
		// transcode.
		ProtocolMapEntry entry=GameContextGlobals.getAnnotationIntrospector().getProtocolMapEntryByRequestClass(obj);
		if(entry==null){
			throw new LocalServiceException(-1, "can't find the object protocol map!");
		}
		Request req=Request.broadcast(entry.namespace,entry.transcode,obj,"");
		RouterHeader header=new RouterHeader();
		//header.setTo(to);
		
		
		ProxyMessageInvoker.onRecieve("BM0001", req, header);
	}

	
	
}
