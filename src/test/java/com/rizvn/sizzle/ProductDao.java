package com.rizvn.sizzle;

public class ProductDao implements SizzleAware {
  Sizzle sizzle;
  
  @Override
  public void setSizzle(Sizzle sizzle) {
    this.sizzle = sizzle;
  }
  
  public Sizzle getSizzle(){
    return this.sizzle;
  }

}
