package com.ywl.dmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ywl.dmi.model.CalModel;
import com.ywl.dmi.model.CoApperationMatix;
import com.ywl.dmi.model.Column;
import com.ywl.dmi.model.MissingData;
import com.ywl.dmi.model.NameValuePair;
import com.ywl.dmi.model.NormalData;
import com.ywl.dmi.model.VoteResult;
import com.ywl.dmi.util.Access;
import com.ywl.dmi.util.CalCoApperation;
import com.ywl.dmi.util.HtmlWriter;
import com.ywl.dmi.util.NumberUtils;
import com.ywl.dmi.util.Vote;

public class DMIMain {
  /**
   * TODO 
   */
	
	// Define number of columns
	
  public static final Column[] columns = new Column[10]; 
  Map<String, Map<String, Integer>> countMap = new HashMap<String, Map<String,Integer>>();
  List<NormalData> missingDatas;
  private Map<String, CalModel> calMap;
  
  public static void main(String[] args) throws IOException {
    initColumns();
    DMIMain main = new DMIMain();
    //TODO 
    
    // Write file directory of HTML file
    HtmlWriter html = new HtmlWriter("C:/Users/Pranav/workspace/C12-Results/Results-BCW/Med1_UD.html");
    html.write("<table><tr><th>the source data from excel</th></tr>");
    
    // Write file directory of Incomplete Data-set file
    List<NormalData> sourceDataSet = main.getMissingSet("C:/Users/Pranav/workspace/C12-Results/Incomplete Datasets/BCW/Med1_UD.xlsx");
    int[][] d0 = main.calculateMissingMatrix(sourceDataSet, html);
    List<NormalData> generlizeDateSet = main
        .generalize(sourceDataSet, html);
    CalCoApperation calCoApp = new CalCoApperation(columns);
    html.write("<h1>cal coapperation matix</h1>");
    CoApperationMatix classMap = calCoApp.classifyAndCount(generlizeDateSet, html);
//    System.out.println(main.classifyAndCount(sourceDataSet));
//    System.out.println(classMap.getTotalCounts());  
    Map<String, Map<String, Double>> normalized = calCoApp.normalized(generlizeDateSet, html);

    double lamda = genLamda();
    // 
    Vote vote = new Vote(calCoApp.getCategories(), columns, html);
    html.write("<h1>first vote</h1>");
    List<VoteResult> result = vote.doVote(sourceDataSet, d0, generlizeDateSet, classMap, lamda, normalized);
    List<MissingData> newMissData =  vote.getNewMissingDatas();
    html.write("<h1>second time cal coapperation matix</h1>");
    classMap = calCoApp.classifyAndCount(sourceDataSet, html);
    normalized = calCoApp.normalized(sourceDataSet, html);
    html.write("<h1>second vote</h1>");
    System.out.println("secode vote");
    List<VoteResult> secRes = vote.CSR(sourceDataSet, newMissData, classMap, lamda, normalized, main.calMap);
    result.addAll(secRes);
    html.write("<h1>the lastly result</h1>");
    html.setToWrite(true);//TODO 
    html.prepar();//TODO 
//    System.out.println(result);
    List<NormalData> myResult = writeValues(sourceDataSet, result, html);
    //TODO 
    html.write("<table><tr><th>the real data form teacher </th></tr>");
    
    //If data-set has categorical values write "doAE" instead of "doNRMS" and vice-versa
    // Write file directory of Original Data-set
    Access.doNRMS(myResult, main.getMissingSet("C:/Users/Pranav/workspace/C12-Results/Original Datasets-1/BCW.xlsx"), html);
    html.write("</body></html>");
    html.close();
  }


  /**
   * 
   * @param sourceDataSet
   * @param result
   * @param html 
   */
  private static List<NormalData> writeValues(List<NormalData> sourceDataSet,
      List<VoteResult> result, HtmlWriter html) {
    for(VoteResult vResult : result){
      NormalData missData = sourceDataSet.get(vResult.getIndex());
      NameValuePair pair = missData.get(vResult.getMissingCol().getIndex());
      pair.setValue(vResult.getNorValue());
    }
    html.write("<table><tr>");
    for(Column col : columns){
      html.write("<th>").write(col.getName()).write("</th>");
    }
    html.write("</tr>");
    for(NormalData data : sourceDataSet){
//      System.out.println(data);
      html.write("<tr>");
      for(NameValuePair val : data.getData()){
        html.write("<td>").write(val.getValue()).write("</td>");
      }
      html.write("</tr>");
    }
    html.write("</table>");
    return sourceDataSet;
  }


