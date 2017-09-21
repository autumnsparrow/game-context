/**
 * @sparrow
 * @8:34:44 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.event.IGameSession;
import com.sky.game.context.game.GameLifeEnum;
import com.sky.game.context.game.GameWorldTimer;
import com.sky.game.context.game.IGameLife;

/**
 * @author sparrow
 *
 */
public abstract class AbstractGameLife implements IGameLife {
	
	private static Log logger =LogFactory.getLog(AbstractGameLife.class);
	
	protected GameLifeEnum state;
	long lastActive;
	long life;
	// 1 hour.
	public static final int DEFAULT_GAME_LIFE=1*60*60*1000;
	public static final int MIN=60*1000;
	public static final int HOUR=60*MIN;
	
	public static final int DAY=24*HOUR;
	
	
	IGameSession session;
	

	/**
	 * 
	 */
	public AbstractGameLife() {
		// TODO Auto-generated constructor stub
		life=DEFAULT_GAME_LIFE;
		init();
	}
	
	protected void init(){
		state=GameLifeEnum.Born;
		lastActive=System.currentTimeMillis();
		GameWorldTimer.getGameWorldTimer().addGameLifeObject(this);
		log();
	}
	
	public AbstractGameLife(long life) {
		// TODO Auto-generated constructor stub
		//life=DEFAULT_GAME_LIFE;
		this.life=life;
	}
	
	public AbstractGameLife start(){
		init();
		return this;
	}
	

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#getName()
	 */
	public abstract String getName() ;

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#timeout()
	 */
	public void timeout() {
		// TODO Auto-generated method stub
		state=GameLifeEnum.Timeout;
		log();
		
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#isTimeout()
	 */
	public boolean isTimeout() {
		// TODO Auto-generated method stub
		state=GameLifeEnum.Life;
		return System.currentTimeMillis()-lastActive>life;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#destory()
	 */
	public void destory() {
		// TODO Auto-generated method stub
		state=GameLifeEnum.Destory;
		log();
		GameWorldTimer.getGameWorldTimer().removeGameLifeObject(this);

	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#getStage()
	 */
	public GameLifeEnum getStage() {
		// TODO Auto-generated method stub
		return state;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		lastActive=System.currentTimeMillis();
	}
	
	private void log(){
		//logger.info(toString());
	}

	@Override
	public String toString() {
		return "AbstractGameLife@"+getName()+" [state=" + state + ", lastActive="
				+ lastActive + ", life=" + life + "]";
	}

	@Override
	public void setGameSession(IGameSession session) {
		// TODO Auto-generated method stub
		this.session=session;
	}

	@Override
	public IGameSession getGameSession() {
		// TODO Auto-generated method stub
		return this.session;
	}

	
	
}

