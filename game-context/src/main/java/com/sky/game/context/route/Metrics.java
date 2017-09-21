/**
 * 
 */
package com.sky.game.context.route;

/**
 * @author sparrow
 *
 */
public class Metrics {
	String k;
	long b;
	int d;
	
	
	
	
	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public long getB() {
		return b;
	}

	public void setB(long b) {
		this.b = b;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	
	public Metrics(String k) {
		super();
		this.k = k;
	
	}
	
	public Metrics() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void e(){
		this.d=(int) (System.currentTimeMillis()-this.b);
		
	}
	
	public void reset(){
		this.b = System.currentTimeMillis();
		this.d=0;
	}

	@Override
	public String toString() {
		return "{"+k+":"+this.d+"}";
	}
}
