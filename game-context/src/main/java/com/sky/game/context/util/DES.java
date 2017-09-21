package com.sky.game.context.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sky.game.context.GameContextGlobals;
import com.sky.game.context.util.HexDump;
 
 
/**
 * DES{3DES加密解密的工具类 }
 */
public class DES {
    private  final String Algorithm = "DESede"; 
    private static DES des = null;
    protected String _genaratedKey = "";
    protected byte[] _desKey = null;
    
    /**
     * 单例 
     * @return
     */
    public static DES des() {
    	if (des == null)
    		des = new DES();
    	return des;
    }
    
    /**
     * 加密方法
     * @param src Դ源数据的字节数组
     * @return 
     */
    public byte[] encrypt(String _desKey, byte[] src) {
        try {
             SecretKey deskey = new SecretKeySpec(desKey(_desKey), Algorithm);    //生成密钥Կ
             Cipher c1 = Cipher.getInstance(Algorithm);    //实例化负责加密/解密的Cipher工具类
             c1.init(Cipher.ENCRYPT_MODE, deskey);    //初始化为加密模式
             return c1.doFinal(src);
         } catch (java.security.NoSuchAlgorithmException e1) {
             e1.printStackTrace();
         } catch (javax.crypto.NoSuchPaddingException e2) {
             e2.printStackTrace();
         } catch (java.lang.Exception e3) {
             e3.printStackTrace();
         }
         return null;
     }
    
