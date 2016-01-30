package com.ywl.dmi.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ywl.dmi.model.CoApperationMatix;
import com.ywl.dmi.model.CoApperationModel;
import com.ywl.dmi.model.Column;
import com.ywl.dmi.model.NormalData;
import com.ywl.dmi.model.NameValuePair;

public class CalCoApperation {
  Map<String, Set<Object>> categories = new HashMap<String, Set<Object>>();
  private Column[] columns;
  /**
   * 散列化的矩阵。在第二次投票时会用到
   */
  private List<NormalData> norDataSet;

  public CalCoApperation(Column[] columns) {
    this.columns = columns;
  }


  /**
   * step3 分类，统计
   * 
   * @param sourceDataSet
   *          key 列名 + 取值
   * @param misCols 
   * @param writer 
   * @return
   */
  public CoApperationMatix classifyAndCount(List<NormalData> sourceDataSet,
      HtmlWriter html) {
    categories.clear();
    CoApperationMatix coApperationMatix = new CoApperationMatix();
    System.out.println("step 3 : ");
    List<String> keyList = new ArrayList<>();
    for (int i = 0; i < columns.length; i++) {
      String colName = columns[i].getName();// TODO 获取每个column对应的值
      for (int j = 0, genLen = sourceDataSet.size(); j < genLen; j++) {
        NormalData map = sourceDataSet.get(j);
//        NormalData norMap = null;
        NameValuePair outVal = map.get(i);
        if(norDataSet != null){
          map = norDataSet.get(j);
        }
        
        if (outVal.getValue() == null) {
          continue;
        }
        String key = colName + "_" + outVal.getValue();
        if (!keyList.contains(key)) {
          keyList.add(key);
        }
        for (int k = 0; k < map.size(); k++) {
          if (k == i) {
            continue;
          }
          NameValuePair nameVal = map.get(k);
          if (nameVal.getValue() == null) {
            continue;
          }
          putInCategory(nameVal.getName(), nameVal.getValue());
          coApperationMatix.put(outVal, nameVal, map);
        }
      }
    }
    printCountMap(keyList, coApperationMatix, html);

    return coApperationMatix;
  }

  /**
   * 打印分类Map
   * 
   * @param keyList
   * @param countMap
   * @param html 
   */
  private void printCountMap(List<String> keyList, CoApperationMatix countMap,HtmlWriter html) {
//    System.out.print("\t");
    html.write("<table><tr><th>&nbsp;&nbsp;&nbsp;&nbsp;</th>");
    for (String key : keyList) {
//      System.out.print("\t" + key);
      html.write("<th>").write(key).write("</th>");
    }
//    System.out.println();
    html.write("</tr>");
    for (String key : keyList) {
//      System.out.print(key);
      html.write("<tr><th>").write(key).write("</th>");
      String[] keys = key.split("_");
      for (String k : keyList) {
        String[] ks = k.split("_");
        String countStr;
//        System.out.println(key + ";;;" + k);
        CoApperationModel coModel = countMap.get(key, k);
        if (keys[0].equals(ks[0])) {
          countStr = "--";
        } else if (coModel == null) {
          countStr = "0";
        } else {
          countStr = String.valueOf(coModel.getData().size());
        }
        html.write("<td>").write(countStr).write("</td>");
//        System.out.print("\t" + countStr);
      }
      html.write("</tr>");
//      System.out.println();
    }
    html.write("</table><br/><br/><span>======================================================================================</span><br/><br/>");
//    System.out
//        .println("======================================================================================");
  }

  /**
   * 对各个属性的值进行分类
   * 
   * @param name
   * @param value
   */
  private void putInCategory(String name, Object value) {
    Set<Object> cat = categories.get(name);
    if (cat == null)
      cat = new HashSet<Object>();
    cat.add(value);
    categories.put(name, cat);
  }

  public Map<String, Map<String, Double>> normalized(
      List<NormalData> generlizeDateSet, HtmlWriter html) {
    Map<String, Map<Object, Integer>> countMap = new HashMap<String, Map<Object, Integer>>();
    for (NormalData misData : generlizeDateSet) {
      List<NameValuePair> vals = misData.getData();
      for (NameValuePair nameVal : vals) {
        String name = nameVal.getName();
        Object val = nameVal.getValue();
        if (val == null || "null".equals(val)) {
          continue;
        }
        Map<Object, Integer> map = countMap.get(name);
        if (map == null) {
          map = new HashMap<Object, Integer>();
        }
        Integer count = map.get(val);
        if (count == null) {
          count = 0;
        }
        count++;
        map.put(val, count);
        countMap.put(name, map);
      }
    }
    Map<String, Map<String, Double>> normalizedMap = new HashMap<String, Map<String, Double>>();
    int totalNum = generlizeDateSet.size();
    html.write("<h1>count</h1>");
    html.write("<table>");
    for (Column col : columns) {
      String colName = col.getName();
      Map<Object, Integer> count = countMap.get(colName);
      Set<Object> cates = categories.get(colName);
//      System.out.println(colName + " = " + count);
      html.write("<tr><th colspan='2'>").write(colName).write("</th></tr>");
      Map<String, Double> norMap = new HashMap<String, Double>();
      for (Object cat : cates) {
        int catCount = count.get(cat);
        html.write("<tr><td>").write(cat).write("</td><td>").write(catCount).write("</td></tr>");
        Map<String, Double> tmpMap = new HashMap<String, Double>();
        double totalNorCount = 0;
        for (Object c : cates) {
          if (c.equals(cat)) {
            tmpMap.put(cat + "_" + cat, 1.0);
            totalNorCount += 1.0;
          } else {
            double tmpNor = 1 / (1 + Math.log10(totalNum / catCount)
                * Math.log10(totalNum / count.get(c)));
            tmpMap.put(cat + "_" + c, tmpNor);
            totalNorCount += tmpNor;
          }
        }
        Set<Entry<String, Double>> ens = tmpMap.entrySet();
        for (Entry<String, Double> en : ens) {
          //这里是求了相似性
          BigDecimal bd = new BigDecimal(en.getValue() / totalNorCount);
          norMap.put(en.getKey(), bd.setScale(3, BigDecimal.ROUND_HALF_UP)
              .doubleValue());
        }
      }
      html.write("<tr><td colspan='2'></td></tr>");
      normalizedMap.put(colName, norMap);
    }
    html.write("</table>");
    return normalizedMap;
  }

  public Map<String, Set<Object>> getCategories() {
    return categories;
  }

  public void setCategories(Map<String, Set<Object>> categories) {
    this.categories = categories;
  }

}
