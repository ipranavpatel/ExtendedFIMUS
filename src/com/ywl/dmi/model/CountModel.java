package com.ywl.dmi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountModel{
  private int totalCount;
  private int misCount;
  private Map<String, Integer> countMap = new HashMap<>();
  public void addOne(String val){
    if(val == null || "null".equals(val)){
      misCount ++;
    } else {
      Integer count = countMap.get(val);
      if(count == null){
        count = 0;
      }
      count ++;
      countMap.put(val, count);
    }
    totalCount ++;
  }
  
  public Integer get(String val){
    return countMap.get(val);
  }

  public Set<String> keys() {
    return countMap.keySet();
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public int getMisCount() {
    return misCount;
  }

  public void setMisCount(int misCount) {
    this.misCount = misCount;
  }

  public Map<String, Integer> getCountMap() {
    return countMap;
  }

  public void setCountMap(Map<String, Integer> countMap) {
    this.countMap = countMap;
  }
  @Override
  public String toString() {
    return countMap.toString();
  }

}
