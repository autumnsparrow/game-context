package com.sky.game.context.domain;

import com.sky.game.context.annotation.HandlerRequestType;
import com.sky.game.context.annotation.HandlerResponseType;
import com.sky.game.context.annotation.HandlerType;


public class ErrorBeans {

	public ErrorBeans() {
		// TODO Auto-generated constructor stub
	}
	
	@HandlerType(namespace="GameMina",enable=true,transcode="ERROR")
	@HandlerRequestType(transcode="ERROR")
	@HandlerResponseType(transcode="ERROR",responsecode="ERROR")
	public static class Request{
		private int state;
		private String transcode;
		private String message;
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		@Override
		public String toString() {
			return "Request [state=" + state + ", message=" + message + "]";
		}
		private Request(int state, String message) {
			super();
			this.state = state;
			this.message = message;
		}
		
		
		
		public String getTranscode() {
			return transcode;
		}
		public void setTranscode(String transcode) {
			this.transcode = transcode;
		}
		public Request() {
			super();
			// TODO Auto-generated constructor stub
		}
		public static Request get(int state,String transcode, String message){
			return new Request(state,transcode, message);
		}
		public Request(int state, String transcode, String message) {
			super();
			this.state = state;
			this.transcode = transcode;
			this.message = message;
		}
		
		
	}
	
	

}
