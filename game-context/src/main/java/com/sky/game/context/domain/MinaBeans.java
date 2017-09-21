/**
 * 
 */
package com.sky.game.context.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.Message;
import com.sky.game.context.MessageException;
import com.sky.game.context.annotation.HandlerAsyncType;
import com.sky.game.context.annotation.HandlerNamespaceExtraType;
import com.sky.game.context.annotation.HandlerRequestType;
import com.sky.game.context.message.IceProxyMessageInvoker;
import com.sky.game.context.message.MessageBean;
import com.sky.game.context.message.ProxyMessageInvoker;
import com.sky.game.context.route.RouterHeader;


/**
 * @author sparrow
 *
 */
public class MinaBeans {
	private static final Log logger=LogFactory.getLog(MinaBeans.class);
	
	@HandlerRequestType(transcode="SM0001")
	public static class Request{
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
		
		
		public Request(MessageBean message){
			super();
			String m = message.transcode + "&"+message.token+"&" + message.content;
			this.content=m;
		}
		
		
		@Override
		public String toString() {
			return  content;
		}
		
		
		public Request() {
			super();
			// TODO Auto-generated constructor stub
		}

		public static Request getRequest(String transcode,Object request,String token){
			MessageBean message=new MessageBean();
			message.transcode=transcode;
			message.content=GameContextGlobals.getJsonConvertor().format(request);
			message.token=token;//GameContextGlobals.getToken();
			return new Request(message);
		}
		
	}
	
	
	@HandlerNamespaceExtraType(namespace="GameMina")
	public static class Extra extends RouterHeader{
		
		
	}
	
	@HandlerAsyncType(namespace="GameMina",transcode="SM0001",enable=true,enableFilter=false)
	public static class Handler{
		
	}
	
	
	// add
	// don't allow the 
	
	public static void sendMinaMessage(String token,RouterHeader header,String transcode,Object obj){
		Request req=Request.getRequest(transcode,obj,token);
		
		logger.info(header.toString()+" - "+req.getContent());
		ProxyMessageInvoker.onRecieve("SM0001", req, header);
	}

	
	
}
