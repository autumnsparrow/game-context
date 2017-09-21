/**
 * 
 */
package com.sky.game.context.handler;

/**
 * 
 * ValueHolder to hold the return parameter in the function parameter.
 * 
 *  Declare:
 *  class Handler{
 *  	boolean handle(Request request,ValueHolder<Response> holder){
 *  		
 *  		// process the request.
 *  		// fill the reponse
 *  		Response response=....;
 *  		// 
 *  		holder.value=response.
 *  	}
 *  
 *  }
 *  
 *  Usage:
 *  
 *  ValueHolder<Response> holder==new ValueHolder<Response>();
 *  Request request= ...;
 *  Handler hanlder=new Hander();
 *  // sync method invoke
 *  handler.handle(request,holder);
 *  // retrieve the holder value
 *  Response resp=holder.value;
 * 
 * 
 * @author sparrow
 *
 */
public class ValueHolder <T>{
	
	public T value;
	
	public Object extra;
	
	public boolean enableExtra;

	/**
	 * 
	 */
	public ValueHolder() {
		// TODO Auto-generated constructor stub
	}

	

	public ValueHolder(T t) {
		super();
		this.value = t;
	}
	
	

}
