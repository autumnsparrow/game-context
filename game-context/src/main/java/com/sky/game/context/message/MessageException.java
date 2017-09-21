/**
 * 
 */
package com.sky.game.context.message;

/**
 * @author sparrow
 *
 */
public class MessageException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6158580776705682524L;
	int state;

	public MessageException(int state,String message) {
		super(message);
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 
	 */
	public MessageException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public MessageException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public MessageException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MessageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
