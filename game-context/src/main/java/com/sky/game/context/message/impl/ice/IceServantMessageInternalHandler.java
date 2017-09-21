/**
 * 
 */
package com.sky.game.context.message.impl.ice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import Ice.Current;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageException;
import com.sky.game.context.MessageInternalBean;
import com.sky.game.context.SpringContext;
import com.sky.game.context._MessageInternalHandlerDisp;
import com.sky.game.context.event.LocalServiceException;
import com.sky.game.context.handler.ValueHolder;
import com.sky.game.context.spring.IRemoteService;
import com.sky.game.context.spring.RemoteServiceException;
import com.sky.game.context.spring.ice.MessageInternalBeanParameterWrapper;
import com.sky.game.context.spring.ice.MessageInternalBeanResponseWrapper;
import com.sky.game.context.spring.ice.SeqWrapper;
import com.sky.game.context.util.CronUtil;
import com.sky.game.context.util.GameUtil;


/**
 * @author sparrow
 *
 */
public class IceServantMessageInternalHandler extends _MessageInternalHandlerDisp {
	private static final Log logger = LogFactory.getLog(IceServantMessageInternalHandler.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public IceServantMessageInternalHandler() {
		// TODO Auto-generated constructor stub
	}

	private static final Map<String, InvocationTarget> targets = new HashMap<String, InvocationTarget>();

	private InvocationTarget getInvocationTarget(String namespace) {
		InvocationTarget target = null;
		if (targets.containsKey(namespace)) {
			target = targets.get(namespace);
		} else {
			target = new InvocationTarget(namespace);
			targets.put(namespace, target);
		}
		return target;
	}

	public static class InvocationTarget {
		String beanName;
		Class<?> mapperInterface;
		Method method;

		private void findMapperInterface(Class<?>[] cls, String m) {

			// Class<?> cls[] = t.getClass().getInterfaces();

			for (Class<?> clz : cls) {

				// is a interface IRemoteService.
				if (clz.isInterface() && clz.getSimpleName().equals(this.beanName)) {
					// class has super interface as IRemoteService
					mapperInterface = clz;

					Method[] mmm = mapperInterface.getMethods();
					for (Method mm : mmm) {
						if (mm.getName().equals(m)) {
							this.method = mm;

							break;
						}
					}
					break;

				}

				//
				Class<?> superClz[] = clz.getInterfaces();
				findMapperInterface(superClz, m);

			}
		}

		/**
		 * @param beanName
		 */
		public InvocationTarget(String namespace) {
			super();
			String[] arrays = StringUtils.tokenizeToStringArray(namespace, ".");
			if (arrays != null && arrays.length == 2) {
				this.beanName = arrays[0];
				String m = arrays[1];
				Object t = null;
				if (SpringContext.isEmpty()) {

				} else {
					t = SpringContext.getBean(beanName);
				}

				Class<?> cls[] = t.getClass().getInterfaces();
				findMapperInterface(cls, m);

			}
		}

	}

	private Object[] getArgs(InvocationTarget target, String parameter) {
		Object[] args = null;
		try {
			Class<?> parametersTypes[] = target.method.getParameterTypes();
			Type[]  types=target.method.getGenericParameterTypes();
		//	target.method.getGenericParameterTypes()
			
			args = new Object[0];
			if (parametersTypes != null && parametersTypes.length > 0) {
				args = new Object[parametersTypes.length];
				MessageInternalBeanParameterWrapper wrapper = GameContextGlobals.getJsonConvertor().convert(parameter,
						MessageInternalBeanParameterWrapper.class);
				Map<Integer, String> parameters = wrapper.getParameters();
				for (int i = 0; i < parametersTypes.length; i++) {
					String p = parameters.get(Integer.valueOf(i));
					
					JavaType t=TypeFactory.defaultInstance().constructType(types[i]);
					
					args[i] = p != null ?  GameContextGlobals.getJsonConvertor().convert(p, t) : null;

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(target.beanName + " - " + target.method.getName() + " - " + parameter);
			e.printStackTrace();

		}

		return args;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sky.game.context._MessageInternalHandlerOperations#invoke(com.sky
	 * .game.context.MessageInternalBean, Ice.Current)
	 */
	@Override
	public MessageInternalBean invoke(MessageInternalBean param, Current __current) throws MessageException {
		// TODO Auto-generated method stub
		
		MessageInternalBeanResponseWrapper resp = MessageInternalBeanResponseWrapper.getObject();

		
		try {
			logger.debug(param.ns+" - "+param.operation+" - "+param.parameter);
			InvocationTarget target = getInvocationTarget(param.operation);
			
			Object[] args = getArgs(target, param.parameter);
			Object respObject = null;
			Object object = SpringContext.getBean(target.beanName);
			respObject = target.method.invoke(object, args);

			resp.setResponseObject(GameContextGlobals.getJsonConvertor().format(respObject));

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

			if (e.getTargetException() instanceof RemoteServiceException) {
				resp.setExceptionObject((RemoteServiceException) e.getTargetException());
			}

		}

		catch (Exception e) {

			if (e instanceof RemoteServiceException) {
				resp.setExceptionObject((RemoteServiceException) e);
			}
			if (e instanceof LocalServiceException) {
				resp.setExceptionObject((RemoteServiceException) e);
			}
			e.printStackTrace();

		}

		try {
			param.parameter = GameContextGlobals.getJsonConvertor().format(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return param;
	}
	
//	public static void main(String args[]){
//		//String param="{"parameters":{"0":"1445","1":"\"我们交个朋友吧\"","2":"","3":"2","4":"\"2016-03-30 14:51:05\"","5":"1440","6":"\"{\\\"attachments\\\":[{\\\"id\\\":\\\"Friends_Request_Cancel\\\",\\\"type\\\":2,\\\"value\\\":\\\"-1\\\"},{\\\"id\\\":\\\"Friends_Request_Ok\\\",\\\"type\\\":2,\\\"value\\\":\\\"1\\\"}]}\"","7":"\"{\\\"userLevel\\\":null,\\\"apkVersion\\\":false,\\\"loginAfterNDays\\\":0,\\\"userIds\\\":[1479]}\""}}";
//		
//		String param= "{\"parameters\":{\"0\":\"1479\",\"1\":\"{\\\"seq\\\":[1445]}\"}}";
//		MessageInternalBeanParameterWrapper wrapper = GameContextGlobals.getJsonConvertor().convert(param,
//				MessageInternalBeanParameterWrapper.class);
//		
//		String json=wrapper.getParameters().get(Integer.valueOf(1));
//		json="[1445,1446]";
//		
//		List<Long> seq=(List<Long>)GameContextGlobals.getJsonConvertor().convert(json,List.class);
//		System.out.println("");
//	}


	public static void main(String args[]){
		String activeDateTime="2016-03-31 14:50:00";
		boolean ret=System.currentTimeMillis()-CronUtil.getDateFromString(activeDateTime).getTime()>0;
		System.out.println(ret);
		
	}
	
}
