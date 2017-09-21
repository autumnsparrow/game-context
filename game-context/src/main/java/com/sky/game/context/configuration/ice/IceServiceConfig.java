/**
 * 
 */
package com.sky.game.context.configuration.ice;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.InitializationData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageAsyncHandlerPrx;
import com.sky.game.context.MessageInternalHandlerPrx;
import com.sky.game.context.message.MessageException;
import com.sky.game.context.util.G;
import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */
@JsonRootName(value = "Configuration")
public class IceServiceConfig {
	private static Log logger = LogFactory.getLog(IceServiceConfig.class);
	String name;

	String ip;
	int port;
	long timeout;
	ServiceLocators sync;
	ServiceLocators async;
	ServiceLocators remote;
	IceConfig iceConfig;
	

	@JsonIgnore
	Properties properties;
	@JsonIgnore
	boolean active;
	@JsonIgnore
	int connections;
	@JsonIgnore
	Map<String, List<IceServiceConfigLookup>> lookupTables;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		if (this.active != active) {
			logger.info(name + "@" + ip + ":" + port + " state : " + this.active + "  -- > " + active);
		}
		this.active = active;

	}

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	/**
	 * 
	 */
	public IceServiceConfig() {
		// TODO Auto-generated constructor stub
		this.timeout = 30000;
		this.lookupTables = GameUtil.getMap();
		// this.iceSerivceConfigLookups=GameUtil.getMap();
		this.connections = 0;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private static final int ADMIN_SERVER_PORT_OFFSET = 90;
	private static final int ADMIN_CLIENT_PORT_OFFSET = 91;

	// GameBlockadeObjectAdapter.Endpoints=tcp -h 127.0.0.1 -p 12000
	private Properties getServerProperties() {
		Properties o = new Properties();
		String k = String.format("%s.ObjectAdapter.Endpoints", name);
		String v = String.format("tcp -h %s -p %d", ip, port);
		o.put(k, v);
		// Ice.ThreadPool.name.Size Ice.ThreadPool.name.SizeMax
		// Ice.ThreadPool.name.SizeWarn
		if (iceConfig != null && iceConfig.serverConfig != null) {
			for (Entry<String, String> entry : iceConfig.serverConfig.entrySet()) {
				o.put(entry.getKey(), entry.getValue());
			}

		}

		// o.put("Ice.Trace.ThreadPool",String.valueOf(1));
		// o.put("Ice.Warn.Connections", String.valueOf(300));
		// o.put("Ice.ThreadPool.Server.Size", String.valueOf(1));
		// o.put("Ice.ThreadPool.Server.SizeMax", String.valueOf(300));
		// o.put("Ice.ThreadPool.Server.SizeWarn", String.valueOf(180));
		if (iceConfig != null && iceConfig.enableMetrics) {
			String adminPort = String.format("tcp -h %s -p %d", ip, port + ADMIN_SERVER_PORT_OFFSET);
			o.put("Ice.Admin.Endpoints", adminPort);
			o.put("Ice.Admin.InstanceName", name);
			o.put("IceMX.Metrics.Debug.GroupBy", "id");
			o.put("IceMX.Metrics.ByParent.GroupBy", "parent");
		}
		/**
		 * 
		 * Ice.Admin.Endpoints=tcp -h localhost -p 10002
		 * Ice.Admin.InstanceName=server IceMX.Metrics.Debug.GroupBy=id
		 * IceMX.Metrics.ByParent.GroupBy=parent
		 * 
		 * 
		 */
		// o.put("Ice.Compression.Level", String.valueOf(9));
		// o.put("Ice.Trace.Protocol", String.valueOf(1));
		// o.put("Ice.Trace.Network", String.valueOf(2));

		// o.put("Ice.ThreadPool.Server.Serialize", String.valueOf(2));

		return o;
	}

	public InitializationData getIceServerConfig(boolean client) {
		InitializationData config = new InitializationData();
		config.properties = Ice.Util.createProperties();
		StringBuffer buffer = new StringBuffer();
		buffer.append("\r\n############################################################################################################################\r\n");
		Properties pros = client ? getClientProperties() : getServerProperties();
		// sort the key.
		
		List<String> keys=GameUtil.getList();
		for(Object k:pros.keySet()){
			String kk=(String)k;
			keys.add(kk);
		}
		
		Collections.sort(keys);
		for (Object k : keys) {

			Object v = pros.get(k);
			if (v != null) {
				String key = (String) k;
				String value = (String) v;
				config.properties.setProperty(key, value);
				buffer.append(String.format("#%s=%s\r\n", key, value));
			}

		}
		
		if(pros.getProperty("Ice.Admin.Endpoints")!=null&&pros.getProperty("Ice.Admin.InstanceName")!=null){
			String metrics=String.format("# ~/Metrics.py --Endpoints=\"%s\" --InstanceName=\"%s\" dump \r\n",pros.getProperty("Ice.Admin.Endpoints"),pros.getProperty("Ice.Admin.InstanceName") );
			buffer.append(metrics);
		}
		buffer.append("\r\n############################################################################################################################\r\n");
		//./Metrics.py --Endpoints="tcp -h w1 -p 15020" --InstanceName=Websocket dump
		
		//logger.info(buffer.toString());
		
		
		G.dump(name+(client?"/client":"/server"),buffer.toString());
		
		
		return config;
	}

	public boolean loaded(String namespace) {
		return lookupTables.containsKey(namespace);
	}

	public List<IceServiceConfigLookup> lookup(String namespace) {
		// just return the active
		// List<IceServiceConfigLookup>

		return lookupTables.get(namespace);
	}

	private IceServiceConfigLookup addLookup(String namespace, String k) {
		List<IceServiceConfigLookup> lookups = null;
		if (lookupTables.containsKey(namespace)) {
			lookups = lookupTables.get(namespace);
		} else {
			lookups = GameUtil.getList();
			lookupTables.put(namespace, lookups);
		}

		IceServiceConfigLookup l = new IceServiceConfigLookup(k);
		lookups.add(l);

		return l;
	}

	private Properties getClientProperties() {
		Properties o = new Properties();
		if (sync.getLocator() != null && sync != null)
			for (ServiceLocator locator : sync.getLocator()) {
				Map<String, IceServiceConfig> services = IceServiceConfigRegstry
						.getServiceConfigByNamespace(locator.getService());

				for (IceServiceConfig config : services.values()) {
					if (config != null) {
						String k = String.format("%s.%s.MessageHandlerPrx", locator.getNs(), config.getName());
						String v = String.format("IceServantMessageHandler:tcp -h %s -p %d -t %d", config.getIp(),
								config.getPort(), config.getTimeout());
						o.put(k, v);

						IceServiceConfigLookup l = addLookup(locator.getNs(), k);
						l.conf = config;
					}
				}
			}

		if (async.getLocator() != null && async != null)
			for (ServiceLocator locator : async.getLocator()) {
				Map<String, IceServiceConfig> services = IceServiceConfigRegstry
						.getServiceConfigByNamespace(locator.getService());

				for (IceServiceConfig config : services.values()) {
					if (config != null) {
						String k = String.format("%s.%s.MessageAsyncHandlerPrx", locator.getNs(), config.getName());
						String v = String.format("IceServantAsyncMessageHandler:tcp -h %s -p %d -t %d", config.getIp(),
								config.getPort(), config.getTimeout());
						o.put(k, v);
						IceServiceConfigLookup l = addLookup(locator.getNs(), k);
						l.conf = config;
					}
				}

			}

		if (remote.getLocator() != null && remote != null)
			for (ServiceLocator locator : remote.getLocator()) {
				Map<String, IceServiceConfig> services = IceServiceConfigRegstry
						.getServiceConfigByNamespace(locator.getService());

				for (IceServiceConfig config : services.values()) {
					if (config != null) {
						String k = String.format("%s.%s.MessageInternalHandlerPrx", locator.getNs(), config.getName());
						String v = String.format("IceServantMessageInternalHandler:tcp -h %s -p %d -t %d",
								config.getIp(), config.getPort(), config.getTimeout());
						o.put(k, v);
						IceServiceConfigLookup l = addLookup(locator.getNs(), k);
						l.conf = config;
					}
				}

			}

		if (iceConfig != null && iceConfig.clientConfig != null) {
			for (Entry<String, String> entry : iceConfig.clientConfig.entrySet()) {
				o.put(entry.getKey(), entry.getValue());
			}

		}
		
		if (iceConfig != null && iceConfig.enableMetrics) {
			String adminPort = String.format("tcp -h %s -p %d", ip, port + ADMIN_CLIENT_PORT_OFFSET);
			o.put("Ice.Admin.Endpoints", adminPort);
			o.put("Ice.Admin.InstanceName", name + "-client");
			o.put("IceMX.Metrics.Debug.GroupBy", "id");
			o.put("IceMX.Metrics.ByParent.GroupBy", "parent");
		}
		properties = o;

		return o;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServiceLocators getSync() {
		return sync;
	}

	public void setSync(ServiceLocators sync) {
		this.sync = sync;
	}

	public ServiceLocators getAsync() {
		return async;
	}

	public void setAsync(ServiceLocators async) {
		this.async = async;
	}

	public ServiceLocators getRemote() {
		return remote;
	}

	public void setRemote(ServiceLocators remote) {
		this.remote = remote;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getStatics() {

		return statics==null?"":statics;
	}
	
	private String statics;
	private final StringBuffer pingBuffer = new StringBuffer();
	private final Map<String, IceServiceConfigLookup> pingTables = GameUtil.getMap();

	// ping which node is active.
	public synchronized void ping() {
		Properties props = properties;
		if (props == null)
			return;
		Iterator<Object> it = props.keySet().iterator();
		if (pingBuffer.length() > 0)
			pingBuffer.delete(0, pingBuffer.length() - 1);
		pingBuffer.append("\r\n");
		pingBuffer.append(name).append("\r\n");
		pingBuffer.append(String.format("%-95s\t%-10s\t%-5s\r\n", IceServiceConfigLookup.getTitle(), "status",
				"connections")+"\r\n");
		
		// choose the ping tables
		// pingTables.clear();
		if (pingTables.size() == 0) {
			while (it.hasNext()) {
				String k = (String) it.next();
				if (k.startsWith("Ice")) {
					continue;
				}

				IceServiceConfigLookup lookUp = IceServiceConfigLookup.lookups.get(k);

				pingTables.put(k, lookUp);

			}
		}
		List<String> keys=GameUtil.getList();
		for(String kk:pingTables.keySet()){
			keys.add(kk);
		}
		Collections.sort(keys);
	//	for (IceServiceConfigLookup lookUp : pingTables.values()){
		for (String kk : keys) {
			IceServiceConfigLookup lookUp=pingTables.get(kk);
			// IceServiceConfig conf=lookUp.conf;
			try {
				lookUp.ping();
			} finally {
				String status = String.format("%-95s\t%-10s\t%05d\r\n", lookUp.toString(), lookUp.isAlive() ? "active" : "dead",
						lookUp.connections);
				pingBuffer.append(status);
				
			}
		}

		statics=pingBuffer.toString();
	}

	@Override
	public String toString() {
		return "IceServiceConfig[name=" + name + ", active=" + active + ", connections=" + connections + "]";
	}
	
	public IceConfig getIceConfig() {
		return iceConfig;
	}

	public void setIceConfig(IceConfig iceConfig) {
		this.iceConfig = iceConfig;
	}
}
