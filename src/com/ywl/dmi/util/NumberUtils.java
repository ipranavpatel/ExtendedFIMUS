package com.ywl.dmi.util;

import java.math.BigDecimal;

public class NumberUtils {
  public static double round(Double d, int scale){
    if (d == null){
      return 0.0;
    }
    BigDecimal decimal = new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
    return decimal.doubleValue();
  }
}
