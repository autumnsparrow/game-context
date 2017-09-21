/**
 * 
 */
package com.sky.game.context.configuration.ice;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */

public class ServiceLocators {
	
	@JacksonXmlElementWrapper(localName="locators")
	List<ServiceLocator> locator;

	/**
	 * 
	 */
	public ServiceLocators() {
		// TODO Auto-generated constructor stub
		locator=GameUtil.getList();
	}

	public List<ServiceLocator> getLocator() {
		return locator;
	}

	public void setLocator(List<ServiceLocator> locators) {
		this.locator = locators;
	}

}
