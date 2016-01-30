package com.ywl.dmi.model;

import java.math.BigDecimal;

/**
 * 分类区间
 *
 */
public class CalModel {
  private double max;
  private double min;

  public Double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public Double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public Double getRange() {
    return new BigDecimal(Math.sqrt(max - min + 1)).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
  }
  
  public Integer getRangeInteger() {
    return Double.valueOf(Math.sqrt(max - min + 1)).intValue();
  }

  @Override
  public String toString() {
    return "max=" + max + ", min=" + min + ", range=" + getRange();
  }
}
