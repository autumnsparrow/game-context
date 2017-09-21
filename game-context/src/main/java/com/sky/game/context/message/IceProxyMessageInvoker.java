/**
 * 
 */
package com.sky.game.context.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.Message;
import com.sky.game.context.MessageAsyncHandlerPrx;
import com.sky.game.context.MessageException;
import com.sky.game.context.MessageHandlerPrx;
import com.sky.game.context.annotation.introspector.AnnotationIntrospector.ProtocolMapEntry;
import com.sky.game.context.configuration.ice.IceServiceConfig;
import com.sky.game.context.configuration.ice.IceServiceConfigLookup;
import com.sky.game.context.configuration.ice.IceServiceConfigRegstry;
import com.sky.game.context.ice.IceProxyManager;
import com.sky.game.context.route.RouterHeader;

/**
 * @author sparrow
 * 
 */
public class IceProxyMessageInvoker {
	
	private static final Log logger=LogFactory.getLog(IceProxyMessageInvoker.class);

	/**
	 * 
	 */
	public IceProxyMessageInvoker() {
		// TODO Auto-generated constructor stub
	}

	public static boolean sync(Message msg) {
		ProtocolMapEntry entry = GameContextGlobals.getAnnotationIntrospector()
				.getProtocolMapEntry(msg.transcode);
		return entry == null ? false : entry.sync;

	}

	public static Message invoke(Message msg) throws MessageException {
		MessageHandlerPrx prx = null;
		Message resp = null;
		String namespace = GameContextGlobals.locateNamespace(msg.transcode);
		if (namespace == null) {
			throw new RuntimeException("transcode :" + msg.transcode
					+ " can't find namespace");
		}

		prx = null;//GameContextGlobals.locateProxy(namespace);
		if (prx == null)
			throw new RuntimeException("transcode :" + msg.transcode
					+ " can't find proxy" + " ,namesapce -" + namespace);
		resp = prx.invoke(msg);
		
		return resp;

	}

	public static void onRecieve(Message msg, RouterHeader extra)
			throws MessageException {
		MessageAsyncHandlerPrx prx = null;
		try {
		
			//long b=System.currentTimeMillis();
			extra.m(IceProxyMessageInvoker.class.getSimpleName()+"_onReceive");
			extra.m(MessageAsyncHandlerPrx.class.getSimpleName()+"_onReceive");
			//extra.m(MessageAsyncHandlerPrx.class.getSimpleName()+"_onReceive");
//					
			IceServiceConfigLookup l=extra.locate(msg.transcode);
			
			if (l != null) {
				String ex = GameContextGlobals.getJsonConvertor().format(extra);
				
				prx = l.locatePrx();
			
				logger.debug(extra.toString()+"-"+msg.transcode+" - "+msg.content);
				if(prx==null)
					throw new MessageException(-1, "proxy=null - "+ extra.toString());
				
				prx.onRecieve(msg, ex);
				//logger.info(Thread.currentThread().getId()+" - "+ extra.toString() +"  -   delta:"+(System.currentTimeMillis()-b)+" ms");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("error :"+(prx!=null?prx.toString():"")+e.getMessage()+" - "+ extra.toString());
			
			throw new MessageException(-1, e.getMessage());
		}finally{
			extra.e(IceProxyMessageInvoker.class.getSimpleName()+"_onReceive");
			
			extra.metrics();
		}

	}

}
