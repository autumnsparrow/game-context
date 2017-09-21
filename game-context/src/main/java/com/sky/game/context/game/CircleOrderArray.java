/**
 * 
 */
package com.sky.game.context.game;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.util.GameUtil;

/**
 * 
 * 
 * circle order array is core of the Texas'em poker game.
 * 
 * @author sparrow
 *
 */
public class CircleOrderArray <T> implements Cloneable{
	
	private static final Log logger=LogFactory.getLog(CircleOrderArray.class);

	
	private List<T> values;
	
	private int holder;
	private int mover;
	private int length;
	
	
	
	
	
	public CircleOrderArray<T> clone()  {
		// TODO Auto-generated method stub
		return new CircleOrderArray<T>(values, holder, mover, length);
	}

	/**
	 * 
	 */
	public CircleOrderArray() {
		// TODO Auto-generated constructor stub
	}
	

	private CircleOrderArray(List<T> values, int holder, int mover, int length) {
		super();
		this.values = values;
		this.holder = holder;
		this.mover = mover;
		this.length = length;
	}

	/**
	 * This data structure must support thread-safe operation.
	 * @param values
	 */
	public CircleOrderArray(T[] values) {
		super();
		this.values=GameUtil.asList(values);
		this.length=this.values.size();
		this.holder=0;
		this.mover=0;
		
	}
	
	public void reset(){
		this.holder=0;
		this.mover=0;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized boolean isMoveAroud(){
		return this.mover==this.holder;
	}
	
	public synchronized T currentMover(){
		//logger.info("currentMover:"+toString());
		return values.get(this.mover);
	}
	
	public synchronized T currentHolder(){
		//logger.info("currentHolder:"+toString());
		return this.values.get(this.holder);
	}
	
	private  void increaseMover(){
		this.mover=this.mover+1;
		this.mover=this.mover%this.length;
	}
	
	private void increaseHolder(){
		this.holder++;
		this.holder=this.holder%this.length;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized T moveNext(){
		increaseMover();
		T t=values.get(this.mover);
		return t;
	}
	/**
	 * 
	 * @return
	 */
	public  synchronized T holdNext(){
		increaseHolder();
		T t=values.get(this.holder);
		return t;
	}
	
	
	public synchronized void holderMoveTo(T t){
		int position=getObjectPosition(t);
		holderMoveTo(position);
	}
	
	public synchronized void holderMoveTo(int position){
		this.holder=position;
		this.mover=this.holder;
	}
	
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public synchronized int getObjectPosition(T t){
		int pos=0;
		for(int i=0;i<this.values.size();i++){
			if(this.values.get(i).equals(t)){
				pos=i;
				break;
			}
		}
		return pos;
	}
	/**
	 * 
	 * when remove the element,the current holder and mover also changed.
	 * 
	 * @param t
	 */
	public synchronized void remove(T t){
		// find the object current position.
		//int objectPosition=getObjectPosition(t);
		//
		// before the delete
		T objectHolder=this.values.get(this.holder);
		T objectMover=this.values.get(this.mover);
		
		
		this.holder=this.getObjectPosition(objectHolder);
		this.mover=this.getObjectPosition(objectMover);
		this.values.remove(t);
		
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "CircleOrderArray [ holder=" + holder
				+ ", mover=" + mover + ", length=" + length + "]";
	}

	public static void main(String args[]){
		String values[]=new String[10];
		for(int i=0;i<10;i++){
			values[i]=String.valueOf(i);
		}
		
		CircleOrderArray<String> circleOrder=new CircleOrderArray<String>(values);
		
		//
		logger.info("mover move 3 steps");
		for(int i=0;i<3;i++){
			String p=circleOrder.moveNext();
			logger.info(p);
		}
		logger.info(circleOrder);
		
		logger.info("holder move 1 steps");
		String h=circleOrder.holdNext();
		logger.info(h);
		
		logger.info(circleOrder);
		logger.info("remove next holder");
		circleOrder.remove(circleOrder.currentHolder());
		
		logger.info(circleOrder);
		
		
		
		
		
	}
	
	
	

}
