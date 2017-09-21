/**
 * 
 */
package com.sky.game.context.configuration.ice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageAsyncHandlerPrx;
import com.sky.game.context.MessageInternalHandlerPrx;
import com.sky.game.context.message.MessageException;
import com.sky.game.context.util.GameUtil;


/**
 * @author sparrow
 *
 */
public class IceServiceConfigLookup {

	private static final Log logger = LogFactory.getLog(IceServiceConfigLookup.class);
	// ice properties key to get lookup
	final static Map<String, IceServiceConfigLookup> lookups = GameUtil.getMap();

	// for the proxy(client)
	// ice service name to get lookup
	final static Map<String, IceServiceConfigLookup> serviceTable = GameUtil.getMap();
	// ice namespace to get lookup.
	static final ConcurrentHashMap<String, IceServiceConfigLookup> namespaceLookups = new ConcurrentHashMap<String, IceServiceConfigLookup>();
	// ice name cache by name.service
	static final ConcurrentHashMap<String, IceServiceConfigLookup> lookupLookups = new ConcurrentHashMap<String, IceServiceConfigLookup>();

	String namespace;
	String service; //
	String proxy; // Internal,AsyncIceProxy
	String lookup;
	String k;
	IceServiceConfig conf;

	boolean active;
	public  int connections;

	public IceServiceConfigLookup(String k) {
		this.k = k;

		String[] items = k.split("\\.");
		service = items[items.length - 2];
		proxy = items[items.length - 1];
		namespace = k.substring(0, k.indexOf(service) - 1);
		lookup = String.format("%s.%s", namespace, service);
		lookups.put(k, this);

	}

	public String getService() {
		return service;
	}

	
	private void pingpong(){
		GameContextGlobals.postTask(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ping();
			}
		});
		
		
	}
	
	
	public   boolean isAlive() {
		
		
		return active;
	}

	public boolean isAsync() {
		return proxy.equals(MessageAsyncHandlerPrx.class.getSimpleName());
	}

	public boolean isInternal() {
		return proxy.equals(MessageInternalHandlerPrx.class.getSimpleName());
	}

	public MessageAsyncHandlerPrx locatePrx() {
		MessageAsyncHandlerPrx prx = GameContextGlobals.getIceProxyManager().getMessageAsyncHandlerProxy(k);
		return prx;
	}

	public MessageInternalHandlerPrx locateInternalPrx() {
		MessageInternalHandlerPrx prx = GameContextGlobals.getIceProxyManager().getMessageInternalHandlerPrx(k);
		return prx;
	}

	long b;
	long p;
	
	private static ConcurrentHashMap<String, Boolean> pingQueue=new ConcurrentHashMap<String,Boolean>();
	
	// if the dead check the ping with interval chanages.
	// 100 500 1000 5000 10000 60000
	
	private static final long[] intervals={1000,3000};
	private AtomicInteger counter=new AtomicInteger(0);
	private synchronized long getInterval(){
		counter.incrementAndGet();
		if(counter.intValue()>=intervals.length){
			counter.set(0);
		}
		return intervals[counter.intValue()];
	}
	
	public void ping() {
		if(active){
			if(System.currentTimeMillis()-p<60000){
				// using the task 
				return;
			}
		}else{
			if(System.currentTimeMillis()-p<getInterval()){
				return;
			}
		}
		
		//b = System.nanoTime();
		p=System.currentTimeMillis();
		
		if(pingQueue.containsKey(k))
			return;
		pingQueue.put(k, Boolean.valueOf(true));
		// boolean ret=false;
		try {
			if (isInternal()) {

				
				MessageInternalHandlerPrx prx = locateInternalPrx();
				if (prx == null)
					throw new MessageException(-1, "prx is null");

				prx.ice_twoway().ice_ping();
				active = true;
				if (conf.getConnections() < 0) {
					conf.setConnections(0);
				}

			} else if (isAsync()) {

				MessageAsyncHandlerPrx prx = locatePrx();

				if (prx == null)
					throw new MessageException(-1, "prx is null");
				prx.ice_twoway().ice_ping();
				active = true;
				if (conf.getConnections() < 0) {
					conf.setConnections(0);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			//conf.setActive(false);
			logger.debug(e.getMessage()+ "  :"+k);
			active=false;
			conf.setConnections(-1);
			clearCache();

		} finally {
			pingQueue.remove(k);
			//logger.debug("ping duration:" + (System.nanoTime() - b)+" - "+k);
		 }

	}
	
	public static String getFormat(){
		return "%-30s%-20s%-40s";
	}

	@Override
	public String toString() {
		
		String msg=String.format(getFormat(),proxy,service,namespace);
		
		return msg;
	}
	
	public static String getTitle(){
		String msg=String.format(getFormat(), "Proxy","Service","Namespace");
		return msg;
	}

	public void clearCache() {
		lookupLookups.remove(lookup);
		namespaceLookups.remove(namespace);
	}

	public void cacheByLookup() {
		lookupLookups.put(lookup, this);
	}

	public void cacheByNamespace() {
		namespaceLookups.put(namespace, this);
	}

	public static IceServiceConfigLookup locateByLookup(String lookUp) {
		IceServiceConfigLookup l = lookupLookups.get(lookUp);
		if (l != null)
			l.connections++;
		return l;
	}

	public static IceServiceConfigLookup locateByNamespace(String ns) {
		IceServiceConfigLookup l = namespaceLookups.get(ns);
		if (l != null)
			l.connections++;
		return l;

	}
	
	

}
