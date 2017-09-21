/**
 * 
 * @Date:Nov 21, 2014 - 11:32:19 AM
 * @Author: sparrow
 * @Project :game-texas 
 * 
 *
 */
package com.sky.game.context.game;

/**
 * @author sparrow
 *
 */
public interface IGameStage {
	
	public void begin();
	
	public void setStage(int stage,boolean resume);
	
}
