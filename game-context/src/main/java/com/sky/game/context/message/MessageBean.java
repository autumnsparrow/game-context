/**
 * 
 */
package com.sky.game.context.message;

import com.sky.game.context.Message;

/**
 * @author sparrow
 *
 */
public class MessageBean {
	
	public String transcode;
	public String content;
	public String token;

	/**
	 * 
	 */
	public MessageBean() {
		// TODO Auto-generated constructor stub
	}

	
	public MessageBean(Message m){
		if(m!=null){
		this.transcode=m.transcode;
		this.content=m.content;
		this.token=m.token;
		}
	}


	@Override
	public String toString() {
		return  content;
	}
	
	
}
