/**
 * 
 */
package com.sky.game.context.message;

import com.sky.game.context.route.RouterHeader;


/**
 * @author sparrow
 *
 */
public interface IMessageHandler {
	
	/**
	 * interface for the ice 
	 * 
	 * @param message
	 * @return
	 * @throws MessageException
	 */
	public MessageBean invoke(MessageBean message) throws MessageException;
	
	
	public void onRecieve(MessageBean message,RouterHeader extra) throws MessageException;

}
