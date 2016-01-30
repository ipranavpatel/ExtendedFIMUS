//package com.ywl.dmi;
//
//import java.io.*;
//import java.util.*;
//
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import com.ywl.dmi.model.CalModel;
//import com.ywl.dmi.model.CoApperationMatix;
//import com.ywl.dmi.model.Column;
//import com.ywl.dmi.model.MissingData;
//import com.ywl.dmi.model.NameValuePair;
//import com.ywl.dmi.model.VoteResult;
//import com.ywl.dmi.util.Access;
//import com.ywl.dmi.util.CalCoApperation;
//import com.ywl.dmi.util.Vote;
//
//public class DMIMainNew {
//  public static final Column[] columns = new Column[15];
//  Map<String, Map<String, Integer>> countMap = new HashMap<String, Map<String,Integer>>();
//  List<MissingData> missingDatas;
//  
//  public static void main(String[] args) throws IOException {
//    initColumns();
//    DMIMainNew main = new DMIMainNew();
//    StringBuilder html = new StringBuilder("<html><head><title>FIMUS process</title></head><body>");
//    //TODO 修改excel路径
//    List<MissingData> sourceDataSet = main.getMissingSet("D:/DataMining/test1.xlsx");
//    int[][] d0 = main.calculateMissingMatrix(sourceDataSet, html);
//    List<MissingData> generlizeDateSet = main
//        .generalize(sourceDataSet, html);
//    CalCoApperation calCoApp = new CalCoApperation(columns);
//    html.append("<h1>cal coapperation matix</h1>");
//    CoApperationMatix classMap = calCoApp.classifyAndCount(generlizeDateSet, html);
////    System.out.println(main.classifyAndCount(sourceDataSet));
//    System.out.println(classMap.getTotalCounts());
//    Map<String, Map<String, Double>> normalized = calCoApp.normalized(generlizeDateSet, html);
//    double lamda = genLamda();
//    // 暂未完成的方法
//    Vote vote = new Vote(calCoApp.getCategories(), columns);
//    html.append("<h1>first vote</h1>");
//    List<VoteResult> result = vote.doVote(sourceDataSet, d0, generlizeDateSet, classMap, lamda, normalized, html);
//    List<MissingData> newMissData =  vote.getNewMissingDatas();
//    html.append("<h1>second time cal coapperation matix</h1>");
//    classMap = calCoApp.classifyAndCount(sourceDataSet, html);
//    normalized = calCoApp.normalized(sourceDataSet, html);
//    html.append("<h1>second vote</h1>");
//    Map<String, VoteResult> secRes = vote.CSR(newMissData, classMap, lamda, normalized, html);
//    result.addAll(secRes.values());
//    html.append("<h1>the lastly result</h1>");
//    List<MissingData> myResult = writeValues(sourceDataSet, result, html);
//    //TODO 获取标准答案的excel
//    Access.doAE(myResult, main.getMissingSet("D:/develop/workspace/dmi/DMI/real-dataset.xlsx"), html);
//    html.append("</body></html>");
//    //TODO 修改结果路径
//    writeHtml(html, "D:/result.html");
//  }
//
//
//  private static void writeHtml(StringBuilder html, String path) {
//    PrintWriter pw = null;
//    try {
//      pw = new PrintWriter(path);
//      pw.print(html.toString());
//      pw.flush();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }finally{
//      if(pw != null){
//        pw.close();
//      }
//    }
//  }
//
//
//  /**
//   * 填充缺失值
//   * @param sourceDataSet
//   * @param result
//   * @param html 
//   */
//  private static List<MissingData> writeValues(List<MissingData> sourceDataSet,
//      List<VoteResult> result, StringBuilder html) {
//    for(VoteResult vResult : result){
//      MissingData missData = sourceDataSet.get(vResult.getIndex());
//      NameValuePair pair = missData.get(vResult.getMissingCol().getIndex());
//      pair.setValue(vResult.getNorValue());
//    }
//    html.append("<table><tr>");
//    for(Column col : columns){
//      html.append("<th>").append(col.getName()).append("</th>");
//    }
//    html.append("</tr>");
//    for(MissingData data : sourceDataSet){
////      System.out.println(data);
//      html.append("<tr>");
//      for(NameValuePair val : data.getData()){
//        html.append("<td>").append(val.getValue()).append("</td>");
//      }
//      html.append("</tr>");
//    }
//    html.append("</table>");
//    return sourceDataSet;
//  }
//
//
//  private static double genLamda() {
//    return 0.2;
//  }
//
//  private static void initColumns() {
////    columns[0] = new Column(0, "Age", true);
////    columns[1] = new Column(1, "Edu", false);
////    columns[2] = new Column(2, "Salary", true);
////    columns[3] = new Column(3, "Pos", false);
//    columns[0] = new Column(0, "c0", true);
//    columns[1] = new Column(1, "c1", false);
//    columns[2] = new Column(2, "c2", true);
//    columns[3] = new Column(3, "c3", false);
//    columns[4] = new Column(4, "c4", true);
//    columns[5] = new Column(5, "c5", false);
//    columns[6] = new Column(6, "c6", false);
//    columns[7] = new Column(7, "c7", false);
//    columns[8] = new Column(8, "c8", false);
//    columns[9] = new Column(9, "c9", false);
//    columns[10] = new Column(10, "c10", true);
//    columns[11] = new Column(11, "c11", true);
//    columns[12] = new Column(12, "c12", true);
//    columns[13] = new Column(13, "c13", false);
//    columns[14] = new Column(14, "c14", false);
//  }
//
//  /**
//   * 从给定的excel中读取数据源
//   *
//   * @return
//   * @throws FileNotFoundException 
//   */
//  public List<MissingData> getMissingSet(String fileName) throws IOException {
//    List<MissingData> sourceDataSet = new ArrayList<>();
//    FileInputStream fis = null;
//      //TODO 修改文件路径
//    fis = new FileInputStream(fileName);
//    XSSFWorkbook wb = new XSSFWorkbook(fis);
//    XSSFSheet sheet = wb.getSheetAt(0);
//    for (int i = 0, count = sheet.getLastRowNum(); i <= count; i++) {
//      XSSFRow rows = sheet.getRow(i);
//      MissingData misData = new MissingData();
//      int cellNum = rows.getLastCellNum();
//      for (int j = 0; j < cellNum; j++) {
//        Column column = columns[j];
//        XSSFCell cell = rows.getCell(j);
//        Object value = null;
//        if(cell != null){
//          switch (cell.getCellType()) {
//          case XSSFCell.CELL_TYPE_BLANK:
//            value = null;
//            break;
//          case XSSFCell.CELL_TYPE_STRING:
//            value = cell.getStringCellValue();
//            break;
//          case XSSFCell.CELL_TYPE_NUMERIC:
//            value = Double.valueOf(cell.getNumericCellValue()).intValue();
//            break;
//          default:
//            break;
//          }
//        }
//        misData.add(column.getName(), value);
//        // 此处为容错处理，当最后一个字段为空时，会导致arr.length < columns.length
//        System.out.print((value == null ? "?" : value) + "\t");
//      }
//      if(columns.length != cellNum){
//        //TODO 此处做了简单处理，未考虑最后有多个缺失值的情况
//        misData.add(columns[columns.length-1].getName(), null);
//      }
//      sourceDataSet.add(misData);
//      System.out.println();
//    }
//    
//    return sourceDataSet;
//  }
// 
//
//  public static Integer toInteger(String str) {
//    return isEmpty(str) ? null : Integer.valueOf(str);
//  }
//
//  public static String toString(String str) {
//    return isEmpty(str) ? null : str;
//  }
//
//  public static boolean isEmpty(String str) {
//    return str == null || str.length() == 0 || str.trim().equals("");
//  }
//
//  /**
//   * step 1 根据是否缺失数据，获取对应的矩阵
//   * @param html 
//   *
//   * @return
//   */
//  public int[][] calculateMissingMatrix(List<MissingData> source, StringBuilder html) {
//    // 是否已给定属性的顺序和名称？就像文档中那样 age edu ...
//    System.out.println("step1 " + source.size());
//    html.append("<h1>step1</h1><table>");
//    int[][] b = new int[source.size()][columns.length];
//    for (int i = 0, len = source.size(); i < len; i++) {
//      MissingData record = source.get(i);
//      html.append("<tr>");
//      for (int j = 0; j < columns.length; j++) {
//        if (record.get(j).getValue() == null) {
//          b[i][j] = 1;
//        } else {
//          b[i][j] = 0;
//        }
//        html.append("<td>").append(b[i][j]).append("\t</td>");
//        System.out.print(b[i][j] + "\t");
//      }
//      html.append("</tr>");
//      System.out.println();
//    }
//    html.append("</table><br/><br/><span>======================================================================================</span><br/><br/>");
//    System.out
//        .println("======================================================================================");
//    return b;
//  }
//
//  /**
//   * 获取区间范围值
//   * 
//   * @param source
//   * @param columns
//   * @return
//   */
//  public Map<String, CalModel> getCalRange(
//      List<MissingData> source) {
//    Map<String, CalModel> map = new HashMap<String, CalModel>();
//    for (int i = 0, len = source.size(); i < len; i++) {
//      MissingData misData =  source.get(i);
//      for (Column column : columns) {
//        if(!column.isInteger()){
//          continue;
//        }
//        NameValuePair nameValue = misData.get(column.getIndex());
//        CalModel calModel = map.get(nameValue.getName());
//        if (calModel == null) {
//          calModel = new CalModel();
//        }
//        Integer val = (Integer) nameValue.getValue();
//        if (val == null) {
//          continue;
//        }
//        if (calModel.getMin() == 0) {
//          calModel.setMin(val);
//        }
//        if (val < calModel.getMin()) {
//          calModel.setMin(val);
//        }
//        if (val > calModel.getMax()) {
//          calModel.setMax(val);
//        }
//        map.put(nameValue.getName(), calModel);
//      }
//    }
//    Set<Map.Entry<String, CalModel>> entry = map.entrySet();
//    for (Map.Entry<String, CalModel> en : entry) {
//      String col = en.getKey();
//      CalModel cal = en.getValue();
//      cal.setRange(Double.valueOf(Math.sqrt(cal.getMax() - cal.getMin() + 1))
//          .intValue());
//      System.out.println(cal);
//      map.put(col, cal);
//    }
//    return map;
//  }
//
//  /**
//   * step 2 归纳
//   * 
//   * @param sourceDataSet
//   *          源数据集
//   * @param html 
//   * @return 离散化后的数据集
//   */
//  public List<MissingData> generalize(
//      List<MissingData> sourceDataSet, StringBuilder html) {
//    System.out.println("step 2 ");
//    html.append("<h1>step2</h1><table>");
//    List<MissingData> generalizeDatas = new ArrayList<>();
//    Map<String, CalModel> calMap = getCalRange(sourceDataSet);
//    for (int i = 0, len = sourceDataSet.size(); i < len; i++) {
//      MissingData sourceMap = sourceDataSet.get(i);
//      MissingData generalizeMap = new MissingData();
//      generalizeMap.setIndex(sourceMap.getIndex());
//      html.append("<tr>");
//      for (int j = 0; j < sourceMap.size(); j++) {
//        String name = columns[j].getName();
//        NameValuePair value = sourceMap.get(j);
//        String val = null;
//        html.append("<td>");
//        if (value == null || value.getValue() == null) {
//          generalizeMap.add(name, null);
//          System.out.print("?\t");
//          html.append("?");
//        } else{
//          if (columns[j].isInteger()) {
//            val = geGenValue((Integer) value.getValue(), calMap.get(name));
//          } else {
//            val = (String)value.getValue();
//          }
//          System.out.print(val + "\t");
//          html.append(val);
//          generalizeMap.add(name, val);
//        }
//        html.append("</td>");
//      }
//      html.append("</tr>");
//      System.out.println();
//      generalizeDatas.add(generalizeMap);
//    }
//    html.append("</table><br/><br/><span>======================================================================================</span><br/><br/>");
//    System.out
//        .println("======================================================================================");
//    return generalizeDatas;
//  }
//
//  /**
//   * 获取散列区间
//   * 
//   * @param currValue
//   * @param calModel
//   * @return
//   */
//  private String geGenValue(Integer currValue, CalModel calModel) {
//    int prox = calModel.getRange();
//    int start;
//    int end;
//    if ((currValue - calModel.getMin()) % prox == 0
//        && currValue - calModel.getMin() != 0) {
//      start = currValue;
//    } else {
//      start = (currValue - calModel.getMin()) / prox * prox + calModel.getMin();
//    }
//    end = start + prox - 1;
//    return start + "-" + end;
//  }
//}
