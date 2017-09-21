/**
 * 
 */
package com.sky.game.context.event;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.annotation.introspector.EventHandlerClass;
import com.sky.game.context.annotation.introspector.IIdentifiedObject;
import com.sky.game.context.domain.ErrorBeans;
import com.sky.game.context.domain.ErrorBeans.Request;
import com.sky.game.context.lock.IObtain;
import com.sky.game.context.lock.VolatileLocking;
import com.sky.game.context.spring.RemoteServiceException;

/**
 * @author Administrator
 *
 */

public class WebsocketEventHandler {
	private static Log logger = LogFactory.getLog(WebsocketEventHandler.class);

	private static WebsocketEventHandler handler = new WebsocketEventHandler();

	public static WebsocketEventHandler getHandler() {
		return handler;
	}

	public static void addRecieveMinEvent(WebsocketEvent evt) {
		handler.addRecievedEvent(evt);
	}

	// static LinkedBlockingQueue<MinaEvent> minaEventQueue;

	// static LinkedBlockingQueue<MinaEvent> minaReceivedQueue;

	// static LinkedList<IMinaRecieveObserver> minaRecivedObservers;

	// private final static int MAX_THREADS=5;
	/**
	 * 
	 */
	public WebsocketEventHandler() {
		// TODO Auto-generated constructor stub
		super();
		init();

	}

	VolatileLocking<ExecutorService> vlExecutorService;

	public void init() {

		vlExecutorService = new VolatileLocking<ExecutorService>();

	}

	private ExecutorService getExecutorService() {
		return vlExecutorService.getHelper(
				new IObtain<ExecutorService, Object>() {

					public ExecutorService obtain(Object o) {
						// TODO Auto-generated method stub

						return GameContextGlobals.getExecutorUnOrdered();
					}
				}, null);
	}

	// static ExecutorService executorService;

	private static class WebsocketRecievedTask implements Runnable {

		WebsocketEvent minaEvent;

		public WebsocketRecievedTask(WebsocketEvent minaEvent) {
			super();

			this.minaEvent = minaEvent;
			this.minaEvent.header.m(WebsocketRecievedTask.class.getSimpleName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			// TODO Auto-generated method stub
			try {
				
				this.minaEvent.header.e(WebsocketRecievedTask.class.getSimpleName());
				this.minaEvent.header.metrics();
				this.minaEvent.header.m(WebsocketRecievedTask.class.getSimpleName()+"_run");
				// checking if the protocol is ping
				//logger.info("handle:"+this.minaEvent.transcode);
				// don't need the register event handler annotation anymore.
				EventHandlerClass eventHandleClass = GameContextGlobals
						.getEventHandlerClass(this.minaEvent.transcode);
				if (eventHandleClass == null || this.minaEvent == null)
					return;
				IIdentifiedObject obj = eventHandleClass
						.getObject(this.minaEvent.getId());
				if (obj == null) {
					obj = eventHandleClass
							.getObject(EventHandlerClass.DEFAULT_ID);
					//logger.info("EventHandlerClass Manager");
				}

				if (obj != null) {
					if (obj instanceof IIdentifiedObject) {

						eventHandleClass.handle(obj, this.minaEvent);
						
						
					} else {
						logger.info("ERRROR -object not instance of IIdentifiedObject :"
								+ obj.getClass());
					}
				} else {

					throw new RuntimeException("transcode:"
							+ this.minaEvent.transcode + " obj:"
							+ minaEvent.getId() + " userId:"
							+ this.minaEvent.header.toString()
							+ " can't found the event handler instance.");
				}

				// this.observer.onRecievedMinaEvent(minaEvent);
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				// WebsocketEventException we=new WebsocketEventException();
				e.printStackTrace();
				//logger.error("Websocket:" + );
				Request obj = null;
				String transcode = this.minaEvent
						.protocolRetranscodeMapEntryByRequest();

				if ((e instanceof InvocationTargetException)) {
					InvocationTargetException target = (InvocationTargetException) e;
					Throwable targetException=target.getTargetException();
					if (targetException instanceof RemoteServiceException) {

						RemoteServiceException re = (RemoteServiceException) targetException;
						obj = ErrorBeans.Request.get(re.status, transcode,
								re.getMessage());
					} else if (targetException instanceof LocalServiceException) {
						LocalServiceException re = (LocalServiceException) 
								targetException;
						obj = ErrorBeans.Request.get(re.state, transcode,
								re.getMessage());
					}else{
						
						//
						targetException.printStackTrace();
						obj = ErrorBeans.Request.get(-1, transcode,
								" system internal error");
					}
				} else {

					obj = ErrorBeans.Request.get(-1, transcode, e.getMessage());
				}
				// evt.sendMessage();

				// WebsocketEvent evt=WebsocketEvent.o(this.minaEvent.header,
				// obj, false);//new WebsocketEvent();
				if(obj!=null){
					WebsocketEvent.send(this.minaEvent.header, obj, false);
				}

			}

		}

	}

	public void addRecievedEvent(WebsocketEvent evt) {
		try {

			if (evt != null) {
				getExecutorService().execute(new WebsocketRecievedTask(evt));
			}
			// minaReceivedQueue.put(evt);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
