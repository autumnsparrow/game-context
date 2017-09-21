/**
 * 
 */
package com.sky.game.context.service;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;













import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.event.GameEvent;
import com.sky.game.context.event.GameEventHandler;
import com.sky.game.context.event.IGameEventObserver;
import com.sky.game.context.message.impl.ice.IceServantAsyncMessageHandler;
import com.sky.game.context.message.impl.ice.IceServantMessageHandler;
import com.sky.game.context.message.impl.ice.IceServantMessageInternalHandler;
import com.sky.game.context.util.GameUtil;

/**
 * @author Administrator
 *
 */
@Service
public class ServerStarupService implements IGameEventObserver {
	
	//"/META-INF/game-context.conf"
	
	String configuration;
	
	IServerStarupLifeCycle lifeCycle;
	
	public IServerStarupLifeCycle getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(IServerStarupLifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public String getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public ServerStarupService() {
		super();
		// TODO Auto-generated constructor stub
		
	}

	@Autowired
	public ServerStarupService(@Value("${game.context}")String configuration) {
		super();
		this.configuration = configuration;
		//init();
	}
	@Deprecated
	public void loadClient(){
		  new Thread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					  GameContextGlobals.loadClient(ServerStarupService.class.getResource(configuration));// 初始化 MessageHandlerPrx （ice 接口）
				      
				}
			}).start();
	}
	
	/**
	 * {@link IServerStarupLifeCycle#beforeStartup()}
	 * load()
	 * {@link IServerStarupLifeCycle#middleOfStartup()}
	 * start()
	 * {@link IServerStarupLifeCycle#afterStartup()}
	 * 
	 * 
	 */
	public void load(){
		  try {
			  GameContextGlobals.init(GameUtil.getInputStream(configuration));
			  List<Ice.Object> servants=new LinkedList<Ice.Object>();
		      servants.add(new IceServantMessageHandler());//添加ice 对象IceServantMessageHandler这里继承了_MessageHandlerDisp实现了它的invoke 放法
		      servants.add(new IceServantAsyncMessageHandler());
		      servants.add(new IceServantMessageInternalHandler());
		      GameContextGlobals.getIceServerManager().setServants(servants);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 初始化 MessageHandlerPrx （ice 接口）
	     
	}
	
	/**
	 * 
	 */
	public void start(){
		 GameContextGlobals.getIceServerManager().start();
	}

	public void startup(){
		 load();
		 start();
	}
	
	private Thread t;
	@SuppressWarnings("unused")
	public void init(){
		GameEventHandler.handler.registerObserver(GameEvent.EVENT_ICE_SERVER_ACTIVED, this);
	      t=new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				
				 try {
					if(lifeCycle!=null){
						  lifeCycle.beforeStartup();
					  }
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				  try {
					 
					 load();
					  
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
				  try {
						if(lifeCycle!=null){
							  lifeCycle.middleOfStartup();
						  }
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				  
				
				  try {
					start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
				  // service never reach that in this thread.
				  
				
				 
			}
		});
	     t.start();
	      
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return GameEvent.EVENT_ICE_SERVER_ACTIVED;
	}

	@Override
	public void observer(GameEvent evt) {
		// TODO Auto-generated method stub
		if(evt.isEvent(GameEvent.EVENT_ICE_SERVER_ACTIVED)){
			  try {
					if(lifeCycle!=null){
						  lifeCycle.afterStartup();
					  }
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
		
	}
	
	
	

}
