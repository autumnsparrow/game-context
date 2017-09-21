/**
 * 
 */
package com.sky.game.context.event;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.sky.game.context.util.GameUtil;

/**
 * @author sparrow
 *
 */
public class DefaultGameSession implements IGameSession {
	
	private static AtomicLong ID=new AtomicLong(1L);
	private long id;
	//private boolean focus;
	
	/**
	 * 
	 */
	public DefaultGameSession() {
		// TODO Auto-generated constructor stub
		attributes=GameUtil.getMap();
		id=ID.getAndIncrement();
		//this.focus=false;
		setAttribute(FOCUS, Boolean.valueOf(true));
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id=id;
	}

	private Map<Object,Object> attributes;

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#getAttribute(java.lang.Object)
	 */
	public Object getAttribute(Object key) {
		// TODO Auto-generated method stub
		Object obj=attributes.get(key);
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#getAttribute(java.lang.Object, java.lang.Object)
	 */
	public Object getAttribute(Object key, Object defaultValue) {
		// TODO Auto-generated method stub
		 if (containsAttribute(key)) {
		     return getAttribute(key);
		 } else {
		     setAttribute(key, defaultValue);
		     return defaultValue;
		 }
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#setAttribute(java.lang.Object, java.lang.Object)
	 */
	public Object setAttribute(Object key, Object value) {
		// TODO Auto-generated method stub
		Object obj=getAttribute(key);
		attributes.put(key, value);
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#setAttribute(java.lang.Object)
	 */
	public Object setAttribute(Object key) {
		// TODO Auto-generated method stub
	
		return setAttribute(key, Boolean.TRUE);
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#setAttributeIfAbsent(java.lang.Object, java.lang.Object)
	 */
	public Object setAttributeIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		 if (containsAttribute(key)) {
		     return getAttribute(key);
		 } else {
		     return setAttribute(key, value);
		 }
		 

	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#setAttributeIfAbsent(java.lang.Object)
	 */
	public Object setAttributeIfAbsent(Object key) {
		// TODO Auto-generated method stub
		
		if (containsAttribute(key)) {
		     return getAttribute(key);  // might not always be Boolean.TRUE.
		 } else {
		     return setAttribute(key);
		 }
		 
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#removeAttribute(java.lang.Object)
	 */
	public Object removeAttribute(Object key) {
		// TODO Auto-generated method stub
		Object obj=attributes.get(key);
		attributes.remove(key);
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#removeAttribute(java.lang.Object, java.lang.Object)
	 */
	public boolean removeAttribute(Object key, Object value) {
		// TODO Auto-generated method stub
		if (containsAttribute(key) && getAttribute(key).equals(value)) {
		     removeAttribute(key);
		     return true;
		 } else {
		     return false;
		 }
		 
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#replaceAttribute(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		if (containsAttribute(key) && getAttribute(key).equals(oldValue)) {
		     setAttribute(key, newValue);
		     return true;
		 } else {
		     return false;
		 }
		 
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#containsAttribute(java.lang.Object)
	 */
	public boolean containsAttribute(Object key) {
		// TODO Auto-generated method stub
		return attributes.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.sky.game.context.event.IGameSession#getAttributeKeys()
	 */
	public Set<Object> getAttributeKeys() {
		// TODO Auto-generated method stub
		return attributes.keySet();
	}

	
}
