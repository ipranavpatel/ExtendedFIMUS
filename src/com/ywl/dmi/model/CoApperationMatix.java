package com.ywl.dmi.model;

import java.util.HashMap;
import java.util.Map;

public class CoApperationMatix {
  private Map<String, Map<String, CoApperationModel>> values = new HashMap<>();
  private Map<String, Map<String, Integer>> totalCounts = new HashMap<>();

  public CoApperationModel get(NameValuePair out, NameValuePair inner) {
    Map<String, CoApperationModel> map = values.get(out.getName() + "_" + out.getValue());
    if(map == null)
      return null;
    return map.get(inner.getName() + "_" + inner.getValue());
  }
  
  public CoApperationModel get(String out, String inner){
    if(values.get(out) == null){
      return null;
    }
    return values.get(out).get(inner);
  }

  public Map<String, CoApperationModel> getInner(String name, Object val){
    return values.get(name + "_" + val);
  }
  public void put(NameValuePair value, NameValuePair inner, NormalData data) {
    String outKey = value.getName() + "_" + value.getValue();
    Map<String, CoApperationModel> map = values.get(outKey);
    Map<String, Integer> totalMap = totalCounts.get(outKey);
    if (map == null){
      map = new HashMap<String, CoApperationModel>();
      totalMap = new HashMap<String, Integer>();
    }
    String innerKey = inner.getName() + "_" + inner.getValue();
    CoApperationModel model = map.get(innerKey);
    Integer count = totalMap.get(inner.getName());
    if(count == null){
      count = 0;
    }
    if(model == null){
      model = new CoApperationModel();
      model.setColumnName(inner.getName());
      model.setColumnVal(inner.getValue());
    }
    count ++;
    totalMap.put(inner.getName(), count);
    totalCounts.put(outKey, totalMap);
    model.add(data);
    map.put(innerKey, model);
    values.put(outKey, map);
  }
  @Override
  public String toString() {
    return values.toString();
  }

  public Map<String, Map<String, CoApperationModel>> getValues() {
    return values;
  }

  public void setValues(Map<String, Map<String, CoApperationModel>> values) {
    this.values = values;
  }
  
  public int getTotal(NameValuePair out, String col){
    return totalCounts.get(out.getName() + "_" + out.getValue()).get(col);
  }

  public Map<String, Map<String, Integer>> getTotalCounts() {
    return totalCounts;
  }
  
}
