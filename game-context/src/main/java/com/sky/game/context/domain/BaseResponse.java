/**
 * 
 */
package com.sky.game.context.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Administrator
 *
 */
public class BaseResponse {

	/**
	 * 
	 */
	public BaseResponse() {
		// TODO Auto-generated constructor stub
	}
	@JsonIgnore
	String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
