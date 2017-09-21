/**
 * @company Palm Lottery Information&Technology Co.,Ltd.
 *
 * @author  sparrow
 *
 * @date    Sep 9, 2013-9:18:00 AM
 *
 */
package com.sky.game.context.ice;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.configuration.ice.IceServiceConfig;
import com.sky.game.context.configuration.ice.IceServiceConfigRegstry;





import com.sky.game.context.event.GameEvent;
import com.sky.game.context.event.GameEventHandler;
import com.sky.game.context.util.G;

import Ice.InitializationData;
import Ice.ObjectAdapter;

/**
 * 
 * Ice Servants manager
 * 
 * @author sparrow
 *
 */
public class IceServerManager {
	
	
	
	
	private static final Log logger=LogFactory.getLog(IceServerManager.class);
	



	Ice.Communicator communictor;
	
	String serviceName;
	
	
	
	
	String objectAdapter;
	
	

	/**
	 * @return the objectAdapter
	 */
	public String getObjectAdapter() {
		return objectAdapter;
	}

	
	
	List<Ice.Object> servants;
	

	
	/**
	 * @return the servants
	 */
	public List<Ice.Object> getServants() {
		return servants;
	}

	/**
	 * @param servants the servants to set
	 */
	public void setServants(List<Ice.Object> servants) {
		this.servants = servants;
	}

	/**
	 * 
	 */
	public IceServerManager() {
	}
	
	public void init(){
		
		IceServiceConfig iceServiceConfig=IceServiceConfigRegstry.getServiceConfigByServiceName(serviceName);
		this.objectAdapter=String.format("%s.ObjectAdapter", serviceName);//serviceName;
		InitializationData config=iceServiceConfig.getIceServerConfig(false);
		communictor=Ice.Util.initialize(config);
	}
	
	public void cleanup(){
		if(communictor!=null)
			communictor.destroy();
	}
	
	

	
	public void start(){
		init();
		ObjectAdapter adapter=communictor.createObjectAdapter(this.objectAdapter);
		StringBuffer buffer=new StringBuffer();
		buffer.append("\r\n");
		for(Ice.Object servant:servants){
			String identity=servant.getClass().getSimpleName();
			adapter.add(servant, communictor.stringToIdentity(identity));
			buffer.append("#"+identity+"@"+this.objectAdapter);
			buffer.append("\r\n");
		}
		//logger.info(buffer.toString());
		G.dump(serviceName+"/servant", buffer.toString());
		//adapter.add(getServant(), communictor.stringToIdentity(servant.getClass().getSimpleName()));
		
		adapter.activate();
		logger.info("IceServerManager Started!");
		GameEventHandler.handler.broadcast(new GameEvent(GameEvent.EVENT_ICE_SERVER_ACTIVED, "OK"));;
		communictor.waitForShutdown();
		
		cleanup();
		
		
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
	

}
