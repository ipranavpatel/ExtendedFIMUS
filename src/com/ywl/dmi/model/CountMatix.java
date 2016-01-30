package com.ywl.dmi.model;

import java.util.HashMap;
import java.util.Map;

public class CountMatix {
  private Map<String, CountModel> countMap = new HashMap<>();
  
  public void add(String colName, String val){
    CountModel count = get(colName);
    if(count == null){
      count = new CountModel();
    }
    count.addOne(val);
    countMap.put(colName, count);
  }
  
  public CountModel get(String colName){
    return countMap.get(colName);
  }
}

