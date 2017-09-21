/**
 * 
 */
package com.sky.game.context;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.security.action.GetLongAction;

import com.sky.game.context.annotation.introspector.AnnotationIntrospector;
import com.sky.game.context.annotation.introspector.EventHandlerClass;
import com.sky.game.context.annotation.introspector.IIdentifiedObject;
import com.sky.game.context.configuration.GameContextConfiguration;
import com.sky.game.context.configuration.GameContxtConfigurationLoader;
import com.sky.game.context.event.EventHandler;
import com.sky.game.context.event.EventProcessTask;
import com.sky.game.context.exception.GlobalExceptionHandler;
import com.sky.game.context.executor.OrderedThreadPoolExecutor;
import com.sky.game.context.executor.UnorderedThreadPoolExecutor;
import com.sky.game.context.ice.IceProxyManager;
import com.sky.game.context.ice.IceServerManager;
import com.sky.game.context.json.IJsonConvertor;
import com.sky.game.context.json.impl.DefaultJsonConvertor;
import com.sky.game.context.json.impl.XmlJsonConvertor;
import com.sky.game.context.message.IMessageHandler;
import com.sky.game.context.message.impl.DefaultServantMessageHandler;
import com.sky.game.context.message.impl.ice.IceProxyMessageHandler;
import com.sky.game.context.util.BeanUtil.D;
import com.sky.game.context.util.GameUtil;

/**
 * Game context globals.
 * 
 * The main entry-point of the game context.
 * 
 * 
 * 
 * 
 * @author sparrow
 * 
 */
public class GameContextGlobals {
	
	
	
	private static final Log logger = LogFactory
			.getLog(GameContextGlobals.class);

	/**
	 * 
	 */
	public GameContextGlobals() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Json convertor. 
	 * {@see DefaultJsonConvertor}
	 */
	private static final IJsonConvertor JSON_CONVERTOR = new DefaultJsonConvertor();
	/**
	 * Xml convertor
	 * {@see XmlJsonConvertor}
	 */
	private static final IJsonConvertor XMLJSON_CONVERTOR=new XmlJsonConvertor();
	
	/**
	 * Servant message handler.
	 * {@see IMessageHandler}
	 * {@see DefaultServantMessageHandler}
	 */
	private final static IMessageHandler SERVANT_MESSAGE_HANDLER = new DefaultServantMessageHandler();
	/**
	 * Proxy message handler.
	 * {@see IMessageHandler}
	 * {@see IceProxyMessageHandler}
	 * 
	 */
	private static final IMessageHandler PROXY_MESSAGE_HANDLER = new IceProxyMessageHandler();

	/**
	 * Beans & Handlers package scanner, introspect the annotation.
	 * {@see AnnotationIntrospector}
	 * 
	 */
	final static AnnotationIntrospector ANNOTATION_INTROSPECTOR = new AnnotationIntrospector();
	
	/**
	 * Global exception handler.
	 * {@see GlobalExceptionHandler}
	 * 
	 */
	private final static GlobalExceptionHandler exceptionHandler=new GlobalExceptionHandler();

	/**
	 * 
	 * @return Xml json convertor.
	 */
	public static IJsonConvertor getXmlJsonConvertor(){
		return XMLJSON_CONVERTOR;
	}
	/**
	 * 
	 * @return text json convertor.
	 */
	public static IJsonConvertor getJsonConvertor() {
		return JSON_CONVERTOR;
	}

	/**
	 * 
	 * 
	 * @return servant message handler.
	 */
	public static IMessageHandler getServantMessageHandler() {
		return SERVANT_MESSAGE_HANDLER;
	}

	/**
	 * 
	 * @return proxy message handler.
	 */
	public static IMessageHandler getProxyMessageHandler() {
		return PROXY_MESSAGE_HANDLER;
	}

	/**
	 * 
	 * @return annotation introspector.
	 */
	public static AnnotationIntrospector getAnnotationIntrospector() {
		return ANNOTATION_INTROSPECTOR;
	}
	
	/**
	 *  Ice Servant manager
	 *  {@see IceServerManager}
	 * 
	 */
	private static final IceServerManager ICESER_ICE_SERVER_MANAGER = new IceServerManager();

	/**
	 * 
	 * 
	 * @return Ice Servant manager.
	 */
	public static IceServerManager getIceServerManager() {
		return ICESER_ICE_SERVER_MANAGER;
	}

	/**
	 * Ice Proxy Manager.
	 * {@see IceProxyMessageHandler}
	 */
	private static final IceProxyManager ICE_PROXY_MANAGER = new IceProxyManager();

