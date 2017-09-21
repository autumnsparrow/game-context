/**
 * 
 */
package com.sky.game.context.annotation.introspector;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sparrow
 *
 */
public class ClassScanner {
	private static final Log logger=LogFactory.getLog(ClassScanner.class);
	/**
	 * 
	 */
	public ClassScanner() {
		// TODO Auto-generated constructor stub
	}
	
	
	private final static char DOT = '.';
    private final static char SLASH = '/';
    private final static String CLASS_SUFFIX = ".class";
    private final static String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the given '%s' package exists?";

    public final static List<Class<?>> find(final String scannedPackage) throws UnsupportedEncodingException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
       
        final String scannedPath = scannedPackage.replace(DOT, SLASH);
      //  logger.info("ScannedPackage:"+scannedPath);
        final Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(scannedPath);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage), e);
        }
        final List<Class<?>> classes = new LinkedList<Class<?>>();
        while (resources.hasMoreElements()) {
        	// checking if the resource is jar
        	URL url=resources.nextElement();
        	//logger.debug("jar!"+url.getPath());
        	if(url.getProtocol().equals("jar")){
        		classes.addAll(findJarClass(url,scannedPackage));
        	}else{
        		final File file = new File(url.getFile());
        		//logger.debug("ScannedPackage:"+file.getAbsolutePath());
        		classes.addAll(find(file, scannedPackage));
        	}
        }
        return classes;
    }
    
    private static void addJarEntry(JarEntry jarEntry,Set<String> result,String scannedPackage){
    		String name = jarEntry.getName().replace('/', DOT);
    	//	logger.info("Jar!:"+name);
          if (name.startsWith(scannedPackage)) { //filter according to the path
            String entry = name.substring(scannedPackage.length());
            result.add(entry);
          }
    	
   
    }
   
    
    private final static List<Class<?>> findJarClass(URL dirURL,final String scannedPackage) throws IOException {
    	String jarPath = dirURL.getPath().substring(0, dirURL.getPath().indexOf("!")); //strip out only the JAR file
    	JarInputStream jis = null;
    	try {
			URL url=new URL(jarPath);
			
			jis=new JarInputStream(url.openStream());

			JarEntry jarEntry = jis.getNextJarEntry();
			Set<String> result = new HashSet<String>();
			while(jarEntry!=null){
				addJarEntry(jarEntry,result,scannedPackage);
				jarEntry=jis.getNextJarEntry();
			}
			
  
			List<Class<?>> classes=new LinkedList<Class<?>>();
			Iterator<String> it=result.iterator();
			while(it.hasNext()){
				String name=it.next();
				if (name.endsWith(CLASS_SUFFIX) && !name.contains("$")) {

			        final int beginIndex = 0;
			        final int endIndex = name.length() - CLASS_SUFFIX.length();
			        final String className = name.substring(beginIndex, endIndex);
			        final String resource = scannedPackage  + className;
		         //   logger.debug("ScannedPackage:"+resource+ " - name:"+name);
			        try {
			        	
			        	Class<?> clz=Class.forName(resource);
			           
			           // classes.add(Class.forName(resource));
			        	if(clz!=null){
			        		classes.add(clz);
			        	}
			            
			        } catch (ClassNotFoundException ignore) {
			        	logger.info("fatal:"+ignore.getMessage());
			        }catch (Exception ex){
			        	logger.info("fatal:"+ex.getMessage());
			        }
			        
			    }
			}
			return classes;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(jis!=null)
				jis.close();
		}
    	
    	throw new RuntimeException("Can't find the calsses");
    	//return null;
       // String result.toArray(new String[result.size()]);
    }
    

    private final static List<Class<?>> find(final File file, final String scannedPackage) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();
        
        if (file.isDirectory()) {
            for (File nestedFile : file.listFiles()) {
                classes.addAll(find(nestedFile, scannedPackage));
               
            }
        //File names with the $1, $2 holds the anonymous inner classes, we are not interested on them. 
        } else if (file.getName().endsWith(CLASS_SUFFIX) && !file.getName().contains("$")) {

            final int beginIndex = 0;
            final int endIndex = file.getName().length() - CLASS_SUFFIX.length();
            final String className = file.getName().substring(beginIndex, endIndex);
            try {
                final String resource = scannedPackage + DOT + className;
                classes.add(Class.forName(resource));
                logger.debug("ScannedPackage:"+resource);
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }


}
