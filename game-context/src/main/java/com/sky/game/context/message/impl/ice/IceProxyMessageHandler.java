/**
 * 
 */
package com.sky.game.context.message.impl.ice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.Message;
import com.sky.game.context.message.IMessageHandler;
import com.sky.game.context.message.MessageBean;
import com.sky.game.context.message.MessageException;
import com.sky.game.context.message.IceProxyMessageInvoker;
import com.sky.game.context.route.RouterHeader;

/**
 * 
 * client side
 * 
 * @author sparrow
 *
 */
public class IceProxyMessageHandler implements IMessageHandler {
	private static final Log logger=LogFactory.getLog(IceProxyMessageHandler.class);
	/**
	 * 
	 */
	public IceProxyMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.message.IMessageHandler#invoke(com.sky.game.context.message.MessageBean)
	 */
	public MessageBean invoke(MessageBean msg) throws MessageException {
		// TODO Auto-generated method stub
		Message message=new Message();
		message.transcode=msg.transcode;
		message.token=msg.token;
		message.content=msg.content;
		Message resp=null;
		try {
			resp = IceProxyMessageInvoker.invoke(message);
		} catch (com.sky.game.context.MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MessageException(e.state, e.message);
		}
		MessageBean response=new MessageBean();
		response.token=resp.token;
		response.content=resp.content;
		response.transcode=resp.transcode;
		
		return response;
		
	}

	/**
	 * 
	 * 
	 * 
	 */
	public void onRecieve(MessageBean msg, RouterHeader extra)
			throws MessageException {
		// TODO Auto-generated method stub
		
		Message message=new Message();
		message.transcode=msg.transcode;
		message.token=msg.token;
		message.content=msg.content;
		
		
		try {
			
			IceProxyMessageInvoker.onRecieve(message, extra);
			
		} catch (com.sky.game.context.MessageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new MessageException(e1.state, e1.message);
		}
		
		
		
	}


	

}
