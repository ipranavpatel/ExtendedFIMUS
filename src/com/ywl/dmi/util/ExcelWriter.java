package com.ywl.dmi.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 生成导出Excel文件对象
 * 
 * @author caihua
 * 
 */
public class ExcelWriter {
	// 设置cell编码解决中文高位字节截断
	private static short XLS_ENCODING = HSSFCell.ENCODING_UTF_16;

	// 定制浮点数格式
	private static String NUMBER_FORMAT = "#,##0.00";

	// 定制日期格式
	private static String DATE_FORMAT = "m/d/yy"; 

	private String filePath;

	private XSSFWorkbook workbook = null;

	private XSSFSheet sheet = null;

	private XSSFRow row = null;
	private int currRowIndex = 0;
	private boolean isWrite = false;
	private int currCellIndex = 0;

	public ExcelWriter() {
	}

	/**
	 * 初始化Excel
	 * 
	 */
	public ExcelWriter(String filePath) {
	  if(!isWrite){
	    return;
	  }
		this.filePath = filePath;
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet();
	}

	/**
	 * 导出Excel文件
	 * 
	 * @throws IOException
	 */
	public void export() throws FileNotFoundException, IOException {
	  if(!isWrite){
	    return;
	  }
		try {
		  FileOutputStream fos = new FileOutputStream(filePath);
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new IOException(" 生成导出Excel文件出错! ", e);
		} catch (IOException e) {
			throw new IOException(" 写入Excel文件出错! ", e);
		}

	}

	/**
	 * 增加一行
	 * 
	 * @param index
	 *            行号
	 */
	public ExcelWriter createRow() {
	  if(!isWrite){
      return this;
    }
		this.row = this.sheet.createRow(currRowIndex ++);
		currCellIndex = 0;
		return this;
	}
	public ExcelWriter writeHead(Object val, Integer merge){
	  if(!isWrite){
      return this;
    }
	  createRow();
	  CellStyle style = workbook.createCellStyle();
	  style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
	  return writeCell(style, merge, val);
	}
	
	public ExcelWriter writeHead(Object val){
	  if(!isWrite){
      return this;
    }
    return writeHead(val, 20);
  }
	
	public ExcelWriter write(Object... vals){
	  if(!isWrite){
      return this;
    }
	  createRow();
	  for(Object val : vals){
	    writeCell(val);
	  }
	  return this;
	}
	public ExcelWriter writeCell(CellStyle style, Integer merge, Object val){
	  if(!isWrite){
      return this;
    }
    XSSFCell cell = this.row.createCell(currCellIndex);
    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell.setCellValue(val == null ? "" : val.toString());
    if(style != null){
      cell.setCellStyle(style);
    }
    if(merge != null){
      sheet.addMergedRegion(new CellRangeAddress(
          currRowIndex - 1,
          currRowIndex - 1,
          currCellIndex,
          currCellIndex + merge));
    }
    currCellIndex ++;
    return this;
  }
	
	public ExcelWriter writeCell(Object val){
    return writeCell(null, null, val);
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param index
	 *            列号
	 */
	public String getCell(int index) {
		XSSFCell cell = this.row.createCell(index);
		String strExcelCell = "";
		if (cell != null) { // add this condition
			// judge
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_FORMULA:
				strExcelCell = "FORMULA ";
				break;
			case XSSFCell.CELL_TYPE_NUMERIC: {
				strExcelCell = String.valueOf(cell.getNumericCellValue());
			}
				break;
			case XSSFCell.CELL_TYPE_STRING:
				strExcelCell = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_BLANK:
				strExcelCell = "";
				break;
			default:
				strExcelCell = "";
				break;
			}
		}
		return strExcelCell;
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, int value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, double value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		XSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		XSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, String value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(XLS_ENCODING);
		cell.setCellValue(value);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, Calendar value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellValue(XLS_ENCODING);
		cell.setCellValue(value.getTime());
		XSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT)); // 设置cell样式为定制的日期格式
		cell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
	}

	public static void main(String[] args) {
		System.out.println(" 开始导出Excel文件 ");

		ExcelWriter e = new ExcelWriter();
		e = new ExcelWriter("C:\\qt.xls");

		e.createRow();
		e.setCell(0, "试题编码 ");
		e.setCell(1, "题型");
		e.setCell(2, "分值");
		e.setCell(3, "难度");
		e.setCell(4, "级别");
		e.setCell(5, "知识点");

		e.createRow();
		e.setCell(0, "t1");
		e.setCell(1, 1);
		e.setCell(2, 3.0);
		e.setCell(3, 1);
		e.setCell(4, "重要");
		e.setCell(5, "专业");
		e.createRow();
		e.setCell(0, "t2");
		e.setCell(1, 2);
		e.setCell(2, 5.0);
		e.setCell(3, 2);
		e.setCell(4, "重要");
		e.setCell(5, "业余");

		try {
			e.export();
			System.out.println(" 导出Excel文件[成功] ");
		} catch (IOException ex) {
			System.out.println(" 导出Excel文件[失败] ");
			ex.printStackTrace();
		}
	}

}
