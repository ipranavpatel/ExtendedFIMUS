package com.ywl.dmi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ywl.dmi.model.CoApperationMatix;
import com.ywl.dmi.model.CoApperationModel;
import com.ywl.dmi.model.Column;

public class Persion {
  private Map<String, Set<Object>> categories;
  private Map<String, Double> caches = new HashMap<String, Double>();
  private HtmlWriter html;
  public Persion(Map<String, Set<Object>> categories, HtmlWriter html){
    this.categories = categories;
    this.html = html;
  }
  private void calTotalCount(String outCol, String innerCol, CoApperationMatix classMap,
      Map<String, Double> exceptCountMap, Map<String, Double> execeptTotalCount){
    for(Object outCat : categories.get(outCol)){ 
      Map<String, CoApperationModel> countModel = classMap.getInner(outCol, outCat);
      String exceptKey = outCol + "_" + outCat + ";;" + innerCol;
//      System.out.println(exceptKey);
      Double count = exceptCountMap.get(exceptKey);
      if(count == null){
        count = 0.0;
      }
      String totalKey = outCol + "_" + innerCol;
      Double total = execeptTotalCount.get(totalKey);
      if(total == null){
        total = 0.0;
      }
      for(Object inCat : categories.get(innerCol)){
        CoApperationModel inCo = countModel.get(innerCol + "_" + inCat);
        if(inCo != null){
          count += inCo.size();
          total += inCo.size();
        }
      }
      html.write("<p> except : ").write(exceptKey).write("=").write(count).write("</p>");
      exceptCountMap.put(exceptKey, count);
      html.write("<p> total : ").write(totalKey).write("=").write(total).write("</p>");
      execeptTotalCount.put(totalKey, total);
    }
  }
  
  private void calPersionT(String outCol, String innerCol, CoApperationMatix classMap,
      Map<String, Double> exceptCountMap, Map<String, Double> execeptTotalCount,
      Map<String, Double> exceptTMap, Map<String, Double> exceptTotalT){
    html.write("<h2> cal persion T value ").write(outCol).write(" ").write(innerCol).write("  </h2>");
    for(Object cat : categories.get(outCol)){
      String misKey = outCol + "_" + cat + ";;" + innerCol;
      Double mCount = exceptCountMap.get(misKey);
      if(mCount == null)
        mCount = 0.0;
      for(Object inCat : categories.get(innerCol)){
//        CoApperationModel coModel = innerCount.get(inCat);
        String exceptKey = innerCol + "_" + inCat + ";;" + outCol;
        Double eCount = exceptCountMap.get(exceptKey);
        if(eCount == null)
          eCount = 0.0;
//        System.out.println(misKey + " excepts===" + mCount + ";" + eCount + ";");
        double count = execeptTotalCount.get(outCol + "_" + innerCol);
        double except = 0.0;
        if(count  > 0){
          except = NumberUtils.round(mCount * eCount / count, 3);
        }
        html.write("<p>").write("column=").write(misKey);
        double t = mCount - except;
        if(except > 0.0){
          t = t * t / except;
          t = NumberUtils.round(t, 3);
          Double total = exceptTotalT.get(outCol + "_" + innerCol);
          if(total == null)
            total = 0.0;
          total += t;
          html.write(misKey + "_" + inCat).write(" : ").write(t).writeln("</p>");
          exceptTotalT.put(outCol + "_" + innerCol, NumberUtils.round(total, 3));
          exceptTMap.put(misKey + "_" + inCat, t);
        }
      }
    }
  }
  private void calPersion(String misColName, String exceptColName, CoApperationMatix classMap){
    if(caches.containsKey(misColName + "_" + exceptColName)){
      return;
    }
    Map<String, Double> exceptCountMap = new HashMap<String, Double>();
    Map<String, Double> execeptTotalCount = new HashMap<String, Double>();
    html.write("<h2> count cates</h2>");
    calTotalCount(misColName, exceptColName, classMap, exceptCountMap, execeptTotalCount);
    calTotalCount(exceptColName, misColName, classMap, exceptCountMap, execeptTotalCount);
//    System.out.println("count : " + exceptCountMap  + "\ntotal: " + execeptTotalCount);
    Map<String, Double> exceptTMap = new HashMap<String, Double>();
    Map<String, Double> exceptTotalT = new HashMap<String, Double>();
    calPersionT(misColName, exceptColName, classMap, exceptCountMap, execeptTotalCount, exceptTMap, exceptTotalT);
//    calPersionT(exceptColName, misColName, classMap, exceptCountMap, execeptTotalCount, exceptTMap, exceptTotalT);
    Double t = exceptTotalT.get(misColName + "_" + exceptColName);
    double p = 0.0;
    if( t != null){
      double total = execeptTotalCount.get(misColName + "_" + exceptColName);
      p = t / (t + total);
//    System.out.println("---------------------------------------------------" +NumberUtils.round(p, 3));
//    System.out.println("{exceptTMap: }" + exceptTMap + "\ntotal" + exceptTotalT);
    }
    caches.put(misColName + "_" + exceptColName, p);
  }
  public double getPersion(Column misCol, Column exceptCol, CoApperationMatix classMap){
    String misColName = misCol.getName();
    String exceptColName = exceptCol.getName();
    if(caches.get(misColName + "_" + exceptColName) == null){
      calPersion(misColName, exceptColName, classMap);
    }
    return caches.get(misColName + "_" + exceptColName);
  }
}
