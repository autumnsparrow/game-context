/**
 * @company Palm Lottery Information&Technology Co.,Ltd.
 *
 * @author  sparrow
 *
 * @date    Sep 7, 2013-6:14:52 PM
 *
 */
package com.sky.game.context.ice;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.MessageAsyncHandlerPrx;
import com.sky.game.context.MessageAsyncHandlerPrxHelper;
import com.sky.game.context.MessageHandlerPrx;
import com.sky.game.context.MessageHandlerPrxHelper;
import com.sky.game.context.MessageInternalHandlerHolder;
import com.sky.game.context.MessageInternalHandlerPrx;
import com.sky.game.context.MessageInternalHandlerPrxHelper;
import com.sky.game.context.configuration.ice.IceServiceConfig;
import com.sky.game.context.configuration.ice.IceServiceConfigRegstry;
import com.sky.game.context.util.GameUtil;











import Ice.InitializationData;
import Ice.ObjectPrx;

/**
 * 
 * Ice Proxy manager.
 * 
 * @author sparrow
 *
 */
public class IceProxyManager {
	
	
	private static final Log logger=LogFactory.getLog(IceProxyManager.class);
	Ice.Communicator communictor;
	
	String serviceName;
	
	

	
	

	/**
	 * 
	 */
	public IceProxyManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void init(){
		IceServiceConfig iceServiceConfig=IceServiceConfigRegstry.getServiceConfigByServiceName(serviceName);
		if(iceServiceConfig==null)
			throw new RuntimeException("Can't load ice config service :"+serviceName);
		InitializationData config= iceServiceConfig.getIceServerConfig(true);
	
		communictor=Ice.Util.initialize(config);
		
		// i should ping each client.
		//iceServiceConfig.ping();
	}
	
	HashMap<String,String> handlerProxies=new HashMap<String,String>();
	
	
	public synchronized Set<String> getProxyNames(){
		return handlerProxies.keySet();
	}
	
	
	
	public synchronized MessageInternalHandlerPrx getMessageInternalHandlerPrx(String name){
		MessageInternalHandlerPrx handlerPrx=null;
		try {
		
		ObjectPrx prx=communictor.propertyToProxy(name);//communictor.stringToProxy(ProtocolStoragePrx);
		
		if(prx!=null){
			
				handlerPrx=MessageInternalHandlerPrxHelper.uncheckedCast (prx);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return handlerPrx;
	}
	//private static final String ProtocolStoragePrx="ProtocolStorage.Proxy";
	
	public synchronized  MessageHandlerPrx getMessageHandlerProxy(String name){
		MessageHandlerPrx handlerPrx=null;
		try {
		
		String k=String.format("%s.MessageHandlerPrx", name);
	
		ObjectPrx prx=communictor.propertyToProxy(k);//communictor.stringToProxy(ProtocolStoragePrx);
		
		if(prx!=null){
			
				handlerPrx=MessageHandlerPrxHelper.checkedCast(prx);
				//handlerPrx.ice_ping();
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return handlerPrx;
	}
	
	
	public synchronized MessageAsyncHandlerPrx getMessageAsyncHandlerProxy(String k){
		
		//String k=String.format("%s.MessageAsyncHandlerPrx", name);
		
		
		ObjectPrx prx=communictor.propertyToProxy(k);
		MessageAsyncHandlerPrx handlerPrx=null;
		if(prx==null){
			throw new RuntimeException("Can't cast the property :"+k+" into ObjectPrx");
		}
		if(prx!=null){
			try {
				
				handlerPrx=MessageAsyncHandlerPrxHelper.uncheckedCast(prx);
			} catch (Exception e) {
				
				//logger.error("get.async.proxy:"+e.getMessage()+" k:"+k);
			}
		}
		return handlerPrx;
	}
	

	
	public void cleanup(){
		if(communictor!=null)
			communictor.destroy();
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	

}
