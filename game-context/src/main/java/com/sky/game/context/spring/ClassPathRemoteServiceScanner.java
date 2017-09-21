/**
 * 
 */
package com.sky.game.context.spring;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

/**
 * @author sparrow
 *
 */
public class ClassPathRemoteServiceScanner extends
		ClassPathBeanDefinitionScanner {
	
	 private Class<?> markerInterface;

	/**
	 * @param registry
	 */
	public ClassPathRemoteServiceScanner(BeanDefinitionRegistry registry) {
		super(registry);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param registry
	 * @param useDefaultFilters
	 */
	public ClassPathRemoteServiceScanner(BeanDefinitionRegistry registry,
			boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param registry
	 * @param useDefaultFilters
	 * @param environment
	 */
	public ClassPathRemoteServiceScanner(BeanDefinitionRegistry registry,
			boolean useDefaultFilters, Environment environment) {
		super(registry, useDefaultFilters, environment);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	   * Configures parent scanner to search for the right interfaces. It can search
	   * for all interfaces or just for those that extends a markerInterface or/and
	   * those annotated with the annotationClass
	   */
	  public void registerFilters() {
	    boolean acceptAllInterfaces = true;

	   

	    if (acceptAllInterfaces) {
	      // default include filter that accepts all classes
	      addIncludeFilter(new TypeFilter() {
	        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
	          return true;
	        }
	      });
	    }

	    // exclude package-info.java
	    addExcludeFilter(new TypeFilter() {
	      public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
	        String className = metadataReader.getClassMetadata().getClassName();
	        return className.endsWith("package-info");
	      }
	    });
	  }

	
	
	 /**
	   * Calls the parent search that will search and register all the candidates.
	   * Then the registered objects are post processed to set them as
	   * MapperFactoryBeans
	   */
	  @Override
	  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
	    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

	    if (beanDefinitions.isEmpty()) {
	      logger.warn("No RemoteService mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
	    } else {
	      for (BeanDefinitionHolder holder : beanDefinitions) {
	        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

	        if (logger.isDebugEnabled()) {
	          logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() 
	              + "' and '" + definition.getBeanClassName() + "' mapperInterface");
	        }

	        // the mapper interface is the original class of the bean
	        // but, the actual class of the bean is MapperFactoryBean
	        definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
	        definition.setBeanClass(RemoteServiceFactoryBean.class);

//	        definition.getPropertyValues().add("addToConfig", this.addToConfig);
//
	        boolean explicitFactoryUsed = false;
//	        if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
//	          definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
//	          explicitFactoryUsed = true;
//	        } else if (this.sqlSessionFactory != null) {
//	          definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
//	          explicitFactoryUsed = true;
//	        }
//
//	        if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
//	          if (explicitFactoryUsed) {
//	            logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
//	          }
//	          definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
//	          explicitFactoryUsed = true;
//	        } else if (this.sqlSessionTemplate != null) {
//	          if (explicitFactoryUsed) {
//	            logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
//	          }
//	          definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
//	          explicitFactoryUsed = true;
//	        }

	        if (!explicitFactoryUsed) {
	          if (logger.isDebugEnabled()) {
	            logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
	          }
	          definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
	        }
	      }
	    }

	    return beanDefinitions;
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
	    return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
	    if (super.checkCandidate(beanName, beanDefinition)) {
	      return true;
	    } else {
	      logger.warn("Skipping MapperFactoryBean with name '" + beanName 
	          + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
	          + ". Bean already defined with the same name!");
	      return false;
	    }
	  }


}
