import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by VYTKB on 6/1/2016.
 */
public class LuminanceCalculator {
    public static double calculateLuminance(BufferedImage bi){
        ArrayList<Double> values = new ArrayList<>();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color c = new Color(bi.getRGB(x,y));
                values.add(calculateLuminance(c));
            }
        }
        double total =0;
        for (double d : values){
            total+=d;
        }
        return total/values.size();
    }

    private static double calculateLuminance(Color c){
        return Math.sqrt(
                .299*Math.pow(c.getRed(),2)
                        +0.587*Math.pow(c.getGreen(),2)
                        +0.114*Math.pow(c.getBlue(),2));
    }
}
