package com.sky.game.context.util;

import java.io.InputStream;
import com.sky.game.context.configuration.GameContxtConfigurationLoader;


public class WebsocketConfiguration {
	
	
	private String publicKeyFile;
	
	private String privateKeyFile;
	
	private String password;
	
	private String contextConf;
	
	private int threadPoolSize;
	
	public String getContextConf() {
		return contextConf;
	}

	public void setContextConf(String contextConf) {
		this.contextConf = contextConf;
	}

	static WebsocketConfiguration configuration=null;
	static final String MINA_CONTEXT_CONFIG="/META-INF/websocket-context.conf";
	
	static{
		load(MINA_CONTEXT_CONFIG);
	}
	
	public static WebsocketConfiguration load(String file){
		InputStream url=WebsocketConfiguration.class.getResourceAsStream(file);
		configuration=GameContxtConfigurationLoader.loadConfiguration(url, WebsocketConfiguration.class);
		return configuration;
	}
	public static WebsocketConfiguration getInstance(){
		return configuration;
	}
	


	
	public String getVersion() {
		return null;
	}

	public void validate() {
		
	}


	public String getPublicKeyFile() {
		return publicKeyFile;
	}

	public void setPublicKeyFile(String publicKeyFile) {
		this.publicKeyFile = publicKeyFile;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

}
