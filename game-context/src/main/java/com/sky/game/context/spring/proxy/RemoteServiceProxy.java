/**
 * 
 */
package com.sky.game.context.spring.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.Dump.INode;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.MessageInternalBean;
import com.sky.game.context.MessageInternalHandlerPrx;
import com.sky.game.context.configuration.ice.IceServiceConfig;
import com.sky.game.context.configuration.ice.IceServiceConfigLookup;
import com.sky.game.context.configuration.ice.IceServiceConfigRegstry;
import com.sky.game.context.handler.ValueHolder;
import com.sky.game.context.ice.IceProxyManager;
import com.sky.game.context.spring.RemoteServiceException;
import com.sky.game.context.spring.ice.MessageInternalBeanParameterWrapper;
import com.sky.game.context.spring.ice.MessageInternalBeanResponseWrapper;

/**
 * @author sparrow
 *
 */

public class RemoteServiceProxy<T> implements InvocationHandler, Serializable {
	private static final Log logger = LogFactory.getLog(RemoteServiceProxy.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 7927810381416340002L;
	private final Class<T> mapperInterface;

	/**
	 * @param mapperInterface
	 * @param methodCache
	 */
	public RemoteServiceProxy(Class<T> mapperInterface) {
		super();
		this.mapperInterface = mapperInterface;

	}

	// static AtomicInteger invoked=new AtomicInteger(9);
	// static AtomicLong duration=new AtomicLong(0);

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object o = null;
		MessageInternalHandlerPrx prx = null;
		try {
			long b = System.currentTimeMillis();
			prx = locateProxy();

			MessageInternalBean request = buildRequest(method, args);

			MessageInternalBean response = prx.invoke(request);

			o = parseResponse(method, response);
			//
			logger.debug(Thread.currentThread().getId() + " - " + getNamespace() + "." + method.getName()
					+ "  -   delta:" + (System.currentTimeMillis() - b) + " ms");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("error :" + (prx != null ? prx.toString() : "") + e.getMessage() + " - " + getNamespace() + "."
					+ method.getName());
			// throw e;
		}

		return o;
	}

	/**
	 * 
	 * get the namespace by the class path.
	 * 
	 * @return
	 */
	private String getNamespace() {
		return mapperInterface.getPackage().getName();
	}

	private MessageInternalHandlerPrx locateProxy() {

		// long b=System.nanoTime();
		IceServiceConfigLookup l = IceServiceConfigRegstry.locateLookup(getNamespace());

		// invoked.incrementAndGet();
		// duration.addAndGet((System.nanoTime()-b));
		// logger.info(Thread.currentThread().getId() +" - invoked: "+invoked+"
		// delta:"+duration.longValue()+"
		// average:"+(duration.longValue()/invoked.intValue()));
		//
		MessageInternalHandlerPrx prx= l.locateInternalPrx();
		
		return prx;
	}

	private MessageInternalBean buildRequest(Method method, Object[] args) {
		MessageInternalBean o = new MessageInternalBean();
		o.ns = getNamespace();
		o.operation = mapperInterface.getSimpleName() + "." + method.getName();
		Map<Integer, String> parameters = new HashMap<Integer, String>();

		if (args != null) {

			for (int i = 0; i < args.length; i++) {

				parameters.put(Integer.valueOf(i), GameContextGlobals.getJsonConvertor().format(args[i]));
			}
			o.parameter = GameContextGlobals.getJsonConvertor()
					.format(MessageInternalBeanParameterWrapper.obtain(parameters));
		} else {
			o.parameter = "";
		}
		logger.debug(" << " + o.ns + " - " + o.operation + " - " + o.parameter);

		return o;

	}

	private Object parseResponse(Method method, MessageInternalBean response) throws RemoteServiceException {

		MessageInternalBeanResponseWrapper resp = GameContextGlobals.getJsonConvertor().convert(response.parameter,
				MessageInternalBeanResponseWrapper.class);

		logger.debug(">> " + response.ns + " - " + response.operation + " - " + response.parameter);

		Object o = null;
		if (!method.getReturnType().getName().equals("void") && resp.getResponseObject() != null) {
			JavaType t = TypeFactory.defaultInstance().constructType(method.getGenericReturnType());
			o = GameContextGlobals.getJsonConvertor().convert(resp.getResponseObject(), t);
		}

		if (resp.getExceptionObject() != null) {
			throw resp.getExceptionObject();
		}

		return o;

	}

}