  private static double genLamda() {
    return 0.2;
  }

  
  // Here , column mapping is done, Define type of each column
  // 							if it is Categorical value - e.g. new Column(0, "c0")
  //							else if it is Integer value - e.g. new Column(0, "c0", Column.TYPE_INTEGER)
  //							else if it is double value - e.g. new Column(0, "c0", Column.TYPE_DOUBLE)
  private static void initColumns() {

	  	columns[0] = new Column(0, "c0", Column.TYPE_INTEGER);
	    columns[1] = new Column(1, "c1", Column.TYPE_INTEGER);
	    columns[2] = new Column(2, "c2", Column.TYPE_INTEGER);
	    columns[3] = new Column(3, "c3", Column.TYPE_INTEGER);
	    columns[4] = new Column(4, "c4", Column.TYPE_INTEGER);
	    columns[5] = new Column(5, "c5", Column.TYPE_INTEGER);
	    columns[6] = new Column(6, "c6", Column.TYPE_INTEGER);
	    columns[7] = new Column(7, "c7", Column.TYPE_INTEGER);
	    columns[8] = new Column(8, "c8", Column.TYPE_INTEGER);
	    columns[9] = new Column(9, "c9", Column.TYPE_INTEGER);
//	    columns[10] = new Column(10, "c10", Column.TYPE_DOUBLE);
//	    columns[11] = new Column(11, "c11", Column.TYPE_DOUBLE);
//	    columns[12] = new Column(12, "c12", Column.TYPE_INTEGER);
//	    columns[13] = new Column(13, "c13", Column.TYPE_INTEGER);
//	    columns[14] = new Column(14, "c14", Column.TYPE_DOUBLE);
//	    columns[15] = new Column(15, "c15", Column.TYPE_DOUBLE);
//	    columns[16] = new Column(16, "c16", Column.TYPE_DOUBLE);
//	    columns[17] = new Column(17, "c17", Column.TYPE_DOUBLE);
//	    columns[18] = new Column(18, "c18", Column.TYPE_DOUBLE);
//	    columns[19] = new Column(19, "c19", Column.TYPE_DOUBLE);
//	    columns[20] = new Column(20, "c20", Column.TYPE_DOUBLE);
//	    columns[21] = new Column(21, "c21", Column.TYPE_DOUBLE);
//	    columns[22] = new Column(22, "c22", Column.TYPE_DOUBLE);
//	    columns[23] = new Column(23, "c23", Column.TYPE_DOUBLE);
//	    columns[24] = new Column(24, "c24", Column.TYPE_DOUBLE);
//	    columns[25] = new Column(25, "c25", Column.TYPE_DOUBLE);
//	    columns[26] = new Column(26, "c26", Column.TYPE_DOUBLE);
//	    columns[27] = new Column(27, "c27", Column.TYPE_DOUBLE);
//	    columns[28] = new Column(28, "c28", Column.TYPE_DOUBLE);
//	    columns[29] = new Column(29, "c29", Column.TYPE_DOUBLE);
//	    columns[30] = new Column(30, "c30", Column.TYPE_DOUBLE);
//	    columns[31] = new Column(31, "c31", Column.TYPE_DOUBLE);
//	    columns[32] = new Column(32, "c32", Column.TYPE_DOUBLE);
//	    columns[33] = new Column(33, "c33", Column.TYPE_DOUBLE);
//	    columns[34] = new Column(34, "c34", Column.TYPE_DOUBLE);
//	    columns[35] = new Column(35, "c35", Column.TYPE_DOUBLE);
//	    columns[36] = new Column(36, "c36", Column.TYPE_DOUBLE);
//	    columns[37] = new Column(37, "c37", Column.TYPE_DOUBLE);
//	    columns[38] = new Column(38, "c38", Column.TYPE_DOUBLE);
//	    columns[39] = new Column(39, "c39", Column.TYPE_DOUBLE);
//	    columns[40] = new Column(40, "c40", Column.TYPE_DOUBLE);
//	    columns[41] = new Column(41, "c41", Column.TYPE_DOUBLE);
//	    columns[42] = new Column(42, "c42", Column.TYPE_DOUBLE);
//	    columns[43] = new Column(43, "c43", Column.TYPE_DOUBLE);
//	    columns[44] = new Column(44, "c44", Column.TYPE_DOUBLE);
//	    columns[45] = new Column(45, "c45", Column.TYPE_DOUBLE);
//	    columns[46] = new Column(46, "c46", Column.TYPE_DOUBLE);
//	    columns[47] = new Column(47, "c47", Column.TYPE_DOUBLE);
//	    columns[48] = new Column(48, "c48", Column.TYPE_DOUBLE);
//	    columns[49] = new Column(49, "c49", Column.TYPE_DOUBLE);
//	    columns[50] = new Column(50, "c50", Column.TYPE_DOUBLE);
//	    columns[51] = new Column(51, "c51", Column.TYPE_DOUBLE);
//	    columns[52] = new Column(52, "c52", Column.TYPE_DOUBLE);
//	    columns[53] = new Column(53, "c53", Column.TYPE_DOUBLE);
//	    columns[54] = new Column(54, "c54", Column.TYPE_DOUBLE);
//	    columns[55] = new Column(55, "c55", Column.TYPE_DOUBLE);
//	    columns[56] = new Column(56, "c56", Column.TYPE_DOUBLE);
//	    columns[57] = new Column(57, "c57", Column.TYPE_DOUBLE);
//	    columns[58] = new Column(58, "c58", Column.TYPE_DOUBLE);
//	    columns[59] = new Column(59, "c59", Column.TYPE_DOUBLE);
//	    columns[60] = new Column(60, "c60", Column.TYPE_INTEGER);
//    columns[12] = new Column(12, "c12", Column.TYPE_INTEGER);
//    columns[13] = new Column(13, "c13");
//    columns[14] = new Column(14, "c14");
  }

