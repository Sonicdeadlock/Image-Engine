package render;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DebugImagePartProcessor extends ImagePartProcessor{
    private BufferedImage diffRender,expectedRender,blockDiffRender;
    private int errors=0;
    public DebugImagePartProcessor(BufferedImage bi, BufferedImage finalImg, int x, int y, int xl, int yl, Settings s) {
        super(bi, finalImg, x, y, xl, yl, s);
        if(settings.isDebugRender()){
            diffRender = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
            expectedRender = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
            blockDiffRender = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
        }

    }

    @Override
    protected void processImagePart() {
        this.debugImageProcess();

    }

    public int debugImageProcess(){
        super.processImagePart();
        if(settings.isDebugRender()){
            Graphics diffRenderGraphics = diffRender.getGraphics();
            diffRenderGraphics.setColor(Color.black);
            diffRenderGraphics.fillRect(0,0,diffRender.getWidth(),diffRender.getHeight());
            for (int x = 0; x < finalImage.getWidth(); x++) {
                for (int y = 0; y < finalImage.getHeight(); y++) {
                    int expectedColor = finalImage.getRGB(x,y),
                            actualColor = expectedRender.getRGB(x,y);
                    if(expectedColor!=actualColor){
                        if(expectedColor != Color.white.getRGB()){
                            diffRenderGraphics.setColor(Color.cyan);
                        }else{
                            diffRenderGraphics.setColor(Color.magenta);
                        }
                        diffRenderGraphics.fillRect(x,y,1,1);
                    }
                }
            }
            try {
                ImageIO.write(diffRender,"PNG",new File("debug"+errors+".png"));
                ImageIO.write(blockDiffRender,"PNG",new File("debug-block"+errors+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return errors;
    }

    @Override
    protected void drawValue(int value, Graphics g, int x, int y) {
        super.drawValue(value,g,x,y);
        boolean[][] valueGrid = intToBoolArray(value);
        if(settings.isDebugRender()){
            g = expectedRender.getGraphics();
            for (int gridX = 0; gridX < valueGrid.length; gridX++) {
                for (int gridY = 0; gridY < valueGrid[gridX].length; gridY++) {
                    if(valueGrid[gridX][gridY]){
                        g.setColor(Color.white);
                        g.fillRect(x+gridX*Settings.getPRECISION(),y+gridY*Settings.getPRECISION(),Settings.getPRECISION(),Settings.getPRECISION());
                    }
                }
            }
        }
        boolean[][] characterValueGrid = intToBoolArray(settings.getCharacterSet().getCharacterIndex(settings.getCharacterSet().getMatch(value)));
        if(settings.isDebugRender()){
            g = blockDiffRender.getGraphics();
            for (int gridX = 0; gridX < valueGrid.length; gridX++) {
                for (int gridY = 0; gridY < valueGrid[gridX].length; gridY++) {
                    if(valueGrid[gridX][gridY] != characterValueGrid[gridX][gridY]){
                        errors++;
                        if(valueGrid[gridX][gridY])
                            g.setColor(Color.orange);
                        else{
                            g.setColor(Color.magenta);
                        }
                        g.fillRect(x+gridX*Settings.getPRECISION(),y+gridY*Settings.getPRECISION(),Settings.getPRECISION(),Settings.getPRECISION());
                    }else if(valueGrid[gridX][gridY]){
                        g.setColor(Color.green);
                        g.fillRect(x+gridX*Settings.getPRECISION(),y+gridY*Settings.getPRECISION(),Settings.getPRECISION(),Settings.getPRECISION());
                    }

                }
            }
        }else{
            for (int gridX = 0; gridX < valueGrid.length; gridX++) {
                for (int gridY = 0; gridY < valueGrid[gridX].length; gridY++) {
                    if (valueGrid[gridX][gridY] != characterValueGrid[gridX][gridY]) {
                        errors++;
                    }
                }
            }
        }



    }

    private boolean[][] intToBoolArray(int number){
        int gridSize = Settings.getCharImageWidth()/Settings.getPRECISION();
        boolean[][] grid = new boolean[gridSize][gridSize];
        for (int i = 0; i < gridSize * gridSize; i++) {
            int x = i/gridSize;
            int y = i%gridSize;
            grid[x][y]= (number& 1<<i)>0;
        }
        return grid;
    }
}
