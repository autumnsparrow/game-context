/**
 * 
 */
package com.sky.game.context.configuration.ice;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageInternalBean;
import com.sky.game.context.MessageInternalHandlerPrx;
import com.sky.game.context.message.MessageException;
import com.sky.game.context.util.GameUtil;


/**
 * @author sparrow
 *
 */
public class IceServiceConfigRegstry {
	
	private static final Log logger=LogFactory.getLog(IceServiceConfigRegstry.class);

	/**
	 * 
	 */
	public IceServiceConfigRegstry() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * Map<FishUser,Map<FishUser-1,IceSerivceConfig>>
	 * 
	 * global ice service configuration.
	 */
	public static Map<String,Map<String,IceServiceConfig>> configMap=new HashMap<String,Map<String,IceServiceConfig>>();
	
	
	/**
	 * get the ice service config of service name.
	 * @param serviceName
	 * @return
	 */
	public static IceServiceConfig getServiceConfigByServiceName(String serviceName){
		String service=serviceName.split("-")[0]; // namespace.
		Map<String,IceServiceConfig> services=configMap.get(service);
		IceServiceConfig conf=null;
		if(services!=null){
			conf=services.get(serviceName);
		}
		
		return conf;
		
	}
	
	/**
	 * get collection of ice service by namespace.
	 * @param namespace
	 * @return
	 */
	public static Map<String,IceServiceConfig> getServiceConfigByNamespace(String namespace){
		Map<String,IceServiceConfig> services=configMap.get(namespace);
		return services;
	}
	
	
	 	/**
	 * locate the proxy IceServiceConfig by the namespace and serivce .
	 * @param namespace
	 * @param service
	 * @return
	 */
	public static  IceServiceConfigLookup locateLooup(String namespace,String service){
	
		String lookupKey=String.format("%s.%s", namespace,service);
		IceServiceConfigLookup lookUp=IceServiceConfigLookup.locateByLookup(lookupKey);
		if(lookUp!=null){
			return lookUp;
		}
		
		
		IceServiceConfig conf=localIceServiceConfig();
		List<IceServiceConfigLookup> lookup=conf.lookup(namespace);
		IceServiceConfigLookup look=null;
		if(lookup!=null)
		for(IceServiceConfigLookup l:lookup){
			if(l.getService().equals(service)){
				look=l;
				break;
			}
		}
		if(look!=null){
			look.cacheByLookup();
		}
		
		return look;
		
	}
	
	public static  IceServiceConfig localIceServiceConfig(){
		String serviceName=GameContextGlobals.getConfig().getServiceName();
		IceServiceConfig conf=getServiceConfigByServiceName(serviceName);
		return conf;
		
	}
	
	static AtomicInteger invoked=new AtomicInteger(9);
	static AtomicLong duration=new AtomicLong(0);
	public static  List<IceServiceConfigLookup> locate(String namespace){
		
		

		// local ice serice config
		
		IceServiceConfig conf=localIceServiceConfig();
		
	//	long b=System.nanoTime();
		List<IceServiceConfigLookup> lookup=conf.lookup(namespace);
		
		List<IceServiceConfigLookup> activeLookup=GameUtil.getList();
		for(IceServiceConfigLookup iceServiceConfig:lookup){
			
			if(iceServiceConfig.isAlive()){
				activeLookup.add(iceServiceConfig);
			}
		}
		
	
	
		
		Collections.sort(activeLookup,new Comparator<IceServiceConfigLookup>() {

			@Override
			public int compare(IceServiceConfigLookup o1,
					IceServiceConfigLookup o2) {
				// TODO Auto-generated method stub
				return o1.connections-o2.connections;
				
			}
		});
		
		
//		invoked.incrementAndGet();
//		duration.addAndGet((System.nanoTime()-b));
//		logger.info(Thread.currentThread().getId() +" ===  invoked: "+invoked+"  delta:"+duration.longValue()+" average:"+(duration.longValue()/invoked.intValue()));
//		
		
		// find a service.
		// find the node active.
		
		return activeLookup;
	}
	
	
	public static  IceServiceConfigLookup locateLookup(String namespace){
		
		
		
		List<IceServiceConfigLookup> activeLookup=locate(namespace);
		
		// find a service.
		// find the node active.
		
		IceServiceConfigLookup l=activeLookup.size()>0?activeLookup.get(0):null;
		if(l!=null){
			l.conf.connections++;
		}
		
		return l;
	}
	
	

	
	
	
	
	

}
