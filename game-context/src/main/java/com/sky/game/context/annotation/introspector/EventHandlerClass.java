/**
 * 
 * @Date:Nov 14, 2014 - 9:57:47 AM
 * @Author: sparrow
 * @Project :game-context 
 * 
 *
 */
package com.sky.game.context.annotation.introspector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sparrow
 *
 */
public class EventHandlerClass {
	// all the object has the id larger than 0,
	// when the id is -1, means is a manager of all objects.
	public static final Long DEFAULT_ID=Long.valueOf(-1);
	private static final Log logger=LogFactory.getLog(EventHandlerClass.class);
	
	String transcode;
	Class<?> eventHandlerClz;
	Method filter;
	Method handler;
	ConcurrentHashMap<Long, IIdentifiedObject> instance;
	private EventHandlerClass() {
		super();
		// TODO Auto-generated constructor stub
		instance=new ConcurrentHashMap<Long, IIdentifiedObject>();
	}
	
	public static EventHandlerClass obtain(){
		return new EventHandlerClass();
	}
	
	
	public IIdentifiedObject getObject(Long id){
		
		return id==null?null:instance.get(id);
	}
	public Collection<IIdentifiedObject> getObjects(){
		return instance.values();
	}
	
	public void addObserver(IIdentifiedObject obj){
		//if(!instance.containsKey(obj.getId())){
			instance.put(obj.getId(), obj);
		//}
	}
	
	public void removeObserver(IIdentifiedObject obj){
		if(instance.containsKey(obj.getId())){
			
			//instance.remove(obj.getId());
			//Object obj2=instance.get(obj.getId());
			if(obj instanceof IUniqueIdentifiedObject){
				IIdentifiedObject obj2=instance.get(obj.getId());
				IUniqueIdentifiedObject uobj=(IUniqueIdentifiedObject)obj;
				IUniqueIdentifiedObject uobj2=(IUniqueIdentifiedObject)obj2;
				if(uobj.equals(uobj2)){
					instance.remove(obj.getId());
				}else{
					throw new RuntimeException("IUniqueIdentifiedObject not equals:"+uobj.getId());
				}
			}else{
				instance.remove(obj.getId());
			}
		}
	}
	
	public boolean filter(Object object,Object evt){
		boolean ret=false;
		try {
			Object obj=filter.invoke(object, evt);
			if(obj instanceof Boolean){
				ret=((Boolean)obj).booleanValue();
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public void handle(Object object,Object evt) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
			//logger.info(handler.toString());
			handler.invoke(object, evt);
		
	}

	@Override
	public String toString() {
		return "\nEventHandlerClass [\ntranscode=" + transcode
				+ ", \neventHandlerClz=" + eventHandlerClz + ", \nfilter=" + filter
				+ ", \nhandler=" + handler + ", \ninstance=" + instance + "]";
	}
	
	
	

}
