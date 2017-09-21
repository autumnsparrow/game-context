/**
 * @sparrow
 * @4:07:32 PM
 * @coptyright Beijing BZWT Technology Co ., Ltd .
 */
package com.sky.game.context.id;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sparrow
 *
 */
public class LLGlobalIdGenerator implements IGlobalIdGenerator{
	private static final long MIN_SEQ=1000000L;
	private static final long MAX_SEQ=9999999L;
	public static class IdGenerator{
		Integer type;
		AtomicLong id;
		public IdGenerator(Integer type) {
			super();
			this.type = type;
			id=new AtomicLong(type*MIN_SEQ);
		}
		
		public synchronized Long next(){
			// if the id > type*MAX_SEQ.
			if(id.longValue()>type*MAX_SEQ){
				id=new AtomicLong(type*MIN_SEQ);
			}
			return Long.valueOf(id.getAndIncrement());
		}
		
		
	}
	
	
	public static LLGlobalIdGenerator g=new LLGlobalIdGenerator();
	
	ConcurrentHashMap<Integer,IdGenerator> ids_map;
	
	/**
	 * 
	 */
	public LLGlobalIdGenerator() {
		// TODO Auto-generated constructor stub
		ids_map=new ConcurrentHashMap<Integer, IdGenerator>();
	}
	
	/* (non-Javadoc)
	 * @see com.sky.game.landlord.id.IGlobalIdGenerator#getId(java.lang.String)
	 */
	public Long getId(int type) {
		// TODO Auto-generated method stub
		Long id=null;
		IdGenerator idGen=null;
		if(ids_map.containsKey(Integer.valueOf(type))){
			idGen=ids_map.get(Integer.valueOf(type));
		}else{
			idGen=new IdGenerator(Integer.valueOf(type));
			ids_map.put(Integer.valueOf(type),idGen);
		}
		
		id=idGen.next();
		return id;
		
	}

}
