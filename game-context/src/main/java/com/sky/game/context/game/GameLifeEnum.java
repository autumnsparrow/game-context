/**
 * 
 * @Date:Nov 26, 2014 - 4:41:28 PM
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
public enum GameLifeEnum {
	Born,
	Life,
	Timeout,
	Destory;
	
	public boolean equals(GameLifeEnum stage){
		return this.ordinal()==stage.ordinal();
	}

}
