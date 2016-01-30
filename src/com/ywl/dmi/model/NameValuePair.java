package com.ywl.dmi.model;

public class NameValuePair {
  private String name;
  private Object value;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
  @Override
  public String toString() {
    return name + " = " + value;
  }
  @Override
  public boolean equals(Object obj) {
    if(obj == null)
      return false;
    if(obj == this)
      return true;
    if(obj instanceof NameValuePair){
      NameValuePair out = (NameValuePair) obj;
      return out.name.equals(this.name) && out.value.equals(this.value);
    }
    return false;
  }
}
