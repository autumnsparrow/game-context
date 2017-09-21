/**
 * 
 */
package com.sky.game.context.executor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sparrow
 *
 */
public class DefaultRejectHandler implements RejectedExecutionHandler {
	private static final Log logger=LogFactory.getLog(DefaultRejectHandler.class);
	
	private ConcurrentHashMap<Runnable,Integer> rejects=new ConcurrentHashMap<Runnable,Integer>();

	/* (non-Javadoc)
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		// TODO Auto-generated method stub
		rejects.put(r, Integer.valueOf(0));
		logger.warn(" reject task:"+rejects.size()+" - "+executor.getClass().getName());
		
	}

}