  /**
   * 
   *
   * @return
   * @throws FileNotFoundException 
   */
  public List<NormalData> getMissingSet(String fileName) throws IOException {
    List<NormalData> sourceDataSet = new ArrayList<>();
    FileInputStream fis = null;
      //TODO 
    fis = new FileInputStream(new File(fileName));
    XSSFWorkbook wb = new XSSFWorkbook(fis);
    XSSFSheet sheet = wb.getSheetAt(0);
    for (int i = 0, count = sheet.getLastRowNum(); i <= count; i++) {
      XSSFRow rows = sheet.getRow(i);
      NormalData misData = new NormalData();
      int cellNum = rows.getLastCellNum();
      for (int j = 0; j < cellNum; j++) {
        Column column = columns[j];
        XSSFCell cell = rows.getCell(j);
        Object value = null;
        if(cell != null){
          switch (cell.getCellType()) {
          case XSSFCell.CELL_TYPE_BLANK:
            value = null;
            break;
          case XSSFCell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;
          case XSSFCell.CELL_TYPE_NUMERIC:
            Double numVal = Double.valueOf(cell.getNumericCellValue());
            switch (column.getType()) {
            case Column.TYPE_DOUBLE:
              value = numVal;
              break;
            case Column.TYPE_INTEGER:
              value = numVal.intValue();
              break;
            default:
              value = String.valueOf(numVal);
              break;
            }
            break;
          default:
            value = null;
            break;
          }
        }
        misData.add(column.getName(), value);
        // System.out.print((value == null ? "?" : value) + "\t");
//        writer.write("<td>").write(value == null ? "?" : value).write("</td>");
      }
      if(columns.length != cellNum){
        //TODO 
        for(int j = cellNum  ; j < columns.length; j++){
          misData.add(columns[j].getName(), null);
//          writer.write("?");
        }
      }
      sourceDataSet.add(misData);
//      System.out.println();
    }
    
    return sourceDataSet;
  }
 

  public static Integer toInteger(String str) {
    return isEmpty(str) ? null : Integer.valueOf(str);
  }

