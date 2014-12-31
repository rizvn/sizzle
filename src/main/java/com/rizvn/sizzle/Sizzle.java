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
  private static Sizzle ctx;
  private Map<Class<?>, Object> singletons      = new HashMap<Class<?>, Object>();
  private Map<String, Object>   namedSingletons = new HashMap<String, Object>();

  protected Sizzle(String propPath){
    if(propPath != null){
      loadProperties(propPath);
    }
  }
  
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
   * Create a new context witout any properties
   * @return A new context, Sizzle.getCtx() will now return the created context 
   */
  public static Sizzle newCtx(){
    return createContext(null);
  }
  
  /**
   * Create a new context with properties loaded from path
   * @return A new context, Sizzle.getCtx() will now return the created context 
   */
  public static Sizzle newCtx(String propertiesPath){
    return createContext(propertiesPath);
  }
 
  /**
   * Return Context create through newCtx() method 
   * @return
   */
  public static Sizzle getCtx(){
    if(ctx == null){ 
      throw new SizzleException("Context not created"); 
    }
    else{
      return ctx;
    }
  }
  
  
  private synchronized static Sizzle createContext(String propertiesPath){
    //double saftey to avoid race conditions when creating context
    ctx = new Sizzle(propertiesPath);
    return ctx;
  }
  
  /**
   * Use for replacing context, in tesing
   * @param ctx context to replace with
   */
  public static void setCtx(Sizzle actx){
    ctx = actx;
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
        singletons.put(clazz, clazz.newInstance());
      }
    } catch (Exception e){
      throw new SizzleException(e);
    }
    return this;
  }
  
  /**Get singleton if one exists, otherwise creates
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
