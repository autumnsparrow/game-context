/**
 * @sparrow
 * @5:17:32 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.id;

/**
 * @author sparrow
 *
 */
public enum LLIdTypes {
	
	
	IdTypeRoom(1),
	IdTypeDeck(2),
	IdTypeGame(3),
	IdTypeGameLife(4),
	IdTypeMinaEvent(5),
	IdTypeTeam(6),
	IdTypeBTLevel(7),
	IdTypeUser(8);
	
	
	public int type;

	private LLIdTypes(int type) {
		this.type = type;
	}
	
	
	

}
