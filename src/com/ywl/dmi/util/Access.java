package com.ywl.dmi.util;

import java.util.List;

import com.ywl.dmi.model.NormalData;

/**
 * 进行评估
 * 
 * @author 永锋
 *
 */
public class Access {
  /**
   * 
   * @param myResult
   *          计算出的结果
   * @param realResult
   *          真实结果
   * @param html 
   */
  public static void doAE(List<NormalData> myResult,
      List<NormalData> realResult, HtmlWriter html) {
    System.out.println("start do AE -------------------------------------");
    html.write("<h1> the AE </h1>");
    html.write("<table>");
    double totalEq = 0.0;
    double total = 0.0;
    for (int i = 0, len = myResult.size(); i < len; i++) {
      NormalData my = myResult.get(i);
      NormalData real = realResult.get(i);
      html.write("<tr>");
      for (int j = 0; j < my.size(); j++) {
        html.write("<td width='40px'>");
        total += 1;
        if(my.get(j).equals(real.get(j))){
//          System.out.print(1);
          html.write(1);
          totalEq += 1;
        } else {
//          System.out.print(0);
          html.write(0);
        }
        html.write("</td>");
//        System.out.print("\t");
      }
      html.write("</tr>");
//      System.out.println();
    }
    double ae = NumberUtils.round(totalEq / total, 3);
    System.out.println(ae);
    html.write("</table>");
    html.write("<p>result : ").write(ae).write("</p>");
  }

  public static double doNRMS(List<NormalData> myResult,
      List<NormalData> realResult, HtmlWriter html) {
    double total = 0.0;
    double plus = 0.0;
    System.out.println("start do NRMS -------------------------------------");
    for(int i=0, len = myResult.size(); i<len ;i++){
      NormalData my = myResult.get(i);
      NormalData real = realResult.get(i);
//      System.out.println(real + ";;; " + my);
      for (int j = 0; j < my.size(); j++) {
        double val = Double.valueOf(real.get(j).getValue().toString());
        total += val * val;
        double myVal = Double.valueOf(my.get(j).getValue().toString()) - val;
        plus += myVal * myVal;
      }
    }
    double nrms = NumberUtils.round(Math.sqrt(plus) / Math.sqrt(total), 3);
    html.write("<h1> the NRMS RESULT ").write(nrms).write("</h1>");
    System.out.println(nrms);
    return nrms;
  }
}
