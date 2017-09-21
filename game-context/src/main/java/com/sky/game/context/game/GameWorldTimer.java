/**
 * 
 * @Date:Nov 13, 2014 - 1:58:27 PM
 * @Author: sparrow
 * @Project :game-texas 
 * 
 *
 */
package com.sky.game.context.game;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.event.DefaultGameSession;
import com.sky.game.context.event.EventProcessTask;
import com.sky.game.context.event.IGameSession;


/**
 * @author sparrow
 *
 */

public class GameWorldTimer extends TimerTask {
	private static final Log logger=LogFactory.getLog(GameWorldTimer.class);
	
	private static final GameWorldTimer timer=new GameWorldTimer();
	
	/**
	 * 
	 * @return
	 */
	public static GameWorldTimer getGameWorldTimer(){
		return timer;
	}
	
	
	
	ConcurrentHashMap<String, IGameLife> gameLives;

	/**
	 * 
	 */
	public GameWorldTimer() {
		// TODO Auto-generated constructor stub
		gameLives=new ConcurrentHashMap<String,IGameLife>();
		Timer t=new Timer();
		
		t.scheduleAtFixedRate(this, 1000L, 100L);
	}
	
	
	
	public void addGameLifeObject(IGameLife gameLifeObject){
		if(!gameLives.containsKey(gameLifeObject.getName())){
			gameLives.put(gameLifeObject.getName(), gameLifeObject);
		}
	}
	
	
	
	public void removeGameLifeObject(IGameLife gameLife){
	if(gameLives.containsKey(gameLife.getName())){
			gameLives.remove(gameLife.getName());
	}
	
	
}

	
	public void destoryGameLife(String uri){
		IGameLife life=gameLives.get(uri);
		if(life!=null){
			life.destory();
		}
	}


	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Iterator<IGameLife> it=gameLives.values().iterator();
		while(it.hasNext()){
			final IGameLife gameLife=it.next();
			if(gameLife.isTimeout()){
				removeGameLifeObject(gameLife);
				
				//gameLives.remove(gameLife.getName());
				//
				EventProcessTask<IGameLife> task=new EventProcessTask<IGameLife>(gameLife){
					

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							if(gameLife!=null)
								gameLife.timeout();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//gameLife.destory();
					}

					
				};
				
				if(gameLife.getGameSession()==null){
					IGameSession session=new DefaultGameSession();
					session.setAttribute(IGameSession.FOCUS, Boolean.valueOf(false));
					task.setSession(session);
				}else{
					//gameLife.getGameSession().setAttribute(IGameSession.FOCUS,Boolean.valueOf(true));
					task.setSession(gameLife.getGameSession());
				}
				
				GameContextGlobals.addEventProcessTask(task);
				//gameLife.destory();
				
				
				
				
			}
			
		}
		
	}
	
	public static boolean gameLifeStage(IGameLife obj,GameLifeEnum stage){
		
		return obj!=null?(obj.getStage().equals(stage)):false;
	}

}