	/**
	 * 
	 * @return Ice Proxy manager.
	 */
	public static IceProxyManager getIceProxyManager() {
		return ICE_PROXY_MANAGER;
	}
	
	
	/**
	 * 
	 * @param url the jar resource or http protocol resource.
	 * 
	 */
	@Deprecated
	public static void loadClient(URL url){
		try {
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			
			GameContxtConfigurationLoader.load(url);
			GameContextConfiguration config = GameContxtConfigurationLoader
					.getConfiguration();


		
			getIceProxyManager().setServiceName(config.getServiceName());
			getIceProxyManager().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * 
	 * loading game context by url resource.
	 * 
	 * client token
	 */
	
	@Deprecated
	public static void init(URL url) {

		//
		//
		try {
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			
			GameContxtConfigurationLoader.load(url);
			GameContextConfiguration config = GameContxtConfigurationLoader
					.getConfiguration();


			getAnnotationIntrospector().setBeanLocations(config.getBeanPackages());
			getAnnotationIntrospector().setHandlerLocations(config.getHandlerPackages());
			getAnnotationIntrospector().scan();
			getIceServerManager().setServiceName(config.getServiceName());
			

			getIceProxyManager().setServiceName(config.getServiceName());
			getIceProxyManager().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

	}
	
	private static EventHandler<EventProcessTask<?>> eventHandler;
	
	
	private static GameContextConfiguration GAME_CONTEXT_CONFIGURATION;
	
	public static GameContextConfiguration getConfig(){
		
		return GAME_CONTEXT_CONFIGURATION;
	}
	

	/**
	 * loading the configuration ,initializing the game-context.
	 * 1.load the game-context.conf
	 * 2.scan the bean&handle package location, introspect the annotation.
	 * 3.setting the service name for the Ice Proxy Manager.
	 * 4.setting the service name for the Ice Servant Manager.
	 * 5.create ordered&unorder thread pool.
	 * 6.create the event-handler.
	 * @param url game-context.conf 
	 */
	public static void init(InputStream url) {

		//
		//
		try {
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			GameContxtConfigurationLoader.load(url);
			GameContextConfiguration config = GameContxtConfigurationLoader
					.getConfiguration();
			GAME_CONTEXT_CONFIGURATION=config;

			getAnnotationIntrospector().setBeanLocations(config.getBeanPackages());
			getAnnotationIntrospector().setHandlerLocations(config.getHandlerPackages());
			logger.info("begin scan ....");
			getAnnotationIntrospector().scan();
			logger.info("end of scan ....");
			getIceServerManager().setServiceName(config.getServiceName());
			

			getIceProxyManager().setServiceName(config.getServiceName());
			getIceProxyManager().init();
			
			//
			//Executors.newFixedThreadPool(nThreads, new Thread);
			executorService=new OrderedThreadPoolExecutor(config.getTaskThreads(), config.getTaskThreads());//Executors.newFixedThreadPool(config.getTaskThreads());
			executorServiceUnOrdered=new UnorderedThreadPoolExecutor(config.getTaskThreads(), config.getTaskThreads());//Executors.newFixedThreadPool(config.getTaskThreads());
			
			//GameWorldTimer.getGameWorldTimer();
			eventHandler=EventHandler.obtain(executorService);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Fatal error:"+e.getMessage());
			System.exit(-1);
		}

	}

	
	/**
	 * 
	 * @param  the response transaction code of  a message.
	 * @return the transaction code of message.
	 */
	public static String getTranscodeByResponse(String responsecode) {
		return ANNOTATION_INTROSPECTOR
				.getTranscodeByResponseTranscode(responsecode);
	}
	
	/**
	 * 
	 * @param transcode  of message.
	 * @return EventHandlerClass of that transcode.
	 */
	public static EventHandlerClass getEventHandlerClass(String transcode){
		EventHandlerClass objects=null;
		try {
			objects=GameContextGlobals.getAnnotationIntrospector().getEventHandler(transcode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return objects;
	}
	 
	//private static final Object lock=new Object();
	
	/**
	 * register event handler for a object.
	 * {@see IIdentifiedObject}
	 * @param transcode a transaction code.
	 * @param obj an identified object that want to observe a transaction code.
	 */
	public static void registerEventHandler(String transcode,IIdentifiedObject obj){
		synchronized (lock) {
			
		
		try {
			GameContextGlobals.getAnnotationIntrospector().getEventHandler(transcode).addObserver(obj);
			//logger.info(GameUtil.formatId(obj.getId(),obj)+" - register ("+transcode+"");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	/**
	 * 
	 * @param transcode a transaction code.
	 * @param obj an indentified object that want to remove from the observer of a transaction code.
	 */
	public static void unregisterEventHandler(String transcode,IIdentifiedObject obj){
		synchronized (lock) {
		try {
			GameContextGlobals.getAnnotationIntrospector().getEventHandler(transcode).removeObserver(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	/**
	 * 
	 * @param obj the identified object that remove all transaction codes.
	 */
	public static void removeObjectEventHandler(IIdentifiedObject obj) {
		synchronized (lock) {
		for(EventHandlerClass evtHandler:GameContextGlobals.getAnnotationIntrospector().getEventHandlers()){
			evtHandler.removeObserver(obj);
			logger.info(GameUtil.formatId(obj.getId(),obj)+" - remove ("+evtHandler+")");
		}
		}
	}
	
	//private static ConcurrentHashMap<String, String> 
	public static String locateNamespace(String transcode){
		String namespace=getAnnotationIntrospector().getProtocolMapEntry(transcode).namespace;
		if(namespace==null){
			logger.info("======"+transcode+" Can't find the namespace");
		}
		return namespace;
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * ordered thread pool executor service.
	 */
	private static ExecutorService executorService=null;
	/**
	 * un-ordered thread poll executor service.
	 */
	private static ExecutorService executorServiceUnOrdered=null;
	
	/**
	 * 
	 * @return ordered thread pool service.
	 */
	public static ExecutorService getExecutor(){
		return executorService;
	}
	
	/**
	 * 
	 * @return un-ordered thread pool service.
	 */
	public static ExecutorService getExecutorUnOrdered(){
		return executorServiceUnOrdered;
	}
	
	//private static ConcurrentHashMap<Long, IIdentifiedObject> unique_map=new ConcurrentHashMap<Long, IIdentifiedObject>();
	
	private static final Object lock=new Object();
	/**
	 * 
	 * 
	 * @param task post the runnable into the thread pool(un-ordered).
	 */
	public static void postTask(Runnable  task){
		executorServiceUnOrdered.execute(task);
		
	}
	
	/**
	 * 
	 * @param processTask post the task into the event handler.
	 */
	public static void addEventProcessTask(EventProcessTask<?> processTask){
		eventHandler.addEvent(processTask);
	}
}
