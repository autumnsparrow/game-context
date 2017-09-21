/**
 * 
 * @Date:Nov 21, 2014 - 10:51:51 AM
 * @Author: sparrow
 * @Project :game-texas 
 * 
 *
 */
package com.sky.game.context.game;


/**
 * 
 * @author sparrow
 *
 */
public interface IGameDoOnTurn<T,S> {
	
	public T getGame();
	public CircleOrderArray<S> getSeats();
	public boolean onTurn(S seat)
			throws GameDoOnTurnException;

}
