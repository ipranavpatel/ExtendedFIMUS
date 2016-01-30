import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ywl.dmi.DMIMain;


public class FileTest {

  public static void main(String[] args) throws IOException {
    File[] files = new File("E:/").listFiles();
    for(File file : files){
      System.out.println(file.getAbsolutePath());
    }
    System.out.println(new File("D:\\DataMining\\IncompleteDatasets\\4-gauss\\Blend1.xlsx").exists());
//    FileInputStream fis = new FileInputStream(new File("D:\\DataMining\\IncompleteDatasets\\DERM\\planned 1.xlsx"));
//    DMIMain main = new DMIMain();
//    main.getMissingSet("E:\\planned1.xlsx");
  }
}
