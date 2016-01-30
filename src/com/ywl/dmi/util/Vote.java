package com.ywl.dmi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ywl.dmi.model.CalModel;
import com.ywl.dmi.model.CoApperationMatix;
import com.ywl.dmi.model.CoApperationModel;
import com.ywl.dmi.model.Column;
import com.ywl.dmi.model.MissingData;
import com.ywl.dmi.model.NormalData;
import com.ywl.dmi.model.VoteResult;

public class Vote {
  private Map<String, Set<Object>> norCategories = new HashMap<>(); // 序列化后的分类
  private Column[] columns;
  List<MissingData> newMissingDatas = new ArrayList<MissingData>();
  Set<Column> misColumns = new HashSet<Column>();
  private HtmlWriter html;

  public Set<Column> getMisColumns() {
    return misColumns;
  }

  public void setMisColumns(Set<Column> misColumns) {
    this.misColumns = misColumns;
  }

  public Vote(Map<String, Set<Object>> norCategories, Column[] columns, HtmlWriter html) {
    super();
    this.norCategories = norCategories;
    this.columns = columns;
    this.html = html;
  }

  /**
   * 第一步 获取String类型的投票结果
   * 
   * @param sourceDataSet
   * @param d0
   * @param generlizeDateSet
   * @param classMap
   * @param lamda
   * @param normalized
   * @param writer 
   * @return
   */
  public List<VoteResult> doVote(List<NormalData> sourceDataSet, int[][] d0,
      List<NormalData> generlizeDateSet, CoApperationMatix classMap,
      double lamda, Map<String, Map<String, Double>> normalized) {
    newMissingDatas.clear();
    List<MissingData> missingDatas = getMissingDatas(d0, generlizeDateSet);
//    System.out.println(missingDatas);
    List<VoteResult> firstResult = CSR(generlizeDateSet, missingDatas,
        classMap, lamda, normalized, null);
    List<VoteResult> result = new ArrayList<VoteResult>();
    for (VoteResult vr : firstResult) {
      if (vr.getMissingCol().isClassify()) {
        MissingData missData = new MissingData();
        missData.setIndex(vr.getIndex());
        missData.setMisCol(vr.getMissingCol());
        newMissingDatas.add(missData);
//        System.out.println(missData);
        misColumns.add(vr.getMissingCol());
      } else {
        result.add(vr);
      }
    }
    return result;
  }

  public List<MissingData> getNewMissingDatas() {
    return newMissingDatas;
  }

  public void setNewMissingDatas(List<MissingData> newMissingDatas) {
    this.newMissingDatas = newMissingDatas;
  }

