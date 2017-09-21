/**
 * 
 */
package com.sky.game.context.message.impl.ice;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.Message;
import com.sky.game.context.MessageException;
import com.sky.game.context._MessageHandlerDisp;
import com.sky.game.context.domain.ErrorBeans;
import com.sky.game.context.handler.ProtocolException;
import com.sky.game.context.message.MessageBean;

/**
 * 
 * Ice servant
 * 
 * 
 * server side
 * @author sparrow
 *
 */
public class IceServantMessageHandler extends _MessageHandlerDisp {

	/**
	 * 
	 */
	private static final Log logger=LogFactory.getLog(IceServantMessageHandler.class);
	private static final long serialVersionUID = 948103216861289749L;

	/**
	 * 
	 */
	public IceServantMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context._MessageHandlerOperations#invoke(com.sky.game.context.Message, Ice.Current)
	 */
	public Message invoke(Message msg, Current __current)
			throws MessageException {
		// TODO Auto-generated method stub
		MessageBean message=new MessageBean();
		message.transcode=msg.transcode;
		message.content=msg.content;
		message.token=msg.token;
		MessageBean resp=null;
		Message response=new Message();
		try {
			resp = GameContextGlobals.getServantMessageHandler().invoke(message);
			logger.debug("Request - "+ message.content+" Response - "+resp.content);
		} catch (com.sky.game.context.message.MessageException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			//throw new MessageException(e.getState(), e.getMessage());
			response.transcode="ERROR";
			response.content=GameContextGlobals.getJsonConvertor().format(ErrorBeans.Request.get(e.getState(), msg.transcode,e.getMessage()));
			response.token=message.token;
			return response;
			
		}
		
		catch (Exception e){
			e.printStackTrace();
			if(e instanceof ProtocolException){
				//throw new MessageException(((ProtocolException) e).getState(), e.getMessage());
				response.transcode="ERROR";
				response.content=GameContextGlobals.getJsonConvertor().format(ErrorBeans.Request.get(((ProtocolException) e).getState(),message.transcode, e.getMessage()));
				response.token=message.token;
				return response;
			}else{
				response.transcode="ERROR";
				response.content=GameContextGlobals.getJsonConvertor().format(ErrorBeans.Request.get(-1,message.transcode, e.getMessage()));
				response.token=message.token;
			}
		}
		
		
		response.token=resp.token;
		response.content=resp.content;
		response.transcode=resp.transcode;
		
		return response;
	}

	public String testIce(String str, Current __current) {
		// TODO Auto-generated method stub
		String s=System.currentTimeMillis()+"  ---- "+str;
		logger.info(s);
		return "JL";
	}
	

}
