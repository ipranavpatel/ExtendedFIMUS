package com.ywl.dmi.model;

import java.util.ArrayList;
import java.util.List;

public class NormalData {
  private int index;
  private List<NameValuePair> data;

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public List<NameValuePair> getData() {
    return data;
  }
  public void add(NameValuePair nameValue){
    if(data == null){
      data = new ArrayList<NameValuePair>();
    }
    data.add(nameValue);
  }

  public void setData(List<NameValuePair> data) {
    this.data = data;
  }
  public NameValuePair get(int index){
    return data.get(index);
  }
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    for(NameValuePair nameValue : data){
      str.append(nameValue.toString()).append("\t");
    }
    return str.toString();
  }

  public void add(String name, Object value) {
    NameValuePair data = new NameValuePair();
    data.setName(name);
    data.setValue(value);
    add(data);
  }

  public int size() {
    return data == null ? 0 : data.size();
  }
  @Override
  public boolean equals(Object obj) {
    if(obj == null){
      return false;
    }
    if(obj == this){
      return true;
    }
    if(obj instanceof NormalData){
      return ((NormalData) obj).index == this.index;
    }
    return false;
  }
}
