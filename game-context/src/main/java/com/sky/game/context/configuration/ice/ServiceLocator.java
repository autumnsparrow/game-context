/**
 * 
 */
package com.sky.game.context.configuration.ice;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sky.game.context.util.GameUtil;

/**
 * FishUser 
 *  service  (serviceName,serviceName)
 * +-----------+
 * |namespace  |
 * +-----------+
 * |namespace  |
 * +-----------+
 * 
 * @param service  serivce collection.(FishUser [FishUser-1,FishUser-2....])
 * @param namespace  Namespace (ProtocolSystem,ProtocolUser)
 * 
 * @author sparrow
 *
 */

public class ServiceLocator {
	
	private String service;
	private String ns;
	

	/**
	 * 
	 */
	public ServiceLocator() {
		// TODO Auto-generated constructor stub
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}
	
	public static ServiceLocator obtain(String service,String ns){
		ServiceLocator o=GameUtil.obtain(ServiceLocator.class);
		o.ns=ns;
		o.service=service;
		return o;
	}

}
