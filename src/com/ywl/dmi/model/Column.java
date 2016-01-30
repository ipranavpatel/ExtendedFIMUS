package com.ywl.dmi.model;

/**
 * Created by candice 简单对每个每一列的属性进行概括
 */
public class Column {
  /**
   * 列类型为字符串
   */
  public static final int TYPE_STRING = 0;
  /**
   * 列类型为整数
   */
  public static final int TYPE_INTEGER = 1;
  /**
   * 列类型为小数
   */
  public static final int TYPE_DOUBLE = 2;
  private int index;
  private String name;
  private int type;

  public Column() {
  }

  public Column(int index, String name, int type) {
    this.index = index;
    this.name = name;
    this.type = type;
  }
  
  public Column(int index, String name) {
    this.index = index;
    this.name = name;
    this.type = TYPE_STRING;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isClassify() {
    return type == TYPE_DOUBLE || type == TYPE_INTEGER;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
  @Override
  public String toString() {
    return "(index=" + index + ", name=" + name + ", isClassify=" + isClassify() + ")" ;
  }
  @Override
  public boolean equals(Object obj) {
    if(obj == null){
      return false;
    }
    if(obj instanceof Column){
      Column out = (Column)obj;
      return this.name.equals(out.name) && this.index == out.index;
    }
    return false;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
  
}
