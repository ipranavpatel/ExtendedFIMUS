package com.ywl.dmi.model;

import java.util.ArrayList;
import java.util.List;

public class VoteResultMatix {
  private int index;// 在数据源中的数据
  private List<VoteResult> norValue = new ArrayList<>();

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void addMissValue(VoteResult voRes) {
    norValue.add(voRes);
  }

  public List<VoteResult> getNorValue() {
    return norValue;
  }

  public void setNorValue(List<VoteResult> norValue) {
    this.norValue = norValue;
  }

  @Override
  public String toString() {
    return index + "\t" + norValue;
  }
 
}
