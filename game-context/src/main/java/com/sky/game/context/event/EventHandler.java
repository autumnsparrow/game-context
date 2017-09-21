/**
 * 
 * @Date:Nov 21, 2014 - 9:25:57 AM
 * @Author: sparrow
 * @Project :game-context 
 * 
 *
 */
package com.sky.game.context.event;

import java.util.concurrent.ExecutorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sparrow
 *
 */
public class EventHandler <T extends EventProcessTask<?>> {
	
	private static final Log logger=LogFactory.getLog(EventHandler.class);
	//private LinkedBlockingQueue<T>  queue;
	private ExecutorService executorService;
	
	
	
	//private Thread thread;
	

	/**
	 * 
	 */
	public EventHandler() {
		// TODO Auto-generated constructor stub
		super();
		
	}
	
	
	
	public static <T extends EventProcessTask<?> > EventHandler<T> obtain(ExecutorService service){
		return new EventHandler<T>(service);
	}
	
	
	private EventHandler(ExecutorService executorService) {
		super();
		this.executorService = executorService;
//		queue=new LinkedBlockingQueue<T>();
//		thread=new Thread(this);
//		thread.start();
	}


	public  void addEvent(T t){
		if(t!=null){
			executorService.execute(t);
		}
//		logger.debug("TID.addEvent="+Thread.currentThread().getId());
//		synchronized (queue) {
//			queue.add(t);
//		}
		
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
//	public void run() {
//		// TODO Auto-generated method stub
//		for(;;){
//			try {
//				T t=queue.take();
//				logger.debug("TID.run="+Thread.currentThread().getId());
//				if(t!=null){
//					executorService.execute(t);
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//		}
//		
//	}

}
