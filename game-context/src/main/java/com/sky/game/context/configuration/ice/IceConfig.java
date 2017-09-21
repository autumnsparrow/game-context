package com.sky.game.context.configuration.ice;

import java.util.Map;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.util.G;
import com.sky.game.context.util.GameUtil;

public class IceConfig {
	
	boolean enableMetrics;
	Map<String,String> serverConfig;
	Map<String,String> clientConfig;
	public boolean isEnableMetrics() {
		return enableMetrics;
	}
	public void setEnableMetrics(boolean enableMetrics) {
		this.enableMetrics = enableMetrics;
	}
	public Map<String, String> getServerConfig() {
		return serverConfig;
	}
	public void setServerConfig(Map<String, String> serverConfig) {
		this.serverConfig = serverConfig;
	}
	public Map<String, String> getClientConfig() {
		return clientConfig;
	}
	public void setClientConfig(Map<String, String> clientConfig) {
		this.clientConfig = clientConfig;
	}
	
	public static void main(String args[]){
		
		IceConfig conf=G.o(IceConfig.class);
		conf.setEnableMetrics(true);
		Map<String,String> o=GameUtil.getMap();
		o.put("Ice.Trace.ThreadPool",String.valueOf(1));
		o.put("Ice.Warn.Connections", String.valueOf(300));
		o.put("Ice.ThreadPool.Server.Size", String.valueOf(1));
		o.put("Ice.ThreadPool.Server.SizeMax", String.valueOf(300));
		o.put("Ice.ThreadPool.Server.SizeWarn", String.valueOf(180));
		conf.setServerConfig(o);
		String xml=GameContextGlobals.getXmlJsonConvertor().format(conf);
		
		IceConfig conf2=GameContextGlobals.getXmlJsonConvertor().convert(xml, IceConfig.class);
		System.out.println(xml);
		
		
	}

}
