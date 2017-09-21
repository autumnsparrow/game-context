/**
 * 
 * @Date:Nov 21, 2014 - 9:30:05 AM
 * @Author: sparrow
 * @Project :game-context 
 * 
 *
 */
package com.sky.game.context.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sparrow
 *
 */
public   class EventProcessTask<T> implements Runnable {
	
	protected  T t;
	private static final Log logger=LogFactory.getLog(EventProcessTask.class);

	
	IGameSession session;

	public  EventProcessTask(T t) {
		super();
		this.t = t;
	}
	
	public String getType(){
		return t!=null?t.getClass().getSimpleName():"";
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public   void run(){
		logger.debug("TID="+Thread.currentThread().getId());
	}





	public IGameSession getSession() {
		return session;
	}





	public void setSession(IGameSession session) {
		this.session = session;
	}
	
	

}
