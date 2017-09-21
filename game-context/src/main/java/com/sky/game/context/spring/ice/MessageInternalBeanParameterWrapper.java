/**
 * 
 */
package com.sky.game.context.spring.ice;

import java.util.Map;

import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */
public class MessageInternalBeanParameterWrapper {
	
	Map<Integer,String> parameters;
	
	public static MessageInternalBeanParameterWrapper obtain(Map<Integer,String> parameter){
		MessageInternalBeanParameterWrapper o=GameUtil.obtain(MessageInternalBeanParameterWrapper.class);
		o.parameters=parameter;
		return o;
	}

	/**
	 * 
	 */
	public MessageInternalBeanParameterWrapper() {
		// TODO Auto-generated constructor stub
	}

	public Map<Integer, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<Integer, String> parameters) {
		this.parameters = parameters;
	}

}
