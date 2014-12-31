package com.rizvn.sizzle;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Lightweight dependency management library
 * @author riz
 */
public class Sizzle {
  private Map<Class<?>, Object> singletons      = new HashMap<Class<?>, Object>();
  private Map<String, Object>   namedSingletons = new HashMap<String, Object>();
  
  /**
   * @param propPath path to properties file, can be absolute or relative
   */
  public Sizzle(String propPath){
    if(propPath != null){
      loadProperties(propPath);
    }
  }
  
  /**
   * Default constructor no properties
   */
  public Sizzle(){}
  
  protected void loadProperties(String path){
    try{
      Properties props = new Properties();
      props.load(new FileInputStream(new File(path)));
      addSingletonBean(Properties.class, props, "ContextProperties");
    }catch(Exception ex){
      throw new SizzleException(ex);
    }
  }
  
  /**
   * Manually add singleton bean
   * @param bean Bean to add 
   * @param name Optional name of bean
   */
  public void addSingletonBean(Object bean, String ... name){
    Class<? extends Object> clazz = bean.getClass();
    addSingletonBean(clazz, bean, name);
  }
  
  /**
   * Manually add singleton bean
   * @param clazz Class of bean i.e Long.class
   * @param bean Bean to add 
   * @param name Optional name of bean
   */
  public void addSingletonBean(Class<?> clazz, Object bean, String ... name){
    singletons.put(clazz, bean);
    if(name.length != 0){
      namedSingletons.put(name[0], bean);
    }
  }

  /**
   * Manually add singleton bean, this one is usefull for adding generics
   * @param bean Bean to add 
   * @param name name of bean
   */
  public void addSingletonBean(String beanName, Object bean){
    namedSingletons.put(beanName, bean);
  }
  
  private synchronized Sizzle registerSingleton(Class<?> clazz){
    try {
      //double safety when creating singleton from constructor to avoid race condtion
      if(!singletons.containsKey(clazz)){
        Object newInstance = clazz.newInstance();
        
        //if instance implements sizzleaware, call its sizzle setter
        if(newInstance instanceof SizzleAware){
          ((SizzleAware) newInstance).setSizzle(this);
        }
        singletons.put(clazz, newInstance);
      }
    } catch (Exception e){
      throw new SizzleException(e);
    }
    return this;
  }
  
  /**
   * Get singleton if one exists, otherwise creates
   * one using default constuctor
   * @param clazz
   * @return instance of the class
   */
  @SuppressWarnings("unchecked")
  public <T> T getOrCreate(Class<?> clazz){
    if(!singletons.containsKey(clazz)){
      registerSingleton(clazz);
    }
    return (T) singletons.get(clazz);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Class<?> clazz){
    if(!singletons.containsKey(clazz)){
      throw new SizzleException("Bean of type ["+ clazz + "] could not be found");
    }
    return (T) singletons.get(clazz);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T get(String name){
    return (T) namedSingletons.get(name);
  }

  public Map<Class<?>, Object> getSingletons() {
    return singletons;
  }
}
