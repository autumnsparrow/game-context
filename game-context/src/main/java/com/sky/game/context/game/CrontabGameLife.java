/**
 * 
 */
package com.sky.game.context.game;

import java.util.Date;

import com.sky.game.context.event.IGameSession;
import com.sky.game.context.util.CronUtil;

/**
 * @author sparrow
 *
 */
public class CrontabGameLife implements IGameLife {
	
	String crontab;
	GameLifeEnum state;
	
	
	Date d;

	/**
	 * @param crontab
	 */
	public CrontabGameLife(String crontab) {
		super();
		this.crontab = crontab;
	}

	/**
	 * 
	 */
	public CrontabGameLife() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#getName()
	 */
	@Override
	public  String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#timeout()
	 */
	@Override
	public void timeout() {
		// TODO Auto-generated method stub
		state=GameLifeEnum.Timeout;
		//should create new date.
		
			

	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#isTimeout()
	 */
	@Override
	public boolean isTimeout() {
		state=GameLifeEnum.Life;
		// TODO Auto-generated methodd stub
		return  CronUtil.expired(d);
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#destory()
	 */
	@Override
	public void destory() {
		// TODO Auto-generated method stub
		// the crontab won't destroy.
		//state=GameLifeEnum.Destory;
		//log();
		//GameWorldTimer.getGameWorldTimer().removeGameLifeObject(this);

	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#getStage()
	 */
	@Override
	public GameLifeEnum getStage() {
		// TODO Auto-generated method stub
		return state;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.game.IGameLife#refresh()
	 */
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		// won't refresh
		d=CronUtil.getScheduledTime(crontab);	
		state=GameLifeEnum.Born;
		GameWorldTimer.getGameWorldTimer().addGameLifeObject(this);
	}

	
	
	public CrontabGameLife start() {
		// TODO Auto-generated method stub
		refresh();
		
		return this;
	}
	
	public String getFormatedTime(){
		return CronUtil.getFormatedDate(d);
	}
	
	public long remainTimes(){
		Date c=new Date();
		return d.getTime()-c.getTime();
	}

	IGameSession session;
	@Override
	public void setGameSession(IGameSession session) {
		// TODO Auto-generated method stub
		this.session=session;
		
	}

	@Override
	public IGameSession getGameSession() {
		// TODO Auto-generated method stub
		return session;
	}
	
	
}
