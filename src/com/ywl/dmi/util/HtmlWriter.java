package com.ywl.dmi.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class HtmlWriter {
  PrintWriter pw;
  private boolean toWrite;

  public boolean isToWrite() {
    return toWrite;
  }

  public void setToWrite(boolean toWrite) {
    this.toWrite = toWrite;
  }

  public HtmlWriter(String path) {
    this(path, "UTF-8");
  }

  public HtmlWriter(String path, String charset) {
    try {
      pw = new PrintWriter(path, charset);
      prepar();
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void prepar() {
    write("<html><head><title>FIMUS process</title><style type=\"text/css\">"
        + "table {font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #999999;border-collapse: collapse}"
        + "table th {background-color:#c3dde0;border-width: 1px;padding: 8px;border-style: solid;border-color: #a9c6c9;}"
        + "table tr {background-color:#d4e3e5;}"
        + "table td {border-width: 1px;padding: 8px;border-style: solid;border-color: #a9c6c9;}"
        + "</style></head><body>");
  }

  public HtmlWriter write(Object obj) {
    if(toWrite){
      pw.print(obj);
      pw.flush();
    }
    return this;
  }

  public void writeln(Object obj) {
    if(toWrite){
      pw.print(obj);
      pw.print("<br/>");
    }
    pw.flush();
  }
  public void close(){
    pw.close();
  }

}