  public static String toString(String str) {
    return isEmpty(str) ? null : str;
  }

  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0 || str.trim().equals("");
  }

  /**
   * step 1 
   * @param writer 
   *
   * @return
   */
  public int[][] calculateMissingMatrix(List<NormalData> source, HtmlWriter html) {
    //  age edu ...
    System.out.println("step1 " + source.size());
//    html.write("<h1>step1</h1><table>");
    int[][] b = new int[source.size()][columns.length];
    for (int i = 0, len = source.size(); i < len; i++) {
      NormalData record = source.get(i);
      html.write("<tr>");
      for (int j = 0; j < columns.length; j++) {
        if (record.get(j).getValue() == null) {
          b[i][j] = 1;
        } else {
          b[i][j] = 0;
        }
        html.write("<td>").write(b[i][j]).write("\t</td>");
//        System.out.print(b[i][j] + "\t");
      }
//      System.out.println();
      html.write("</tr>");
    }
    html.write("</table><br/><br/><span>======================================================================================</span><br/><br/>");
//    System.out
//        .println("======================================================================================");
    return b;
  }

  /**
   * 
   * 
   * @param source
   * @param columns
   * @return
   */
  public Map<String, CalModel> getCalRange(
      List<NormalData> source) {
    Map<String, CalModel> map = new HashMap<String, CalModel>();
    for (int i = 0, len = source.size(); i < len; i++) {
      NormalData misData =  source.get(i);
      for (Column column : columns) {
        if(!column.isClassify()){
          continue;
        }
        NameValuePair nameValue = misData.get(column.getIndex());
        CalModel calModel = map.get(nameValue.getName());
        if (calModel == null) {
          calModel = new CalModel();
        }
        if (nameValue.getValue() == null) {
          continue;
        }
        Double val =  Double.parseDouble(nameValue.getValue().toString());
        if (calModel.getMin() == 0) {
          calModel.setMin(val);
        }
        if (val < calModel.getMin()) {
          calModel.setMin(val);
        }
        if (val > calModel.getMax()) {
          calModel.setMax(val);
        }
        map.put(nameValue.getName(), calModel);
      }
    }
//    Set<Map.Entry<String, CalModel>> entry = map.entrySet();
//    for (Map.Entry<String, CalModel> en : entry) {
//      String col = en.getKey();
//      CalModel cal = en.getValue();
//      cal.setRange(Double.valueOf(Math.sqrt(cal.getMax() - cal.getMin() + 1))
//          .intValue());
////      System.out.println(cal);
//      map.put(col, cal);
//    }
    return map;
  }

  /**
   * step 2 
   * 
   * @param sourceDataSet
   *          
   * @param html 
   * @return 
   */
  public List<NormalData> generalize(
      List<NormalData> sourceDataSet, HtmlWriter html) {
    System.out.println("step 2 ");
    html.write("<h1>step2</h1><table>");
    List<NormalData> generalizeDatas = new ArrayList<>();
    calMap = getCalRange(sourceDataSet);
//    System.out.println(calMap);
    for (int i = 0, len = sourceDataSet.size(); i < len; i++) {
      NormalData sourceMap = sourceDataSet.get(i);
      NormalData generalizeMap = new NormalData();
      generalizeMap.setIndex(sourceMap.getIndex());
      html.write("<tr>");
      for (int j = 0; j < sourceMap.size(); j++) {
        String name = columns[j].getName();
        NameValuePair value = sourceMap.get(j);
        String val = null;
        if (value == null || value.getValue() == null) {
          generalizeMap.add(name, null);
//          System.out.print("?\t");
          html.write("?");
        } else{
          if (columns[j].getType() == Column.TYPE_DOUBLE) {
            val = geGenValue((Double)value.getValue(), calMap.get(name));
          } else if (columns[j].getType() == Column.TYPE_INTEGER) {
            val = geGenValue((Integer)value.getValue(), calMap.get(name));
          }else{
            val = (String)value.getValue();
          }
//          System.out.print(val + "\t");
          html.write(val);
          generalizeMap.add(name, val);
        }
      }
      html.write("</tr>");
//      System.out.println();
      generalizeDatas.add(generalizeMap);
    }
    html.write("</table><br/><br/><span>======================================================================================</span><br/><br/>");
//    System.out
//        .println("======================================================================================");
    return generalizeDatas;
  }

  /**
   * 
   * 
   * @param currValue
   * @param calModel
   * @return
   */
  public static String geGenValue(Double currValue, CalModel calModel) {
    Double prox = calModel.getRange();
    Double start;
    Double end;
    Double douVal = Double.valueOf(currValue.toString());
    if ((douVal - calModel.getMin()) % prox == 0
        && douVal - calModel.getMin() != 0) {
      start = douVal;
    } else {
      start = (douVal - calModel.getMin()) / prox * prox + calModel.getMin();
    }
    end = start + prox - 1;
    if(currValue.getClass() == Double.class){
      return NumberUtils.round(start, 7) + "-" + NumberUtils.round(end, 7);
    } else {
      return start.intValue() + "-" + end.intValue();
    }
  }
  
  public static String geGenValue(Integer currValue, CalModel calModel) {
    Integer prox = calModel.getRangeInteger();
    Integer start;
    Integer end;
    if ((currValue - calModel.getMin()) % prox == 0
        && currValue - calModel.getMin() != 0) {
      start = currValue;
    } else {
      start = (currValue - calModel.getMin().intValue()) / prox * prox + calModel.getMin().intValue();
    }
    end = start + prox - 1;
    return start.intValue() + "-" + end.intValue();
  }
}
