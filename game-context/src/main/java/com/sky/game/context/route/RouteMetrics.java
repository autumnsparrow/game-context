/**
 * 
 */
package com.sky.game.context.route;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sky.game.context.util.G;
import com.sky.game.context.util.GameUtil;

import IceInternal.Buffer;

/**
 * @author sparrow
 *
 */
public class RouteMetrics {
	
	
	Map<String,Metrics> metrics;
	
	

	

	public Map<String, Metrics> getMetrics() {
		return metrics;
	}

	public void setMetrics(Map<String, Metrics> metrics) {
		this.metrics = metrics;
	}

	public RouteMetrics() {
		super();
		// TODO Auto-generated constructor stub
		this.metrics=GameUtil.getMap();
	}
	
	public void b(String k){
		Metrics m=null;
		if(this.metrics.containsKey(k)){
			m=this.metrics.get(k);
			
		}else{
			m=new Metrics(k);
			this.metrics.put(m.k, m);
		}
		
		m.reset();
	}
	
	
	public void e(String k){
		Metrics m=null;
		if(this.metrics.containsKey(k)){
			m=this.metrics.get(k);
			m.e();
		}
		
	}

	@Override
	public String toString() {
		StringBuffer buf=new StringBuffer();
		buf.append("[");
		for(Iterator<String> it=this.metrics.keySet().iterator();it.hasNext();){
			String k=it.next();
			Metrics m=this.metrics.get(k);
			if(m.d>0){
				buf.append(m.toString()).append(",");
			}
		}
		
		String ret=buf.toString();
		return ret.subSequence(0, ret.length()-2)+"]";
	}
	
	
	

}