  /**
   * 对缺失矩阵进行CSR运算(投票)，填充缺失值 TODO 注意，此方法未完成
   * 
   * @param sourceDataSet
   * @param d0
   * @param generlizeDateSet
   *          分类后的数组
   * @param classMap
   * @param lamda
   * @param calMap 
   * @param writer 
   * @return
   */
  public List<VoteResult> CSR(List<NormalData> datas, List<MissingData> missingDatas, CoApperationMatix classMap, double lamda,
      Map<String, Map<String, Double>> normalized, Map<String, CalModel> calMap) {
    /*
     * TODO 1. 计算 Cxl/fl 一 个个遍历属性值 当x = MS/PHD时， 对应每个年龄/薪水/职位为某个值的个数 fl表示
     * 年龄/薪水/职位 为某个值在整个集合中的个数
     */
    // double vtx = 0;
    List<VoteResult> voteResult = new ArrayList<VoteResult>();
    Persion persion = new Persion(norCategories, html);
    html.write("<table><tr><th>vote col</th><th>vote_value</th>");
    for (MissingData mis : missingDatas) { // 遍历缺失数据的记录
      Column misCol = mis.getMisCol();
//      System.out.println(misCol);
      System.out.println("---------------start vote for record"
          + mis.getIndex() + " for column : " + misCol.getName());
      String misColName = misCol.getName();
      VoteResult result = new VoteResult();
      result.setMissingCol(misCol);
      NormalData data = datas.get(mis.getIndex());
      result.setIndex(mis.getIndex());
      for (Object cat : norCategories.get(misColName)) { // 遍历缺失 属性对应的分类
        Map<String, CoApperationModel> catCount = classMap.getInner(misColName,
            cat);
        double vtx = 0;
        for (Column colTmp : columns) {
          double vnx = 0, vsx = 0;
          if (colTmp.equals(misCol)) {
            continue;
          }
          Object outVal = data.get(colTmp.getIndex())
              .getValue();
          if(outVal == null){
            continue;
          }
          String misKey = colTmp.getName() + "_" + outVal;
          Map<String, Double> norMap = normalized.get(colTmp.getName());
          Integer count = null;
          // System.out.println("norMap" + norMap);
          if (catCount.get(misKey) != null)
            count = catCount.get(misKey).size();
          if (count == null) {
            count = 0;
          }
          if (lamda > 0) {
            vnx = count / getCateCount(misKey, classMap);
          }
          if (lamda < 1) {
            // System.out.println("out key : " + misKey);
            for (Object oCat : norCategories.get(colTmp.getName())) {
              String okey = colTmp.getName() + "_" + oCat;
              // count = catCount.get(okey);
              if (count == null) {
                count = 0;
              }
              // System.out.println("cat = " + cat + ",,, okey = " + okey);
              if(norMap.get(outVal + "_" + oCat) == null){
//                System.out.println(outVal + "_" + oCat + " ;; " + norMap);
                continue;
              }
              double h = NumberUtils.round(count / getCateCount(okey, classMap)
                  * norMap.get(outVal + "_" + oCat), 3);
              vsx += h;// TODO get Slpa
              // System.out.println(colTmp.getName() + "\t" + oCat + "\t" + h +
              // "\t" + vsx);
            }
          }
          double vpx = (vnx * lamda + vsx * (1 - lamda))
              * persion.getPersion(misCol, colTmp, classMap);
          ;// kjp ∈ K is the correlation between the jth and pth attribute
          vtx += vpx;
        }
        html.write("<tr><td>").write(misColName + "_" + cat).write("</td><td>").write(vtx).write("</td></tr>");
//        System.out.println(misColName + "\t" + cat + "\t" + vtx);
        if (vtx > result.getVoteResult()) {
          result.setNorValue(cat);
          result.setVoteResult(vtx);
        }
      }
      if(result == null || result.getNorValue() == null && calMap != null){
        CalModel norCal = calMap.get(misCol.getName());
        result.setNorValue((norCal.getMax() + norCal.getMin())/2);
      }
      voteResult.add(result);
      html.write("<tr><tr><th>winner</th><td>").write(result.getNorValue()).write("</td></tr>");
    }
    html.write("</table>");
//    System.out.println(voteResult);
    return voteResult;
  }

  private double getCateCount(String misKey, CoApperationMatix classMap) {
    double count = 0;
    Map<String, CoApperationModel> map = classMap.getValues().get(misKey);
//    System.out.println(misKey + ",," + classMap.getValues());
    if(map == null){
      return 0.0;
    }
    Set<Entry<String, CoApperationModel>> entries = map.entrySet();
    for (Entry<String, CoApperationModel> en : entries)
      count += en.getValue().size();
    return count;
  }

  private List<MissingData> getMissingDatas(int[][] d,
      List<NormalData> generlizeDateSet) {
    List<MissingData> list = new ArrayList<MissingData>();
    for (int i = 0; i < d.length; i++) {
      for (int j = 0; j < d[i].length; j++) {
        if (d[i][j] == 1) {
          MissingData md = new MissingData();
          md.setIndex(i);
          md.setMisCol(columns[j]);
          list.add(md);
        }
      }
    }
    return list;
  }

}
