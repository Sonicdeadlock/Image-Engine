package render;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by VYTKB on 6/1/2016.
 */
public class BitwiseCalculator {

    public static int  calculateBitwize(char c,Settings settings){
        BufferedImage bi  = new BufferedImage(Settings.getCharImageWidth(),Settings.getCharImageWidth(),BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        drawCenterChar(g, c);

        return calculateBitwize(bi,settings);
    }
    public static int  calculateBitwize(char c,double tolerance){
        BufferedImage bi  = new BufferedImage(Settings.getCharImageWidth(),Settings.getCharImageWidth(),BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        drawCenterChar(g, c);

        return calculateBitwize(bi,tolerance);
    }
    public static int calculateBitwize(BufferedImage bi , Settings settings){
        double calculateLuminance = LuminanceCalculator.calculateLuminance(bi);
        return calculateBitwize(bi,calculateLuminance,settings);
    }
    public static int calculateBitwize(BufferedImage bi , double tolerance){
        double calculateLuminance = LuminanceCalculator.calculateLuminance(bi);
        return calculateBitwize(bi,calculateLuminance,tolerance);
    }

    public static int calculateBitwize(BufferedImage bi, double averageLuminance,Settings settings ){
        return calculateBitwize(bi,averageLuminance,settings.getTolerance());
    }

    public static int calculateBitwize(BufferedImage bi, double averageLuminance,double tolerance ){
        int value=0;
        int index=0;
        for(int x = 0; x<Settings.getCharImageWidth(); x+=Settings.getPRECISION()){
            for(int y = 0; y<Settings.getCharImageWidth(); y+=Settings.getPRECISION()){
                double luminance = LuminanceCalculator.calculateLuminance(bi.getSubimage(x,y,Settings.getPRECISION(),Settings.getPRECISION()));
                if(Math.abs(luminance-averageLuminance)/averageLuminance>tolerance){
                    value = value | 1 << index;
                }
                index++;
            }
        }

        return value;
    }


    private static void drawCenterChar(Graphics g,char c){
        String s ="";
        s+=c;
        g.setColor(Color.white);
        g.fillRect(0, 0, Settings.getCharImageWidth(), Settings.getCharImageWidth());
        g.setFont(Settings.getFONT());
        g.setColor(Color.black);
        g.drawString(s, 0, Settings.getCharImageWidth());
    }


}
