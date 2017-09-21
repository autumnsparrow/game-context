/**
 * 
 */
package com.sky.game.context.handler;
/**
 * @author sparrow
 *
 */
public interface IProtocolAsyncHandler<Request,Extra> {
	
	
	void onRecieve(Request request,Extra extra) throws ProtocolException;

}
