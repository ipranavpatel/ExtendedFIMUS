package com.ywl.dmi.model;

import java.util.ArrayList;
import java.util.List;

public class CoApperationModel {
  private String columnName;
  private Object columnVal;
  private List<NormalData> data = new ArrayList<NormalData>();
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public Object getColumnVal() {
    return columnVal;
  }
  public void setColumnVal(Object columnVal) {
    this.columnVal = columnVal;
  }
  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return String.valueOf(data.size());
  }
  public List<NormalData> getData() {
    return data;
  }
  public void setData(List<NormalData> data) {
    this.data = data;
  }
  public void add(NormalData data){
    this.data.add(data);
  }
  public boolean contains(NormalData data){
    return this.data.contains(data);
  }
  public int size(){
    return data.size();
  }
}
