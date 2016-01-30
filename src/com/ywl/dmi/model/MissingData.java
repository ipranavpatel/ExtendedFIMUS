package com.ywl.dmi.model;

public class MissingData {
  private int index;//该缺失值在源数据中的索引
  private Column misCol;
  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }
  public Column getMisCol() {
    return misCol;
  }
  public void setMisCol(Column misCol) {
    this.misCol = misCol;
  }
  @Override
  public String toString() {
    return "(" + index + "," + misCol + ")";
  }
}
