/**
 * 
 */
package com.sky.game.context.message.impl.ice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.Message;
import com.sky.game.context.MessageAsyncHandlerPrx;
import com.sky.game.context.MessageException;
import com.sky.game.context._MessageAsyncHandlerDisp;
import com.sky.game.context.handler.ProtocolException;
import com.sky.game.context.message.MessageBean;
import com.sky.game.context.route.RouterHeader;

/**
 * 
 * IceServant.
 * 
 * @author sparrow
 *
 */

public class IceServantAsyncMessageHandler extends _MessageAsyncHandlerDisp {
	private static final Log logger=LogFactory.getLog(IceServantMessageHandler.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -3165103076655201190L;

	/**
	 * 
	 */
	public IceServantAsyncMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context._MessageAsyncHandlerOperations#onRecieve(com.sky.game.context.Message, java.util.Map, Ice.Current)
	 */
	public void onRecieve(Message msg, String extra,
			Current __current) throws MessageException {
		// TODO Auto-generated method stub
		
		MessageBean message=new MessageBean();
		message.transcode=msg.transcode;
		message.token=msg.token;
		message.content=msg.content;
		
		RouterHeader header=null;
		try {
			header=GameContextGlobals.getJsonConvertor().convert(extra, RouterHeader.class);
			
			
			GameContextGlobals.getServantMessageHandler().onRecieve(message, header);
			logger.info(header.toString()+" [Request] - "+message.transcode+", "+ message.content);
			header.e(MessageAsyncHandlerPrx.class.getSimpleName()+"_onRecieve");
			header.metrics();
		} catch (com.sky.game.context.message.MessageException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			throw new MessageException(e.getState(), e.getMessage());
			
			
		}
		
		catch (Exception e){
			
			if(e instanceof ProtocolException){
				throw new MessageException(((ProtocolException) e).getState(), e.getMessage());
				
			}
		}

	}

}
