/**
 * 
 */
package com.sky.game.context.configuration;
/**
 * @author sparrow
 *
 */

public class GameContextConfiguration {
	
	

	/**
	 * 
	 */
	public GameContextConfiguration() {
		// TODO Auto-generated constructor stub
	}
	

	private String serviceName;
	private String[] beanPackages;
	private String[] handlerPackages;
	private int taskThreads;
	
	
	
	
	
	
	
	public int getTaskThreads() {
		return taskThreads;
	}
	public void setTaskThreads(int taskThreads) {
		this.taskThreads = taskThreads;
	}
	
//	public String getBeansBasePackages() {
//		return beansBasePackages;
//	}
//	public void setBeansBasePackages(String beansBasePackages) {
//		this.beansBasePackages = beansBasePackages;
//	}
//	public String getHandlerBasePackages() {
//		return handlerBasePackages;
//	}
//	public void setHandlerBasePackages(String handlerBasePackages) {
//		this.handlerBasePackages = handlerBasePackages;
//	}
	public String[] getBeanPackages() {
		return beanPackages;
	}
	public void setBeanPackages(String[] beanPackages) {
		this.beanPackages = beanPackages;
	}
	public String[] getHandlerPackages() {
		return handlerPackages;
	}
	public void setHandlerPackages(String[] handlerPackages) {
		this.handlerPackages = handlerPackages;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	

}
