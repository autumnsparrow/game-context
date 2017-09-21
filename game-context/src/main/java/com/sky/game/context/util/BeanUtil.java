/**
 * 
 */
package com.sky.game.context.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sparrow
 *
 */
public class BeanUtil {

	/**
	 * 
	 */
	public BeanUtil() {
		// TODO Auto-generated constructor stub
	}
	
	private static String[]  getExpression(String e){
		return e.split("\\.");
	}
	
	private static <T> T property(Object o,String p){
		// checking if the bean
		T t=null;
		if(o==null)
			return t;
		
		Object obj=null;
		try {
			Field f=null;
			
			f=o.getClass().getDeclaredField(p);
			f.setAccessible(true);
			obj=f.get(o);
			
		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(obj!=null){
			t=(T)obj;
		}
		
		return t;
	}
	
	public static <T> T call(Object o,String p,String m,Object ...args){
		T t=null;
		Object target=v(o,p);
		if(target!=null){
			t=call(target,m,args);
		}
		return t;
	}
	
	/**
	 * 
	 * o.getObjectA().getObjectB().getObjectC()
	 * o.objectA.objectB.getObjectC()
	 * performance of the reflection is bad.
	 * 
	 * 
	 * @param expression = (objectA.objectB.objectC)
	 * @return
	 */
	public static <T> T v(Object o,String e){
		T t=null;
		
		String[] p=getExpression(e);
		Object obj=o;
		for(int i=0;i<p.length;i++){
			obj=property(obj, p[i]);
		}
		t=(T)obj;
		
		return t;
	}
	
	public static int getInt(Object o,String e){
		int i=0;
		Integer ii=v(o,e);
		if(ii!=null){
			i=ii.intValue();
		}
		return i;
	}
	
	
	public static long getLong(Object o,String e){
		long l=0;
		Long ll=v(o,e);
		if(ll!=null){
			l=ll.longValue();
		}
		return l;
	}
	
	
	public static <T> T call(Object o,String method,Object args[]){
		Class<?> clz=o.getClass();
		Method mm=null;
		for(Method m:clz.getDeclaredMethods()){
			if(m.getName().equals(method)){
				mm=m;
				break;
			}
		}
		
		Object obj=null;
		if(mm!=null){
			try {
				mm.setAccessible(true);
				obj=mm.invoke(o, args);
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
		}
		return obj==null?null:(T)obj;
	}
	
	
	public static void invoke(Object o,String method,Object args[]){
		Class<?> clz=o.getClass();
		Method mm=null;
		for(Method m:clz.getDeclaredMethods()){
			if(m.getName().equals(method)){
				mm=m;
				break;
			}
		}
		
		
		if(mm!=null){
			try {
				mm.setAccessible(true);
				mm.invoke(o, args);
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
		}
		
	}
	
	
	public static class A{
		
		private B b;

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}
		
		
	}
	
	public static class B{
		C c;
		
	}
	
	
	public static class C{
		D d;
		
	}
	
	
	
	public static class D{
		private E value;

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}
		
		
		
	}
	
	public static class E{
		String s;
		int i;
		long l;
		float f;
		/**
		 * @param s
		 * @param i
		 * @param l
		 * @param f
		 */
		public E(String s, int i, long l, float f) {
			super();
			this.s = s;
			this.i = i;
			this.l = l;
			this.f = f;
		}
		@Override
		public String toString() {
			return "E [s=" + s + ", i=" + i + ", l=" + l + ", f=" + f + "]";
		}
		/**
		 * 
		 */
		public E() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		
	}
	
	
	
	public static void main(String args[]){
		
		A a=new A();
		B b=new B();
		C c=new C();
		D d=new D();
		E e=new E();
		e.s="AAAAA";
		e.i=0;
		e.l=10000L;
		e.f=0.01f;
		d.setValue(e);
		c.d=d;
		b.c=c;
		a.setB(b);
		
		
		
		long begin=System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			E localC=BeanUtil.v(a, "b.c.d.value");
		}
		System.out.println(" invoke duration:"+(System.currentTimeMillis()-begin));
		begin=System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			E localC=a.getB().c.d.getValue();
		}
		System.out.println(" invoke duration:"+(System.currentTimeMillis()-begin));
		
		begin=System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			B lb=a.getB();
			if(lb!=null){
				C lc=b.c;
				if(lc!=null){
					D ld=lc.d;
					if(ld!=null){
						E le=ld.getValue();
					}
				}
			}
			//E localC=a.getB().c.d.getValue();
		}
		System.out.println(" invoke duration:"+(System.currentTimeMillis()-begin));
		
		
	}
	
	
	

}
