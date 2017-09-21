/**
 * 
 */
package com.sky.game.context.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;







import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageAsyncHandlerPrx;
import com.sky.game.context.MessageInternalHandlerPrx;
import com.sky.game.context.configuration.ice.IceServiceConfig;
import com.sky.game.context.configuration.ice.IceServiceConfigLookup;
import com.sky.game.context.configuration.ice.IceServiceConfigRegstry;
import com.sky.game.context.util.G;
import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */
public class PingService {
	
	private static final Log logger=LogFactory.getLog(PingService.class);
	//private final Map<String,String> lookupTables=GameUtil.getMap();
	//private final StringBuffer buffer=new StringBuffer();
	@Scheduled(initialDelay=0,fixedRate=1000)
	public synchronized void ping(){
	
			try {
				IceServiceConfig config=IceServiceConfigRegstry.localIceServiceConfig();
				config.ping();
				
				//logger.info("\r\n"+config.getStatics()+"\r\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	@Scheduled(initialDelay=0,fixedRate=60000)
	public synchronized void show(){
	//	IceServiceConfig config=IceServiceConfigRegstry.localIceServiceConfig();
		
		try {
			IceServiceConfig config=IceServiceConfigRegstry.localIceServiceConfig();
			//config.ping();
			//logger.info("\r\n"+config.getStatics()+"\r\n");
			G.dump(config.getName()+"/ping", config.getStatics());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

}
