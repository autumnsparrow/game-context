/**
 * 
 */
package com.sky.game.context.event;

/**
 * @author sparrow
 *
 */
public class LocalServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8640246625585454657L;

	
	public int state;
	

	
	
	public LocalServiceException(int state,String message) {
		super(message);
		// TODO Auto-generated constructor stub
	
		this.state=state;
	}

	public LocalServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
