/**
 * 
 */
package com.sky.game.context.handler;

/**
 * @author sparrow
 *
 */
public class ProtocolException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4464756925205578064L;

	/**
	 * 
	 */
	public ProtocolException() {
		// TODO Auto-generated constructor stub
	}
	
	int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public ProtocolException(int state,String message) {
		super(message);
		this.state = state;
	}

	/**
	 * @param arg0
	 */
	public ProtocolException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ProtocolException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ProtocolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
