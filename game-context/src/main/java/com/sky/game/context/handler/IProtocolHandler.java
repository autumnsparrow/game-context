/**
 * 
 */
package com.sky.game.context.handler;

/**
 * @author sparrow
 *
 */
public interface IProtocolHandler<Req,Resp> {
	
	/**
	 * 
	 * 
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws ProtocolException
	 */
	public  boolean handler(Req req,Resp res) throws ProtocolException;

}
