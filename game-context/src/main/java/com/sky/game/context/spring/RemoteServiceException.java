/**
 * 
 */
package com.sky.game.context.spring;

/**
 * @author sparrow
 *
 */
public class RemoteServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8161249130768492428L;
	public int status;
	public RemoteServiceException(int status) {
		super();
		this.status = status;
	}
	public RemoteServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RemoteServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public RemoteServiceException(int status,String message) {
		super(message);
		// TODO Auto-generated constructor stub
		this.status = status;
	}
	public RemoteServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	
	
}

