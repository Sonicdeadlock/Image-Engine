package render;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePartProcessor extends Thread{
    protected BufferedImage image;
    private int xStart;
    private int yStart;
    private int xLength;
    private int yLength;
    protected BufferedImage finalImage;
    protected Settings settings;
    private long pixesRendered = 0;

    public ImagePartProcessor(BufferedImage bi,BufferedImage finalImg,int x,int y,int xl,int yl,Settings s){
        image=bi;
        xStart=x;
        yStart=y;
        xLength=xl;
        yLength=yl;
        finalImage = finalImg;
        this.settings = s;
    }

    public void run(){
        processImagePart();
    }

    protected void processImagePart(){
        Graphics g = finalImage.getGraphics();
        g.setFont(Settings.getFONT());
        //if(useColor)
        g.setColor(Color.black);
//		else
//			g.setColor(Color.white);
        g.fillRect(xStart, yStart, xLength, yLength);
        for(int x=xStart;x<xLength;x+=Settings.getCharImageWidth()){
            for(int y=yStart;y<yLength;y+=Settings.getCharImageHeight()){
                try{
                    int value;
                    if(settings.isUseColor()){
                        Color avg = getAverageColor(image.getSubimage(x, y, Settings.getCharImageWidth(), Settings.getCharImageHeight()));
                        value = BitwiseCalculator.calculateBitwize(image.getSubimage(x, y, Settings.getCharImageWidth(), Settings.getCharImageHeight()),settings);
                        g.setColor(avg);
                    }else{
                        int blockX = x,
                                blockY = y,
                                blockWidth = Settings.getCharImageWidth(),
                                blockHeight = Settings.getCharImageHeight();
                        if(x>=Settings.getCharImageWidth()){
                            blockX-=Settings.getCharImageWidth();
                            blockWidth+=Settings.getCharImageWidth();
                        }
                        if(x+Settings.getCharImageWidth()<xLength){
                            blockWidth+=Settings.getCharImageWidth();
                        }
                        if(y>Settings.getCharImageHeight()){
                            blockY-=Settings.getCharImageHeight();
                            blockHeight+=Settings.getCharImageHeight();
                        }
                        if(y+Settings.getCharImageHeight()+1<yLength){
                            blockHeight+=Settings.getCharImageHeight();
                        }
                        double averageLuminance = LuminanceCalculator.calculateLuminance(image.getSubimage(blockX,blockY,blockWidth,blockHeight));
                        value = BitwiseCalculator.calculateBitwize(image.getSubimage(x, y, Settings.getCharImageWidth(), Settings.getCharImageHeight()),averageLuminance,settings);
                        g.setColor(Color.white);
                    }

                    drawValue(value,g,x,y);



                    pixesRendered+=Settings.getCharImageWidth()*Settings.getCharImageWidth();
                }catch(Exception ex){
					//ex.printStackTrace();
                }

            }
        }

    }

    protected void drawValue(int value,Graphics g,int x,int y){
        char c = settings.getCharacterSet().getMatch(value);
        if(value!=0)
            g.drawString(""+c, x, y);
    }

    public long getPixesRendered() {
        return pixesRendered;
    }

    private Color getAverageColor(BufferedImage bi){
        int totalRed=0;
        int totalGreen=0;
        int totalBlue=0;
        for(int x=0;x<bi.getWidth();x++){
            for(int y=0;y<bi.getHeight();y++){
                Color c = new Color(bi.getRGB(x, y));
                totalRed+=c.getRed();
                totalGreen+=c.getGreen();
                totalBlue+=c.getBlue();
            }
        }
        int indexCount = bi.getHeight()*bi.getWidth();
        return new Color(totalRed/indexCount,totalGreen/indexCount,totalBlue/indexCount);
    }

    private boolean getMatch(int RGB,Color c){

        Color color = new Color(RGB);
        int red=Math.abs(color.getRed() - c.getRed());
        int green = Math.abs(color.getGreen() - c.getGreen());
        int blue= Math.abs(color.getBlue() - c.getBlue());
        int avgDiff = (red+blue+green)/3;
        return avgDiff <= 10;
    }


}
