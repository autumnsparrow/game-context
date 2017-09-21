package com.sky.game.context;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sky.game.context.spring.RemoteServiceScannerConfigurer;

/**
 * 
 * Spring context that holder the {@link ApplicationContext}
 *
 */
public class SpringContext {
	
	private static Logger logger = LoggerFactory.getLogger(SpringContext.class);
    
	/**
	 * Spring Application context(Spring container)
	 */
	private static ApplicationContext cx = null;
	
	/**
	 * 
	 * @return true the Spring Application context not exist.
	 */
	public static boolean isEmpty(){
		return cx==null;
	}
	
	/**
	 * 
	 * @param ctx set the Spring Application context. {@link RemoteServiceScannerConfigurer#setApplicationContext(ApplicationContext)}
	 */
	public static void setApplicationContext(ApplicationContext ctx){
			cx=ctx;
	}
	
	/**
	 * 
	 * @param beanId the bean name in the Application context.
	 * @return the Bean instance in the Spring Application context.
	 */
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId){
       return (T)cx.getBean(beanId);
	}
    
    /**
     * loading Spring Application context from a configuration.
     */
    public synchronized static void init(){
    	if(cx == null){
      	  cx = new ClassPathXmlApplicationContext("classpath:spring/game-server-config.xml");
      	  logger.info("Spring config success!,ApplicationContext set a object");
      	}
    }
    
    /**
     * loading the Spring Application context.
     * @param path the path of the spring configuration.
     */
    public synchronized static void init(String path){
    	
    	if(cx == null){
    	  cx = new ClassPathXmlApplicationContext(path);
    	  logger.info("Spring config success!,ApplicationContext set a object");
    	}
    }
    
    /**
     * loading the Spring Application context from multi-file.
     * @param path files of the spring configuration.
     */
    public synchronized static void init(String[] path){
    	
    	if(cx == null){
    	  cx = new ClassPathXmlApplicationContext(path);
    	  logger.info("Spring config success!,ApplicationContext set a object");
    	}
    }
   
    
    
   
    
  
}
