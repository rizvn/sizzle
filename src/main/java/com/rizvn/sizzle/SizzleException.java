package com.rizvn.sizzle;

/**
 * A sizzle exception
 * @author riz
 */
public class SizzleException extends RuntimeException{
  
  private static final long serialVersionUID = -6007146458523144337L;

  public SizzleException(Exception ex) {
    super(ex);
  }

  public SizzleException(String msg) {
    super(msg);
  }
}
