/**
 * 
 */
package com.sky.game.context.route;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.configuration.GameContxtConfigurationLoader;
import com.sky.game.context.util.G;
import com.sky.game.context.util.GameUtil;
import com.sky.game.context.util.WebsocketConfiguration;

/**
 * @author sparrow
 *
 */
public class RouteMetricsConfig {
	
	public static final String config="/META-INF/metrics/RouteMetrics.conf";
	
	static RouteMetricsConfig instance ;
	static{
		InputStream url=WebsocketConfiguration.class.getResourceAsStream(config);
		try {
			instance=GameContxtConfigurationLoader.loadConfiguration(url, RouteMetricsConfig.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			instance=new RouteMetricsConfig();
			instance.devices=GameUtil.getMap();
			instance.enable=false;
		}
	}
	
	Map<String,Integer> devices;
	boolean enable;
	

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Map<String, Integer> getDevices() {
		return devices;
	}

	public void setDevices(Map<String, Integer> devices) {
		this.devices = devices;
	}
	
	boolean containsKey(String device){
		return this.devices.containsKey(device);
	}
	
	public static void main(String args[]){
		RouteMetricsConfig o=G.o(RouteMetricsConfig.class);
		o.enable=true;
		o.devices=GameUtil.getMap();
		o.devices.put("ae11181f4798d03a26fbad153bbd86b4", Integer.valueOf(1));
		
		System.out.println(GameContextGlobals.getJsonConvertor().format(o));
		
	}

}
