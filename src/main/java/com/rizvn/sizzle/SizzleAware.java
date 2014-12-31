package com.rizvn.sizzle;


/**
 * A sizzle aware singleton
 * @author riz
 */
public interface SizzleAware {
  
  /**
   * Method called upon instantiation
   * @param sizzle instance of sizzle which created the bean
   */
  public void setSizzle(Sizzle sizzle);
}
