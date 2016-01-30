package com.ywl.dmi.model;

public class VoteResult {
  private Column missingCol;
  private int index;//在数据源中的数据
  private Object norValue;
  private double voteResult;
  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }
  public Object getNorValue() {
    return norValue;
  }
  public void setNorValue(Object norValue) {
    this.norValue = norValue;
  }
  public double getVoteResult() {
    return voteResult;
  }
  public void setVoteResult(double voteResult) {
    this.voteResult = voteResult;
  }
  public Column getMissingCol() {
    return missingCol;
  }
  public void setMissingCol(Column missingCol) {
    this.missingCol = missingCol;
  }
  @Override
  public String toString() {
    return index + "\t" +  missingCol + "\t" + norValue;
  }

}
