/**
 * 
 */
package com.sky.game.context.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sparrow
 *
 */
public class GlobalExceptionHandler implements UncaughtExceptionHandler {
	private static final Log logger=LogFactory.getLog(GlobalExceptionHandler.class);

	/**
	 * 
	 */
	public GlobalExceptionHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		logger.equals("thread["+arg0.getId()+"] "+getStackTrace(arg1));
	}
	
	 private String getStackTrace(Throwable aThrowable) {
		    final Writer result = new StringWriter();
		    final PrintWriter printWriter = new PrintWriter(result);
		    aThrowable.printStackTrace(printWriter);
		    return result.toString();
		 }

}
