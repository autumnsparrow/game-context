/**
 * 
 */
package com.sky.game.context.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Administrator
 *
 */
public class BaseRequest {
	@JsonIgnore
	String token;
	

	/**
	 * 
	 */
	public BaseRequest() {
		// TODO Auto-generated constructor stub
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}
	
	

}
