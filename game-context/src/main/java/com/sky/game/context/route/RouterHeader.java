/**
 * 
 */
package com.sky.game.context.route;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageException;
import com.sky.game.context.annotation.introspector.IIdentifiedObject;
import com.sky.game.context.configuration.ice.IceServiceConfig;
import com.sky.game.context.configuration.ice.IceServiceConfigLookup;
import com.sky.game.context.configuration.ice.IceServiceConfigRegstry;
import com.sky.game.context.util.G;
import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */
public class RouterHeader  implements IIdentifiedObject ,Cloneable{
	
	private static final Log logger=LogFactory.getLog(RouterHeader.class);
	protected Long id;
	protected String connectionId;
	protected String transcode;
	protected String from;
	protected String to;
	
	
	protected String deviceId;
	
	protected RouteMetrics routeMetrics;
	
	@JsonIgnore
	protected String namespace;
	@JsonIgnore
	protected IceServiceConfigLookup lookup;
	@JsonIgnore
	protected String token;
	
	
	
	
	
	
	

	public RouteMetrics getRouteMetrics() {
		return routeMetrics;
	}

	public void setRouteMetrics(RouteMetrics routeMetrics) {
		this.routeMetrics = routeMetrics;
	}

	public String getTranscode() {
		return transcode;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public void m(String k){
		if(this.routeMetrics!=null){
			this.routeMetrics.b(k);
		}
	}
	
	public String metrics(){
		String ret=null;
		if(this.routeMetrics!=null){
			ret="<metrics>:["+this.deviceId+"]"+"["+this.transcode+"]"+this.routeMetrics.toString();
			logger.info(ret);
		}
		
		
		return ret;
	}
	

	public void e(String k){
		if(this.routeMetrics!=null){
			this.routeMetrics.e(k);
		}
	} 
	
	

	public RouterHeader(String deviceId, String connectionId) {
		super();
		this.deviceId = deviceId;
		this.connectionId = connectionId;
		
		this.id=Long.valueOf(0);
		this.from=GameContextGlobals.getConfig().getServiceName();
		this.to="";
		this.token="";
		if(RouteMetricsConfig.instance.containsKey(deviceId)&&RouteMetricsConfig.instance.enable){
			routeMetrics=G.o(RouteMetrics.class);
		}else{
			routeMetrics=null;
		}
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

	public String getConnectionId() {
		return connectionId;
	}


	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	@JsonIgnore
	public boolean isValid(){
		return lookup!=null?(lookup.isAlive()):false;
	}

	public RouterHeader() {
		super();
		// TODO Auto-generated constructor stub
		this.deviceId = "";
		this.connectionId = "";
		
		this.id=Long.valueOf(0);
		this.from=GameContextGlobals.getConfig().getServiceName();
		this.to="";
		this.token="";
		if(RouteMetricsConfig.instance.containsKey(deviceId)){
			routeMetrics=G.o(RouteMetrics.class);
		}else{
			routeMetrics=null;
		}
	}

	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String toString(){
		return "<R>:"+(namespace==null?"-":namespace)+"("+(lookup==null?"":lookup.getService())+")"+"@"+GameContextGlobals.getJsonConvertor().format(this);
	}
	
	public static RouterHeader toObject(String json){
		RouterHeader o=GameContextGlobals.getJsonConvertor().convert(json, RouterHeader.class);
		return o;
		
	}


	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}


	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}
	
	
	
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
		// 
		
	}

	public IceServiceConfigLookup getLookup() {
		return lookup;
	}

	public void setLookup(IceServiceConfigLookup lookup) {
		this.lookup = lookup;
	}
	
	
	
	public IceServiceConfigLookup locate(String transcode) throws Exception{
		IceServiceConfigLookup l = null;
		
			this.transcode=transcode;
			String ns = GameContextGlobals.locateNamespace(transcode);
			if (ns == null) {
				throw new Exception("transcode :" + transcode
						+ " can't find namespace");
			}
			
			setNamespace(ns);

			l = getLookup();

			if (l == null) {
				
				if(!("".equals(to)||null==to)){
					l = IceServiceConfigRegstry.locateLooup(namespace,
							to);
				}
				if (l == null) {
					l = IceServiceConfigRegstry.locateLookup(namespace);
					if(l==null){
						throw new Exception("transcode :" + transcode
								+ " can't find namespace  "+namespace);
					}
					to=l.getService();
					setLookup(l);
				}
				
			}
		
	
		return l;
	}



}
