/**
 * 
 * @Date:Nov 13, 2014 - 2:00:08 PM
 * @Author: sparrow
 * @Project :game-texas 
 * 
 *
 */
package com.sky.game.context.game;

import com.sky.game.context.event.IGameSession;

/**
 * 
 * GameLife :
 * 
 * 	born
 *  life
 * 	die.
 * 
 * @author sparrow
 *
 */
public interface IGameLife {
	
	/**
	 * 
	 * @return
	 */
	public String getName();
	
	
	/**
	 * 
	 * @param life
	 */
	
	
	
	/**
	 * 
	 * 
	 */
	public void timeout();
	
	/**
	 * 
	 * @return if the life end!
	 */
	public boolean isTimeout();
	
	
	public void destory();
	
	
	public GameLifeEnum getStage();
	
	
	public void refresh();
	
	
	public void setGameSession(IGameSession session);
	public IGameSession getGameSession();
	
	
	

}
