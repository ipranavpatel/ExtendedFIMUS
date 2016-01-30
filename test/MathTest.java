import java.math.BigDecimal;

import com.ywl.dmi.DMIMain;
import com.ywl.dmi.model.CalModel;


public class MathTest {
  public static void main(String[] args) {
    System.out.println(1/ (1 + Math.log10(15/1) * Math.log10(15/7)));
    System.out.println(5 & 15);
    BigDecimal decimal = new BigDecimal(2.3730612813370477);
    System.out.println(decimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
    
    
    CalModel cal = new CalModel();
    cal.setMin(5);
    cal.setMax(297);
    System.out.println(DMIMain.geGenValue(6, cal));
  }
}
