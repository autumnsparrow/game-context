/**
 * @sparrow
 * @Jan 7, 2015   @2:57:33 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.event.EventHandler;
import com.sky.game.context.event.EventProcessTask;
import com.sky.game.context.lock.IObtain;
import com.sky.game.context.lock.VolatileLocking;

/**
 * @author sparrow
 *
 */
public class GameEventHandler {

	//EventHandler<EventProcessTask<GameEvent>> evntHandler;

	public static final GameEventHandler handler = new GameEventHandler();
	
	VolatileLocking<EventHandler<EventProcessTask<GameEvent>>> vlEventHandler;
	/**
	 * @param t
	 */
	public GameEventHandler() {

		// TODO Auto-generated constructor stub
		vlEventHandler=new VolatileLocking<EventHandler<EventProcessTask<GameEvent>>>();
	}

	
	
	private EventHandler<EventProcessTask<GameEvent>>  getEventHandler(){
		return vlEventHandler.getHelper(new IObtain<EventHandler<EventProcessTask<GameEvent>>, Object>() {

			@Override
			public EventHandler<EventProcessTask<GameEvent>> obtain(Object a) {
				// TODO Auto-generated method stub
				return EventHandler.obtain(GameContextGlobals.getExecutorUnOrdered());
			}
		}, null);
	}
	ConcurrentHashMap<String, HashMap<String, IGameEventObserver>> observers = new ConcurrentHashMap<String, HashMap<String, IGameEventObserver>>();

	public void registerObserver(String evtName, IGameEventObserver observer) {
		HashMap<String, IGameEventObserver> evtObserver = observers
				.get(evtName);
		if (evtObserver == null) {
			evtObserver = new HashMap<String, IGameEventObserver>();
			observers.put(evtName, evtObserver);
		}

		evtObserver.put(observer.getUri(), observer);
	}

	public void unRegisterObserver(String evtName, IGameEventObserver observer) {
		HashMap<String, IGameEventObserver> evtObserver = observers
				.get(evtName);
		if (evtObserver != null) {
			evtObserver.remove(observer.getUri());
		}

	}

	private Object lock=new Object();
	public void broadcast(final GameEvent evt) {
		
		
		
		getEventHandler().addEvent(new EventProcessTask<GameEvent>(evt) {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					HashMap<String, IGameEventObserver> evtObserver = observers
							.get(evt.name);
					if (evtObserver != null) {
						Collection<IGameEventObserver> collections = evtObserver
								.values();
						for (IGameEventObserver o : collections) {

							try {

								o.observer(evt);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

}
