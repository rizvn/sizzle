package com.rizvn.sizzle;

import org.junit.Assert;
import org.junit.Test;



public class SizzeTest {
  
  @Test
  public void testSizzleCreation(){
    Sizzle sizzle = new Sizzle(null);
    ProductDao productDao = sizzle.getOrCreate(ProductDao.class);
    Sizzle sizzleInstance = productDao.getSizzle();
    Assert.assertEquals(sizzle, sizzleInstance);
  }
}
