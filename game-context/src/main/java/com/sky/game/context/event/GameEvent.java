/**
 * @sparrow
 * @Jan 7, 2015   @2:55:53 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.event;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.configuration.ice.IceServiceConfigLookup;
import com.sky.game.context.domain.MinaBeans;
import com.sky.game.context.util.G;

/**
 * @author sparrow
 *
 */
public class GameEvent {
	
	public static final String GAME_END="GameEndEvent";
	
	public static final String POKER_TYPE_EVENT="PokerTypeEvent";
	
	public static final String DECK_GAME_END="GameDeckEnd";
	public static final String WINNER_IN_ET="WinnerInElimination";
	public static final String WINNER_IN_BT="WinnerInBlockade";
	public static final String HALF_FINAL_IN_ET="HalfFinalInET";
	
	
	public static final String GAME_SYSTEM_MESSAGE="GameSystemMessage";
	public static final String GAME_PROTOCOL_MESSAGE="GameProtocolMessage";
	public static final String NETWORK_EVENT="network_event";
	public static final String ROOM_EVENT="roomEvent";
	
	public static final String EVENT_ICE_SERVER_ACTIVED="IceServerActived";
	public static final String EVENT_WEBSOCKET_RECEIVE_DATA="WebsocketRecieveData";
	
	public static final String EVENT_USER_LOADED="UserLoaded";
	public static final String EVENT_USER_UNLOADED="UserUnLoaded";
	
	
	
	// event name
	public String name;
	// event parameters
	public Object obj;
	
	public String clz;
	
	public static GameEvent obtain(String name, Object obj){
		return new GameEvent(name, obj);
	}

	public boolean isEvent(String evt){
		return evt.equals(name);
	}
	
	/**
	 * @param name
	 * @param obj
	 */
	public GameEvent(String name, Object obj) {
		super();
		this.name = name;
		this.obj = obj;
	}


	
	
	public GameEvent(String name, Object obj, String clz) {
		super();
		this.name = name;
		this.obj = obj;
		this.clz = clz;
	}

	/**
	 * 
	 */
	public GameEvent() {
		// TODO Auto-generated constructor stub
	}
	
	
//	public static void main(String args[]){
//		MinaBeans.Extra extra=G.o(MinaBeans.Extra.class);
//		extra.setConnectionId("xxxx");
//		extra.setDeviceId("xxxx");
//		extra.setFrom("we");
//		extra.setTo("lanndon");
//		extra.setId(Long.valueOf(100));
//		
//		GameEvent evt=G.o(GameEvent.class);
//		evt.name="mina";
//		evt.obj=extra;
//		String content=GameContextGlobals.getJsonConvertor().format(evt);
//		
//		GameEvent e=GameContextGlobals.getJsonConvertor().convert(content, GameEvent.class);
//		System.out.println(content);
//	}

}
