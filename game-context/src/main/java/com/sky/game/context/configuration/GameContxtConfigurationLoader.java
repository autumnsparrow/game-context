/**
 * 
 */
package com.sky.game.context.configuration;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import com.sky.game.context.GameContextGlobals;

/**
 * @author sparrow
 *
 */
public class GameContxtConfigurationLoader {

	/**
	 * 
	 */
	public GameContxtConfigurationLoader() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	private static final String CONFIG_PATH="/META-INF/game-context.conf";
	
	static GameContextConfiguration gameContextConfiguration;
	
	
	public static void load(InputStream is) throws MalformedURLException{
		//URL url=GameContxtConfigurationLoader.class.getResource(CONFIG_PATH);
		// dealing with jar files
		
			gameContextConfiguration=(GameContextConfiguration)GameContextGlobals.getJsonConvertor().convertConfig(is, GameContextConfiguration.class);
		
	}
	
	public static void load(URL url) throws MalformedURLException{
		//URL url=GameContxtConfigurationLoader.class.getResource(CONFIG_PATH);
		// dealing with jar files
		
			gameContextConfiguration=(GameContextConfiguration)GameContextGlobals.getJsonConvertor().convertConfig(url, GameContextConfiguration.class);
		
	}
	
	public static GameContextConfiguration getConfiguration(){
		return gameContextConfiguration;
	}
	
	public static <T> T loadConfiguration(URL url,Class<T> clzz){
		@SuppressWarnings("unchecked")
		T t=(T)GameContextGlobals.getJsonConvertor().convertConfig(url, clzz);
		return t;
	}
	
	public static <T> T loadConfiguration(InputStream is,Class<T> clzz){
		@SuppressWarnings("unchecked")
		T t=(T)GameContextGlobals.getJsonConvertor().convertConfig(is, clzz);
		return t;
	}
	
	
	public static <T> T loadXmlConfiguration(InputStream is,Class<T> clzz){
		@SuppressWarnings("unchecked")
		T t=(T)GameContextGlobals.getXmlJsonConvertor().convertConfig(is, clzz);
		return t;
	}
	
	
	
	public static void main(String args[]){
		
		GameContextConfiguration configuration=new GameContextConfiguration();
		//"com.sky.game.blockade.handler",
		//  "com.sky.game.blockade.handler.server,com.sky.game.blockade.handler.client"
//		configuration.setBeansBasePackages("com.sky.game.blockade.handler");
//		configuration.setHandlerBasePackages("com.sky.game.blockade.handler.server");
		configuration.setBeanPackages(new String[]{"com.sky.game.blockade.handler","com.sky.game.blockade.handler"});
		configuration.setHandlerPackages(new String[]{"com.sky.game.blockade.handler.server","com.sky.game.blockade.handler.server"});
		configuration.setServiceName("Protocol");
		String conf=GameContextGlobals.getJsonConvertor().format(configuration);
		System.out.println(conf);
		
		
	}

}