    /**
     * 解密函数
     * @param src 密文的字节数组
     * @return
     */
    public byte[] decrypt(String _desKey, byte[] src,int offset,int len) {      
        try {
//        	src = Base64.decodeBase64(src);
            SecretKey deskey = new SecretKeySpec(desKey(_desKey), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm,"BC");
            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式
            return c1.doFinal(src,offset,len);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
     }
    
    /**
     * 随机生成一个Key
     * @return
     */
	public String genrateKey(String key) {
		List<String> letters = Arrays.asList(key.split(""));
		Collections.shuffle(letters);
		_genaratedKey = "";
		for (String letter : letters) {
			_genaratedKey += letter;
		}
		
		_desKey = desKey(_genaratedKey);
		
		return _genaratedKey;
	}
    /*
     * 根据字符串生成密钥字节数组 
     * @param keyStr 密钥字符串
     */
    private byte[] desKey(String keyStr) {
        byte[] key = new byte[24];     //声明一个24位的字节数组，默认里面都是0
		try {
			 byte[] temp = keyStr.getBytes("UTF-8");
			 /*
	         * 执行数组拷贝
	         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
	         */
	        if(key.length > temp.length){
	        	 //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
	            System.arraycopy(temp, 0, key, 0, temp.length);
	        }else{
	        	//如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
	            System.arraycopy(temp, 0, key, 0, key.length);
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}    
       
        return key;
    } 
    
    
    public static class Request{
    	String phone;
    	long moneyAmount;
    	long timestamp;
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public long getMoneyAmount() {
			return moneyAmount;
		}
		public void setMoneyAmount(long moneyAmount) {
			this.moneyAmount = moneyAmount;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		@Override
		public String toString() {
			return "Request [phone=" + phone + ", moneyAmount=" + moneyAmount
					+ ", timestamp=" + timestamp + "]";
		}
		
    	
    }
    
    public static class Response{
    	int  state;
    	String message;
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
    	
    }
    
    public static class ProtocolEntry {

    	/**
    	 * 
    	 */
    	public ProtocolEntry() {
    		// TODO Auto-generated constructor stub
    	}
    	
    	private Integer  code;
    	private Integer status;
    	private String content;
    	private Integer elapsed;
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Integer getElapsed() {
			return elapsed;
		}
		public void setElapsed(Integer elapsed) {
			this.elapsed = elapsed;
		}
		@Override
		public String toString() {
			return "ProtocolEntry [code=" + code + ", status=" + status
					+ ", content=" + content + ", elapsed=" + elapsed + "]";
		}
    	
    	
    	

    }
    
    
    

    public static  String encrypt(String json,String desKey) throws UnsupportedEncodingException{
    //	String json=GameContextGlobals.getJsonConvertor().format(obj);
    	byte[]  jsonBytes=json.getBytes("UTF-8");
    	byte[] encrypt = DES.des().encrypt(desKey,jsonBytes);
    	String req=org.apache.commons.codec.binary.Base64.encodeBase64String(encrypt);
    	return req;
    	
    }
    
    public static String decrypt(String req,String desKey) throws UnsupportedEncodingException{
    	byte[] reqBase64=org.apache.commons.codec.binary.Base64.decodeBase64(req);
    	byte[] decrypt = DES.des().decrypt(desKey,reqBase64,0,reqBase64.length);
    	String ming = new String(decrypt,"UTF-8");
    	
    	return ming;
    }
    
   
    
    public static  String encrypt(Object obj,String desKey) throws UnsupportedEncodingException{
    	String json=GameContextGlobals.getJsonConvertor().format(obj);
    	byte[]  jsonBytes=json.getBytes("UTF-8");
    	byte[] encrypt = DES.des().encrypt(desKey,jsonBytes);
    	String req=org.apache.commons.codec.binary.Base64.encodeBase64String(encrypt);
    	return req;
    	
    }
    
    public static <T> T decrypt(String req,String desKey,Class<?> clz) throws UnsupportedEncodingException{
    	byte[] reqBase64=org.apache.commons.codec.binary.Base64.decodeBase64(req);
    	byte[] decrypt = DES.des().decrypt(desKey,reqBase64,0,reqBase64.length);
    	String ming = new String(decrypt,"UTF-8");
    	T t=GameContextGlobals.getJsonConvertor().convert(ming, clz);
    	return t;
    }
    
    public static String request(int code,Object req,String desKey) throws UnsupportedEncodingException{
    	ProtocolEntry entry=new ProtocolEntry();
    	entry.code=code;
    	entry.content=encrypt(req, desKey);
    	entry.elapsed=Integer.valueOf(0);
    	entry.status=Integer.valueOf(0);
    	System.out.println(entry.toString());
    	String r=GameContextGlobals.getJsonConvertor().format(entry);
    	return r;
    }
    
    public static ProtocolEntry handRequest(String req){
    	ProtocolEntry entry=GameContextGlobals.getJsonConvertor().convert(req, ProtocolEntry.class);
    	return entry;
    }

    
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    	String desKey="504107060920821";
    	String str = "123456789kjasfdjkba";
    	desKey="9199437611949";
    	desKey="504107060920821";
    	str= "GU0002&&{\"account\":\"zhanghui\",\"password\":\"123456\",\"deviceId\":\"1419939679419\"}";
    	byte[]  jsonBytes =str.getBytes("UTF-8");
    	byte[] encrypt = DES.des().encrypt(desKey,jsonBytes);
    	//String base64=org.apache.commons.codec.binary.Base64.encodeBase64String(encrypt);
    	
    	System.out.println(HexDump.dumpHexString(encrypt));
    	byte[]  raw=DES.des().decrypt(desKey,encrypt, 0,encrypt.length);
    	System.out.println(HexDump.dumpHexString(raw));
    	String content=new String(raw,"UTF-8");
    	System.out.println(content);
//    	Request request=new Request();
//    	request.phone="13810715929";
//    	request.moneyAmount=100;// fen
//    	request.timestamp=System.currentTimeMillis();
//    	
//    	String req=request(10008, request, desKey);
//    	System.out.println("Request->"+req);
//    	
//    	
//    	ProtocolEntry entry=handRequest(req);
//    	
//    	Request rr=decrypt(entry.content, desKey, Request.class);
//    	System.out.println(rr.toString());
//    	
//    	Response resp=new Response();
//    	resp.message="ok";
//    	resp.state=0;
//    	
//    	String response=request(entry.code, resp, desKey);
//    	
//    	System.out.println("Respone->"+response);
    	
    	
    	
    	
	}
}
