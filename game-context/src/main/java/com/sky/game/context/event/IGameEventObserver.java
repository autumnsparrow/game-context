/**
 * @sparrow
 * @Jan 7, 2015   @2:58:55 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.event;

/**
 * @author sparrow
 *
 */
public interface IGameEventObserver {
	
	public String getUri();
	public void observer(GameEvent evt);
	

}
