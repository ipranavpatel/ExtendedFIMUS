

/**
 * Created by 永锋 on 2015/7/12.
 */
public class TestDistance {
  public static void main(String[] args) {
    double[] p1 = { 0, 0 };
    double[] p2 = { 3, 4 };
    double result = getNormalizeDistance(p1, p2);
    System.out.println("Normalization result is " + result);
  }

  /**
   * @param point1
   *          first point
   * @param point2
   *          second point
   * @return the normalization distance of two points
   */
  public static double getNormalizeDistance(double[] point1, double[] point2) {
    return 1 / (getDistance(point1, point2) + 1);
  }

  public static double getDistance(double[] point1, double[] point2) {
    double distance = 0;
    for (int i = 0; i < point1.length; i++) {
      distance = distance + Math.pow(point1[i] - point2[i], 2);
    }
    return Math.pow(distance, 0.5);

  }
}
