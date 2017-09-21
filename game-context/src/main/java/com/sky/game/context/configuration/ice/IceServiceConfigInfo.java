/**
 * 
 */
package com.sky.game.context.configuration.ice;

import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */
public class IceServiceConfigInfo {
	
	String name;
	String url;

	/**
	 * 
	 */
	public IceServiceConfigInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static IceServiceConfigInfo obtain(String name,String url){
		IceServiceConfigInfo o=GameUtil.obtain(IceServiceConfigInfo.class);
		o.name=name;
		o.url=url;
		return o;
	}

}
