/**
 * 
 */
package com.sky.game.context.service;

/**
 * @author sparrow
 *
 */
public interface IServerStarupLifeCycle {
	
	public void beforeStartup();
	
	public void middleOfStartup();
	
	public void afterStartup();

}
