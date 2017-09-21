/**
 * 
 * @Date:Nov 21, 2014 - 10:50:28 AM
 * @Author: sparrow
 * @Project :game-texas 
 * 
 *
 */
package com.sky.game.context.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author sparrow
 *
 */
public class GameOnTurnManager {
	
	private static final Log logger=LogFactory.getLog(GameOnTurnManager.class);
	private static final GameOnTurnManager ON_TURN_MANAGER=new GameOnTurnManager();
	
	public static GameOnTurnManager obtain(){
		return new GameOnTurnManager();
	}

	/**
	 * 
	 */
	public GameOnTurnManager() {
		// TODO Auto-generated constructor stub
	}
	
	public <T,S> void turn(IGameDoOnTurn<T,S> doOnTurn,boolean resume){
		if(resume)
			doOnTurnResume(doOnTurn);
		else
			doOnTurn(doOnTurn);
	}
	
	/**
	 * begin on turn 
	 * 
	 * 
	 * @param doOnTurn
	 */
	public synchronized <T,S>  void doOnTurn(IGameDoOnTurn<T,S> doOnTurn){
		CircleOrderArray<S> seats = doOnTurn.getSeats().clone();
		seats.reset();
		// GameTexasPlayer player=null;
		if(seats==null)
			return;
		S seat=seats.currentHolder();
		logger.debug("doOnTurn:"+seats.toString());
		do {
			// player=seat.getPlayer();
			try {
				if (!doOnTurn.onTurn(seat))
					break;
			} catch (GameDoOnTurnException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			seat=seats.moveNext();
		} while (!seats.isMoveAroud());
	}
	
	/**
	 * continue on turn.
	 * 
	 * @param doOnTurn
	 */
	public  synchronized <T,S> void doOnTurnResume(IGameDoOnTurn<T,S> doOnTurn){
		
		CircleOrderArray<S> seats = doOnTurn.getSeats();
		if(seats==null)
			return;
		// GameTexasPlayer player=null;
		S seat=seats.currentMover();
	//	logger.info("doOnTurnResume:"+seats.toString());
		do {
			// player=seat.getPlayer();
			try {
				if (!doOnTurn.onTurn(seat)){
					// the next time should be next elements.
					seat=seats.moveNext();
		//			logger.info("after.doOnTurnResume:"+seats.toString());
					break;
				}
			} catch (GameDoOnTurnException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			seat=seats.moveNext();
		} while (!seats.isMoveAroud());
		
	}

}
